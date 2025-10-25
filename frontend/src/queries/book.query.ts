import { BookSearchResultDto } from '@/types/book.types';
import { GetSearchedBooksProps } from '@/types/props.types';
import { useQuery } from '@tanstack/react-query';
import { apiClient } from './axios';

export const useGetSearchedBooks = ({
  activeSearchType,
  activeSearchWord,
  page,
  booksPerPage,
}: GetSearchedBooksProps) => {
  return useQuery({
    queryKey: ['bookSearch', activeSearchType, activeSearchWord, page],
    queryFn: async () => {
      const startIndex = (page - 1) * booksPerPage;
      const response = await apiClient.get(
        `/books?searchType=${activeSearchType}&searchWord=${activeSearchWord}&startIndex=${startIndex}&maxResults=${booksPerPage}`,
      );
      return response.data as BookSearchResultDto;
    },
    enabled: !!activeSearchWord && !!activeSearchType,
    staleTime: 5 * 60 * 1000, // Cache results for 5 minutes
  });
};
