import { useState } from 'react';
import { useGetBookStatus } from '@/queries/book-status.query';
import { useAuthStore } from '@/store/useAuthStore';
import BookStartDialog from '@/components/BookStartDialog/BookStartDialog';
import { Card, CardContent } from '@/components/ui/card';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';
import { BookCollection } from '@/lib/books';
import StartByBookCollectionCTA from './StartByBookCollectionCTA';
import StartByBookSearchLink from './StartByBookSearchLink';

const StartByBookCollection = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedBookId, setSelectedBookId] = useState<number>();

  const userId = useAuthStore.getState().user?.userId;

  const { data: userBookRecord, isPending } = useGetBookStatus(
    userId,
    selectedBookId,
  );

  const handleOnClickBook = (bookId: number) => {
    setSelectedBookId(bookId);
    setDialogOpen(true);
  };

  return (
    <div className="relative flex flex-1 flex-col items-center justify-start mt-20">
      <div className="relative z-1 flex flex-col items-center justify-center">
        <StartByBookCollectionCTA />

        <Carousel
          opts={{
            align: 'start',
          }}
          className="w-full max-w-xl mb-15"
        >
          <CarouselContent className="h-full">
            {BookCollection.map((book) => (
              <CarouselItem
                key={book.bookId}
                className="md:basis-1/4 lg:basis-1/3"
              >
                <Card className="cursor-pointer">
                  <CardContent onClick={() => handleOnClickBook(book.bookId)}>
                    <img
                      className="rounded-sm"
                      src={book.imageLinks}
                      alt={book.title}
                    />
                  </CardContent>
                </Card>
              </CarouselItem>
            ))}
          </CarouselContent>
          <CarouselPrevious />
          <CarouselNext />
        </Carousel>

        {selectedBookId && !isPending && (
          <BookStartDialog
            key={selectedBookId}
            open={dialogOpen}
            onOpenChange={setDialogOpen}
            selectedBookId={selectedBookId}
            initialData={userBookRecord}
          />
        )}

        <StartByBookSearchLink />
      </div>
    </div>
  );
};

export default StartByBookCollection;
