import { useState } from 'react';
import { UNIT } from '@/constants/onboarding.constants';
import {
  DropdownMenuArrow,
  DropdownMenuContent,
  DropdownMenuItem,
} from '@radix-ui/react-dropdown-menu';
import { DropdownMenu, DropdownMenuTrigger } from '../ui/dropdown-menu';

const UnitDropdown = () => {
  const [unit, setUnit] = useState(UNIT[0].toString());

  const handleSelect = (unit: string) => {
    setUnit(unit);
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <div className="cursor-pointer font-extrabold text-xl">{unit}</div>
      </DropdownMenuTrigger>
      <DropdownMenuContent
        className="cursor-pointer border-1 border-red-400"
        align="center"
      >
        <DropdownMenuArrow />
        {UNIT.map((unit) => (
          <DropdownMenuItem
            key={unit}
            textValue={unit}
            onSelect={() => handleSelect(unit)}
            className="text-center focus:outline-1 focus:outline-green-500"
          >
            {unit}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default UnitDropdown;
