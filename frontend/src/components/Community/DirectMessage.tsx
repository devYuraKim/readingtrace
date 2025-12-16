import { useEffect, useState } from 'react';
import { useAuthStore } from '@/store/useAuthStore';
import { useWebSocketStore } from '@/store/useWebSocketStore';
import { Send } from 'lucide-react';
import { toast } from 'sonner';
import { Button } from '../ui/button';
import { Input } from '../ui/input';

const DirectMessage = ({ receiverId }) => {
  const stompClient = useWebSocketStore((state) => state.stompClient);
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);

  const userId = useAuthStore((state) => state.user?.userId);

  useEffect(() => {
    if (!stompClient) return;

    const subscription = stompClient.subscribe('/user/queue/dm', (message) => {
      const dm = JSON.parse(message.body);
      setMessages((prev) => [...prev, dm]);
    });

    return () => {
      subscription.unsubscribe(); // clean up when component unmounts
    };
  }, [stompClient]);

  const handleClickSend = () => {
    alert('button clicked');
    setMessage('');

    if (message.trim().length === 0) toast.error('Message needed');

    stompClient?.publish({
      destination: '/app/dm',
      body: JSON.stringify({
        senderId: userId,
        receiverId: receiverId,
        message: message,
      }),
    });
  };

  return (
    <div>
      DirectMessage
      <div className="flex">
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
