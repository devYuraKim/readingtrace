import React from 'react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from './ui/select';

interface BookVisibilitySelectProps {
  value: string;
  onChange: (value: string) => void;
}

const BookVisibilitySelect = ({
  value,
  onChange,
}: BookVisibilitySelectProps) => {
  return (
    <Select value={value} onValueChange={(value) => onChange(value)}>
      <SelectTrigger>
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
