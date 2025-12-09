import { useEffect, useState } from 'react';
import { useAuthStore } from '@/store/useAuthStore';
import { Client } from '@stomp/stompjs';
import { Outlet } from 'react-router-dom';
import { toast } from 'sonner';

const WebSocketProvider = () => {
  const [stompClient, setStompClient] = useState<Client | null>(null);

  const getBrokerURL = () => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    // const host = window.location.host;
    const host = 'localhost:8080';
    return `${protocol}//${host}/ws`;
  };

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
          toast.success('Established WebSocket Connection');
        },
      });
      client.activate();
      setStompClient(client);
    }
  }, [stompClient, accessToken]);

  return <Outlet />;
};

export default WebSocketProvider;
