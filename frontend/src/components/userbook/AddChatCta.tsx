import { Sparkles } from 'lucide-react';
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
} from '../ui/empty';

const AddChatCta = () => {
  return (
    <Empty className="border border-dashed border-indigo-100 bg-gradient-to-br from-teal-50/50 via-indigo-50/50 to-teal-50/50">
      <EmptyHeader>
        <EmptyMedia variant="icon" className="bg-white/60 shadow-sm">
          <Sparkles className="stroke-indigo-900" />
        </EmptyMedia>
        <EmptyTitle className="text-indigo-900">No AI Chats Yet</EmptyTitle>
        <EmptyDescription className="text-indigo-900/70">
          You have no AI chats for this book.
          <br />
          Dive in and explore this book with Gemini or ChatGPT!
        </EmptyDescription>
      </EmptyHeader>
    </Empty>
  );
};

export default AddChatCta;
