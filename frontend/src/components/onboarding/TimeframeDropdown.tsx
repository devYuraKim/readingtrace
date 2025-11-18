import { useState } from 'react';
import { TIMEFRAME } from '@/constants/onboarding.constants';
import {
  DropdownMenuContent,
  DropdownMenuItem,
} from '@radix-ui/react-dropdown-menu';
import { DropdownMenu, DropdownMenuTrigger } from '../ui/dropdown-menu';

const TimeframeDropdown = () => {
  const [timeframe, setTimeframe] = useState(TIMEFRAME[0].toString());

  const handleSelect = (timeframe: string) => {
    setTimeframe(timeframe);
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <div className="cursor-pointer font-extrabold text-xl">{timeframe}</div>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="cursor-pointer">
        {TIMEFRAME.map((timeframe) => (
          <DropdownMenuItem
            key={timeframe}
            textValue={timeframe}
            onSelect={() => handleSelect(timeframe)}
          >
            {timeframe}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default TimeframeDropdown;
