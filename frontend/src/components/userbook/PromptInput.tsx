import { useState } from 'react';
import { CHATMODEL } from '@/constants/prompt.constants';
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
  const [chatModel, setChatModel] = useState('ChatModel');
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
        disabled={!isPendingPostUserMessage ? false : true}
      />

      <InputGroupAddon align="block-end">
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <InputGroupButton className="text-black cursor-pointer">
              {chatModel}
            </InputGroupButton>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            side="top"
            align="start"
            className="cursor-pointer p-1 bg-white border-1 rounded-[0.3rem] font-light w-25"
            sideOffset={5}
          >
            {CHATMODEL.map((model) => (
              <DropdownMenuItem
                key={model}
                textValue={model}
                onSelect={() => handleSelect(model)}
                className="text-sm text-center focus:outline-none focus:font-bold focus:bg-[#f5f5f5] focus:text-black p-1.5 focus:rounded-[0.3rem]"
              >
                {model}
              </DropdownMenuItem>
            ))}
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
