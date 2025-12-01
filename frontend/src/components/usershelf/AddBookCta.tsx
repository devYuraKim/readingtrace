import { useAuthStore } from '@/store/useAuthStore';
import { BookAlert } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { Button } from '../ui/button';
import {
  Empty,
  EmptyContent,
  EmptyDescription,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
} from '../ui/empty';

const AddBookCta = () => {
  const navigate = useNavigate();
  const userId = useAuthStore((state) => state.user?.userId);
  const handleClick = () => {
    navigate(`/users/${userId}/search`);
  };

  return (
    <Empty className="-mt-40">
      <EmptyHeader>
        <EmptyMedia variant="icon">
          <BookAlert />
        </EmptyMedia>
        <EmptyTitle>No Books Yet</EmptyTitle>
        <EmptyDescription>
          This bookshelf is empty. Add some books to start tracking your reading
          journey and discover new favorites!
        </EmptyDescription>
      </EmptyHeader>
      <EmptyContent>
        <div className="flex gap-2">
          <Button className="cursor-pointer" onClick={handleClick}>
            Search Books
          </Button>
        </div>
      </EmptyContent>
    </Empty>
  );
};

export default AddBookCta;
