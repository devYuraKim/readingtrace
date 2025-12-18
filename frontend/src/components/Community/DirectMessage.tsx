import { useEffect, useRef, useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useWebSocketStore } from '@/store/useWebSocketStore';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { Send } from 'lucide-react';
import { useSearchParams } from 'react-router-dom';
import { toast } from 'sonner';
import { Button } from '../ui/button';
import { Input } from '../ui/input';

const DirectMessage = () => {
  const stompClient = useWebSocketStore((state) => state.stompClient);
  const queryClient = useQueryClient();

  const [searchParams] = useSearchParams();

  const [message, setMessage] = useState('');

  const userId = useAuthStore((state) => state.user?.userId);
  const receiverId = Number(searchParams.get('to'));

  const bottomRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'auto' });
  }, []);

  useEffect(() => {
    if (!stompClient) return;

    const subscription = stompClient.subscribe('/user/queue/dm', (message) => {
      const dm = JSON.parse(message.body);

      queryClient.setQueryData(
        ['dms', userId, receiverId],
        (old: any[] | undefined) => {
          if (!old) return [dm];
          return [...old, dm];
        },
      );
    });

    return () => {
      subscription.unsubscribe();
    };
  }, [stompClient, userId, receiverId, queryClient]);

  const { data: dms, isPending } = useQuery({
    queryKey: ['dms', userId, receiverId],
    queryFn: async () => {
      const res = await apiClient.get(`/users/${userId}/dms?to=${receiverId}`);
      return res.data;
    },
  });

  const handleClickSend = () => {
    alert('button clicked');
    if (message.trim().length === 0) {
      toast.error('Message needed');
      return;
    }

    stompClient?.publish({
      destination: '/app/dm',
      body: JSON.stringify({
        senderId: userId,
        receiverId: receiverId,
        message: message,
      }),
    });

    setMessage('');
  };

  return (
    <div>
      DirectMessage
      {!isPending &&
        dms?.map((dm) => (
          <div key={dm.id} className="mb-2">
            <div
              className={`flex w-full ${
                dm.senderId === userId ? 'justify-end' : 'justify-start'
              }`}
            >
              <div
                className={`inline-block max-w-[60%] px-3 py-1 rounded-lg mx-3 text-sm ${dm.senderId === userId ? 'bg-green-50' : 'bg-gray-50'}`}
              >
                {dm.message}
              </div>
            </div>
          </div>
        ))}
      <div ref={bottomRef} />
      <div className="flex mt-10 mb-5 gap-3 mx-3">
        <Input value={message} onChange={(e) => setMessage(e.target.value)} />
        <Button
          className="cursor-pointer"
          onClick={handleClickSend}
          disabled={message.trim().length === 0}
        >
          <Send />
        </Button>
      </div>
    </div>
  );
};

export default DirectMessage;
