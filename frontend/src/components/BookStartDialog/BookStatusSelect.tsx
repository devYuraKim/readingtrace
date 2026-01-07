import { Status, StatusSlug } from '@/constants/reading-status.constants';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select';

interface BookVisibilitySelectProps {
  value: string | null | undefined;
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
        <SelectItem value={StatusSlug.ALREADY_READ}>
          😎 {Status.ALREADY_READ}
        </SelectItem>
        <SelectItem value={StatusSlug.WANT_TO_READ}>
          🤩 {Status.WANT_TO_READ}
        </SelectItem>
        <SelectItem value={StatusSlug.CURRENTLY_READING}>
          🧐 {Status.CURRENTLY_READING}
        </SelectItem>
        <SelectItem value={StatusSlug.PAUSED_READING}>
          😪 {Status.PAUSED_READING}
        </SelectItem>
        <SelectItem value={StatusSlug.NEVER_FINISHED}>
          😔 {Status.NEVER_FINISHED}
        </SelectItem>
      </SelectContent>
    </Select>
  );
};

export default BookStatusSelect;
