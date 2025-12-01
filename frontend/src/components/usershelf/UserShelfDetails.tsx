import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { UserBookDto } from '@/types/book.types';
import { useQuery } from '@tanstack/react-query';
import { useSearchParams } from 'react-router-dom';
import AddBookCta from './AddBookCta';
import UserBookCard from './UserBookCard';

const UserShelfDetails = () => {
  const [searchParams] = useSearchParams();
  const shelfId = Number(searchParams.get('shelfId'));
  const shelfSlug = searchParams.get('shelfSlug');

  const userId = useAuthStore((state) => state.user?.userId);

  let url = `/users/${userId}/books`;
  if (shelfId) {
    url += `?shelfId=${shelfId}`;
  } else if (shelfSlug) {
    url += `?shelfSlug=${shelfSlug}`;
  }

  const { data: userBooks, isPending } = useQuery<UserBookDto[]>({
    queryFn: async () => {
      const res = await apiClient.get(url);
      return res.data;
    },
    queryKey: ['userShelf', userId, shelfId ?? shelfSlug],
    enabled: !!userId && (!!shelfId || !!shelfSlug),
  });

  return (
    <>
      {!isPending &&
        userBooks?.map((userBook) => (
          <UserBookCard key={userBook.bookId} data={userBook} />
        ))}

      {!isPending && userBooks?.length === 0 && <AddBookCta />}
    </>
  );
};

export default UserShelfDetails;
