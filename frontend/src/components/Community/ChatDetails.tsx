import { useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { DialogClose } from '@radix-ui/react-dialog';
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

const ChatDetails = () => {
  const [chatRoomName, setChatRoomName] = useState('');
  const userId = useAuthStore((state) => state.user?.userId);

  const handleClickCreate = () => {
    apiClient.post(`/users/${userId}/chats`, { chatRoomName });
  };

  return (
    <div>
      ChatDetails
      <Dialog>
        <DialogTrigger>
          <Button className="cursor-pointer">start a chat</Button>
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
    </div>
  );
};

export default ChatDetails;
