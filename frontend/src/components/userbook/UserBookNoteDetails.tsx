import { useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useGetUserBook } from '@/queries/book-status.query';
import { useAuthStore } from '@/store/useAuthStore';
import { Tooltip, TooltipContent } from '@radix-ui/react-tooltip';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Eye, EyeOff, MessageSquareQuote, Quote, Sparkles } from 'lucide-react';
import { useParams } from 'react-router-dom';
import { toast } from 'sonner';
import {
  InputGroup,
  InputGroupAddon,
  InputGroupButton,
  InputGroupInput,
  InputGroupTextarea,
} from '../ui/input-group';
import { Label } from '../ui/label';
import { TooltipTrigger } from '../ui/tooltip';
import AddNoteCta from './AddNoteCta';

const UserBookNoteDetails = () => {
  const { bookId } = useParams();
  const numericBookId = Number(bookId);
  const userId = useAuthStore((state) => state.user?.userId);

  const [userNoteForm, setUserNoteForm] = useState({
    userId,
    bookId: numericBookId,
    referencePage: '',
    referenceLine: '',
    noteTitle: '',
    noteContent: '',
  });

  const { data: userBook, isPending: isPendingUserBook } = useGetUserBook(
    userId,
    numericBookId,
  );

  const { data: userNotes, isPending: isPendingUserNote } = useQuery({
    queryKey: ['getUserNote', userId, numericBookId],
    queryFn: async () => {
      const res = await apiClient.get(
        `/users/${userId}/notes?bookId=${numericBookId}`,
      );
      return res.data;
    },
  });

  const queryClient = useQueryClient();
  const { mutate, isPending: isPendingSaveNote } = useMutation({
    mutationFn: async () => {
      const res = await apiClient.post(`/users/${userId}/notes`, userNoteForm);
      return res.data;
    },
    mutationKey: ['saveUserNote'],
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['getUserNote', userId, numericBookId],
      });
    },
  });

  const handleSubmit = () => {
    const normalizedNoteContent = userNoteForm.noteContent.trim();
    const normalizedNoteTitle = userNoteForm.noteTitle.trim();

    setUserNoteForm((prev) => ({
      ...prev,
      noteContent: normalizedNoteContent,
      noteTitle: normalizedNoteTitle,
    }));

    if (normalizedNoteContent === '') {
      toast.error('note content cannot be empty');
      return;
    }

    alert(JSON.stringify(userNoteForm));
  };

  const [tagInput, setTagInput] = useState('');

  const addTag = (e) => {
    if (e.key === 'Enter' && tagInput.trim()) {
      setUserNoteForm((prev) => ({
        ...prev,
        tags: [...(prev.tags || []), tagInput.trim()],
      }));
      setTagInput('');
    }
  };

  const removeTag = (tag) => {
    setUserNoteForm((prev) => ({
      ...prev,
      tags: prev.tags.filter((t) => t !== tag),
    }));
  };

  const noteTypes = [
    { key: 'reflection', label: 'Reflection', icon: Sparkles },
    { key: 'quote', label: 'Quote', icon: MessageSquareQuote },
    { key: 'summary', label: 'Summary', icon: Quote },
  ];

  const emotions = ['❤️', '🤯', '🤔', '✨', '😡'];

  return (
    <>
      <div className="flex justify-between items-center mx-1">
        <div>
          <div className="font-semibold text-lg text-orange-950 leading-tight">
            {userBook?.title}
          </div>
          <div className="text-sm text-orange-900/70">{userBook?.authors}</div>
        </div>
      </div>
      <div>
        <AddNoteCta />
        {!isPendingUserNote && userNotes?.length === 0 && <AddNoteCta />}
      </div>

      <InputGroup className="border border-rose-100 rounded-xl shadow-sm bg-white overflow-hidden transition-all hover:shadow-md duration-300 group">
        <InputGroupAddon
          align="block-start"
          className="bg-gradient-to-r from-amber-50/50 to-rose-50/50 border-b border-rose-100/50 px-4 py-3 flex justify-between items-center"
        >
          <div className="flex-1 mr-4">
            <InputGroupInput
              placeholder="Title (optional)"
              className="p-0 bg-transparent border-none text-rose-950 font-bold placeholder:text-stone-400/80 placeholder:font-medium focus:ring-0 text-sm w-full"
              value={userNoteForm.noteTitle}
              onChange={(e) =>
                setUserNoteForm((prev) => ({
                  ...prev,
                  noteTitle: e.target.value,
                }))
              }
            />
          </div>

          <Tooltip>
            <TooltipTrigger asChild>
              <button
                onClick={() =>
                  setUserNoteForm((prev) => ({
                    ...prev,
                    isQuote: !prev.isQuote,
                  }))
                }
                className={`p-1.5 rounded-md transition-all duration-200 border ${userNoteForm.isQuote ? 'bg-rose-100 text-rose-600 shadow-sm border-rose-200' : 'text-stone-400 hover:bg-stone-100 border-transparent'}`}
              >
                <Quote className="w-4 h-4" />
              </button>
            </TooltipTrigger>
            <TooltipContent className="bg-rose-950 text-rose-50 border-none text-xs">
              {userNoteForm.isQuote ? 'Quote Mode On' : 'Switch to Quote Mode'}
            </TooltipContent>
          </Tooltip>
        </InputGroupAddon>

        <div className="relative">
          {userNoteForm.isQuote && (
            <div className="absolute left-0 top-4 bottom-4 w-1 bg-rose-300 rounded-r-full opacity-80" />
          )}
          <InputGroupTextarea
            placeholder={
              userNoteForm.isQuote
                ? 'Paste the passage directly from the book...'
                : `Write your thoughts on '${userBook?.title}'...`
            }
            className={`w-full min-h-[120px] py-4 pr-4 text-stone-700 bg-white border-none focus:ring-0 resize-none leading-relaxed transition-all ${userNoteForm.isQuote ? 'pl-6 font-serif italic text-lg text-stone-800 placeholder:italic' : 'pl-4 font-sans text-sm placeholder:font-light'}`}
            value={userNoteForm.noteContent}
            onChange={(e) =>
              setUserNoteForm((prev) => ({
                ...prev,
                noteContent: e.target.value,
              }))
            }
          />
        </div>

        <InputGroupAddon
          align="block-end"
          className="px-4 py-3 bg-stone-50/30 border-t border-rose-50 flex flex-col gap-4"
        >
          <div className="flex flex-col gap-2 w-full">
            <div className="flex items-center gap-2">
              <input
                type="text"
                placeholder="Add tags (press Enter)"
                className="flex-1 bg-transparent border-none text-xs text-stone-600 placeholder:text-stone-400 focus:ring-0 p-0"
                value={tagInput}
                onChange={(e) => setTagInput(e.target.value)}
                onKeyDown={addTag}
              />
            </div>
            {userNoteForm.tags?.length > 0 && (
              <div className="flex flex-wrap gap-2">
                {userNoteForm.tags.map((tag) => (
                  <div
                    key={tag}
                    className="px-2 py-0.5 bg-rose-100 text-rose-700 text-[10px] rounded-full flex items-center gap-1"
                  >
                    <Tag className="w-3 h-3" /> {tag}
                    <button
                      className="text-rose-500 hover:text-rose-700"
                      onClick={() => removeTag(tag)}
                    >
                      ×
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="flex justify-between items-start w-full pt-3 border-t border-rose-100/30">
            <div className="flex flex-col gap-3">
              <div className="flex gap-2">
                {noteTypes.map((t) => {
                  const Icon = t.icon;
                  return (
                    <button
                      key={t.key}
                      onClick={() =>
                        setUserNoteForm((prev) => ({
                          ...prev,
                          noteType: t.key,
                        }))
                      }
                      className={`px-2 py-1 rounded-md border text-[10px] flex items-center gap-1 transition-all ${userNoteForm.noteType === t.key ? 'bg-rose-100 border-rose-200 text-rose-700' : 'bg-white border-stone-200 text-stone-400 hover:bg-stone-100'}`}
                    >
                      <Icon className="w-3 h-3" /> {t.label}
                    </button>
                  );
                })}
              </div>
              <div className="flex gap-1 text-lg">
                {emotions.map((emo) => (
                  <button
                    key={emo}
                    onClick={() =>
                      setUserNoteForm((prev) => ({ ...prev, emotion: emo }))
                    }
                    className={`transition-all ${userNoteForm.emotion === emo ? 'scale-110' : 'opacity-60 hover:opacity-100'}`}
                  >
                    {emo}
                  </button>
                ))}
              </div>
            </div>

            <div className="flex flex-col items-end gap-3">
              <div className="flex items-center gap-1 bg-white border border-rose-100 rounded-md px-2 py-1 shadow-sm">
                <span className="text-[10px] font-bold text-stone-400 uppercase">
                  Pg
                </span>
                <input
                  className="w-8 text-xs text-center text-stone-600 bg-transparent border-none focus:ring-0 p-0 placeholder:text-stone-300"
                  placeholder="-"
                  value={userNoteForm.referencePage}
                  onChange={(e) =>
                    setUserNoteForm((prev) => ({
                      ...prev,
                      referencePage: e.target.value,
                    }))
                  }
                />
                <div className="w-[1px] h-3 bg-stone-200 mx-1" />
                <span className="text-[10px] font-bold text-stone-400 uppercase">
                  Ln
                </span>
                <input
                  className="w-8 text-xs text-center text-stone-600 bg-transparent border-none focus:ring-0 p-0 placeholder:text-stone-300"
                  placeholder="-"
                  value={userNoteForm.referenceLine}
                  onChange={(e) =>
                    setUserNoteForm((prev) => ({
                      ...prev,
                      referenceLine: e.target.value,
                    }))
                  }
                />
              </div>

              <Tooltip>
                <TooltipTrigger asChild>
                  <button
                    onClick={() =>
                      setUserNoteForm((prev) => ({
                        ...prev,
                        isPrivate: !prev.isPrivate,
                      }))
                    }
                    className="flex items-center gap-1.5 text-xs font-medium text-stone-500 hover:text-rose-600 transition-colors"
                  >
                    {userNoteForm.isPrivate ? (
                      <>
                        <EyeOff className="w-3.5 h-3.5" /> Private
                      </>
                    ) : (
                      <>
                        <Eye className="w-3.5 h-3.5" /> Public
                      </>
                    )}
                  </button>
                </TooltipTrigger>
                <TooltipContent className="text-xs bg-stone-800 text-white">
                  {userNoteForm.isPrivate
                    ? 'Only visible to you'
                    : 'Visible on your profile'}
                </TooltipContent>
              </Tooltip>

              <InputGroupButton
                variant="default"
                className="h-7 px-4 rounded-full text-xs font-bold text-white shadow-sm shadow-rose-100 bg-gradient-to-r from-rose-400 to-amber-400 hover:from-rose-500 hover:to-amber-500 transition-all duration-300 hover:shadow-md cursor-pointer border-none flex items-center gap-2"
                onClick={handleSubmit}
              >
                Save Note
              </InputGroupButton>
            </div>
          </div>
        </InputGroupAddon>
      </InputGroup>
    </>
  );
};

export default UserBookNoteDetails;
