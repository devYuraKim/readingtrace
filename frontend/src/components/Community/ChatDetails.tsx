import { useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { DialogClose } from '@radix-ui/react-dialog';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Button } from '../ui/button';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTrigger,
} from '../ui/dialog';
import { Input } from '../ui/input';
import { Spinner } from '../ui/spinner';

const ChatDetails = () => {
  const [chatRoomName, setChatRoomName] = useState('');
  const userId = useAuthStore((state) => state.user?.userId);

  const queryClient = useQueryClient();

  const { mutate } = useMutation({
    mutationKey: ['createChatRoom'],
    mutationFn: async () => {
      const res = await apiClient.post(`/users/${userId}/chats`, {
        chatRoomName,
      });
      return res.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['getChatRooms'],
      });
    },
  });

  const handleClickCreate = () => {
    mutate();
  };

  const { data: chatRooms, isPending } = useQuery({
    queryKey: ['getChatRooms'],
    queryFn: async () => {
      const res = await apiClient.get(`users/${userId}/chats`);
      return res.data;
    },
  });

  const handleClickJoin = () => {};

  return (
    <div>
      <Dialog>
        <DialogTrigger>
          <Button className="cursor-pointer mb-3">start a chat</Button>
        </DialogTrigger>
        <DialogContent>
          <DialogHeader>Start a chatroom</DialogHeader>
          <DialogDescription>Enter chatroom name</DialogDescription>
          <Input
            placeholder="chatroom name"
            value={chatRoomName}
            onChange={(e) => setChatRoomName(e.target.value)}
          />
          <DialogFooter>
            <DialogClose asChild>
              <Button className="cursor-pointer">Cancel</Button>
            </DialogClose>
            <Button className="cursor-pointer" onClick={handleClickCreate}>
              Create
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
      {isPending && <Spinner />}
      {!isPending &&
        chatRooms.map((chatRoom) => (
          <div
            key={chatRoom.chatRoomId}
            className="flex items-center justify-between p-3 rounded-2xl border border-amber-300 shadow-sm hover:bg-amber-50 transition mb-3"
          >
            <div className="w-[30%] font-medium truncate ml-5">
              {chatRoom.chatRoomName}
            </div>
            <div className="flex w-[20%] items-center gap-1">
              <img
                className="w-6 h-6 rounded-full"
                src={chatRoom.ownerProfileImageUrl}
              />
              <span className="truncate">{chatRoom.ownerNickname}</span>
            </div>
            <div className="w-[10%]">
              <Button
                variant="outline"
                size="sm"
                className="cursor-pointer"
                onClick={handleClickJoin}
              >
                join
              </Button>
            </div>
          </div>
        ))}
    </div>
  );
};

export default ChatDetails;
