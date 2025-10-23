import React from 'react';
import {
  Visibility,
  VisibilitySlug,
} from '@/constants/reading-status.constants';
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
        <SelectValue placeholder="Select Visibility" />
      </SelectTrigger>
      <SelectContent>
        <SelectItem value={VisibilitySlug.PUBLIC}>
          🌍 {Visibility.PUBLIC}
        </SelectItem>
        <SelectItem value={VisibilitySlug.FRIENDS}>
          👥 {Visibility.FRIENDS}
        </SelectItem>
        <SelectItem value={VisibilitySlug.PRIVATE}>
          🔒 {Visibility.PRIVATE}
        </SelectItem>
      </SelectContent>
    </Select>
  );
};

export default BookVisibilitySelect;
