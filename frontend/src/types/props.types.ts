import { ReadingStatusInitialFormValues } from './book-status.types';
import { BookDto } from './book.types';

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
