import { useEffect } from 'react';
import { useAuthStore } from '@/store/useAuthStore';
import { useUserPresenceStore } from '@/store/useUserPresenceStore';
import { useWebSocketStore } from '@/store/useWebSocketStore';
import { Client } from '@stomp/stompjs';
import { Outlet } from 'react-router-dom';
import { toast } from 'sonner';

const WebSocketProvider = () => {
  const stompClient = useWebSocketStore((state) => state.stompClient);
  const setStompClient = useWebSocketStore((state) => state.setStompClient);
  const setOnlineUserIds = useUserPresenceStore(
    (state) => state.setOnlineUserIds,
  );

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
          Authorization: `${getBearerToken(accessToken ?? '')}`,
        },
        onConnect: () => {
          toast.success('Established WebSocket Connection');

          client.subscribe('/topic/presence', (message) => {
            const onlineUserIds = JSON.parse(message.body);
            setOnlineUserIds(onlineUserIds);
          });
        },
      });
      client.activate();
      setStompClient(client);
    }
  }, [stompClient, accessToken, setStompClient, setOnlineUserIds]);

  return <Outlet />;
};

export default WebSocketProvider;
