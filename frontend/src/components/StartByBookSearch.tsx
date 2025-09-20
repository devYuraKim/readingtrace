import React from 'react';
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
import { Input } from './ui/input';

interface bookType {
  id: string;
  title: string;
  authors: string;
  imageLinks: string;
  publisher: string;
  publishedDate: string;
  description: string;
  isbn_10: string;
  isbn_13: string;
}

const StartByBookSearch = () => {
  const [searchType, setSearchType] = React.useState('title');
  const [searchWord, setSearchWord] = React.useState('');

  const handleSearch = (searchType: string, searchWord: string) => {
    const normalizedSearchWord = searchWord
      .trim()
      .replace(/\s+/g, ' ')
      .toLowerCase();

    if (normalizedSearchWord === '') {
      toast.error('Please enter a search word');
      return;
    }
    console.log(searchType, normalizedSearchWord);
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
    </div>
  );
};

export default StartByBookSearch;
