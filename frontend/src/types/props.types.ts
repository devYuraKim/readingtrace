import { BookDto, UserBookDto } from './book.types';
import { ReadingStatusInitialFormValues } from './reading-status.types';

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

export type OnboardingStepProps = {
  setCanProceed: React.Dispatch<React.SetStateAction<boolean>>;
};

export type ReadingProgressPopoverProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  totalPages: number;
  currentPage: number;
  bookId: number;
  userReadingStatusId: number;
};
