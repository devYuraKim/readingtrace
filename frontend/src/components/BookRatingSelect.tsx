import React from 'react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from './ui/select';

interface BookVisibilitySelectProps {
  value: number | '';
  onChange: (value: number) => void;
}

const BookRateSelect = ({ value, onChange }: BookVisibilitySelectProps) => {
  return (
    <Select
      value={value.toString()}
      onValueChange={(value) => onChange(Number(value))}
    >
      <SelectTrigger>
        <SelectValue placeholder="My Rating" />
      </SelectTrigger>
      <SelectContent>
        {[1, 2, 3, 4, 5].map((n) => (
          <SelectItem key={n} value={n.toString()}>
            {'⭐'.repeat(n)}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};

export default BookRateSelect;
