import { BookDto, UserBookDto } from './book.types';
import { ReadingStatusInitialFormValues } from './reading-status.types';

// export type BookStartDialogProps = {
//   open: boolean;
//   onOpenChange: (open: boolean) => void;
//   initialData: ReadingStatusInitialFormValues;
//   selectedBookId: number;
// };

export type BookStartDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  initialData: ReadingStatusInitialFormValues;
  book: BookDto;
};

export type UserBookCardProps = {
  data: UserBookDto;
};

export type GetSearchedBooksProps = {
  activeSearchType: string | null;
  activeSearchWord: string | null;
  page: number;
  booksPerPage: number;
};
