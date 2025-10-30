import { useState } from 'react';
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
import { TooltipArrow, TooltipContent } from '@radix-ui/react-tooltip';
import { useMutation } from '@tanstack/react-query';
import { ArrowUpIcon, Save } from 'lucide-react';
import { toast } from 'sonner';
import {
  InputGroup,
  InputGroupAddon,
  InputGroupButton,
  InputGroupText,
  InputGroupTextarea,
} from '../ui/input-group';
import { Tooltip, TooltipTrigger } from '../ui/tooltip';

type AiChat = {
  userInput: string;
  assistantOutput: string;
};

export const PromptInput = ({ userBook }: { userBook: UserBookDto }) => {
  const [model, setModel] = useState('');
  const [userMessage, setUserMessage] = useState('');
  const [finalUserMessage, setFinalUserMessage] = useState('');
  const [aiChatList, setAiChatList] = useState<AiChat[]>([]);

  const userId = useAuthStore((state) => state.user?.userId);

  const handleSelect = (value: string) => {
    setModel(value);
  };

  const handleSubmit = () => {
    const normalizedUserMessage = userMessage.trim().replace(/\s+/g, ' ');

    if (normalizedUserMessage === '') {
      toast.error('prompt input cannot be empty');
      return;
    }
    setUserMessage(normalizedUserMessage);
    setFinalUserMessage(userMessage);

    mutate();
  };

  const { mutate, isPending, isSuccess } = useMutation({
    mutationKey: ['sendUserMessage'],
    mutationFn: async () => {
      const res = await apiClient.post(`users/${userId}/ai?model=${model}`, {
        userMessage,
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
    onSuccess: (aiResponse) => {
      setAiChatList((prev) => [
        ...prev,
        { userInput: finalUserMessage, assistantOutput: aiResponse },
      ]);
      setUserMessage('');
    },
  });

  return (
    <div>
      {!isPending &&
        isSuccess &&
        aiChatList.map((aiChat) => (
          <div className="border-1 mb-2 rounded-sm">
            <div className="flex justify-between m-2 font-extralight text-xs ">
              {new Date().toLocaleString()}
              <Tooltip>
                <TooltipTrigger>
                  <Save className="cursor-pointer hover:stroke-accent-foreground/50" />
                </TooltipTrigger>

                <TooltipContent className="bg-black text-white p-1 rounded-sm">
                  <TooltipArrow />
                  <p>Save</p>
                </TooltipContent>
              </Tooltip>
            </div>
            <div className="m-2">{aiChat.userInput}</div>
            <div className="m-2">{aiChat.assistantOutput}</div>
          </div>
        ))}
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
              <InputGroupButton variant="ghost">Model</InputGroupButton>
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
            disabled={userMessage && !isPending ? false : true}
          >
            <ArrowUpIcon onClick={handleSubmit} className="cursor-pointer" />
            <span className="sr-only">Send</span>
          </InputGroupButton>
        </InputGroupAddon>
      </InputGroup>
    </div>
  );
};

export default PromptInput;
