import { useState } from 'react';
import { ReadingProgressPopoverProps } from '@/types/props.types';
import { PopoverContent } from '@radix-ui/react-popover';
import { PercentCircle, StickyNote } from 'lucide-react';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { Popover, PopoverTrigger } from '../ui/popover';

export const ReadingProgressPopover = ({
  open,
  onOpenChange,
  totalPages,
}: ReadingProgressPopoverProps) => {
  const [mode, setMode] = useState<'page' | 'percent'>('page');
  const [page, setPage] = useState(0);
  const [percent, setPercent] = useState(0);

  const handleClickDone = () => {
    if (mode === 'page') {
      setPage(totalPages);
    } else {
      setPercent(100);
    }
    onOpenChange(false);
  };

  return (
    <Popover open={open} onOpenChange={onOpenChange}>
      <PopoverTrigger className="hidden" />
      <PopoverContent className="w-80 p-0 rounded-xl shadow-sm mt-50 ml-100 bg-white border-1 border-lime-700 shadow-sky-700">
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
              <span>You’re on page</span>
              <Input
                type="number"
                className="w-20 text-center"
                value={page}
                onChange={(e) => setPage(Number(e.target.value))}
              />
              <span>of {totalPages}</span>
            </div>
          )}

          {mode === 'percent' && (
            <div className="flex items-center gap-2 text-sm text-gray-700">
              <span>You’re at</span>
              <Input
                type="number"
                className="w-20 text-center"
                value={percent}
                onChange={(e) => setPercent(Number(e.target.value))}
              />
              <span>%</span>
            </div>
          )}

          <Button
            className="w-[50%] mt-4 bg-gray-400 cursor-pointer hover:bg-lime-400"
            onClick={handleClickDone}
          >
            I'm done!
          </Button>

          <Button className="w-full bg-lime-500 hover:bg-lime-600 text-white mt-8 mb-4 cursor-pointer">
            Update progress
          </Button>
        </div>
      </PopoverContent>
    </Popover>
  );
};
