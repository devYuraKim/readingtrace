import { useEffect } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { Outlet, useNavigate } from 'react-router-dom';
import { decodeAccessToken } from '@/lib/jwt';

function AuthProvider() {
  const navigate = useNavigate();

  useEffect(() => {
    const refresh = async () => {
      const accessToken = useAuthStore.getState().accessToken;
      if (accessToken === null) {
        try {
          const res = await apiClient.post('/auth/refresh');
          const newAccessToken = res.headers['authorization']?.replace(
            'Bearer ',
            '',
          );
          if (!newAccessToken) throw new Error('No access token returned');
          const payload = decodeAccessToken(newAccessToken);
          if (payload) {
            useAuthStore.getState().setAuth(
              {
                userId: payload.userId,
                email: payload.email,
                roles: payload.roles,
              },
              null,
              newAccessToken,
            );
          }
        } catch (error) {
          console.log(error);
          useAuthStore.getState().clearAuth();
          navigate('/login', { replace: true });
        } finally {
          useAuthStore.getState().setIsAuthChecked();
        }
      } else {
        useAuthStore.getState().setIsAuthChecked();
      }
    };
    refresh();
  }, [navigate]);

  return <Outlet />;
}

export default AuthProvider;
