import { useRef } from 'react';
import { Button } from '../ui/button';

const ChatDetails = () => {
  const videoRef = useRef(null);

  const handleClickJoin = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: true,
      });

      // Now you can use the stream, e.g., attach it to a video element
      const videoElement = document.querySelector('video');
      if (videoElement) {
        videoElement.srcObject = stream;
        videoElement.play();
      }
    } catch (err) {
      console.error('Error accessing camera:', err);
    }
  };

  return (
    <div>
      <Button
        variant="outline"
        size="sm"
        className="cursor-pointer"
        onClick={handleClickJoin}
      >
        JOIN
      </Button>
      <video ref={videoRef} width="400" height="300" autoPlay muted />
    </div>
  );
};

export default ChatDetails;
