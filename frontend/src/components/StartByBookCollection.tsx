import React from 'react';
import { NavLink } from 'react-router-dom';
import BookStartDialog from '@/components/BookStartDialog';
import BookStartHover from '@/components/BookStartHover';
import { Card, CardContent } from '@/components/ui/card';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';

interface bookType {
  bookId: string;
  title: string;
  authors: string;
  imageLinks: string;
  publisher: string;
  publishedDate: string;
  description: string;
  isbn10: string;
  isbn13: string;
}

const StartByBookCollection = () => {
  //static
  const books = [
    {
      bookId: '1',
      title: 'A Clockwork Orange',
      authors: 'Anthony Burgess',
      imageLinks: '/a_clockwork_orange.jpg',
      publisher: '',
      publishedDate: '',
      description: '',
      isbn10: '',
      isbn13: '',
    },
    {
      bookId: '2',
      title: 'Cats Cradle',
      authors: 'Kurt Vonnegut',
      imageLinks: '/cats_cradle.jpg',
      publisher: '',
      publishedDate: '',
      description: '',
      isbn10: '',
      isbn13: '',
    },
    {
      bookId: '3',
      title: 'Metamorphosis',
      authors: 'Franz Kafka',
      imageLinks: '/metamorphosis.jpg',
      publisher: '',
      publishedDate: '',
      description: '',
      isbn10: '',
      isbn13: '',
    },
    {
      bookId: '4',
      title: 'Of Human Bondage',
      authors: 'W. Somerset Maugham',
      imageLinks: '/of_human_bondage.jpg',
      description: `A masterpiece of modern literature that mirrors Maugham's own career. 
      
      Of Human Bondage is the first and most autobiographical of Maugham's novels. It is the story of Philip Carey, an orphan eager for life, love and adventure. After a few months studying in Heidelberg, and a brief spell in Paris as a would-be artist, Philip settles in London to train as a doctor. And that is where he meets Mildred, the loud but irresistible waitress with whom he plunges into a formative, tortured and masochistic affair which very nearly ruins him.`,
      publishedDate: 'February 23, 2010',
      publisher: 'Random House',
      isbn10: '1407016458',
      isbn13: '9781407016450',
    },
    {
      bookId: '5',
      title: 'Slaughterhouse Five',
      authors: 'Kurt Vonnegut',
      imageLinks: '/slaughterhouse_five.jpg',
      publisher: '',
      publishedDate: '',
      description: '',
      isbn10: '',
      isbn13: '',
    },
    {
      bookId: '6',
      title: 'Steeppenwolf',
      authors: 'Hermann Hesse',
      imageLinks: '/steppenwolf.jpg',
      publisher: '',
      publishedDate: '',
      description: '',
      isbn10: '',
      isbn13: '',
    },
    {
      bookId: '7',
      title: 'The Bhagavad Gita',
      authors: 'N/A',
      imageLinks: '/the_bhagavad_gita.jpg',
      publisher: '',
      publishedDate: '',
      description: '',
      isbn10: '',
      isbn13: '',
    },
    {
      bookId: '8',
      title: 'The Castle',
      authors: 'Franz Kafka',
      imageLinks: '/the_castle.jpg',
      publisher: '',
      publishedDate: '',
      description: '',
      isbn10: '',
      isbn13: '',
    },
    {
      bookId: '9',
      title: 'The Trial',
      authors: 'Franz Kafka',
      publisher: 'Penguin Books Ltd (UK)',
      publishedDate: '2024',
      imageLinks: '/the_trial.jpg',
      description: '',
      isbn10: '',
      isbn13: '',
    },
  ];

  const [dialogOpen, setDialogOpen] = React.useState(false);
  const [selectedBook, setSelectedBook] = React.useState<bookType | null>(null);

  return (
    <div className="relative flex flex-1 flex-col items-center justify-start mt-20">
      <div className="relative z-1 flex flex-col items-center justify-center">
        {/* TODO: Conditional rendering based on data availability */}
        <h2 className="mb-10 !p-0 text-center !text-3xl leading-none !font-bold text-black">
          Start your reading journey
        </h2>

        <div className="mb-7">
          Select a book from our curated collection — popular, trending, or
          editor’s choice.
        </div>

        <Carousel
          opts={{
            align: 'start',
          }}
          className="w-full max-w-xl mb-15"
        >
          <CarouselContent className="h-full">
            {books.map((book) => (
              <CarouselItem
                key={book.bookId}
                className="md:basis-1/4 lg:basis-1/3"
              >
                <Card className="cursor-pointer">
                  <CardContent
                    onClick={() => {
                      setSelectedBook(book);
                      setDialogOpen(true);
                    }}
                  >
                    <BookStartHover book={book} />
                  </CardContent>
                </Card>

                <BookStartDialog
                  key={book.bookId}
                  open={dialogOpen && selectedBook?.bookId === book.bookId}
                  onOpenChange={setDialogOpen}
                  book={book}
                />
              </CarouselItem>
            ))}
          </CarouselContent>
          <CarouselPrevious />
          <CarouselNext />
        </Carousel>

        {selectedBook && (
          <BookStartDialog
            key={selectedBook.bookId}
            open={dialogOpen}
            onOpenChange={setDialogOpen}
            book={selectedBook}
          />
        )}

        <NavLink to="searchBook">
          <div className="mt-4 flex items-center gap-2 font-medium hover:underline cursor-pointer">
            <span>Or search for any book you like</span>
            <svg
              className="w-4 h-4"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M9 5l7 7-7 7"
              />
            </svg>
          </div>
        </NavLink>
      </div>
    </div>
  );
};

export default StartByBookCollection;
