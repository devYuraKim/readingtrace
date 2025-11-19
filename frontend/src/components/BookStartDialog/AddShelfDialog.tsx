import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { toast } from 'sonner';
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

type AddShelfDialogProps = {
  isAddOpen: boolean;
  setIsAddOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const shelfNameSchema = z.object({
  shelfName: z
    .string()
    .trim()
    .min(1, 'Bookshelf name cannot be empty')
    .transform((val) => val.replace(/\s+/g, ' ')),
});

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

  const onSubmit = (data: ShelfNameFormValues) => {
    addNewShelfMutation.mutate(data.shelfName);
    setIsAddOpen(false);
    reset();
  };

  const userId = useAuthStore.getState().user?.userId;
  const queryClient = useQueryClient();

  const addNewShelfMutation = useMutation({
    mutationFn: async (shelfName: string) => {
      const res = await apiClient.post(`users/${userId}/shelves`, {
        shelfName: shelfName,
      });
      return res.data;
    },
    onSuccess: () => {
      toast.success('Shelf added successfully');
      queryClient.invalidateQueries({
        queryKey: ['customShelves', userId],
      });
    },
    onError: (_error, shelfName) => {
      toast.error(`Failed to add shelf ${shelfName}`);
    },
  });

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
