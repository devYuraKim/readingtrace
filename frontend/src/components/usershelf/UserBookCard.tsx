import { useState } from 'react';
import { VisibilitySlug } from '@/constants/reading-status.constants';
import { useDeleteUserBook } from '@/queries/book-status.mutation';
import { useGetUserBook } from '@/queries/book-status.query';
import { useAuthStore } from '@/store/useAuthStore';
import { UserBookCardProps } from '@/types/props.types';
import { useQueryClient } from '@tanstack/react-query';
import { NotepadText, Sparkles } from 'lucide-react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import BookStartDialog from '../BookStartDialog/BookStartDialog';
import { ReadingProgressPopover } from './ReadingProgressPopover';

const UserBookCard = ({ data: userBook }: UserBookCardProps) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [searchParams] = useSearchParams();
  const shelfId = Number(searchParams.get('shelfId'));
  const shelfSlug = searchParams.get('shelfSlug');

  const [dialogOpen, setDialogOpen] = useState(false);
  const [isPopoverOpen, setIsPopoverOpen] = useState(false);

  const userId = useAuthStore((state) => state.user?.userId);
  const bookId = userBook.bookId;
  const deleteMutation = useDeleteUserBook(userId, bookId, shelfId, shelfSlug);

  const handleClickBookCard = () => {
    setDialogOpen(true);
  };

  const handleClickChats = (e: React.MouseEvent) => {
    e.stopPropagation();
    navigate(`/users/${userId}/books/${bookId}/chats`);
  };

  const handleClickNotes = (e: React.MouseEvent) => {
    e.stopPropagation();
    navigate(`/users/${userId}/books/${bookId}/notes`);
  };

  const handleClickEdit = () => {
    alert('edit');
  };

  const handleClickDelete = () => {
    deleteMutation.mutate();
  };

  const { data: userBookRecord, isPending } = useGetUserBook(userId, bookId);

  const handleClickProgress = (e: React.MouseEvent) => {
    e.stopPropagation();
    setIsPopoverOpen(true);
  };

  return (
    <>
      <div
        className="flex items-start gap-x-7 py-5 w-[80%] mx-auto cursor-pointer rounded-lg border border-muted-foreground/10 bg-white p-3 shadow-sm hover:shadow-lg hover:shadow-gray-300 hover:border-gray-200"
        onClick={handleClickBookCard}
      >
        <div className="flex gap-3 ml-5 my-auto">
          <img
            className="h-24 w-18 rounded-md object-cover"
            src={userBook.imageLinks}
            alt={userBook.title}
          />
        </div>

        <div>
          <div className="items-center w-50">
            <span className="text-xs p-2 py-0.5 bg-muted rounded-full mr-1">
              {userBook.visibility === VisibilitySlug.FRIENDS && '👥 '}
              {userBook.visibility === VisibilitySlug.PUBLIC && '🌍 '}
              {userBook.visibility === VisibilitySlug.PRIVATE && '🔒 '}

              {userBook.visibility}
            </span>
            <span className="text-xs p-2 py-0.5 bg-muted rounded-full">
              {userBook.status}
            </span>

            <div className="my-1">
              <h3 className="font-semibold text-foreground truncate">
                {userBook.title}
              </h3>
              <p className="text-muted-foreground text-sm tracking-tight leading-tight truncate">
                {userBook.authors}
              </p>
            </div>
            <div
              className={`text-sm mt-1 ${userBook.rating ? 'text-black' : 'text-muted-foreground text-xs'}`}
            >
              ⭐ {userBook.rating ? '⭐ '.repeat(userBook.rating - 1) : '-'}
            </div>
          </div>
        </div>

        <div className="h-full border-r-1 border-muted-foreground/10"> </div>

        <div
          className="flex flex-col h-full w-35 justify-between gap-y-2 shadow-md px-2.5 py-2 rounded-lg group bg-gray-100 text-xs text-black hover:ring-1 hover:ring-lime-100 hover:shadow-sky-100 hover:bg-white"
          onClick={handleClickProgress}
        >
          <ReadingProgressPopover
            open={isPopoverOpen}
            onOpenChange={setIsPopoverOpen}
            totalPages={userBook.pageCount}
          />

          <div className="flex flex-col items-end">
            <span className="flex gap-2 mt-1 tracking-tight">
              123 of {userBook.pageCount} p (100%)
            </span>
          </div>
          <div>
            <div className="w-full group-hover:bg-gray-200/80 rounded-sm h-2 bg-white">
              <div className="w-[30%] bg-gray-600 rounded-sm h-2 group-hover:bg-gradient-to-r group-hover:to-lime-500 group-hover:from-sky-500"></div>
            </div>
          </div>

          <div className="text-xs">
            <div>
              <span>Start: </span>
              <span className="tracking-tight">
                {userBook.startDate
                  ? new Date(userBook.startDate).toLocaleString('en-GB', {
                      year: '2-digit',
                      month: 'short',
                      day: '2-digit',
                    })
                  : '-'}
              </span>
            </div>

            <div>
              <span>End: </span>
              <span className="tracking-tight">
                {userBook.endDate
                  ? new Date(userBook.endDate).toLocaleString('en-GB', {
                      year: '2-digit',
                      month: 'short',
                      day: '2-digit',
                    })
                  : '-'}
              </span>
            </div>
          </div>
        </div>

        <div className="flex flex-col items-center h-full text-xs text-black gap-y-3">
          <span
            className="flex items-center gap-0.5 rounded-lg px-3 py-1 shadow-sm bg-gray-100 hover: bg-gradient-to-r hover:from-rose-500 hover:to-amber-500 hover:text-white"
            onClick={handleClickNotes}
          >
            <NotepadText className="w-3 h-3" />
            notes
          </span>
          <span
            className="flex items-center gap-0.5 rounded-lg px-3 py-1 shadow-sm bg-gray-100 hover:bg-gradient-to-r hover:from-indigo-500 hover:to-teal-500 hover:text-white"
            onClick={handleClickChats}
          >
            <Sparkles className="w-3 h-3" />
            chats
          </span>
          {/* <span
            className="flex items-center gap-0.5 rounded-lg px-3 py-1 shadow-sm bg-gray-200 hover:bg-gradient-to-r hover:from-black hover:to-pink-500 hover:text-white"
            onClick={handleClickChats}
          >
            <Ellipsis className="w-3 h-3" />
            more
          </span> */}
        </div>

        {/* <div className="text-xs text-muted-foreground flex flex-col justify-center pr-4">
          <div>
            Added:
            {userBook.userReadingStatusCreatedAt
              ? new Date(userBook.userReadingStatusCreatedAt).toLocaleString(
                  'en-GB',
                  {
                    year: '2-digit',
                    month: 'short',
                    day: '2-digit',
                  },
                )
              : '-'}
          </div>
          <div>
            Updated:
            {userBook.userReadingStatusUpdatedAt
              ? new Date(userBook.userReadingStatusUpdatedAt).toLocaleString(
                  'en-GB',
                  {
                    year: '2-digit',
                    month: 'short',
                    day: '2-digit',
                  },
                )
              : '-'}
          </div>
        </div>

        <div
          className="flex flex-col gap-2 items-center justify-center"
          onClick={(e) => e.stopPropagation()}
        >
          <Tooltip>
            <TooltipTrigger>
              <SquarePen className="cursor-pointer" onClick={handleClickEdit} />
            </TooltipTrigger>

            <TooltipContent className="bg-black text-white p-1 rounded-sm">
              <TooltipArrow />
              <p>Edit</p>
            </TooltipContent>
          </Tooltip>
          <Tooltip>
            <TooltipTrigger>
              <Trash2 className="cursor-pointer" onClick={handleClickDelete} />
            </TooltipTrigger>

            <TooltipContent
              className="bg-black text-white p-1 rounded-sm"
              onClick={handleClickDelete}
            >
              <TooltipArrow />
              <p>Delete</p>
            </TooltipContent>
          </Tooltip>
        </div> */}
      </div>

      {dialogOpen && (
        <BookStartDialog
          open={dialogOpen}
          onOpenChange={setDialogOpen}
          book={userBook}
          initialData={userBookRecord}
        />
      )}
    </>
  );
};

export default UserBookCard;
