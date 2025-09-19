import React from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useMutation } from '@tanstack/react-query';
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
});

type FormValues = z.infer<typeof formSchema>;

type FormValuesWithPlaceholder = {
  status: FormValues['status'] | '';
  visibility: FormValues['visibility'] | '';
  rating: FormValues['rating'] | '';
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
  };

  const [formValues, setFormValues] =
    React.useState<FormValuesWithPlaceholder>(defaultFormValues);

  // TODO: Fix value type any
  const handleChange = (field: keyof FormValues, value: any) => {
    setFormValues((prev) => ({
      ...prev,
      [field]: value,
    }));
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
    };
    console.log('payload', payload);

    apiClient.post('/book/add', payload);

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
                  className="w-full"
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
            <div className="grid grid-cols-2 gap-2 items-center">
              <Label className="text-right">Reading Status</Label>
              <BookStatusSelect
                value={formValues.status}
                onChange={(value: string) => handleChange('status', value)}
              />

              <Label className="text-right">Visibility</Label>
              <BookVisibilitySelect
                value={formValues.visibility}
                onChange={(value: string) => handleChange('visibility', value)}
              />

              <Label className="text-right">My Rating</Label>
              <BookRatingSelect
                value={formValues.rating}
                onChange={(value: number) => handleChange('rating', value)}
              />
            </div>
          </div>
          <DialogFooter className="!justify-between">
            <DialogClose asChild>
              <Button
                variant="outline"
                className="cursor-pointer justify-end"
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
