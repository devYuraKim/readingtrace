import { Shelf } from '@/types/shelf.types';
import { useQuery } from '@tanstack/react-query';
import { apiClient } from './axios';

export const useCustomShelves = (userId: number | undefined) => {
  return useQuery<Shelf[]>({
    queryKey: ['customShelves', userId],
    queryFn: async () => {
      const res = await apiClient.get(`/users/${userId}/shelves/custom`);
      return res.data;
    },
    enabled: !!userId,
  });
};
