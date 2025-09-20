import { start } from 'repl';
import React from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
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
import BookRatingSelect from './BookRatingSelect';
import BookStatusSelect from './BookStatusSelect';
import BookVisibilitySelect from './BookVisibilitySelect';
import { SingleDatePicker } from './SingleDatePicker';

interface bookType {
  id: string;
  title: string;
  authors: string;
  imageLinks: string;
  publisher: string;
  publishedDate: string;
  description: string;
  isbn_10: string;
  isbn_13: string;
}

const formSchema = z.object({
  status: z.enum(
    ['wantToRead', 'alreadyRead', 'currentlyReading', 'neverFinished'],
    { message: 'Please select a reading status.' },
  ),
  visibility: z.enum(['public', 'private', 'friends'], {
    message: 'Please select a visibility option.',
  }),
  rating: z.number({ message: 'Please provide a rating.' }).min(0).max(5),
  startDate: z.date().nullish(),
  endDate: z.date().nullish(),
});

type FormValues = z.infer<typeof formSchema>;

type FormValuesWithPlaceholder = {
  status: FormValues['status'] | '';
  visibility: FormValues['visibility'] | '';
  rating: FormValues['rating'] | '';
  startDate: FormValues['startDate'] | '';
  endDate: FormValues['endDate'] | '';
};

const BookStartDialog = ({
  open,
  onOpenChange,
  book,
}: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  book: bookType;
}) => {
  const [isExpanded, setIsExpanded] = React.useState(false);
  const defaultFormValues: FormValuesWithPlaceholder = {
    status: '',
    visibility: '',
    rating: '',
    startDate: '',
    endDate: '',
  };

  const [formValues, setFormValues] =
    React.useState<FormValuesWithPlaceholder>(defaultFormValues);

  // TODO: Fix value type any
  const handleChange = (field: keyof FormValues, value: any) => {
    setFormValues((prev) => {
      const updated = { ...prev, [field]: value };
      const { startDate, endDate } = updated;
      if (startDate && endDate && startDate > endDate) {
        toast.error('Start date should be before end date');
        return prev; // reject the change
      }

      return updated;
    });
  };

  const handleReset = () => {
    setFormValues(defaultFormValues);
    onOpenChange(true);
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const result = formSchema.safeParse(formValues);
    const payload = {
      userId: useAuthStore.getState().user?.userId,
      bookId: book.id,
      status: formValues.status,
      visibility: formValues.visibility,
      rating: formValues.rating,
      startDate: formValues.startDate,
      endDate: formValues.endDate,
    };
    console.log('payload', payload);

    // apiClient.post('/book/add', payload);

    if (!result.success) {
      result.error.issues.forEach((issue) => {
        const fieldName = issue.path[0].toString();
        const message = issue.message;
        toast.error(`${fieldName}: ${message}`);
      });
    } else {
      console.log(result.data);
      setFormValues(defaultFormValues);
      onOpenChange(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[425px] md:max-w-[600px]">
        <form onSubmit={handleSubmit}>
          <DialogHeader className="grid grid-cols-3">
            <div>
              {book.imageLinks ? (
                <img
                  src={book.imageLinks}
                  alt={book.title}
                  className="w-full rounded-sm"
                />
              ) : (
                <div className="w-full h-full flex justify-center items-center text-sm text-muted-foreground">
                  Image N/A
                </div>
              )}
            </div>
            <div className="col-span-2 flex flex-col h-full gap-y-1.5">
              <div>
                <DialogTitle>
                  {book.title.trim() === '' ? 'Title N/A' : book.title}
                </DialogTitle>
                <DialogDescription>
                  By {book.authors.trim() === '' ? 'Authors N/A' : book.authors}
                </DialogDescription>
              </div>

              <div className="flex flex-col gap-y-0.5 text-muted-foreground opacity-80 text-xs">
                <div>
                  {book.publisher.trim() === ''
                    ? 'Publisher N/A'
                    : book.publisher}
                  &nbsp;
                  {book.publishedDate.trim() === ''
                    ? 'Published Date N/A'
                    : book.publishedDate}
                </div>
                <div>
                  {book.isbn_10.trim() === '' ? 'ISBN 10 N/A' : book.isbn_10} |{' '}
                  {book.isbn_13.trim() === '' ? 'ISBN 13 N/A' : book.isbn_13}
                </div>
              </div>

              <div className="text-muted-foreground text-xs">
                <div
                  className={`break-normal hyphens-auto
                  ${isExpanded ? 'line-clamp-none' : 'line-clamp-5'}
                `}
                >
                  {book.description.trim() === ''
                    ? 'Description N/A'
                    : book.description}
                </div>

                <div
                  className="flex flex-row justify-end items-center gap-1 mt-1 cursor-pointer"
                  onClick={() => setIsExpanded(!isExpanded)}
                >
                  {book.description.trim() !== '' ? (
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
              Add Book
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default BookStartDialog;
