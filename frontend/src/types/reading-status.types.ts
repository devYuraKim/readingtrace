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

export type UserReadingStatusDto = {
  userReadingStatusId: number;
  userId: number;
  shelfId: number;
  status: string;
  visibility: string;
  rating: number;
  startDate: Date;
  endDate: Date;
  userBookStatusMetadata: string;
  //TODO: below to properties are of type LocalDateTime, for now keeping them as string
  userReadingStatusCreatedAt: string;
  userReadingStatusUpdatedAt: string;
};
