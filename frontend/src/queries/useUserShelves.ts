import { useQuery } from '@tanstack/react-query';
import { Shelf } from '@/lib/shelves';
import { apiClient } from './axios';

export const useUserShelves = (userId: number | undefined) => {
  return useQuery<Shelf[]>({
    queryKey: ['userShelves', userId],
    queryFn: async () => {
      const res = await apiClient.get(`/users/${userId}/shelves`);
      return res.data;
    },
    enabled: !!userId,
  });
};
