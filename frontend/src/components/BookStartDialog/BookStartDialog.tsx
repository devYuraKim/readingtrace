import { useState } from 'react';
import {
  useCreateBookStatus,
  useDeleteBookStatus,
  useUpdateBookStatus,
} from '@/queries/book-status.mutation';
import {
  bookStatusFormSchema,
  BookStatusFormValues,
} from '@/schemas/book-status.schemas';
import { useAuthStore } from '@/store/useAuthStore';
import { BookStartDialogProps } from '@/types/props.types';
import { ChevronsUpDown } from 'lucide-react';
import { toast } from 'sonner';
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import { BookCollection } from '@/lib/books';
import { SingleDatePicker } from '../SingleDatePicker';
import BookRatingSelect from './BookRatingSelect';
import BookShelfSelect from './BookShelfSelect';
import BookStatusSelect from './BookStatusSelect';
import BookVisibilitySelect from './BookVisibilitySelect';

const BookStartDialog = ({
  open,
  onOpenChange,
  initialData,
  selectedBookId,
}: BookStartDialogProps) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);

  const userId = useAuthStore.getState().user?.userId;

  const createMutation = useCreateBookStatus(userId, selectedBookId);
  const updateMutation = useUpdateBookStatus(userId, selectedBookId);
  const deleteMutation = useDeleteBookStatus(userId, selectedBookId, () => {
    handleOpenChange(false);
    setOpenConfirmDialog(false);
  });

  const initialFormValues = {
    status: initialData?.status ?? null,
    visibility: initialData?.visibility ?? null,
    rating: initialData?.rating ?? null,
    shelfId: initialData?.shelfId ?? null,
    startDate: initialData?.startDate ? new Date(initialData.startDate) : null,
    endDate: initialData?.endDate ? new Date(initialData.endDate) : null,
  };

  const [formValues, setFormValues] =
    useState<BookStatusFormValues>(initialData);

  const handleOpenChange = (open: boolean) => {
    if (!open) {
      setFormValues(initialFormValues);
    }
    onOpenChange(open);
  };

  const handleChange = (
    field: keyof BookStatusFormValues,
    value: BookStatusFormValues,
  ) => {
    setFormValues((prev) => {
      const updated = { ...prev, [field]: value };
      const { startDate, endDate } = updated;
      if (startDate && endDate && startDate > endDate) {
        toast.error('Start date should be before end date');
        return prev;
      }
      return updated;
    });
  };

  const handleDelete = () => {
    setOpenConfirmDialog(true);
  };

  const handleDeleteConfirm = () => {
    deleteMutation.mutate();
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const result = bookStatusFormSchema.safeParse(formValues);

    if (!result.success) {
      result.error.issues.forEach((issue) => {
        const fieldName = issue.path[0].toString();
        const message = issue.message;
        toast.error(`${fieldName}: ${message}`);
      });
      return;
    }

    const payload = {
      status: formValues.status,
      visibility: formValues.visibility,
      rating: formValues.rating,
      shelfId: formValues.shelfId ?? null,
      startDate: formValues.startDate,
      endDate: formValues.endDate,
    };

    console.log(payload);

    if (initialData.userId) {
      updateMutation.mutate(payload);
    } else {
      createMutation.mutate(payload);
    }
  };

  const book = BookCollection.find((book) => book.bookId === selectedBookId);

  return (
    <>
      <Dialog open={open} onOpenChange={handleOpenChange}>
        <DialogContent className="sm:max-w-[425px] md:max-w-[600px]">
          <form onSubmit={handleSubmit}>
            <DialogHeader className="grid grid-cols-3">
              <div>
                <img
                  src={
                    !book?.imageLinks?.trim()
                      ? '/placeholder.png'
                      : book.imageLinks
                  }
                  alt={book?.title}
                  className="w-full object-cover text-xs text-muted-foreground rounded-xs"
                />
              </div>
              <div className="col-span-2 flex flex-col h-full gap-y-1.5">
                <div>
                  <DialogTitle>{book?.title}</DialogTitle>
                  <DialogDescription>
                    By {!book?.authors?.trim() ? 'Author N/A' : book.authors}
                  </DialogDescription>
                </div>

                <div className="flex flex-col gap-y-0.5 text-muted-foreground opacity-80 text-xs">
                  <div>
                    {!book?.publisher?.trim()
                      ? 'Publisher N/A'
                      : book.publisher}{' '}
                    |{' '}
                    {!book?.publishedDate?.trim()
                      ? 'Published Date N/A'
                      : book.publishedDate}
                  </div>
                  <div>
                    {!book?.isbn10?.trim() ? 'ISBN10 N/A' : book.isbn10} |{' '}
                    {!book?.isbn13?.trim() ? 'ISBN13 N/A' : book.isbn13}
                  </div>
                </div>

                <div className="text-muted-foreground text-xs">
                  <div
                    className={`break-normal hyphens-auto
                  ${isExpanded ? 'line-clamp-none' : 'line-clamp-5'}
                `}
                  >
                    {!book?.description?.trim()
                      ? 'Description N/A'
                      : book.description}
                  </div>

                  <div
                    className="flex flex-row justify-end items-center gap-1 mt-1 cursor-pointer"
                    onClick={() => setIsExpanded(!isExpanded)}
                  >
                    {book?.description?.trim() !== '' ? (
                      <>
                        <ChevronsUpDown className="w-3 h-3" />
                        {isExpanded ? 'Show less' : 'Show more'}
                      </>
                    ) : (
                      ''
                    )}
                  </div>
                </div>
              </div>
            </DialogHeader>
            <div className="my-4">
              <div className="grid grid-cols-[minmax(0,1fr)_minmax(0,1fr)_20px_minmax(0,1fr)_minmax(0,1fr)] gap-2 items-center">
                <div className="flex col-start-1 col-span-2">
                  <Label className="w-2/5 text-right">Status</Label>
                  <BookStatusSelect
                    className="w-3/5 text-xs"
                    value={formValues.status}
                    onChange={(value: string) => handleChange('status', value)}
                  />
                </div>

                <div className="flex col-start-4 col-span-2">
                  <Label className="w-2/5 text-right">Visibility</Label>
                  <BookVisibilitySelect
                    className="w-3/5 text-xs"
                    value={formValues.visibility}
                    onChange={(value: string) =>
                      handleChange('visibility', value)
                    }
                  />
                </div>

                <div className="flex col-start-1 col-span-2">
                  <Label className="w-2/5 text-right">Rating</Label>
                  <BookRatingSelect
                    className="w-3/5 text-xs"
                    value={formValues.rating}
                    onChange={(value: number) => handleChange('rating', value)}
                  />
                </div>

                <div className="flex col-start-4 col-span-2">
                  <Label className="w-2/5 text-right">Shelf</Label>
                  <BookShelfSelect
                    className="w-3/5 text-xs"
                    value={formValues.shelfId}
                    onChange={(value: number) =>
                      handleChange('shelfId', value === 0 ? null : value)
                    }
                  />
                </div>

                <div className="flex col-start-1 col-span-2">
                  <Label className="w-2/5 text-right">Start Date</Label>
                  <div className="w-3/5 text-xs">
                    <SingleDatePicker
                      value={formValues.startDate || null}
                      onChange={(date: Date | null) =>
                        handleChange('startDate', date)
                      }
                    />
                  </div>
                </div>

                <div className="flex col-start-4 col-span-2">
                  <Label className="w-2/5 text-right">End Date</Label>
                  <div className="w-3/5 text-xs">
                    <SingleDatePicker
                      value={formValues.endDate || null}
                      onChange={(date: Date | null) =>
                        handleChange('endDate', date)
                      }
                    />
                  </div>
                </div>
              </div>
            </div>
            <DialogFooter
              className={
                initialData.userId ? '!justify-between' : 'justify-end'
              }
            >
              {initialData.userId && (
                <Button
                  type="button"
                  variant="outline"
                  className="cursor-pointer"
                  onClick={handleDelete}
                >
                  Delete
                </Button>
              )}
              <Button type="submit" className="cursor-pointer">
                {initialData.userId ? 'Update' : 'Add'}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>

      {openConfirmDialog && (
        <Dialog open={openConfirmDialog} onOpenChange={setOpenConfirmDialog}>
          <DialogContent showCloseButton={false} className="w-1/2">
            <DialogHeader>
              <DialogDescription>Delete your book record?</DialogDescription>
            </DialogHeader>
            <DialogFooter>
              <DialogClose>
                <Button
                  type="button"
                  variant="outline"
                  className="cursor-pointer"
                >
                  No
                </Button>
              </DialogClose>
              <Button className="cursor-pointer" onClick={handleDeleteConfirm}>
                Yes
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      )}
    </>
  );
};

export default BookStartDialog;
