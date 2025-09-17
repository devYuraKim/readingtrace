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
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';

interface bookType {
  id: string;
  title: string;
  authors: string;
  imageLinks: string;
  //   publisher: string;
  //   publishedDate: string;
  //   description: string;
  //   isbn_10: string;
  //   isbn_13: string;
}

export default function BookStartDialog({
  open,
  onOpenChange,
  book,
}: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  book: bookType;
}) {
  console.log(book.id);
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <form>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader className="grid grid-cols-3">
            <div>
              <img src={book.imageLinks} alt={book.title} className="w-full" />
            </div>
            <div className="col-span-2 flex flex-col">
              <DialogTitle>{book.title}</DialogTitle>
              <DialogDescription>By {book.authors}</DialogDescription>
            </div>
          </DialogHeader>
          <div className="grid gap-4">
            <div className="grid gap-3">
              <Label htmlFor="title">Title</Label>
              <Input id="title" name="title" defaultValue={book.title} />
            </div>
            <div className="grid gap-3">
              <Label htmlFor="author">Author</Label>
              <Input id="author" name="author" defaultValue={book.authors} />
            </div>
          </div>
          <DialogFooter>
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
            <Button type="submit">Save changes</Button>
          </DialogFooter>
        </DialogContent>
      </form>
    </Dialog>
  );
}
