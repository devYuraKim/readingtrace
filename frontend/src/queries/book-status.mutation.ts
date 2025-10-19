import { BookStatusFormValues } from '@/schemas/book-status.schemas';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { apiClient } from './axios';

export const useCreateBookStatus = (
  userId: number | undefined,
  bookId: number,
) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (payload: BookStatusFormValues) => {
      await apiClient.post(`/users/${userId}/books/${bookId}`, {
        ...payload,
      });
    },
    onSuccess: () => {
      toast.success('Book added successfully');
      queryClient.invalidateQueries({
        queryKey: ['userBookStatus', userId, bookId],
      });
    },
    onError: () => {
      toast.error('Failed to add book');
    },
  });
};

export const useUpdateBookStatus = (
  userId: number | undefined,
  bookId: number,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (payload: BookStatusFormValues) =>
      apiClient.put(`/users/${userId}/books/${bookId}`, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['userBookStatus', userId, bookId],
      });
      toast.success('Book updated successfully!');
    },
    onError: () => {
      toast.error('Failed to update book');
    },
  });
};

export const useDeleteBookStatus = (
  userId: number | undefined,
  bookId: number,
  onSuccessCallback?: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => apiClient.delete(`/users/${userId}/books/${bookId}`),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['userBookStatus', userId, bookId],
      });
      toast.success('Book deleted successfully!');
      onSuccessCallback?.();
    },
    onError: () => {
      toast.error('Failed to delete book');
    },
  });
};
