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
  const [searchParams] = useSearchParams();

  const stompClient = useWebSocketStore((state) => state.stompClient);
  const queryClient = useQueryClient();

  const containerRef = useRef<HTMLDivElement | null>(null);
  const bottomRef = useRef<HTMLDivElement | null>(null);

  const [message, setMessage] = useState('');
  const [isAtBottom, setIsAtBottom] = useState(false);
  const [isPageActive, setIsPageActive] = useState(true);

  const userId = useAuthStore((state) => state.user?.userId);
  const receiverId = Number(searchParams.get('to'));

  const limit = 50;
  let offset = 0;

  // ========== FETCH PAST DMS ==========
  const { data: dms, isPending } = useQuery({
    queryKey: ['dms', userId, receiverId],
    queryFn: async () => {
      const res = await apiClient.get(
        `/users/${userId}/dms?to=${receiverId}&limit=${limit}&offset=${offset}`,
      );
      return res.data;
    },
  });

  // ========== PAGE VISIBILITY ==========
  useEffect(() => {
    const handleVisibilityChange = () => {
      setIsPageActive(!document.hidden);
    };

    document.addEventListener('visibilitychange', handleVisibilityChange);

    return () => {
      document.removeEventListener('visibilitychange', handleVisibilityChange);
    };
  }, []);

  // ========== BOTTOM OBSERVER ==========
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

  // ========== WEB SOCKET SUBSCRIPTION FOR DM RECEPTION ==========
  useEffect(() => {
    if (!stompClient) return;

    const subscription = stompClient.subscribe('/user/queue/dm', (message) => {
      const dm = JSON.parse(message.body);

      queryClient.setQueryData(
        ['dms', userId, receiverId],
        (old: any[] | undefined) => {
          if (!old) return [{ ...dm, read: dm.read || false }]; // first message
          return [
            ...old.map((m) => ({ ...m, read: m.read || false })), // existing messages
            { ...dm, read: dm.read || false }, // append new message with read
          ];
        },
      );
    });

    return () => subscription.unsubscribe();
  }, [stompClient, userId, receiverId, queryClient]);

  // ========== WEB SOCKET SUBSCRIPTION FOR LAST ROLLED AT RECEPTION ==========
  useEffect(() => {
    if (!stompClient) return;

    const subscription = stompClient.subscribe(
      '/user/queue/read',
      (message) => {
        const markReadDto = JSON.parse(message.body);

        queryClient.setQueryData(
          ['dms', userId, receiverId],
          (old: any[] | undefined) => {
            if (!old) return [];
            return old.map((dm) =>
              dm.senderId === userId &&
              new Date(dm.createdAt).getTime() <=
                new Date(markReadDto.scrolledAt).getTime()
                ? { ...dm, read: true }
                : dm,
            );
          },
        );
      },
    );
    return () => subscription.unsubscribe();
  }, [stompClient, receiverId, userId, queryClient]);

  // ========== AUTO SCROLL ==========
  useEffect(() => {
    if (!dms?.length) return;

    if (isAtBottom) {
      bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
    }
  }, [dms, isAtBottom]);

  // ========== SAVE LAST SCROLLED AT ON SCROLL REACHING THE BOTTOM ==========
  useEffect(() => {
    if (!isAtBottom || !dms?.length || !isPageActive) return;

    const now = new Date();
    const pad = (n: number) => n.toString().padStart(2, '0');

    const localDateTimeString =
      now.getFullYear() +
      '-' +
      pad(now.getMonth() + 1) +
      '-' +
      pad(now.getDate()) +
      'T' +
      pad(now.getHours()) +
      ':' +
      pad(now.getMinutes()) +
      ':' +
      pad(now.getSeconds());

    stompClient?.publish({
      destination: '/app/dm/read',
      body: JSON.stringify({
        scrolledUserId: userId,
        notifiedUserId: receiverId,
        scrolledAt: localDateTimeString,
      }),
    });
  }, [isAtBottom, dms, isPageActive, userId, receiverId, stompClient]);

  // ?????
  useEffect(() => {
    const handleFocus = () => {
      if (!dms?.length) return;
      const latest = dms[dms.length - 1];
      stompClient?.publish({
        destination: '/app/dm/read',
        body: JSON.stringify({
          scrolledUserId: userId,
          notifiedUserId: receiverId,
          scrolledAt: latest.createdAt,
        }),
      });
    };

    window.addEventListener('focus', handleFocus);
    return () => window.removeEventListener('focus', handleFocus);
  }, [dms]);

  // ========== SEND DM ==========
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
                className={`flex w-full items-center gap-2 ${
                  dm.senderId === userId ? 'justify-end' : 'justify-start'
                }`}
              >
                {dm.senderId === userId && (
                  <div className="text-xs text-gray-400">
                    {dm.read ? '' : 'unread'}
                  </div>
                )}
                <div
                  className={`inline-block max-w-[60%] px-3 py-1 rounded-xl text-sm shadow-sm ${
                    dm.senderId === userId
                      ? 'bg-green-50 rounded-br-none'
                      : 'bg-gray-50 rounded-bl-none'
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
