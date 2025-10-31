import { useState } from 'react';
import { usePostUserMessage } from '@/queries/ai-chat.mutation';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { UserBookDto } from '@/types/book.types';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@radix-ui/react-dropdown-menu';
import { Separator } from '@radix-ui/react-separator';
import { useQuery } from '@tanstack/react-query';
import { ArrowUpIcon } from 'lucide-react';
import { toast } from 'sonner';
import {
  InputGroup,
  InputGroupAddon,
  InputGroupButton,
  InputGroupText,
  InputGroupTextarea,
} from '../ui/input-group';

export const PromptInput = ({ userBook }: { userBook: UserBookDto }) => {
  const [chatModel, setChatModel] = useState('');
  const [userMessage, setUserMessage] = useState('');
  const [finalUserMessage, setFinalUserMessage] = useState('');

  const userId = useAuthStore((state) => state.user?.userId);
  const bookId = userBook.bookId;

  const handleSelect = (value: string) => {
    setChatModel(value);
  };

  const {
    mutate: mutatePostUserMessage,
    isPending: isPendingPostUserMessage,
    isSuccess: isSuccessPostUserMessage,
  } = usePostUserMessage(
    userId,
    chatModel,
    finalUserMessage,
    userBook,
    setUserMessage,
  );

  const handleSubmit = () => {
    const normalizedUserMessage = userMessage.trim().replace(/\s+/g, ' ');

    if (normalizedUserMessage === '') {
      toast.error('prompt input cannot be empty');
      return;
    }
    setUserMessage(normalizedUserMessage);
    setFinalUserMessage(userMessage);

    mutatePostUserMessage();
  };

  useQuery({
    queryKey: ['aiChat', userId, bookId],
    queryFn: async () => {
      apiClient.get(`/users/${userId}/ai?bookId=${bookId}`);
    },
  });

  return (
    <InputGroup>
      <InputGroupTextarea
        placeholder={`Start your discussion on '${userBook.title}'`}
        onChange={(e) => setUserMessage(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === 'Enter') handleSubmit();
        }}
        value={userMessage}
      />

      <InputGroupAddon align="block-end">
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <InputGroupButton variant="ghost">ChatModel</InputGroupButton>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            side="top"
            align="start"
            className="[--radius:0.95rem] cursor-pointer p-1 bg-white border-1 rounded-[0.3rem] font-light"
            sideOffset={5}
          >
            <DropdownMenuItem
              className="p-1"
              textValue="gemini"
              onSelect={() => handleSelect('gemini')}
            >
              Gemini
            </DropdownMenuItem>
            <DropdownMenuItem
              className="p-1"
              textValue="chatgpt"
              onSelect={() => handleSelect('chatgpt')}
            >
              ChatGPT
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
        <InputGroupText className="ml-auto">52% used</InputGroupText>
        <Separator orientation="vertical" className="!h-4" />
        <InputGroupButton
          variant="default"
          className="rounded-full"
          size="icon-xs"
          disabled={userMessage && !isPendingPostUserMessage ? false : true}
        >
          <ArrowUpIcon onClick={handleSubmit} className="cursor-pointer" />
          <span className="sr-only">Send</span>
        </InputGroupButton>
      </InputGroupAddon>
    </InputGroup>
  );
};

export default PromptInput;
