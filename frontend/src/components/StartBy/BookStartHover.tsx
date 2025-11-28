import { BookDto } from '@/types/book.types';
import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from '@/components/ui/hover-card';

export default function BookStartHover({ book }: { book: BookDto }) {
  return (
    <HoverCard openDelay={0} closeDelay={1}>
      <HoverCardTrigger asChild></HoverCardTrigger>
      <HoverCardContent className="w-80">
        <div className="flex justify-between gap-4">
          <div className="space-y-1">
            <h4 className="text-sm font-semibold">{book.title}</h4>
            <p className="text-sm">
              By{' '}
              {!book?.authors || book.authors.length === 0
                ? 'Author N/A'
                : book.authors.join(', ')}
            </p>
            <div className="text-muted-foreground text-xs">
              {book.publishedDate}
            </div>
          </div>
        </div>
      </HoverCardContent>
    </HoverCard>
  );
}
