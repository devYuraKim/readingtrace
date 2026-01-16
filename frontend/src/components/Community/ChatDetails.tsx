import { useRef } from 'react';
import { Button } from '../ui/button';

const ChatDetails = () => {
  const videoRef = useRef(null);

  let peerConnection = null;

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

      await createPeerConnection();
    } catch (err) {
      console.error('Error accessing camera:', err);
    }
  };

  const createPeerConnection = () => {
    peerConnection = new RTCPeerConnection();
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
      <video
        ref={videoRef}
        width="400"
        height="300"
        autoPlay
        muted
        style={{ transform: 'scaleX(-1)' }}
      />
    </div>
  );
};

export default ChatDetails;
