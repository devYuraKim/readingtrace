import axios from 'axios';

export const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
  withCredentials: true,
});

apiClient.interceptors.request.use(
  async (config) => {
    const xsrfToken = getCookie('XSRF-TOKEN');
    if (xsrfToken) {
      config.headers['X-XSRF-TOKEN'] = xsrfToken;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

apiClient.interceptors.response.use(
  (response) => {
    return response; // pass successful responses through
  },
  //Axios attaches the original request’s config to the error object
  (error) => {
    if (
      error.response.status === 403 &&
      error.response.data.error.includes('CSRF')
    ) {
      apiClient.get('/auth/csrf');

      // dynamically adding a custom property(_retry) to an existing object(error.config)
      if (!error.config._retry) {
        //in JavaScript, accessing a non-existent property returns undefined, which is falsy.
        // config: the original request configuration object that was used to make the HTTP request
        error.config._retry = true;
        return apiClient(error.config);
      }
    }
    return Promise.reject(error);
  },
);

function getCookie(name: string) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()?.split(';').shift();
  return undefined;
}
