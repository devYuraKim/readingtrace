import { useQuery } from '@tanstack/react-query';
import { apiClient } from './axios';

export const useGetBookStatus = (
  userId: number | undefined,
  bookId: number | undefined,
) => {
  return useQuery({
    queryKey: ['userBookStatus', userId, bookId],
    queryFn: async () => {
      const res = await apiClient.get(`/users/${userId}/books/${bookId}`);
      return res.data;
    },
    enabled: !!userId && !!bookId,
    staleTime: Infinity,
  });
};
