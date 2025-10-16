import React from 'react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select';

interface BookVisibilitySelectProps {
  value: string | null;
  onChange: (value: string) => void;
  className?: string;
}

const BookStatusSelect = ({
  value,
  onChange,
  className,
}: BookVisibilitySelectProps) => {
  return (
    <Select
      value={value ?? undefined}
      onValueChange={(value) => onChange(value)}
    >
      <SelectTrigger className={className}>
        <SelectValue placeholder="Select Status" />
      </SelectTrigger>
      <SelectContent>
        <SelectItem value="want-to-read">🤩 Want to Read</SelectItem>
        <SelectItem value="already-read">😎 Already Read</SelectItem>
        <SelectItem value="currently-reading"> 🧐 Currently Reading</SelectItem>
        <SelectItem value="never-finished">😔 Never Finished</SelectItem>
      </SelectContent>
    </Select>
  );
};

export default BookStatusSelect;
