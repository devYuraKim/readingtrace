import { useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useGetUserBook } from '@/queries/book-status.query';
import { useAuthStore } from '@/store/useAuthStore';
import { ChatRecordDto } from '@/types/ai-chat.types';
import { Dialog } from '@radix-ui/react-dialog';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@radix-ui/react-dropdown-menu';
import { Tooltip, TooltipContent } from '@radix-ui/react-tooltip';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Bookmark, Ellipsis, Pencil, Trash2 } from 'lucide-react';
import { useParams } from 'react-router-dom';
import { toast } from 'sonner';
import { Button } from '../ui/button';
import {
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '../ui/dialog';
import { DropdownMenuGroup } from '../ui/dropdown-menu';
import { Skeleton } from '../ui/skeleton';
import { Spinner } from '../ui/spinner';
import { TooltipTrigger } from '../ui/tooltip';
import AddChatCta from './AddChatCta';
import { PromptInput } from './PromptInput';

const UserBookChatDetails = () => {
  const { bookId } = useParams();
  const numericBookId = Number(bookId);
  const userId = useAuthStore((state) => state.user?.userId);

  const [isPendingPostUserMessage, setIsPendingPostUserMessage] =
    useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [selectedChatRecordId, setSelectedChatRecordId] = useState<
    number | null
  >(null);

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
      setIsDialogOpen(false);
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const handleClickDelete = (chatRecordId: number) => {
    setIsDialogOpen(true);
    setSelectedChatRecordId(chatRecordId);
  };

  const handleConfirmDelete = () => {
    softDeleteChatRecord(selectedChatRecordId);
  };

  return (
    <>
      <div className="flex justify-between items-center mx-1">
        <div>
          <div className="font-semibold text-lg text-indigo-900">
            {userBook?.title}
          </div>
          <div className="text-sm text-indigo-900/70">{userBook?.authors}</div>
        </div>
        <Bookmark className="cursor-pointer stroke-gray-300 hover:fill-indigo-700 hover:stroke-indigo-700" />
      </div>
      <div>
        {isDialogOpen && (
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogContent className="w-70">
              <DialogHeader>
                <DialogTitle>Are you sure?</DialogTitle>
                <DialogDescription>
                  This action cannot be undone. <br /> This will delete your
                  chat.
                </DialogDescription>
              </DialogHeader>
              <DialogFooter className="!justify-between ">
                <DialogClose asChild>
                  <Button variant="outline" className="cursor-pointer">
                    Cancel
                  </Button>
                </DialogClose>
                <Button
                  onClick={handleConfirmDelete}
                  className="cursor-pointer"
                >
                  Delete
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        )}

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
              className="border-1 border-indigo-100 mb-5 rounded-sm p-2 shadow-md shadow-indigo-50"
            >
              <div className="flex justify-between m-2 font-extralight text-xs">
                <div className="flex gap-1 items-center mb-2">
                  <Tooltip>
                    <TooltipTrigger>
                      <Bookmark
                        className={`cursor-pointer ${
                          userBookChat.isBookmarked
                            ? 'stroke-indigo-700 fill-indigo-700'
                            : 'stroke-gray-300'
                        }`}
                        onClick={() =>
                          handleClickBookmark(userBookChat.chatRecordId)
                        }
                      />
                    </TooltipTrigger>
                    <TooltipContent
                      align="start"
                      className="-mb-[1.8rem] ml-7 bg-black text-white px-2 py-1 font-normal rounded-sm"
                    >
                      {userBookChat.isBookmarked
                        ? 'Remove Bookmark'
                        : 'Add Bookmark'}
                    </TooltipContent>
                  </Tooltip>
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
                    <Ellipsis className="cursor-pointer w-4 h-4 rounded-xs  mr-1 stroke-indigo-900" />
                  </DropdownMenuTrigger>
                  <DropdownMenuContent
                    className="cursor-pointer shadow-md rounded-[0.3rem] p-2 bg-white mt-2"
                    align="center"
                  >
                    <DropdownMenuGroup>
                      <DropdownMenuItem
                        className="text-xs text-center focus:outline-none focus:font-bold focus:bg-indigo-50 p-1.5 focus:rounded-[0.3rem]"
                        textValue="note"
                        onClick={handleClickNote}
                      >
                        <Pencil className="w-4 h-4 stroke-indigo-900" />
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        className="text-xs text-center focus:outline-none focus:font-bold focus:bg-indigo-50 p-1.5 focus:rounded-[0.3rem]"
                        textValue="delete"
                        onClick={() =>
                          handleClickDelete(userBookChat.chatRecordId)
                        }
                      >
                        <Trash2 className="w-4 h-4 stroke-indigo-900" />
                      </DropdownMenuItem>
                    </DropdownMenuGroup>
                  </DropdownMenuContent>
                </DropdownMenu>
              </div>
              <div className="m-2 text-sm font-light bg-indigo-50 text-indigo-900 font-normal p-2 px-3 rounded-sm">
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
