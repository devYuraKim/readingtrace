import React from 'react';
import { apiClient } from '@/queries/axios';
import { useGetSearchedBooks } from '@/queries/book.query';
import { useAuthStore } from '@/store/useAuthStore';
import { BookDto } from '@/types/book.types';
import { useQuery } from '@tanstack/react-query';
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
import BookStartDialog from '../BookStartDialog/BookStartDialog';
import { Spinner } from '../ui/spinner';

const StartByBookSearch = () => {
  const [searchType, setSearchType] = React.useState('title');
  const [searchWord, setSearchWord] = React.useState('');

  const [activeSearchType, setActiveSearchType] = React.useState<string | null>(
    null,
  );
  const [activeSearchWord, setActiveSearchWord] = React.useState<string | null>(
    null,
  );
  const [page, setPage] = React.useState(1);

  const booksPerPage = 6;
  const [dialogOpen, setDialogOpen] = React.useState(false);
  const [selectedBook, setSelectedBook] = React.useState<BookDto>();

  const userId = useAuthStore((state) => state.user?.userId);

  const {
    data: results,
    isPending,
    isError,
    error,
  } = useGetSearchedBooks({
    activeSearchType,
    activeSearchWord,
    page,
    booksPerPage,
  });

  const handleSearch = () => {
    const normalizedSearchWord = searchWord
      .trim()
      .replace(/\s+/g, ' ')
      .toLowerCase();

    if (normalizedSearchWord === '') {
      toast.error('Please enter a search word');
      return;
    }

    setActiveSearchType(searchType);
    setActiveSearchWord(normalizedSearchWord);
    setPage(1);
  };

  const maxVisibleButtons = 10;
  const totalPages = Math.ceil((results?.totalItems ?? 0) / booksPerPage);
  const startPage = Math.max(1, page - Math.floor(maxVisibleButtons / 2));
  const endPage = Math.min(startPage + maxVisibleButtons - 1, totalPages);

  const { data: addedBookData, isPending: isPendingAddedBook } = useQuery({
    queryKey: [selectedBook, selectedBook?.isAdded],
    queryFn: async () => {
      const res = await apiClient.get(
        `/users/${userId}/books/book?externalId=${selectedBook?.externalId}`,
      );
      return res.data;
    },
    enabled: !!selectedBook?.isAdded,
  });

  return (
    <div className="relative flex flex-1 flex-col items-center justify-start mt-10">
      {selectedBook && !selectedBook.isAdded && (
        <BookStartDialog
          open={dialogOpen}
          onOpenChange={setDialogOpen}
          book={selectedBook}
        />
      )}

      {selectedBook && selectedBook.isAdded && !isPendingAddedBook && (
        <BookStartDialog
          open={dialogOpen}
          onOpenChange={setDialogOpen}
          book={selectedBook}
          initialData={addedBookData}
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
                handleSearch();
              }
            }}
          />

          <Search
            className="absolute top-1/2 right-3 size-4 -translate-y-1/2 opacity-50 cursor-pointer hover:opacity-100"
            onClick={handleSearch}
          />
        </div>

        {results?.totalItems && (
          <p className="text-sm">
            {results.totalItems.toLocaleString()} results
          </p>
        )}
      </div>

      {/* Loading state */}
      {isPending && activeSearchWord && <Spinner className="mt-50 w-10 h-10" />}

      {/* Error state */}
      {isError && (
        <div className="mt-10 text-red-500">
          Error:{' '}
          {error instanceof Error ? error.message : 'Failed to fetch books'}
        </div>
      )}

      {/* Results */}
      {results?.books && results.books.length > 0 && (
        <>
          <div className="grid grid-cols-2 gap-7 mt-10 mb-5">
            {results.books.map((book) => (
              <div key={book.externalId}>
                {book.isAdded && (
                  <div className="absolute m-1 bg-green-700  shadow-md rounded-full border-1 border-white text-white text-[0.5rem] px-2 py-1">
                    ADDED
                  </div>
                )}
                <div
                  className="p-5 shadow-md rounded-sm flex flex-row gap-4 hover:bg-muted-foreground/10 hover:cursor-pointer hover:shadow-xl w-full h-33"
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
                    <h3 className="font-bold text-sm truncate">{book.title}</h3>
                    <p className="text-xs text-muted-foreground truncate">
                      {!book?.authors || book.authors.length === 0
                        ? 'Author N/A'
                        : book.authors.join(', ')}
                    </p>
                    <div className="flex flex-col gap-y-0.5 text-xs text-muted-foreground">
                      <div className="truncate">
                        {!book.publisher?.trim()
                          ? 'Publisher N/A'
                          : book.publisher}{' '}
                        |{' '}
                        {!book.publishedDate?.trim()
                          ? 'Published Date N/A'
                          : book.publishedDate}
                      </div>
                      <div className="truncate">
                        {!book.isbn10?.trim() ? 'ISBN10 N/A' : book.isbn10} |{' '}
                        {!book.isbn13?.trim() ? 'ISBN13 N/A' : book.isbn13}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>

          <Pagination className="mt-4">
            <PaginationContent>
              <PaginationItem>
                <PaginationPrevious
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    if (page > 1) setPage(page - 1);
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
                        setPage(pageNumber);
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
                    if (page < totalPages) setPage(page + 1);
                  }}
                />
              </PaginationItem>
            </PaginationContent>
          </Pagination>
        </>
      )}

      {/* No results */}
      {results?.books && results.books.length === 0 && (
        <div className="mt-10">No books found</div>
      )}
    </div>
  );
};

export default StartByBookSearch;
