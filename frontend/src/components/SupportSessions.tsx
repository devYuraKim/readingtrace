import { useAuthStore } from '@/store/useAuthStore';
import { Client } from '@stomp/stompjs';
import { Button } from './ui/button';

function SupportChat() {
  const getBrokerURL = () => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    // const host = window.location.host;
    const host = 'localhost:8080';
    return `${protocol}//${host}/ws`;
  };

  const accessToken = useAuthStore((state) => state.accessToken);
  const getBearerToken = (token: string) =>
    token.startsWith('Bearer ') ? token : `Bearer ${token}`;

  const handleChatStart = () => {
    const stompClient = new Client({
      brokerURL: getBrokerURL(),
      connectHeaders: {
        Authorization: `Bearer ${getBearerToken(accessToken ?? '')}`,
      },
      onConnect: () => {
        stompClient.subscribe('/queue/sessions', (message) => {
          const data = JSON.parse(message.body);
          console.log(data);
        });
        stompClient.publish({
          destination: '/app/sessions',
          body: JSON.stringify({
            sender: 'USER ID HERE',
            recipient: 'ADMIN ID HERE',
            content: 'Hello, I need help!',
            timestamp: new Date().toISOString().replace('Z', ''),
            type: 'CHAT',
          }),
        });
      },
    });
    stompClient.activate();
  };
  return (
    <div>
      <h1>SupportChat</h1>
      <Button onClick={handleChatStart}>Check Sessions</Button>
    </div>
  );
}

export default SupportChat;
