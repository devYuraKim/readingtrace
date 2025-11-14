import { useEffect, useRef, useState } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useQuery } from '@tanstack/react-query';
import { Camera, CircleCheck, CircleX } from 'lucide-react';
import { z } from 'zod';
import {
  InputGroup,
  InputGroupAddon,
  InputGroupInput,
} from '../ui/input-group';
import { Spinner } from '../ui/spinner';
import StepTitle from './StepTitle';

const nicknameSchema = z
  .string()
  .min(3, 'Nickname must be at least 3 characters')
  .max(20, "Nickname can't be longer than 20 characters")
  .regex(
    /^[a-z0-9]+$/,
    'Nickname can only contain lowercase letters and numbers',
  );

type StepProps = {
  setCanProceed: React.Dispatch<React.SetStateAction<boolean>>;
};

const Step1 = ({ setCanProceed }: StepProps) => {
  const userId = useAuthStore((state) => state.user?.userId);
  const email = useAuthStore((state) => state.user?.email);
  const defaultNickname = email?.split('@')[0] || '';

  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [inputNickname, setInputNickname] = useState<string>(defaultNickname);
  const [searchNickname, setSearchNickname] = useState<string | null>(null);
  const [inputError, setInputError] = useState<string | null>(null);

  const imageInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const storedImageUrl = localStorage.getItem('on_profileImageUrl');
    const storedNickname = localStorage.getItem('on_nickname');
    if (storedImageUrl) setImageUrl(storedImageUrl);
    if (storedNickname) setInputNickname(storedNickname);
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      setSearchNickname(inputNickname);
      if (inputNickname && !inputError)
        localStorage.setItem('on_nickname', inputNickname);
    }, 700);
    return () => clearTimeout(timer);
  }, [inputNickname, inputError]);

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
    setInputNickname(e.target.value);
    const result = nicknameSchema.safeParse(e.target.value);
    if (!result.success) {
      setInputError(result.error.issues[0].message);
    } else {
      setInputError(null);
    }
  };

  const { data: isAvailable, isPending } = useQuery({
    queryKey: ['searchNickname', userId, searchNickname],
    queryFn: async () => {
      const res = await apiClient.get(
        `/users/${userId}/onboarding?step=1&nickname=${searchNickname}`,
      );
      return res.data;
    },
    enabled: !!searchNickname && searchNickname.length >= 3,
    retry: 2,
    staleTime: 5 * 60 * 1000,
    refetchOnMount: false,
  });

  if (!isPending && isAvailable && !inputError) {
    setCanProceed(true);
  } else {
    setCanProceed(false);
  }

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
        <InputGroup>
          <InputGroupInput
            value={inputNickname}
            onChange={(e) => handleNicknameInput(e)}
          />
          <InputGroupAddon align="inline-end">
            {!isPending && isAvailable && !inputError && (
              <CircleCheck className="stroke-green-700" />
            )}
            {(!isPending && !isAvailable) ||
              (inputError && <CircleX className="stroke-red-700" />)}
            {isPending && inputNickname.length >= 3 && <Spinner />}
          </InputGroupAddon>
        </InputGroup>
        <span className="text-sm text-red-700">{inputError}</span>
      </div>
    </>
  );
};

export default Step1;
