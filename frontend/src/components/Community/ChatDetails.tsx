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
      // trigger getUserMedia request
      const stream = await navigator.mediaDevices.getUserMedia({
        video: true,
      });
      localStream = stream;

      const videoElement = videoRef.current;
      if (videoElement) {
        videoElement.srcObject = stream;
        videoElement.play();
      }

      createPeerConnection(); //add tracks
      console.log('peer connection');
      console.log(peerConnection);
    } catch (err) {
      console.error('Error accessing camera:', err);
    }
  };

  const handleNegotiationNeededEvent = async () => {
    // create an offer and add offer to peerConnection
    const offer = await peerConnection.createOffer();
    await peerConnection.setLocalDescription(offer);
    console.log('creating offer');
    console.log(offer);
  };

  const createPeerConnection = () => {
    peerConnection = new RTCPeerConnection(peerConfiguration);

    //add an event listener to indicate when we should start the signaling process
    peerConnection.addEventListener(
      'negotiationneeded',
      handleNegotiationNeededEvent,
    );

    // add type of media/data we wish to send&receive
    // add tracks to peerConnection object
    if (localStream) {
      localStream.getTracks().forEach((track) => {
        peerConnection.addTrack(track, localStream);
      });
    }

    // listen for ice candidates
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
