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

  // NEW
  const containerRef = useRef<HTMLDivElement | null>(null);
  const bottomRef = useRef<HTMLDivElement | null>(null);
  const [isAtBottom, setIsAtBottom] = useState(false);

  // Observe whether bottom is visible
  useEffect(() => {
    if (!containerRef.current || !bottomRef.current) return;

    const observer = new IntersectionObserver(
      ([entry]) => {
        setIsAtBottom(entry.isIntersecting);
      },
      {
        root: containerRef.current,
        threshold: 1.0,
      },
    );

    observer.observe(bottomRef.current);
    return () => observer.disconnect();
  }, []);

  // WebSocket subscription
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

    return () => subscription.unsubscribe();
  }, [stompClient, userId, receiverId, queryClient]);

  const { data: dms, isPending } = useQuery({
    queryKey: ['dms', userId, receiverId],
    queryFn: async () => {
      const res = await apiClient.get(`/users/${userId}/dms?to=${receiverId}`);
      return res.data;
    },
  });

  // Auto-scroll ONLY if user is already at bottom
  useEffect(() => {
    if (!dms?.length) return;

    if (isAtBottom) {
      bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
    }
  }, [dms, isAtBottom]);

  // Mark as read ONLY when bottom is visible
  useEffect(() => {
    if (!isAtBottom || !dms?.length) return;

    const latest = dms[dms.length - 1];

    apiClient.post(`/users/${userId}/dms/read`, {
      senderId: receiverId,
      receiverId: userId,
      lastReadAt: latest.createdAt,
    });
  }, [isAtBottom, dms, receiverId, userId]);

  const handleClickSend = () => {
    if (message.trim().length === 0) {
      toast.error('Message needed');
      return;
    }

    stompClient?.publish({
      destination: '/app/dm',
      body: JSON.stringify({
        senderId: userId,
        receiverId,
        message,
      }),
    });

    setMessage('');
  };

  return (
    <div>
      {/* SCROLL CONTAINER */}
      <div
        ref={containerRef}
        className="h-[500px] overflow-y-auto mt-3 scrollbar-hide"
      >
        {!isPending &&
          dms?.map((dm) => (
            <div key={dm.id} className="mb-2">
              <div
                className={`flex w-full ${
                  dm.senderId === userId ? 'justify-end' : 'justify-start'
                }`}
              >
                <div
                  className={`inline-block max-w-[60%] px-3 py-1 rounded-lg mx-5 text-sm ${
                    dm.senderId === userId ? 'bg-green-50' : 'bg-gray-50'
                  }`}
                >
                  {dm.message}
                </div>
              </div>
            </div>
          ))}
        {/* BOTTOM SENTINEL */}
        <div ref={bottomRef} />
      </div>
      <div className="flex mt-5 gap-3 mx-3">
        <Input value={message} onChange={(e) => setMessage(e.target.value)} />
        <Button
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
