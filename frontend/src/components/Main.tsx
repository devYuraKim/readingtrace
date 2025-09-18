import React from 'react';
import { Search } from 'lucide-react';
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
import { Label } from '@/components/ui/label';
import { SidebarInput } from '@/components/ui/sidebar';

interface bookType {
  id: string;
  title: string;
  authors: string;
  imageLinks: string;
  publisher: string;
  publishedDate: string;
  description: string;
  isbn_10: string;
  isbn_13: string;
}

const Main = () => {
  //static
  const books = [
    {
      id: '1',
      title: 'A Clockwork Orange',
      authors: 'Anthony Burgess',
      imageLinks: '/a_clockwork_orange.jpg',
    },
    {
      id: '2',
      title: 'Cats Cradle',
      authors: 'Kurt Vonnegut',
      imageLinks: '/cats_cradle.jpg',
    },
    {
      id: '3',
      title: 'Metamorphosis',
      authors: 'Franz Kafka',
      imageLinks: '/metamorphosis.jpg',
    },
    {
      id: '4',
      title: 'Of Human Bondage',
      authors: 'W. Somerset Maugham',
      imageLinks: '/of_human_bondage.jpg',
      description: `A masterpiece of modern literature that mirrors Maugham's own career. 
      
      Of Human Bondage is the first and most autobiographical of Maugham's novels. It is the story of Philip Carey, an orphan eager for life, love and adventure. After a few months studying in Heidelberg, and a brief spell in Paris as a would-be artist, Philip settles in London to train as a doctor. And that is where he meets Mildred, the loud but irresistible waitress with whom he plunges into a formative, tortured and masochistic affair which very nearly ruins him.`,
      publishedDate: 'February 23, 2010',
      publisher: 'Random House',
      isbn_10: '1407016458',
      isbn_13: '9781407016450',
    },
    {
      id: '5',
      title: 'Slaughterhouse Five',
      authors: 'Kurt Vonnegut',
      imageLinks: '/slaughterhouse_five.jpg',
    },
    {
      id: '6',
      title: 'Steeppenwolf',
      authors: 'Hermann Hesse',
      imageLinks: '/steppenwolf.jpg',
    },
    {
      id: '7',
      title: 'The Bhagavad Gita',
      authors: 'N/A',
      imageLinks: '/the_bhagavad_gita.jpg',
    },
    {
      id: '8',
      title: 'The Castle',
      authors: 'Franz Kafka',
      imageLinks: '/the_castle.jpg',
    },
    {
      id: '9',
      title: 'The Trial',
      authors: 'Franz Kafka',
      publisher: 'Penguin Books Ltd (UK)',
      publishedDate: '2024',
      imageLinks: '/the_trial.jpg',
    },
  ];

  const [dialogOpen, setDialogOpen] = React.useState(false);
  const [selectedBook, setSelectedBook] = React.useState<bookType | null>(null);

  return (
    <div className="relative flex flex-1 flex-col items-center justify-center">
      <div className="relative z-1 flex flex-col items-center justify-center gap-5">
        {/* TODO: Conditional rendering based on data availability */}
        <h2 className="!m-0 !p-0 text-center !text-3xl leading-none !font-bold text-black">
          Start your reading journey
        </h2>

        <Carousel
          opts={{
            align: 'start',
          }}
          className="w-full max-w-xl"
        >
          <CarouselContent className="h-full">
            {books.map((book) => (
              <CarouselItem key={book.id} className="md:basis-1/4 lg:basis-1/3">
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
              </CarouselItem>
            ))}
          </CarouselContent>
          <CarouselPrevious />
          <CarouselNext />
        </Carousel>

        {selectedBook && (
          <BookStartDialog
            open={dialogOpen}
            onOpenChange={setDialogOpen}
            book={selectedBook}
          />
        )}

        <div className="relative">
          <Label htmlFor="search" className="sr-only">
            Search
          </Label>
          <SidebarInput
            id="search"
            placeholder="Search the docs..."
            className="pl-8"
          />
          <Search className="pointer-events-none absolute top-1/2 right-4 size-4 -translate-y-1/2 opacity-50 select-none" />
        </div>
      </div>
    </div>
  );
};

export default Main;
