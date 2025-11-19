import { useEffect, useState } from 'react';
import { UNIT } from '@/constants/onboarding.constants';
import {
  DropdownMenuContent,
  DropdownMenuItem,
} from '@radix-ui/react-dropdown-menu';
import { ChevronDown } from 'lucide-react';
import { DropdownMenu, DropdownMenuTrigger } from '../ui/dropdown-menu';

const UnitDropdown = ({ isPlural }: { isPlural: boolean }) => {
  const [unit, setUnit] = useState(UNIT[0].toString());

  useEffect(() => {
    const goalUnit = localStorage.getItem('on_goalUnit');
    if (goalUnit) setUnit(goalUnit);
  }, []);

  useEffect(() => {
    localStorage.setItem('on_goalUnit', unit);
  }, [unit]);

  const handleSelect = (unit: string) => {
    setUnit(unit);
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <div className="flex items-center cursor-pointer font-extrabold text-xl group">
          {isPlural ? unit + 'S' : unit}
          <ChevronDown className="w-[1rem] h-[1rem] ml-1 group-hover:stroke-black group-hover:stroke-5" />
        </div>
      </DropdownMenuTrigger>
      <DropdownMenuContent
        className="cursor-pointer shadow-md rounded-[0.3rem]  bg-white w-36 mt-2 p-2"
        align="center"
      >
        {UNIT.map((unit) => (
          <DropdownMenuItem
            key={unit}
            textValue={unit}
            onSelect={() => handleSelect(unit)}
            className="text-sm text-center focus:outline-none focus:font-bold focus:bg-[#f5f5f5] p-1.5 focus:rounded-[0.3rem]"
          >
            {isPlural ? unit + 'S' : unit}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default UnitDropdown;
