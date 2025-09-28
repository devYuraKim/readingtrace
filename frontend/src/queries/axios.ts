import { useAuthStore } from '@/store/useAuthStore';
import axios from 'axios';
import { decodeAccessToken } from '@/lib/jwt';

export const apiClient = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
  withCredentials: true,
});

const refreshClient = axios.create({
  baseURL: '/api/v1',
  withCredentials: true,
});

const getBearerToken = (token: string) =>
  token.startsWith('Bearer ') ? token : `Bearer ${token}`;

apiClient.interceptors.request.use(
  async (config) => {
    const xsrfToken = getCookie('XSRF-TOKEN');
    if (xsrfToken) {
      config.headers['X-XSRF-TOKEN'] = xsrfToken;
    }
    const token = useAuthStore.getState().accessToken;
    if (token) {
      config.headers['Authorization'] = getBearerToken(token);
    }
    console.log('api, request', useAuthStore.getState());
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

refreshClient.interceptors.request.use(
  async (config) => {
    const xsrfToken = getCookie('XSRF-TOKEN');
    if (xsrfToken) {
      config.headers['X-XSRF-TOKEN'] = xsrfToken;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

refreshClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error?.response?.status === 401) {
      console.log('INVALID REFRESH TOKEN');
      useAuthStore.getState().clearAuth();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  },
);

apiClient.interceptors.response.use(
  (response) => {
    return response; // pass successful responses through
  },
  async (error) => {
    //Handle 403 CSRF
    if (
      error?.response?.status === 403 &&
      error?.response?.data.error.includes('CSRF') &&
      !error.config._retryCsrf
    ) {
      error.config._retryCsrf = true;
      await refreshClient.get('/auth/csrf');
      return apiClient(error.config);
    }

    //Handle 401 AccessToken Expiration
    if (error?.response?.status === 401 && !error.config._retryAuth) {
      error.config._retryAuth = true;
      try {
        const res = await refreshClient.post('/auth/refresh');
        const newAccessToken = res.headers['authorization']?.replace(
          'Bearer ',
          '',
        );
        if (newAccessToken) {
          const payload = decodeAccessToken(newAccessToken);
          if (payload) {
            useAuthStore.getState().setAuth(
              {
                userId: payload.userId,
                email: payload.email,
                roles: payload.roles,
              },
              newAccessToken,
            );

            apiClient.defaults.headers.common['Authorization'] =
              getBearerToken(newAccessToken);
            error.config.headers['Authorization'] =
              getBearerToken(newAccessToken);

            console.log(
              'new access token issued, useAuthStore.getState(): ',
              useAuthStore.getState(),
            );
          }
        }
        //retry original request with new token
        return apiClient(error.config);
      } catch (refreshError) {
        return Promise.reject(refreshError);
      }
    }
    console.log('api, response', useAuthStore.getState());
    return Promise.reject(error);
  },
);

function getCookie(name: string) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()?.split(';').shift();
  return undefined;
}
