import { useEffect, useRef, useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useQuery } from '@tanstack/react-query';
import { Camera } from 'lucide-react';
import { Input } from '../ui/input';
import StepTitle from './StepTitle';

const Step1 = () => {
  const userId = useAuthStore((state) => state.user?.userId);
  const email = useAuthStore((state) => state.user?.email);
  const defaultNickname = email?.split('@')[0] || '';

  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [inputNickname, setInputNickname] = useState<string>(defaultNickname);
  const [searchNickname, setSearchNickname] = useState<string | null>(null);

  const imageInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const storedImageUrl = localStorage.getItem('on_profileImageUrl');
    if (storedImageUrl) {
      setImageUrl(storedImageUrl);
    }
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      setSearchNickname(inputNickname);
    }, 500);
    return () => clearTimeout(timer);
  }, [inputNickname]);

  const handleCameraClick = () => {
    imageInputRef.current?.click();
  };

  const handleImageFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const profileImageFile = e.target.files?.[0];
    if (profileImageFile) {
      const url = URL.createObjectURL(profileImageFile);
      setImageUrl(url);
      localStorage.setItem('on_profileImageUrl', url);
    }
  };

  const handleNicknameInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const inputValue = e.target.value;
    const normalizedNickname = inputValue.trim().replace(/\s+/g, ' ');
    setInputNickname(normalizedNickname);
  };

  const { data, isPending } = useQuery({
    queryKey: ['searchNickname', userId, searchNickname],
    queryFn: async () => {
      const res = await apiClient.get(
        `/users/${userId}/onboarding?step=1&nickname=${searchNickname}`,
      );
      console.log(res.data);
      return res.data;
    },
    enabled: !!searchNickname && searchNickname.length >= 3,
  });

  return (
    <>
      <StepTitle title="Set Your Profile Picture and Nickname" />
      <div className="m-auto relative w-1/2 h-50 rounded-sm bg-accent-foreground/20">
        {imageUrl && (
          <img
            src={imageUrl}
            alt="Uploaded"
            className="w-full h-full rounded-sm object-cover"
          />
        )}
        <Camera
          className="absolute bottom-0 right-0 m-2 p-1 rounded-sm bg-white/30 cursor-pointer hover:scale-120 transition-transform"
          onClick={handleCameraClick}
        />
        {/* hidden input */}
        <input
          hidden
          type="file"
          accept="image/*"
          ref={imageInputRef}
          onChange={handleImageFileChange}
        />
      </div>

      <div className="m-auto relative w-1/2 pt-6">
        <Input value={inputNickname} onChange={(e) => handleNicknameInput(e)} />
      </div>
    </>
  );
};

export default Step1;
