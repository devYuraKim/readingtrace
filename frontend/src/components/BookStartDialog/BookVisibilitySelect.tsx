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

const BookVisibilitySelect = ({
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
        <SelectValue placeholder="Visibility" />
      </SelectTrigger>
      <SelectContent>
        <SelectItem value="public">🌍 Public</SelectItem>
        <SelectItem value="friends">👥 Friends</SelectItem>
        <SelectItem value="private">🔒 Private</SelectItem>
      </SelectContent>
    </Select>
  );
};

export default BookVisibilitySelect;
