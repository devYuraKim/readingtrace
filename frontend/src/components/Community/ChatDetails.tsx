import { useRef } from 'react';
import { Button } from '../ui/button';

const ChatDetails = () => {
  const videoRef = useRef<HTMLVideoElement>(null);

  let localStream: MediaStream;
  let peerConnection: RTCPeerConnection;

  let peerConfiguration: RTCConfiguration = {
    iceServers: [
      {
        urls: ['stun:stun.l.google.com:19302', 'stun:stun1.l.google.com:19302'],
      },
    ],
  };

  const handleClickJoin = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: true,
      });
      localStream = stream;

      const videoElement = videoRef.current;
      if (videoElement) {
        videoElement.srcObject = stream;
        videoElement.play();
      }

      createPeerConnection();

      const offer = await peerConnection.createOffer();
      await peerConnection.setLocalDescription(offer);
      console.log('creating offer');
      console.log(offer);
    } catch (err) {
      console.error('Error accessing camera:', err);
    }
  };

  const createPeerConnection = () => {
    peerConnection = new RTCPeerConnection(peerConfiguration);

    if (localStream) {
      localStream.getTracks().forEach((track) => {
        peerConnection.addTrack(track, localStream);
      });
    }

    peerConnection.addEventListener('icecandidate', (e) => {
      console.log('ice candidates');
      console.log(e);
    });
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
