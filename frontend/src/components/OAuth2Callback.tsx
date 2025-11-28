import { useEffect } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { decodeAccessToken } from '@/lib/jwt';

function OAuth2Callback() {
  const navigate = useNavigate();

  const { isPending, mutate } = useMutation({
    mutationFn: async () => {
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
          newAccessToken,
        );
      }
    },
    onSuccess: () => {
      // TODO: handle different onboarding completion cases
      const userId = useAuthStore.getState().user?.userId;
      navigate(`/users/${userId}`);
    },
    onError: () => {
      toast.error('Unable to complete Google login. Please try again.');
      navigate('/login');
    },
  });

  useEffect(() => {
    mutate();
  }, [mutate]);

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="text-center">
        {isPending && (
          <>
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto"></div>
            <p className="mt-4">Completing your Google login...</p>
          </>
        )}
      </div>
    </div>
  );
}

export default OAuth2Callback;
