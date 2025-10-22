import { useQuery } from '@tanstack/react-query';
import { apiClient } from './axios';

export const useGetUserBook = (
  userId: number | undefined,
  bookId: number | undefined | null,
) => {
  return useQuery({
    queryKey: ['userBook', userId, bookId],
    queryFn: async () => {
      const res = await apiClient.get(`/users/${userId}/books/${bookId}`);
      return res.data;
    },
    enabled: !!userId && !!bookId,
    staleTime: Infinity,
  });
};
