import { UserReadingStatusDto } from './reading-status.types';

export type BookDto = {
  bookId: number | null;
  externalId: string | null;
  title: string | null;
  authors: string[] | null;
  imageLinks: string | null;
  publisher: string | null;
  publishedDate: string | null;
  description: string | null;
  isbn10: string | null;
  isbn13: string | null;
  pageCount: number | null;
  mainCategory: string | null;
  categories: string[] | null;
  averageRating: number | null;
  ratingsCount: number | null;
  language: string | null;
};

export type UserBookDto = BookDto & UserReadingStatusDto;
