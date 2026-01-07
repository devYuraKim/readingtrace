import { useAddNewShelf } from '@/queries/shelf.mutation';
import { shelfNameSchema } from '@/schemas/shelf.schemas';
import { useAuthStore } from '@/store/useAuthStore';
import { AddShelfDialogProps } from '@/types/props.types';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import z from 'zod';
import { Button } from '../ui/button';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '../ui/dialog';
import { Input } from '../ui/input';

type ShelfNameFormValues = z.infer<typeof shelfNameSchema>;

function AddShelfDialog({ isAddOpen, setIsAddOpen }: AddShelfDialogProps) {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<ShelfNameFormValues>({
    resolver: zodResolver(shelfNameSchema),
  });

  const userId = useAuthStore((state) => state.user?.userId);
  const { mutate } = useAddNewShelf(userId);

  const onSubmit = (data: ShelfNameFormValues) => {
    mutate(data.shelfName);
    setIsAddOpen(false);
    reset();
  };

  return (
    <Dialog open={isAddOpen} onOpenChange={setIsAddOpen}>
      <DialogContent showCloseButton={false} className="w-1/2">
        <DialogHeader>
          <DialogTitle>Create a new shelf</DialogTitle>
        </DialogHeader>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <Input placeholder="Shelf name" {...register('shelfName')} />
          {errors.shelfName && (
            <p className="text-red-500">{errors.shelfName.message}</p>
          )}

          <DialogFooter>
            <DialogClose>
              <Button type="button" variant="outline">
                No
              </Button>
            </DialogClose>
            <Button type="submit" onClick={handleSubmit(onSubmit)}>
              Yes
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

export default AddShelfDialog;
