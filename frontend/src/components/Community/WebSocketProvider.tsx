import { useEffect, useState } from 'react';
import { useAuthStore } from '@/store/useAuthStore';
import { useWebSocketStore } from '@/store/useWebSocketStore';
import { Client } from '@stomp/stompjs';
import { Outlet } from 'react-router-dom';
import { toast } from 'sonner';

const WebSocketProvider = () => {
  const stompClient = useWebSocketStore((state) => state.stompClient);
  const setStompClient = useWebSocketStore((state) => state.setStompClient);

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
  }, [stompClient, accessToken, setStompClient]);

  return <Outlet />;
};

export default WebSocketProvider;
