import { ReadingStatusFormValues } from '@/schemas/reading-status.schemas';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { apiClient } from './axios';

export const useCreateUserBook = (userId: number | undefined) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (payload: ReadingStatusFormValues) => {
      await apiClient.post(`/users/${userId}/books`, {
        ...payload,
      });
    },
    onSuccess: () => {
      toast.success('Book added successfully');
      queryClient.invalidateQueries({
        queryKey: ['userBook', userId],
      });
    },
    onError: () => {
      toast.error('Failed to add book');
    },
  });
};

export const useUpdateUserBook = (
  userId: number | undefined,
  bookId: number | null,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (payload: ReadingStatusFormValues) =>
      apiClient.put(`/users/${userId}/books/${bookId}`, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['userBook', userId, bookId],
      });
      toast.success('Book updated successfully!');
    },
    onError: () => {
      toast.error('Failed to update book');
    },
  });
};

export const useDeleteUserBook = (
  userId: number | undefined,
  bookId: number | null,
  onSuccessCallback?: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => apiClient.delete(`/users/${userId}/books/${bookId}`),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['userBook', userId, bookId],
      });
      toast.success('Book deleted successfully!');
      onSuccessCallback?.();
    },
    onError: () => {
      toast.error('Failed to delete book');
    },
  });
};
