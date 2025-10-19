import { BookStatusInitialFormValues } from './book-status.types';

export type BookStartDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  initialData: BookStatusInitialFormValues;
  selectedBookId: number;
};
