import { useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useGetUserBook } from '@/queries/book-status.query';
import { useAuthStore } from '@/store/useAuthStore';
import { ChatRecordDto } from '@/types/ai-chat.types';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@radix-ui/react-dropdown-menu';
import {
  QueryClient,
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query';
import { Book, Bookmark, Ellipsis, Pencil, Trash2 } from 'lucide-react';
import { useParams } from 'react-router-dom';
import { DropdownMenuGroup } from '../ui/dropdown-menu';
import { Skeleton } from '../ui/skeleton';
import { Spinner } from '../ui/spinner';
import AddChatCta from './AddChatCta';
import { PromptInput } from './PromptInput';

const UserBookChatDetails = () => {
  const { bookId } = useParams();
  const numericBookId = Number(bookId);
  const userId = useAuthStore((state) => state.user?.userId);

  const [isPendingPostUserMessage, setIsPendingPostUserMessage] =
    useState(false);

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
    select: (data) =>
      [...data].sort(
        (a, b) =>
          new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime(),
      ),
  });

  const queryClient = useQueryClient();

  const { mutate: bookmarkChatRecord } = useMutation({
    mutationFn: async (chatRecordId: number) => {
      const res = await apiClient.post(
        `users/${userId}/ai/chats/${chatRecordId}?action=bookmark`,
      );
      return res.data;
    },
    mutationKey: ['bookmarkChatRecord', userId],
    // TODO: check if we can update only one chat record, not the entire record
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['userBookChat', userId, numericBookId],
      });
    },
  });

  const handleClickBookmark = (chatRecordId: number) => {
    bookmarkChatRecord(chatRecordId);
  };

  const handleClickNote = () => {};

  const { mutate: softDeleteChatRecord } = useMutation({
    mutationFn: async (chatRecordId: number) => {
      const res = await apiClient.post(
        `users/${userId}/ai/chats/${chatRecordId}?action=delete`,
      );
      return res.data;
    },
    mutationKey: ['softDeleteChatRecord', userId],
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['userBookChat', userId, numericBookId],
      });
    },
  });

  const handleClickDelete = (chatRecordId: number) => {
    softDeleteChatRecord(chatRecordId);
  };

  return (
    <>
      <div className="flex justify-between items-center mx-1">
        <div>
          <div className="font-semibold text-lg">{userBook?.title}</div>
          <div className="text-sm text-muted-foreground">
            {userBook?.authors}
          </div>
        </div>
        <Bookmark className="cursor-pointer hover:fill-amber-400" />
      </div>
      <div>
        {isPendingPostUserMessage && (
          <Skeleton
            className="w-full h-10
         rounded-sm shadow-md mb-4 text-xs flex gap-2 items-center justify-center"
          >
            <Spinner /> Generating a response...
          </Skeleton>
        )}
        {!isPendingUserBookChat && userBookChats?.length === 0 && (
          <AddChatCta />
        )}
        {!isPendingUserBookChat &&
          userBookChats?.map((userBookChat: ChatRecordDto) => (
            <div
              key={userBookChat.chatRecordId}
              className="border-1 mb-2 rounded-sm p-2 shadow-md"
            >
              <div className="flex justify-between m-2 font-extralight text-xs">
                <div className="flex gap-2 items-center mb-2">
                  {userBookChat.isBookmarked && (
                    <Bookmark className="border-none stroke-amber-400 fill-amber-400" />
                  )}
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
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Ellipsis className="cursor-pointer w-4 h-4 rounded-xs  mr-1" />
                  </DropdownMenuTrigger>
                  <DropdownMenuContent
                    className="cursor-pointer shadow-md rounded-[0.3rem] p-2 bg-white mt-2"
                    align="center"
                  >
                    <DropdownMenuGroup>
                      <DropdownMenuItem
                        className="text-xs text-center focus:outline-none focus:font-bold focus:bg-[#f5f5f5] p-1.5 focus:rounded-[0.3rem]"
                        textValue="bookmark"
                        onClick={() =>
                          handleClickBookmark(userBookChat.chatRecordId)
                        }
                      >
                        <Bookmark className="w-4 h-4" />
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        className="text-xs text-center focus:outline-none focus:font-bold focus:bg-[#f5f5f5] p-1.5 focus:rounded-[0.3rem]"
                        textValue="note"
                        onClick={handleClickNote}
                      >
                        <Pencil className="w-4 h-4" />
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        className="text-xs text-center focus:outline-none focus:font-bold focus:bg-[#f5f5f5] p-1.5 focus:rounded-[0.3rem]"
                        textValue="delete"
                        onClick={() =>
                          handleClickDelete(userBookChat.chatRecordId)
                        }
                      >
                        <Trash2 className="w-4 h-4" />
                      </DropdownMenuItem>
                    </DropdownMenuGroup>
                  </DropdownMenuContent>
                </DropdownMenu>
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
          <PromptInput
            userBook={userBook}
            onPendingChange={setIsPendingPostUserMessage}
          />
        </div>
      )}
    </>
  );
};

export default UserBookChatDetails;
