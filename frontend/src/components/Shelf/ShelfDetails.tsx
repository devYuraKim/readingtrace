import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { UserBookDto } from '@/types/book.types';
import { useQuery } from '@tanstack/react-query';
import { useSearchParams } from 'react-router-dom';

const ShelfDetails = () => {
  const [searchParams] = useSearchParams();
  const shelfId = searchParams.get('shelfId');

  const userId = useAuthStore((state) => state.user?.userId);

  const { data: userBooks, isPending } = useQuery<UserBookDto[]>({
    queryFn: async () => {
      const res = await apiClient.get(
        `/users/${userId}/books?shelfId=${shelfId}`,
      );
      console.log(`UserBookDtoList: ${JSON.stringify(res.data)}`);
      return res.data;
    },
    queryKey: ['userShelf', userId, shelfId],
    enabled: !!userId && !!shelfId,
  });

  return (
    <>
      <div>ShelfDetails</div>
      {!isPending && userBooks?.map((userBook) => <div>{userBook.bookId}</div>)}
    </>
  );
};

export default ShelfDetails;
