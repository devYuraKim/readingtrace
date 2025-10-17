import React from 'react';
import { apiClient } from '@/queries/axios';
import { Search } from 'lucide-react';
import { toast } from 'sonner';
import { Input } from '@/components/ui/input';
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from '@/components/ui/pagination';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { BookDto } from '@/lib/books';
import BookStartDialog from '../BookStartDialog/BookStartDialog';

interface responseType {
  totalItems: number | null;
  books: BookDto[];
}

const StartByBookSearch = () => {
  const [searchType, setSearchType] = React.useState('title');
  const [searchWord, setSearchWord] = React.useState('');
  const [results, setResults] = React.useState<responseType>({
    totalItems: null,
    books: [],
  });

  const [page, setPage] = React.useState(1);
  const [booksPerPage, setBooksPerPage] = React.useState(6);

  const [dialogOpen, setDialogOpen] = React.useState(false);
  const [selectedBook, setSelectedBook] = React.useState<BookDto | null>(null);

  const handleSearch = async (
    searchType: string,
    searchWord: string,
    page: number,
  ) => {
    const normalizedSearchWord = searchWord
      .trim()
      .replace(/\s+/g, ' ')
      .toLowerCase();

    if (normalizedSearchWord === '') {
      toast.error('Please enter a search word');
      return;
    }

    const startIndex = (page - 1) * booksPerPage;

    try {
      const response = await apiClient.get(
        '/book/searchBook?searchType=' +
          searchType +
          '&searchWord=' +
          searchWord +
          '&startIndex=' +
          startIndex +
          '&maxResults=' +
          booksPerPage,
      );
      const data = response.data;
      setResults(data);
      setPage(page);
    } catch (error) {
      console.error('Stream error:', error);
    }
  };

  const maxVisibleButtons = 10;
  const totalPages = Math.ceil((results.totalItems ?? 0) / booksPerPage);
  const startPage = Math.max(1, page - Math.floor(maxVisibleButtons / 2));
  const endPage = Math.min(startPage + maxVisibleButtons - 1, totalPages);

  return (
    <div className="relative flex flex-1 flex-col items-center justify-start mt-10">
      {selectedBook && (
        <BookStartDialog
          open={dialogOpen}
          onOpenChange={setDialogOpen}
          book={selectedBook}
        />
      )}
      <div className="relative z-1 flex items-center justify-center gap-5 w-full">
        <div className="relative flex items-center border border-gray-300 rounded-lg overflow-hidden">
          <Select onValueChange={(value) => setSearchType(value)}>
            <SelectTrigger className="w-30 rounded-none rounded-l-lg">
              <SelectValue placeholder="Title" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectLabel>Search by</SelectLabel>
                <SelectItem value="title">Title</SelectItem>
                <SelectItem value="author">Author</SelectItem>
                <SelectItem value="isbn">ISBN</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>

          <Input
            id="searchWord"
            placeholder="Search"
            className="flex-1 w-md rounded-none rounded-r-lg pl-3"
            value={searchWord}
            onChange={(e) => setSearchWord(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                handleSearch(searchType, searchWord, 1);
              }
            }}
          />

          <Search
            className="pointer-events-none absolute top-1/2 right-3 size-4 -translate-y-1/2 opacity-50 select-none"
            onClick={() => handleSearch(searchType, searchWord, 1)}
          />
        </div>
        {results.totalItems && (
          <p className="text-sm">
            {results.totalItems.toLocaleString()} results
          </p>
        )}
      </div>
      <div className="grid grid-cols-2 gap-4 mt-10 mb-5">
        {results.books.map((book) => (
          <div
            key={book.bookId}
            className="p-2 border flex flex-row gap-4 hover:bg-muted-foreground/10 hover:cursor-pointer"
            onClick={() => {
              setSelectedBook(book);
              setDialogOpen(true);
            }}
          >
            <div className="w-15">
              <img
                src={
                  !book.imageLinks?.trim()
                    ? '/placeholder.png'
                    : book.imageLinks
                }
                alt={book.title}
                className="w-full object-cover text-xs text-muted-foreground"
              />
            </div>

            <div className="w-4/5 flex flex-col gap-1">
              <h3 className="font-bold text-sm">{book.title}</h3>
              <p className="text-xs text-muted-foreground">
                {!book.authors?.trim() ? 'Author N/A' : book.authors}
              </p>
              <div className="flex flex-col gap-y-0.5 text-xs text-muted-foreground">
                <div>
                  {!book.publisher?.trim() ? 'Publisher N/A' : book.publisher} |{' '}
                  {!book.publishedDate?.trim()
                    ? 'Published Date N/A'
                    : book.publishedDate}
                </div>
                <div>
                  {!book.isbn10?.trim() ? 'ISBN10 N/A' : book.isbn10} |{' '}
                  {!book.isbn13?.trim() ? 'ISBN13 N/A' : book.isbn13}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {results.totalItems && results.totalItems > 0 && (
        <Pagination className="mt-4">
          <PaginationContent>
            <PaginationItem>
              <PaginationPrevious
                href="#"
                onClick={(e) => {
                  e.preventDefault();
                  if (page > 1) handleSearch(searchType, searchWord, page - 1);
                }}
              />
            </PaginationItem>

            {Array.from({ length: endPage - startPage + 1 }, (_, i) => {
              const pageNumber = startPage + i;
              return (
                <PaginationItem key={pageNumber}>
                  <PaginationLink
                    href="#"
                    isActive={page === pageNumber}
                    onClick={(e) => {
                      e.preventDefault();
                      handleSearch(searchType, searchWord, pageNumber);
                    }}
                  >
                    {pageNumber}
                  </PaginationLink>
                </PaginationItem>
              );
            })}

            <PaginationItem>
              <PaginationNext
                href="#"
                onClick={(e) => {
                  e.preventDefault();
                  if (page < totalPages)
                    handleSearch(searchType, searchWord, page + 1);
                }}
              />
            </PaginationItem>
          </PaginationContent>
        </Pagination>
      )}
    </div>
  );
};

export default StartByBookSearch;
