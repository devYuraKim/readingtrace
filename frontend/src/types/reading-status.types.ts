import { ReadingStatusFormValues } from '@/schemas/reading-status.schemas';

export type ReadingStatusInitialFormValues = {
  userReadingStatusId: number;
  userId: number;
  shelfId: number;
  bookId: number;
  status: ReadingStatusFormValues['status'];
  visibility: ReadingStatusFormValues['visibility'];
  rating: number;
  startDate: Date;
  endDate: Date;
};
