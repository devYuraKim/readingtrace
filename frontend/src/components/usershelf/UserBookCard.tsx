import { useAuthStore } from '@/store/useAuthStore';
import { UserBookCardProps } from '@/types/props.types';
import { useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';

const UserBookCard = ({ data: userBook }: UserBookCardProps) => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const userId = useAuthStore((state) => state.user?.userId);

  const handleClick = () => {
    queryClient.setQueryData(['userBook', userId, userBook.bookId], userBook);
    navigate(`/users/${userId}/books/${userBook.bookId}`);
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
      </div>
    </>
  );
};

export default UserBookCard;
