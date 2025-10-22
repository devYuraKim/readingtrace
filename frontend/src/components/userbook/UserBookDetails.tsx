import { useGetUserBook } from '@/queries/book-status.query';
import { useAuthStore } from '@/store/useAuthStore';
import { useParams } from 'react-router-dom';

const UserBookDetails = () => {
  const { bookId } = useParams();
  const numericBookId = Number(bookId);
  const userId = useAuthStore((state) => state.user?.userId);

  const { data: userBook, isPending } = useGetUserBook(userId, numericBookId);

  return (
    <>
      BookDetails
      <div>{userBook?.title}</div>
    </>
  );
};

export default UserBookDetails;
