import React from 'react';
import { useAuthStore } from '@/store/useAuthStore';
import { BookDto } from '@/types/book.types';
import { Search } from 'lucide-react';
import { toast } from 'sonner';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Input } from '../ui/input';

const StartByBookSearch = () => {
  const [searchType, setSearchType] = React.useState('title');
  const [searchWord, setSearchWord] = React.useState('');
  const [results, setResults] = React.useState<BookDto[]>([]);

  const handleSearch = async (searchType: string, searchWord: string) => {
    const normalizedSearchWord = searchWord
      .trim()
      .replace(/\s+/g, ' ')
      .toLowerCase();

    if (normalizedSearchWord === '') {
      toast.error('Please enter a search word');
      return;
    }

    const accessToken = useAuthStore.getState().accessToken ?? '';

    function getCookie(name: string) {
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=`);
      if (parts.length === 2) return parts.pop()?.split(';').shift();
      return undefined;
    }
    const csrfToken = getCookie('XSRF-TOKEN') ?? '';

    try {
      const response = await fetch(
        `/api/v1/book/reactive/searchBook?searchType=${searchType}&searchWord=${searchWord}`,
        {
          headers: {
            Authorization: accessToken,
            'X-XSRF-TOKEN': csrfToken,
            Accept: 'text/event-stream',
          },
          credentials: 'include',
        },
      );

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }

      const reader = response?.body?.getReader();
      if (!reader) {
        throw new Error('Unable to get stream reader');
      }
      const decoder = new TextDecoder();
      let buffer = '';

      while (true) {
        const { done, value } = await reader.read();
        if (done) break;

        buffer += decoder.decode(value, { stream: true });
        const events = buffer.split('\n\n');
        buffer = events.pop() || ''; // keep incomplete event for next iteration

        for (const event of events) {
          if (event.startsWith('data:')) {
            const jsonStr = event.replace(/^data:\s*/, '');
            try {
              const book = JSON.parse(jsonStr) as BookDto;
              setResults((prev) => [...prev, book]);
            } catch (err) {
              console.error('JSON parse error:', err, jsonStr);
            }
          }
        }
      }
    } catch (error) {
      console.error('Stream error:', error);
    }
  };

  return (
    <div className="relative flex flex-1 flex-col items-center justify-start mt-10">
      <div className="relative z-1 flex flex-col items-center justify-center gap-5">
        <div className="relative flex items-center w-full max-w-xl border border-gray-300 rounded-lg overflow-hidden">
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

          {/* Input */}
          <Input
            id="searchWord"
            placeholder="Search"
            className="flex-1 w-md rounded-none rounded-r-lg pl-3"
            value={searchWord}
            onChange={(e) => setSearchWord(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                handleSearch(searchType, searchWord);
              }
            }}
          />

          {/* Search icon */}
          <Search
            className="pointer-events-none absolute top-1/2 right-3 size-4 -translate-y-1/2 opacity-50 select-none"
            onClick={() => handleSearch(searchType, searchWord)}
          />
        </div>
      </div>

      <div>
        {results.map((book) => (
          <div key={book.bookId} className="p-2 border-b">
            <h3 className="font-bold">{book.title}</h3>
            <p>
              {' '}
              {!book?.authors || book.authors.length === 0
                ? 'Author N/A'
                : book.authors.join(', ')}
            </p>
            <p className="text-sm text-gray-500">
              {book.publisher} ({book.publishedDate})
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default StartByBookSearch;
