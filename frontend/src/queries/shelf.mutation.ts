import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { apiClient } from './axios';

export const useAddNewShelf = (userId: number | undefined) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (shelfName: string) => {
      const res = await apiClient.post(`users/${userId}/shelves`, {
        shelfName: shelfName,
      });
      return res.data;
    },
    onSuccess: () => {
      toast.success('Shelf added successfully');
      queryClient.invalidateQueries({
        queryKey: ['customShelves', userId],
      });
    },
    onError: (_error, shelfName) => {
      toast.error(`Failed to add shelf ${shelfName}`);
    },
  });
};
