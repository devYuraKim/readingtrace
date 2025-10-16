import * as React from 'react';
import { ChevronDownIcon } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';

interface SingleDatePickerProps {
  value?: Date | null;
  onChange: (date: Date | null) => void;
}

export function SingleDatePicker({ value, onChange }: SingleDatePickerProps) {
  const [open, setOpen] = React.useState(false);
  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          id="date"
          className="!w-full !flex !justify-between !items-center  font-normal text-muted-foreground text-xs "
        >
          {value ? value.toLocaleDateString() : 'Select Date'}
          <ChevronDownIcon className=" size-4 opacity-50 !flex-shrink-0" />
        </Button>
      </PopoverTrigger>
      <PopoverContent align="start">
        <Calendar
          mode="single"
          selected={value ?? undefined}
          captionLayout="dropdown"
          onSelect={(date) => {
            onChange(date ?? null);
            setOpen(false);
          }}
        />
      </PopoverContent>
    </Popover>
  );
}
