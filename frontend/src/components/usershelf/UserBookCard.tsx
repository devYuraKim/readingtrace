import { useDeleteUserBook } from '@/queries/book-status.mutation';
import { useAuthStore } from '@/store/useAuthStore';
import { UserBookCardProps } from '@/types/props.types';
import { useQueryClient } from '@tanstack/react-query';
import { SquarePen, Trash2 } from 'lucide-react';
import { useNavigate, useSearchParams } from 'react-router-dom';

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

  const handleClickEdit = (e: React.MouseEvent) => {
    e.stopPropagation();
    alert('edit');
  };

  const handleClickDelete = (e: React.MouseEvent) => {
    e.stopPropagation();
    deleteMutation.mutate();
  };

  return (
    <>
      UserBookCard
      <div
        onClick={handleClick}
        className="flex flex-row gap-2 cursor-pointer rounded-sm outline-muted-foreground-5 outline-1"
      >
        <div>
          <img
            className="h-20 w-16 rounded-sm"
            src={userBook.imageLinks}
            alt={userBook.title}
          />
        </div>
        <div>
          <div>{userBook.title}</div>
          <div>{userBook.authors}</div>
        </div>
        <div>
          <div>{userBook.rating ?? 'no rating available'}</div>
          <div>
            {userBook.startDate?.toLocaleDateString() ??
              'no start date available'}
          </div>
          <div>
            {userBook.endDate?.toLocaleDateString() ?? 'no end date available'}
          </div>
        </div>
        <div>
          <div>added on {userBook.userReadingStatusCreatedAt}</div>
          <div>updated on {userBook.userReadingStatusUpdatedAt}</div>
        </div>

        <div>empty</div>

        <div className="flex flex-col gap-y-1">
          <SquarePen
            className="hover:stroke-accent-foreground/40"
            onClick={handleClickEdit}
          />
          <Trash2
            className="hover:stroke-accent-foreground/40"
            onClick={handleClickDelete}
          />
        </div>
      </div>
    </>
  );
};

export default UserBookCard;
