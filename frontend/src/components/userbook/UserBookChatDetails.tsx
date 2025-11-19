import { apiClient } from '@/queries/axios';
import { useGetUserBook } from '@/queries/book-status.query';
import { useAuthStore } from '@/store/useAuthStore';
import { AiChat, ChatRecordDto } from '@/types/ai-chat.types';
import {
  Tooltip,
  TooltipArrow,
  TooltipContent,
  TooltipTrigger,
} from '@radix-ui/react-tooltip';
import { useQuery } from '@tanstack/react-query';
import { Ellipsis } from 'lucide-react';
import { useParams } from 'react-router-dom';
import { PromptInput } from './PromptInput';

const UserBookChatDetails = () => {
  const { bookId } = useParams();
  const numericBookId = Number(bookId);
  const userId = useAuthStore((state) => state.user?.userId);

  const { data: userBook, isPending: isPendingUserBook } = useGetUserBook(
    userId,
    numericBookId,
  );

  const { data: userBookChats, isPending: isPendingUserBookChat } = useQuery({
    queryKey: ['userBookChat', userId, numericBookId],
    queryFn: async () => {
      const res = await apiClient.get(
        `users/${userId}/ai?bookId=${numericBookId}`,
      );
      return res.data;
    },
  });

  const handleClickSave = (aiChat: AiChat) => {
    alert(aiChat.chatRecordId);
  };

  return (
    <>
      <div>
        UserBookChatDetails
        <div className="font-semibold text-lg">{userBook?.title}</div>
        <div className="text-sm text-muted-foreground">{userBook?.authors}</div>
      </div>
      <div>
        {!isPendingUserBookChat &&
          userBookChats?.map((userBookChat: ChatRecordDto) => (
            <div
              key={userBookChat.chatRecordId}
              className="border-1 mb-2 rounded-sm p-2 shadow-md"
            >
              <div className="flex justify-between m-2 font-extralight text-xs">
                <div className="flex gap-2 items-center">
                  <span className="rounded-full px-2 border-1 shadow-xs">
                    {userBookChat.chatModel}
                  </span>
                  <span>
                    {new Date(userBookChat.timestamp)
                      .toLocaleString('en-GB', {
                        year: '2-digit',
                        month: 'short',
                        day: '2-digit',
                        hour: '2-digit',
                        minute: '2-digit',
                        second: '2-digit',
                        hour12: false,
                      })
                      .replace(',', ' at ')}{' '}
                  </span>
                </div>
                <Tooltip>
                  <TooltipTrigger>
                    <Ellipsis
                      className="cursor-pointer w-4 h-4 rounded-xs  mr-1"
                      onClick={() => {
                        handleClickSave(userBookChat);
                      }}
                    />
                  </TooltipTrigger>

                  <TooltipContent className="bg-black text-white p-1 rounded-sm mr-1">
                    <TooltipArrow className="mr-1" />
                    <p>Save</p>
                  </TooltipContent>
                </Tooltip>
              </div>
              <div className="m-2 text-sm font-light bg-neutral-200 p-2 px-3 rounded-sm">
                {userBookChat.userMessage}
              </div>
              <div className="m-2 text-sm p-2 leading-relaxed ">
                {userBookChat.assistantMessage}
              </div>
            </div>
          ))}
      </div>
      {!isPendingUserBook && (
        <div className="sticky bottom-5 bg-white">
          <PromptInput userBook={userBook} />
        </div>
      )}
    </>
  );
};

export default UserBookChatDetails;
