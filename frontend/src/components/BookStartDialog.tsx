import React, { useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { ChevronsUpDown } from 'lucide-react';
import { toast } from 'sonner';
import z from 'zod';
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
import BookRatingSelect from './BookRatingSelect';
import BookStatusSelect from './BookStatusSelect';
import BookVisibilitySelect from './BookVisibilitySelect';
import { SingleDatePicker } from './SingleDatePicker';

const formSchema = z.object({
  status: z.enum(
    ['wantToRead', 'alreadyRead', 'currentlyReading', 'neverFinished'],
    {
      message: 'Please select a reading status.',
    },
  ),
  visibility: z.enum(['public', 'private', 'friends'], {
    message: 'Please select a visibility option.',
  }),
  rating: z
    .number({ message: 'Please provide a rating.' })
    .min(0)
    .max(5)
    .nullish(),
  startDate: z.date().nullish(),
  endDate: z.date().nullish(),
});

type FormValues = z.infer<typeof formSchema>;

type FormValuesWithPlaceholder = {
  status: FormValues['status'] | null;
  visibility: FormValues['visibility'] | null;
  rating: FormValues['rating'] | null;
  startDate: FormValues['startDate'] | null;
  endDate: FormValues['endDate'] | null;
};

type BookStartDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  initialData: any;
  selectedBookId: string;
};

const BookStartDialog = ({
  open,
  onOpenChange,
  initialData,
  selectedBookId,
}: BookStartDialogProps) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const queryClient = useQueryClient();
  const userId = useAuthStore.getState().user?.userId;

  const addBookMutation = useMutation({
    mutationFn: async (payload: FormValues) => {
      await apiClient.post(`/users/${userId}/books/${selectedBookId}`, {
        ...payload,
      });
    },
    onSuccess: () => {
      toast.success('Book added/updated successfully');
      queryClient.invalidateQueries({
        queryKey: ['userBook', userId, selectedBookId],
      });
      onOpenChange(false);
    },
    onError: () => {
      toast.error('Failed to add/update book');
    },
  });

  const updateBookMutation = useMutation({
    mutationFn: (payload: FormValues) =>
      apiClient.put(`/users/${userId}/books/${selectedBookId}`, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['userBook', userId, selectedBookId],
      });
      toast.success('Book updated successfully!');
      onOpenChange(false);
    },
    onError: () => {
      toast.error('Failed to update book');
    },
  });

  const defaultFormValues: FormValuesWithPlaceholder = {
    status: null,
    visibility: null,
    rating: null,
    startDate: null,
    endDate: null,
  };

  const initialValues = initialData
    ? {
        status: initialData.status || null,
        visibility: initialData.visibility || null,
        rating: initialData.rating || null,
        startDate: initialData.startDate
          ? new Date(initialData.startDate)
          : null,
        endDate: initialData.endDate ? new Date(initialData.endDate) : null,
      }
    : defaultFormValues;

  const [formValues, setFormValues] = useState(initialValues);

  const handleOpenChange = (open: boolean) => {
    if (!open) {
      setFormValues(initialValues);
    }
    onOpenChange(open);
  };

  const handleChange = (field: keyof FormValues, value: FormValues) => {
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

  const handleReset = () => {
    setFormValues(defaultFormValues);
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const result = formSchema.safeParse(formValues);

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
      startDate: formValues.startDate,
      endDate: formValues.endDate,
    };

    if (initialData) {
      updateBookMutation.mutate(payload);
    }
    addBookMutation.mutate(payload);
  };

  const parsedInitialValues = JSON.stringify(initialValues);
  console.log(`initialValues: ${parsedInitialValues}`);

  const book = BookCollection.find((book) => book.bookId === selectedBookId);

  return (
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
                  {!book?.publisher?.trim() ? 'Publisher N/A' : book.publisher}{' '}
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
                <Label className="w-2/5 text-right">Reading Status</Label>
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
                <Label className="w-2/5 text-right">My Rating</Label>
                <BookRatingSelect
                  className="w-3/5 text-xs"
                  value={formValues.rating}
                  onChange={(value: number) => handleChange('rating', value)}
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
          <DialogFooter className="!justify-between">
            <DialogClose asChild>
              <Button
                variant="outline"
                className="cursor-pointer"
                onClick={handleReset}
              >
                Cancel
              </Button>
            </DialogClose>
            <Button type="submit" className="cursor-pointer">
              {initialData.userId ? 'Update Book' : 'Add Book'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default BookStartDialog;
