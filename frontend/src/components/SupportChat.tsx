import { useEffect, useState } from 'react';
import { useAuthStore } from '@/store/useAuthStore';
import { Client } from '@stomp/stompjs';
import { ArrowUp, Paperclip } from 'lucide-react';
import { toast } from 'sonner';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from './ui/dropdown-menu';
import {
  InputGroup,
  InputGroupAddon,
  InputGroupButton,
  InputGroupInput,
} from './ui/input-group';

type Messages = {
  sender: string;
  recipient: string;
  content: string;
  timestamp: string;
  type: string;
};

function SupportChat() {
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState<Messages[]>([]);

  const getBrokerURL = () => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    // const host = window.location.host;
    const host = 'localhost:8080';
    return `${protocol}//${host}/ws`;
  };

  const userId = useAuthStore((state) => state.user?.userId);
  const accessToken = useAuthStore((state) => state.accessToken);
  const getBearerToken = (token: string) =>
    token.startsWith('Bearer ') ? token : `Bearer ${token}`;

  useEffect(() => {
    if (!stompClient) {
      const client = new Client({
        brokerURL: getBrokerURL(),
        connectHeaders: {
          Authorization: `Bearer ${getBearerToken(accessToken ?? '')}`,
        },
        onConnect: () => {
          toast.success('Connected to support chat');
          client.subscribe('/topic/support', (newMessage) => {
            const parsedMessage = JSON.parse(newMessage.body);
            setMessages((messages) => [...messages, parsedMessage]);
          });
        },
      });
      client.activate();
      setStompClient(client);
    }
  }, [stompClient, accessToken]);

  const sendMessage = (message: string) => {
    if (!stompClient || !stompClient.connected) {
      toast.error('Not connected to chat');
      return;
    }

    stompClient.publish({
      destination: '/app/support.sendMessage',
      body: JSON.stringify({
        sender: userId,
        recipient: 'ADMIN',
        content: message,
        timestamp: new Date().toISOString().replace('Z', ''),
        type: 'CHAT',
      }),
    });
    setMessage('');
  };

  return (
    <>
      {messages.map((message) => (
        <div key={message.timestamp}>{message.content}</div>
      ))}
      <InputGroup className="fixed bottom-5 w-[80%] left-1/2 -translate-x-1/2">
        <InputGroupInput
          placeholder="Send a message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              sendMessage(message);
            }
          }}
        />
        <InputGroupAddon align="inline-end">
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <InputGroupButton
                variant="outline"
                className="rounded-full"
                size="icon-xs"
              >
                <Paperclip />
              </InputGroupButton>
            </DropdownMenuTrigger>
            <DropdownMenuContent
              side="top"
              align="start"
              className="[--radius:0.95rem]"
            >
              <DropdownMenuItem>Auto</DropdownMenuItem>
              <DropdownMenuItem>Agent</DropdownMenuItem>
              <DropdownMenuItem>Manual</DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
          <InputGroupButton
            variant="default"
            className="rounded-full"
            size="icon-xs"
          >
            <ArrowUp />
            <span className="sr-only">Send</span>
          </InputGroupButton>
        </InputGroupAddon>
      </InputGroup>
    </>
  );
}

export default SupportChat;
