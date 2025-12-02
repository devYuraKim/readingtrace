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
    <Empty className="border border-dashed">
      <EmptyHeader>
        <EmptyMedia variant="icon">
          <Sparkles />
        </EmptyMedia>
        <EmptyTitle>No AI Chats Yet</EmptyTitle>
        <EmptyDescription>
          This chat is empty. Dive in and explore this book with Gemini or
          ChatGPT!
        </EmptyDescription>
      </EmptyHeader>
    </Empty>
  );
};

export default AddChatCta;
