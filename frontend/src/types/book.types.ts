import { UserReadingStatusDto } from './reading-status.types';

export type BookDto = {
  bookId: number | null | undefined;
  externalId: string | null | undefined;
  title: string | null | undefined;
  authors: string[] | null | undefined;
  imageLinks: string | null | undefined;
  publisher: string | null | undefined;
  publishedDate: string | null | undefined;
  description: string | null | undefined;
  isbn10: string | null | undefined;
  isbn13: string | null | undefined;
  pageCount: number | null | undefined;
  mainCategory: string | null | undefined;
  categories: string[] | null | undefined;
  averageRating: number | null | undefined;
  ratingsCount: number | null | undefined;
  language: string | null | undefined;
  isAdded: boolean | null | undefined;
};

export type UserBookDto = BookDto & UserReadingStatusDto;

export type BookSearchResultDto = {
  totalItems: number | null;
  books: BookDto[];
};
