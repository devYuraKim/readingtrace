import { CustomShelf, DefaultShelf } from '@/types/shelf.types';
import { useQuery } from '@tanstack/react-query';
import { apiClient } from './axios';

export const useDefaultShelves = (userId: number | undefined) => {
  return useQuery<DefaultShelf[]>({
    queryKey: ['defaultShelves', userId],
    queryFn: async () => {
      const res = await apiClient.get(`/users/${userId}/shelves/default`);
      return res.data;
    },
    enabled: !!userId,
  });
};

export const useCustomShelves = (userId: number | undefined) => {
  return useQuery<CustomShelf[]>({
    queryKey: ['customShelves', userId],
    queryFn: async () => {
      const res = await apiClient.get(`/users/${userId}/shelves/custom`);
      return res.data;
    },
    enabled: !!userId,
  });
};
