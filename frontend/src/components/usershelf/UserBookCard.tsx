import { useState } from 'react';
import { useDeleteUserBook } from '@/queries/book-status.mutation';
import { useAuthStore } from '@/store/useAuthStore';
import { UserBookCardProps } from '@/types/props.types';
import { TooltipArrow } from '@radix-ui/react-tooltip';
import { useQueryClient } from '@tanstack/react-query';
import { SquarePen, Trash2 } from 'lucide-react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import BookStartDialog from '../BookStartDialog/BookStartDialog';
import { Tooltip, TooltipContent, TooltipTrigger } from '../ui/tooltip';

const UserBookCard = ({ data: userBook }: UserBookCardProps) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [searchParams] = useSearchParams();
  const shelfId = Number(searchParams.get('shelfId'));
  const shelfSlug = searchParams.get('shelfSlug');

  const [dialogOpen, setDialogOpen] = useState(false);

  const userId = useAuthStore((state) => state.user?.userId);
  const bookId = userBook.bookId;
  const deleteMutation = useDeleteUserBook(userId, bookId, shelfId, shelfSlug);

  const handleClickBookCard = () => {
    setDialogOpen(true);
  };

  const handleClickChats = () => {
    queryClient.setQueryData(['userBook', userId, bookId], userBook);
    navigate(`/users/${userId}/books/${bookId}/chats`);
  };

  const handleClickNotes = () => {
    navigate(`/users/${userId}/books/${bookId}/notes`);
  };

  const handleClickEdit = () => {
    alert('edit');
  };

  const handleClickDelete = () => {
    deleteMutation.mutate();
  };

  return (
    <>
      <div
        className="flex items-start justify-between w-full cursor-pointer rounded-lg border border-muted-foreground/10 bg-white p-3 shadow-sm hover:shadow-lg transition-shadow"
        onClick={handleClickBookCard}
      >
        <div className="flex gap-3">
          <img
            className="h-24 w-18 rounded-md object-cover"
            src={userBook.imageLinks}
            alt={userBook.title}
          />
        </div>

        <div className="flex flex-col justify-between">
          <div className="flex gap-2 mt-1 items-center">
            <span className="text-xs text-muted-foreground capitalize">
              {userBook.visibility}
            </span>
          </div>

          <div>
            <h3 className="font-semibold text-base text-foreground">
              {userBook.title}
            </h3>
            <p className="text-sm text-muted-foreground">{userBook.authors}</p>
          </div>

          <div className="flex items-center gap-2 text-xs text-muted-foreground mt-1">
            <div>⭐ {userBook.rating ?? 'No rating'}</div>
            <span className="text-xs p-2 py-0.5 bg-muted rounded-full capitalize">
              {userBook.status}
            </span>
          </div>
        </div>

        <div className="text-xs text-muted-foreground flex flex-col justify-center pr-4">
          <div>
            Start:
            {userBook.startDate
              ? new Date(userBook.startDate).toLocaleString('en-GB', {
                  year: '2-digit',
                  month: 'short',
                  day: '2-digit',
                })
              : '-'}
          </div>

          <div>
            End:
            {userBook.startDate
              ? new Date(userBook.endDate).toLocaleString('en-GB', {
                  year: '2-digit',
                  month: 'short',
                  day: '2-digit',
                })
              : '-'}
          </div>
          <div>
            Added:
            {userBook.userReadingStatusCreatedAt
              ? new Date(userBook.userReadingStatusCreatedAt).toLocaleString(
                  'en-GB',
                  {
                    year: '2-digit',
                    month: 'short',
                    day: '2-digit',
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: false,
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
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: false,
                  },
                )
              : '-'}
          </div>
        </div>

        <div className="flex flex-col text-xs text-white text-shadow-xs gap-y-2">
          <span
            className="rounded-full px-2 py-1 bg-purple-500 shadow-sm hover:bg-purple-700"
            onClick={handleClickNotes}
          >
            NOTES
          </span>
          <span
            className="rounded-full px-2 py-1 bg-indigo-500 shadow-sm hover:bg-indigo-700"
            onClick={handleClickChats}
          >
            CHATS
          </span>
        </div>

        <div
          className="flex flex-col gap-2 items-center justify-center"
          onClick={(e) => e.stopPropagation()}
        >
          {' '}
          <Tooltip>
            <TooltipTrigger>
              <SquarePen
                className="cursor-pointer hover:stroke-accent-foreground/50"
                onClick={handleClickEdit}
              />
            </TooltipTrigger>

            <TooltipContent className="bg-black text-white p-1 rounded-sm">
              <TooltipArrow />
              <p>Edit</p>
            </TooltipContent>
          </Tooltip>
          <Tooltip>
            <TooltipTrigger>
              <Trash2
                className="cursor-pointer hover:stroke-accent-foreground/50"
                onClick={handleClickEdit}
              />
            </TooltipTrigger>

            <TooltipContent className="bg-black text-white p-1 rounded-sm">
              <TooltipArrow />
              <p>Delete</p>
            </TooltipContent>
          </Tooltip>
        </div>
      </div>
      {dialogOpen && (
        <BookStartDialog
          open={dialogOpen}
          onOpenChange={setDialogOpen}
          book={userBook}
        />
      )}
    </>
  );
};

export default UserBookCard;
