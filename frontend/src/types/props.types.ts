import { BookDto } from './book.types';
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
