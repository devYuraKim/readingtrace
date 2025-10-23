import { useState } from 'react';
import { useUserShelves } from '@/queries/useUserShelves';
import { useAuthStore } from '@/store/useAuthStore';
import { Plus } from 'lucide-react';
import { SHELF_ID_FOR_DEFAULT_SHELF_GROUP } from '@/lib/constants';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select';
import AddShelfDialog from './AddShelfDialog';

type BookShelfSelectProps = {
  value: number | null | undefined;
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

  const nonDefaultShelves = shelves?.filter((shelf) => !shelf.isDefault) ?? [];

  return (
    <>
      <Select
        value={value ? value.toString() : undefined}
        onValueChange={(value) => {
          if (value === 'add') {
            setIsAddOpen(true);
            return;
          }
          onChange(Number(value));
        }}
      >
        <SelectTrigger className={className}>
          <SelectValue placeholder="Select Shelf" />
        </SelectTrigger>
        <SelectContent>
          <SelectItem value={SHELF_ID_FOR_DEFAULT_SHELF_GROUP.toString()}>
            Default
          </SelectItem>
          {nonDefaultShelves &&
            nonDefaultShelves.map((shelf) => (
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
