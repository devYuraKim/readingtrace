import { BookStatusFormValues } from '@/schemas/book-status.schemas';

export type BookStatusInitialFormValues = {
  userId: number;
  shelfId: number;
  bookId: number;
  status: BookStatusFormValues['status'];
  visibility: BookStatusFormValues['visibility'];
  rating: number;
  startDate: Date;
  endDate: Date;
};
