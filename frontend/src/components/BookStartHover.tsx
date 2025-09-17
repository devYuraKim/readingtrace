import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from '@/components/ui/hover-card';

interface bookType {
  id: string;
  title: string;
  authors: string;
  imageLinks: string;
  //   publisher: string;
  publishedDate: string;
  //   description: string;
  //   isbn_10: string;
  //   isbn_13: string;
}

export default function BookStartHover({ book }: { book: bookType }) {
  return (
    <HoverCard openDelay={0} closeDelay={1}>
      <HoverCardTrigger asChild>
        <img src={book.imageLinks} alt={book.title} />
      </HoverCardTrigger>
      <HoverCardContent className="w-80">
        <div className="flex justify-between gap-4">
          <div className="space-y-1">
            <h4 className="text-sm font-semibold">{book.title}</h4>
            <p className="text-sm">By {book.authors}</p>
            <div className="text-muted-foreground text-xs">
              {book.publishedDate}
            </div>
          </div>
        </div>
      </HoverCardContent>
    </HoverCard>
  );
}
