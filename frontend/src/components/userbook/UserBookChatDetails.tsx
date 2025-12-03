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
          <div className="font-semibold text-lg text-indigo-950 leading-tight">
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
         rounded-md shadow-md mb-4 text-xs flex gap-2 items-center justify-center bg-indigo-50 text-indigo-600"
          >
            <Spinner className="stroke-indigo-600" /> AI generating a
            response...
          </Skeleton>
        )}
        {!isPendingUserBookChat && userBookChats?.length === 0 && (
          <AddChatCta />
        )}
        {!isPendingUserBookChat &&
          userBookChats?.map((userBookChat: ChatRecordDto) => (
            <div
              key={userBookChat.chatRecordId}
              className="border-1 border-indigo-50 mb-5 rounded-xl p-2 shadow-md shadow-indigo-50 hover:border-indigo-400/50 hover:border-1.5 hover:bg-gradient-to-l hover:to-indigo-50/20 hover:from-teal-50/20 group"
            >
              <div className="flex justify-between m-2 font-extralight text-xs">
                <div className="flex gap-1 items-center mb-2">
                  <Tooltip>
                    <TooltipTrigger>
                      <Bookmark
                        className={`cursor-pointer mx-1 transition-all duration-300 ease-in-out stroke-[1.5px] hover:scale-110 ${
                          userBookChat.isBookmarked
                            ? 'stroke-indigo-500 fill-indigo-700 drop-shadow-sm' // Active: Filled, soft indigo, slight glow
                            : 'stroke-slate-300 hover:stroke-indigo-700 hover:fill-indigo-50'
                        }`}
                        width={22}
                        height={22}
                        onClick={() =>
                          handleClickBookmark(userBookChat.chatRecordId)
                        }
                      />
                    </TooltipTrigger>
                    <TooltipContent
                      align="start"
                      className="-mb-[1.8rem] ml-7 bg-indigo-700 text-white px-2 py-1 font-normal rounded-sm"
                    >
                      {userBookChat.isBookmarked
                        ? 'Remove Bookmark'
                        : 'Add Bookmark'}
                    </TooltipContent>
                  </Tooltip>
                  <div className="flex justify-between items-center text-xs text-slate-500">
                    <div className="flex items-center gap-2">
                      <span className="px-2 py-0.5 rounded-full border border-slate-200 font-medium text-[10px] uppercase tracking-tight">
                        {userBookChat.chatModel}
                      </span>
                      <span className="font-light tracking-tight">
                        {new Date(userBookChat.timestamp)
                          .toLocaleString('en-GB', {
                            year: '2-digit',
                            month: 'short',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            hour12: false,
                          })
                          .replace(',', ' at ')}{' '}
                      </span>
                    </div>
                  </div>
                </div>
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <button className="p-1.5 rounded-md hover:shadow-sm transition-colors mr-1">
                      <Ellipsis className="cursor-pointer w-4 h-4 rounded-[0.3rem] stroke-indigo-900 " />
                    </button>
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
                        <Pencil className="w-4 h-4 stroke-indigo-700" />
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
              <div className="m-2 text-sm  text-indigo-900 font-normal p-4 px-5 rounded-lg bg-gradient-to-l to-indigo-50/50 from-teal-50/50 border-1 border-indigo-50 group-hover:border-indigo-200/50">
                {userBookChat.userMessage}
              </div>
              <div className="m-2 text-sm p-2 leading-relaxed text-indigo-950">
                {userBookChat.assistantMessage}
              </div>
            </div>
          ))}
      </div>
      {!isPendingUserBook && (
        <div className="flex sticky bottom-0 bg-white py-5 ">
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
