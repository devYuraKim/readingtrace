import { useState } from 'react';
import { useUserShelves } from '@/queries/useUserShelves';
import { useAuthStore } from '@/store/useAuthStore';
import { Plus } from 'lucide-react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select';
import AddShelfDialog from './AddShelfDialog';

type BookShelfSelectProps = {
  value: number;
  onChange: (value: number) => void;
  className?: string;
};

const BookShelfSelect = ({
  value,
  onChange,
  className,
}: BookShelfSelectProps) => {
  const [isAddOpen, setIsAddOpen] = useState(false);

  const userId = useAuthStore((state) => state.user?.userId);
  const { data: shelves } = useUserShelves(userId);

  return (
    <>
      <Select
        value={value !== 0 ? value.toString() : undefined}
        onValueChange={(value) => {
          if (value === 'add') {
            setIsAddOpen(true);
            return;
          }
          onChange(value ? Number(value) : 0);
        }}
      >
        <SelectTrigger className={className}>
          <SelectValue placeholder="Select Shelf" />
        </SelectTrigger>
        <SelectContent>
          {shelves &&
            shelves.map((shelf) => (
              <SelectItem key={shelf.shelfId} value={shelf.shelfId.toString()}>
                {shelf.name}
              </SelectItem>
            ))}

          <SelectItem value="add" className="cursor-pointer">
            <Plus />
            Add new shelf
          </SelectItem>
        </SelectContent>
      </Select>

      {isAddOpen && (
        <AddShelfDialog isAddOpen={isAddOpen} setIsAddOpen={setIsAddOpen} />
      )}
    </>
  );
};

export default BookShelfSelect;
