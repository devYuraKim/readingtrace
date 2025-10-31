import { useDeleteUserBook } from '@/queries/book-status.mutation';
import { useAuthStore } from '@/store/useAuthStore';
import { UserBookCardProps } from '@/types/props.types';
import { TooltipArrow } from '@radix-ui/react-tooltip';
import { useQueryClient } from '@tanstack/react-query';
import { SquarePen, Trash2 } from 'lucide-react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { Tooltip, TooltipContent, TooltipTrigger } from '../ui/tooltip';

const UserBookCard = ({ data: userBook }: UserBookCardProps) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [searchParams] = useSearchParams();
  const shelfId = Number(searchParams.get('shelfId'));
  const shelfSlug = searchParams.get('shelfSlug');

  const userId = useAuthStore((state) => state.user?.userId);
  const bookId = userBook.bookId;
  const deleteMutation = useDeleteUserBook(userId, bookId, shelfId, shelfSlug);

  const handleClick = () => {
    queryClient.setQueryData(['userBook', userId, bookId], userBook);
    navigate(`/users/${userId}/books/${bookId}`);
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
        onClick={handleClick}
        className="flex items-start justify-between w-full cursor-pointer rounded-lg border border-muted-foreground/10 bg-white p-3 shadow-sm hover:shadow-lg transition-shadow"
      >
        <div className="flex gap-3">
          <img
            className="h-24 w-18 rounded-md object-cover"
            src={userBook.imageLinks}
            alt={userBook.title}
          />
        </div>

        <div className="flex flex-col justify-between">
          <div className="flex gap-2 mt-1">
            <span className="text-xs p-2 py-0.5 bg-muted rounded-full capitalize">
              {userBook.status}
            </span>
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

          <div className="text-xs text-muted-foreground mt-1">
            <div>⭐ {userBook.rating ?? 'No rating'}</div>
          </div>
        </div>

        <div className="text-xs text-muted-foreground flex flex-col justify-center text-right pr-4">
          <div>Start: {userBook.startDate?.toLocaleDateString() ?? '—'}</div>
          <div>End: {userBook.endDate?.toLocaleDateString() ?? '—'}</div>
          <div> hey </div>
          <div>Added: {userBook.userReadingStatusCreatedAt}</div>
          <div>Updated: {userBook.userReadingStatusUpdatedAt}</div>
        </div>

        <div>empty</div>

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
    </>
  );
};

export default UserBookCard;
