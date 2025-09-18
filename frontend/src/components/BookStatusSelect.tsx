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

const BookStatusSelect = ({ value, onChange }: BookVisibilitySelectProps) => {
  return (
    <Select value={value} onValueChange={(value) => onChange(value)}>
      <SelectTrigger>
        <SelectValue placeholder="Reading Status" />
      </SelectTrigger>
      <SelectContent>
        <SelectItem value="wantToRead">🤩 Want to Read</SelectItem>
        <SelectItem value="alreadyRead">😎 Already Read</SelectItem>
        <SelectItem value="currentlyReading"> 🧐 Currently Reading</SelectItem>
        <SelectItem value="neverFinished">😔 Never Finished</SelectItem>
      </SelectContent>
    </Select>
  );
};

export default BookStatusSelect;
