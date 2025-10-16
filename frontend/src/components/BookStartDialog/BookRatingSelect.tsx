import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select';

interface BookVisibilitySelectProps {
  value: number | null;
  onChange: (value: number) => void;
  className?: string;
}

const BookRateSelect = ({
  value,
  onChange,
  className,
}: BookVisibilitySelectProps) => {
  return (
    <Select
      value={value ? value.toString() : undefined}
      onValueChange={(value) => onChange(Number(value))}
    >
      <SelectTrigger className={className}>
        <SelectValue placeholder="Select Rating" />
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
