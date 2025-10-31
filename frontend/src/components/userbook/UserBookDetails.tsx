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
import { Save } from 'lucide-react';
import { useParams } from 'react-router-dom';
import { PromptInput } from './PromptInput';

const UserBookDetails = () => {
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
        BookDetails
        <div className="font-semibold text-lg">{userBook?.title}</div>
        <div className="text-sm text-muted-foreground">{userBook?.authors}</div>
      </div>
      <div>
        {!isPendingUserBookChat &&
          userBookChats?.map((userBookChat: ChatRecordDto) => (
            <>
              <div className="border-1 mb-2 rounded-sm">
                <div className="flex justify-between m-2 font-extralight text-xs ">
                  {userBookChat.timestamp.toLocaleString()}
                  <Tooltip>
                    <TooltipTrigger>
                      <Save
                        className="cursor-pointer hover:stroke-accent-foreground/50"
                        onClick={() => {
                          handleClickSave(userBookChat);
                        }}
                      />
                    </TooltipTrigger>

                    <TooltipContent className="bg-black text-white p-1 rounded-sm">
                      <TooltipArrow />
                      <p>Save</p>
                    </TooltipContent>
                  </Tooltip>
                </div>
                <div className="m-2 text-sm">{userBookChat.userMessage}</div>
                <div className="m-2 text-sm">
                  {userBookChat.assistantMessage}
                </div>
              </div>
            </>
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

export default UserBookDetails;
