import { AiChat, ChatResponseDto } from '@/types/ai-chat.types';
import { UserBookDto } from '@/types/book.types';
import { useMutation } from '@tanstack/react-query';
import { apiClient } from './axios';

export const usePostUserMessage = (
  userId: number | undefined,
  model: string,
  userMessage: string,
  userBook: UserBookDto,
  setAiChatList: React.Dispatch<React.SetStateAction<Array<AiChat>>>,
  setUserMessage: React.Dispatch<React.SetStateAction<string>>,
  finalUserMessage: string,
) => {
  return useMutation({
    mutationKey: ['sendUserMessage'],
    mutationFn: async () => {
      const res = await apiClient.post(`users/${userId}/ai?model=${model}`, {
        userMessage,
        timestamp: new Date(),
        title: userBook.title ?? '',
        author: userBook.authors?.join(',') ?? '',
        publisher: userBook.publisher ?? '',
        publishedDate: userBook.publishedDate ?? '',
        isbn10: userBook.isbn10 ?? '',
        isbn13: userBook.isbn13 ?? '',
        language: userBook.language ?? '',
      });
      console.log(res.data);
      return res.data;
    },
    onSuccess: (aiResponse: ChatResponseDto) => {
      setAiChatList((prev) => [
        ...prev,
        {
          userInput: finalUserMessage,
          assistantOutput: aiResponse.textContent,
          timestamp: aiResponse.timestamp,
        },
      ]);
      setUserMessage('');
    },
  });
};
