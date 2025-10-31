import { UserBookDto } from '@/types/book.types';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { apiClient } from './axios';

export const usePostUserMessage = (
  userId: number | undefined,
  chatModel: string,
  userMessage: string,
  userBook: UserBookDto,
  setUserMessage: React.Dispatch<React.SetStateAction<string>>,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: ['sendUserMessage'],
    mutationFn: async () => {
      const res = await apiClient.post(
        `users/${userId}/ai?chatModel=${chatModel}`,
        {
          userMessage,
          timestamp: new Date(),
          bookId: userBook.bookId ?? '',
          title: userBook.title ?? '',
          author: userBook.authors?.join(',') ?? '',
          publisher: userBook.publisher ?? '',
          publishedDate: userBook.publishedDate ?? '',
          isbn10: userBook.isbn10 ?? '',
          isbn13: userBook.isbn13 ?? '',
          language: userBook.language ?? '',
        },
      );
      console.log(res.data);
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['userBookChat', userId, userBook.bookId],
      });
      setUserMessage('');
    },
  });
};
