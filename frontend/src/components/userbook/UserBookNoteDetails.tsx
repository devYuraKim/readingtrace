import { useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useGetUserBook } from '@/queries/book-status.query';
import { useAuthStore } from '@/store/useAuthStore';
import { Tooltip, TooltipContent } from '@radix-ui/react-tooltip';
import {
  QueryClient,
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query';
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

const UserBookNoteDetails = () => {
  const [userNote, setUserNote] = useState('');
  const [finalUserNote, setFinalUserNote] = useState('');
  const { bookId } = useParams();
  const numericBookId = Number(bookId);
  const userId = useAuthStore((state) => state.user?.userId);

  const { data: userBook, isPending: isPendingUserBook } = useGetUserBook(
    userId,
    numericBookId,
  );

  const { isPending: isPendingUserNote } = useQuery({
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
      const res = await apiClient.post(`/users/${userId}/notes`, {
        finalUserNote,
        bookId: numericBookId,
      });
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
    const normalizedUserNote = userNote.trim();

    if (normalizedUserNote === '') {
      toast.error('note cannot be empty');
      return;
    }
    setUserNote(normalizedUserNote);
    setFinalUserNote(normalizedUserNote);

    alert(normalizedUserNote);
  };

  return (
    <div>
      <div>
        UserBookChatDetails
        <div className="font-semibold text-lg">{userBook?.title}</div>
        <div className="text-sm text-muted-foreground">{userBook?.authors}</div>
      </div>
      <InputGroup>
        <InputGroupAddon
          align="block-start"
          className="bg-gray-100 p-0 px-3 rounded-t-md"
        >
          <Tooltip>
            <TooltipContent
              align="start"
              className="bg-white p-2 px-4 rounded-sm shadow-lg text-xs"
            >
              Title is optional
            </TooltipContent>
            <TooltipTrigger asChild>
              <InputGroupInput
                placeholder="title (optional)"
                className="p-0 text-black font-light"
              />
            </TooltipTrigger>
          </Tooltip>
        </InputGroupAddon>
        <InputGroupTextarea
          placeholder={`Start your notes on '${userBook?.title}'`}
          onChange={(e) => setUserNote(e.target.value)}
          value={userNote}
        />

        <InputGroupAddon align="block-end" className="py-2">
          <Tooltip>
            <TooltipContent
              align="start"
              className="bg-white p-2 px-4 rounded-sm shadow-lg text-xs"
            >
              Reference values are optional
            </TooltipContent>
            <TooltipTrigger asChild>
              <div className="flex gap-3 items-center">
                <span className="mr-3 font-light">Reference</span>
                <Label className="text-xs font-light">
                  Page
                  <InputGroupInput className="border-1 border-gray-200 rounded-sm w-15 h-7 text-xs text-black font-light" />
                </Label>

                <Label className="text-xs font-light">
                  Line
                  <InputGroupInput className="border-1 border-gray-200 rounded-sm w-15 h-7 text-xs text-black font-light" />
                </Label>
              </div>
            </TooltipTrigger>
          </Tooltip>
          <InputGroupButton
            variant="default"
            className="ml-auto rounded-sm cursor-pointer focus:ring-0"
            onClick={handleSubmit}
          >
            Save
          </InputGroupButton>
        </InputGroupAddon>
      </InputGroup>
    </div>
  );
};

export default UserBookNoteDetails;
