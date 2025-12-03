import { NotepadTextDashed } from 'lucide-react';
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
} from '../ui/empty';

const AddNoteCta = () => {
  return (
    <Empty className="border border-dashed border-rose-100 bg-gradient-to-br from-rose-50/50 via-amber-50/50 to-rose-50/50">
      <EmptyHeader>
        <EmptyMedia variant="icon" className="bg-white/60 shadow-sm">
          <NotepadTextDashed className="stroke-amber-900" />
        </EmptyMedia>
        <EmptyTitle className="text-orange-900">No Notes Yet</EmptyTitle>
        <EmptyDescription className="text-amber-900/70">
          You haven't added any notes for this book. <br />
          Start capturing your ideas, insights and quotes as you read.
        </EmptyDescription>
      </EmptyHeader>
    </Empty>
  );
};

export default AddNoteCta;
