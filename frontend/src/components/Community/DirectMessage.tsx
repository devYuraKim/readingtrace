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

type DirectMessageDto = {
  dmId: number;
  senderId: number;
  receiverId: number;
  message: string;
  createdAt: string;
};

const DirectMessage = () => {
  const [searchParams] = useSearchParams();

  const stompClient = useWebSocketStore((state) => state.stompClient);
  const queryClient = useQueryClient();

  const containerRef = useRef<HTMLDivElement | null>(null);
  const bottomRef = useRef<HTMLDivElement | null>(null);
  const topRef = useRef<HTMLDivElement | null>(null);

  const [message, setMessage] = useState('');
  const [isAtBottom, setIsAtBottom] = useState(false);
  const [isPageActive, setIsPageActive] = useState(true);
  const [hasLoadedInitial, setHasLoadedInitial] = useState(false);

  const userId = useAuthStore((state) => state.user?.userId);
  const receiverId = Number(searchParams.get('to'));

  const LIMIT = 50;
  const [offset, setOffset] = useState(0);
  const [messages, setMessages] = useState<DirectMessageDto[]>([]);

  // ========== FETCH PAST DMS ==========
  const { data: newPage, isPending } = useQuery<DirectMessageDto[], Error>({
    queryKey: ['dms', userId, receiverId, offset],
    queryFn: async () => {
      const res = await apiClient.get<DirectMessageDto[]>(
        `/users/${userId}/dms?to=${receiverId}&limit=${LIMIT}&offset=${offset}`,
      );
      return res.data;
    },
    placeholderData: messages,
  });

  // Prepend older messages whenever a new page arrives
  useEffect(() => {
    if (!newPage?.length || !containerRef.current) return;

    const container = containerRef.current;

    if (!hasLoadedInitial) {
      // First load → scroll to bottom
      setMessages(newPage);
      requestAnimationFrame(() => {
        container.scrollTop = container.scrollHeight;
      });
      setHasLoadedInitial(true);
    } else {
      // Prepending older messages
      const scrollHeightBefore = container.scrollHeight;

      setMessages((prev) => {
        const existingIds = new Set(prev.map((m) => m.dmId));
        const filteredNew = newPage.filter((m) => !existingIds.has(m.dmId));
        return [...filteredNew, ...prev];
      });

      requestAnimationFrame(() => {
        requestAnimationFrame(() => {
          const scrollHeightAfter = container.scrollHeight;
          container.scrollTop += scrollHeightAfter - scrollHeightBefore;
        });
      });
    }
  }, [newPage, hasLoadedInitial]);

  // ========== PAGE VISIBILITY ==========
  useEffect(() => {
    const handleVisibilityChange = () => setIsPageActive(!document.hidden);
    document.addEventListener('visibilitychange', handleVisibilityChange);
    return () =>
      document.removeEventListener('visibilitychange', handleVisibilityChange);
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

  // ========== TOP OBSERVER ==========
  const handleScroll = () => {
    if (!containerRef.current) return;
    if (containerRef.current.scrollTop === 0) {
      setOffset((prev) => prev + LIMIT);
    }
  };

  useEffect(() => {
    containerRef.current?.addEventListener('scroll', handleScroll);
    return () =>
      containerRef.current?.removeEventListener('scroll', handleScroll);
  }, []);

  // ========== WEBSOCKET SUBSCRIPTION FOR NEW DMS ==========
  useEffect(() => {
    if (!stompClient) return;

    const subscription = stompClient.subscribe('/user/queue/dm', (message) => {
      const dm: DirectMessageDto = JSON.parse(message.body);

      setMessages((prev) => [...prev, dm]);

      queryClient.setQueryData<DirectMessageDto[]>(
        ['dms', userId, receiverId],
        (old) => {
          if (!old) return [dm];
          return [...old, dm];
        },
      );
    });

    return () => subscription.unsubscribe();
  }, [stompClient, userId, receiverId, queryClient]);

  // ========== WEBSOCKET SUBSCRIPTION FOR READ STATUS ==========
  useEffect(() => {
    if (!stompClient) return;

    const subscription = stompClient.subscribe(
      '/user/queue/read',
      (message) => {
        const markReadDto = JSON.parse(message.body);

        setMessages((prev) =>
          prev.map((dm) =>
            dm.senderId === userId &&
            new Date(dm.createdAt).getTime() <=
              new Date(markReadDto.scrolledAt).getTime()
              ? { ...dm, read: true }
              : dm,
          ),
        );
      },
    );

    return () => subscription.unsubscribe();
  }, [stompClient, userId]);

  // ========== AUTO SCROLL ==========
  useEffect(() => {
    if (!messages.length) return;
    if (isAtBottom) bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isAtBottom]);

  // ========== SAVE LAST SCROLLED AT ON SCROLL REACHING THE BOTTOM ==========
  useEffect(() => {
    if (!isAtBottom || !messages.length || !isPageActive) return;

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
  }, [isAtBottom, messages, isPageActive, userId, receiverId, stompClient]);

  // ========== SEND DM ==========
  const handleClickSend = () => {
    if (!message.trim()) {
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
        {/* TOP SENTINEL */}
        <div ref={topRef} />
        {!isPending &&
          messages.map((dm) => (
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
