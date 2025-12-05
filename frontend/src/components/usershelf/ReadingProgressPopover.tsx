import React, { useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { ReadingProgressPopoverProps } from '@/types/props.types';
import { PopoverContent } from '@radix-ui/react-popover';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MoveLeft, PercentCircle, StickyNote, Trophy, X } from 'lucide-react';
import { Button } from '../ui/button';
import { Input } from '../ui/input';

export const ReadingProgressPopover = ({
  onOpenChange,
  totalPages,
  bookId,
  userReadingStatusId,
}: ReadingProgressPopoverProps) => {
  const queryClient = useQueryClient();
  const userId = useAuthStore((state) => state.user?.userId);

  const { data: currentPage, isPending } = useQuery({
    queryKey: ['latest-progress', userId, bookId],
    queryFn: async () => {
      const res = await apiClient.get(
        `/users/${userId}/books/current-progress?bookId=${bookId}`,
      );
      return res.data;
    },
  });

  const [mode, setMode] = useState<'page' | 'percent'>('page');
  const [page, setPage] = useState(currentPage);
  const [percent, setPercent] = useState(
    Number(((currentPage / totalPages) * 100).toFixed(1)),
  );
  const [log, setLog] = useState(false);

  const handleClickDone = () => {
    setPage(totalPages);
    setPercent(100);
    onOpenChange(false);

    apiClient.post(`/users/${userId}/books/complete-progress`, {
      userId: userId,
      bookId: bookId,
      userReadingStatusId: userReadingStatusId,
      currentPage: totalPages,
    });
  };

  const handleClickUpdate = () => {
    apiClient.post(`/users/${userId}/books/progress`, {
      userId: userId,
      bookId: bookId,
      userReadingStatusId: userReadingStatusId,
      currentPage: page,
    });
  };

  const handleClickLog = () => {
    setLog((prev) => !prev);
  };

  const { data: logData, isPending: isPendingLog } = useQuery({
    queryKey: ['logData', userId, bookId],
    queryFn: async () => {
      const res = await apiClient.get(
        `/users/${userId}/books/${bookId}/progress`,
      );
      return res.data;
    },
    enabled: log,
  });

  const { mutate } = useMutation({
    mutationFn: async (progressId) => {
      await apiClient.delete(
        `/users/${userId}/books/${bookId}/progress?id=${progressId}`,
      );
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['logData', userId, bookId],
      });
    },
  });

  const handlePageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPage(Number(e.target.value));
    setPercent(
      Number(((Number(e.target.value) / totalPages) * 100).toFixed(1)),
    );
  };

  const handlePercentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPercent(Number(e.target.value));
    setPage(Number(((Number(e.target.value) / 100) * totalPages).toFixed(0)));
  };

  const handleClickDeleteLog = (progressId) => {
    mutate(progressId);
  };

  const handleClickBack = () => {
    setLog((prev) => !prev);
  };

  return (
    <PopoverContent
      className="w-80 p-0 rounded-xl shadow-sm bg-white border-1 border-lime-700 shadow-sky-700"
      onClick={(e) => e.stopPropagation()}
    >
      {!log && (
        <>
          <div className="flex items-center justify-center rounded-sm px-4 pt-5 ">
            <Button
              className={`w-[50%] rounded-r-none cursor-pointer ${mode === 'page' ? 'bg-gradient-to-l from-sky-500 to-lime-500' : 'text-gray-500 bg-gray-50 hover:bg-gray-100'}`}
              onClick={() => setMode('page')}
            >
              <StickyNote />
              Page
            </Button>

            <Button
              className={`w-[50%] rounded-l-none cursor-pointer ${mode === 'percent' ? 'bg-gradient-to-l to-sky-500 from-lime-500' : 'text-gray-500 bg-gray-50 hover:bg-gray-100'}`}
              onClick={() => setMode('percent')}
            >
              <PercentCircle /> Percent
            </Button>
          </div>

          <div className="flex flex-col items-center pt-4 px-4">
            {mode === 'page' && (
              <div className="flex items-center gap-2 text-sm text-gray-700">
                <span>You're on page</span>
                <Input
                  type="number"
                  className="w-20 text-center"
                  value={page}
                  onChange={(e) => handlePageChange(e)}
                />
                <span>of {totalPages}</span>
              </div>
            )}

            {mode === 'percent' && (
              <div className="flex items-center gap-2 text-sm text-gray-700">
                <span>You're at</span>
                <Input
                  type="number"
                  className="w-20 text-center"
                  value={percent}
                  onChange={(e) => handlePercentChange(e)}
                />
                <span>%</span>
              </div>
            )}

            <Button
              className="w-[50%] mt-4 bg-gray-400 cursor-pointer hover:bg-gradient-to-b hover:from-lime-500 hover:to-sky-500"
              onClick={handleClickDone}
            >
              <Trophy /> I'm Done
            </Button>

            <div className="flex flex-row w-full gap-2 mt-8 mb-4">
              <Button
                className="w-[50%] cursor-pointer"
                onClick={handleClickLog}
              >
                See Log
              </Button>
              <Button
                className="w-[50%] bg-lime-500 hover:bg-lime-600 text-white cursor-pointer"
                onClick={handleClickUpdate}
              >
                Save Progress
              </Button>
            </div>
          </div>
        </>
      )}

      {log && !isPendingLog && (
        <div className="p-3 pt-5">
          {logData.map((data) => (
            <div className="flex py-0.5">
              <div className="w-[35%] text-sm flex items-center ml-5">
                {data.currentPage} page
              </div>
              <div className=" w-[45%] text-xs tracking-tight flex items-center">
                {data.userReadingProgressCreatedAt
                  ? new Date(data.userReadingProgressCreatedAt)
                      .toLocaleString('en-GB', {
                        year: '2-digit',
                        month: 'short',
                        day: '2-digit',
                        hour: '2-digit',
                        minute: '2-digit',
                      })
                      .replace(', ', ' at ')
                  : '-'}
              </div>

              <div className="w-[20%] flex items-center justify-center">
                <X
                  className="w-3 h-3 hover:bg-yellow-400"
                  onClick={() =>
                    handleClickDeleteLog(data.userReadingProgressId)
                  }
                />
              </div>
            </div>
          ))}
          <Button
            variant="ghost"
            onClick={handleClickBack}
            className="cursor-pointer text-xs font-normal justify-end ml-35"
          >
            <MoveLeft className="w-1 h-1" /> Back to Progress
          </Button>
        </div>
      )}
    </PopoverContent>
  );
};
