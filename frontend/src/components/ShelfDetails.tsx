import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useQuery } from '@tanstack/react-query';
import { useSearchParams } from 'react-router-dom';

const ShelfDetails = () => {
  const [searchParams] = useSearchParams();
  const shelfId = searchParams.get('shelfId');

  const userId = useAuthStore((state) => state.user?.userId);

  const { data: shelfBooks, isPending } = useQuery({
    queryFn: async () => {
      const res = await apiClient.get(
        `/users/${userId}/books?shelfId=${shelfId}`,
      );
      return res.data;
    },
    queryKey: ['userShelf', userId, shelfId],
    enabled: !!userId && !!shelfId,
  });

  return (
    <>
      <div>ShelfDetails</div>
      {!isPending && shelfBooks?.map((book) => <div>{book.bookId}</div>)}
    </>
  );
};

export default ShelfDetails;
