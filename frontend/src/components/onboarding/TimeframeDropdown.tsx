import { useState } from 'react';
import { TIMEFRAME } from '@/constants/onboarding.constants';
import {
  DropdownMenuContent,
  DropdownMenuItem,
} from '@radix-ui/react-dropdown-menu';
import { ChevronDown } from 'lucide-react';
import { DropdownMenu, DropdownMenuTrigger } from '../ui/dropdown-menu';

const TimeframeDropdown = () => {
  const [timeframe, setTimeframe] = useState(TIMEFRAME[0].toString());

  const handleSelect = (timeframe: string) => {
    setTimeframe(timeframe);
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <div className="flex items-center cursor-pointer font-extrabold text-xl group">
          {timeframe}
          <ChevronDown className="w-[1rem] h-[1rem] ml-1 group-hover:stroke-black group-hover:stroke-5" />
        </div>
      </DropdownMenuTrigger>
      <DropdownMenuContent
        className="cursor-pointer shadow-md rounded-[0.3rem] p-2 bg-white w-26 mt-2"
        align="center"
      >
        {TIMEFRAME.map((timeframe) => (
          <DropdownMenuItem
            key={timeframe}
            textValue={timeframe}
            onSelect={() => handleSelect(timeframe)}
            className="text-sm text-center focus:outline-none focus:font-bold focus:bg-[#f5f5f5] p-1.5 focus:rounded-[0.3rem]"
          >
            {timeframe}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default TimeframeDropdown;
