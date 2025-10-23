import { Shelf } from '@/types/shelf.types';
import { useQuery } from '@tanstack/react-query';
import { apiClient } from './axios';

export const useDefaultShelves = (userId: number | undefined) => {
  return useQuery<Shelf[]>({
    queryKey: ['defaultShelves', userId],
    queryFn: async () => {
      const res = await apiClient.get(`/users/${userId}/shelves/default`);
      return res.data;
    },
    enabled: !!userId,
  });
};
