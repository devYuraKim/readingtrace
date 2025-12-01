import { ReactElement, useState } from 'react';
import ReadingTraceIcon from '@/assets/readingtrace.svg';
import { useAuthStore } from '@/store/useAuthStore';
import { CircleChevronLeft, CircleChevronRight } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import ProgressBar from '@/components/onboarding/ProgressBar';
import Step1 from '@/components/onboarding/Step1';
import Step2 from '@/components/onboarding/Step2';
import Step3 from '@/components/onboarding/Step3';
import { Card } from '@/components/ui/card';

const OnboardingPage = () => {
  const navigate = useNavigate();

  const isOnboardingCompleted = useAuthStore(
    (state) => state.userProfile?.isOnboardingCompleted,
  );
  const userId = useAuthStore((state) => state.user?.userId);

  if (isOnboardingCompleted) navigate(`/users/${userId}`);

  const [currentStep, setCurrentStep] = useState<number>(1);
  const [canProceed, setCanProceed] = useState(false);

  const totalSteps = 3;

  const steps: Record<number, ReactElement> = {
    1: <Step1 setCanProceed={setCanProceed} />,
    2: <Step2 setCanProceed={setCanProceed} />,
    3: <Step3 />,
  };

  return (
    <div className="bg-muted flex flex-col min-h-screen items-center gap-6 p-6 md:p-10">
      <div className="flex w-full max-w-lg flex-col gap-6">
        <div className="flex items-center gap-1.5 self-center font-medium text-xl">
          <div className="bg-primary text-primary-foreground flex size-6 items-center justify-center rounded-sm">
            <img src={ReadingTraceIcon} alt="icon" className="size-4" />
          </div>
          ReadingTrace
        </div>

        <Card className="flex flex-col items-center justify-center">
          {/* Navigation + Progress */}
          <div className="flex items-center w-full max-w-md space-x-4">
            {/* Previous Step */}
            <CircleChevronLeft
              className={`${
                currentStep === 1
                  ? 'stroke-gray-300'
                  : 'cursor-pointer stroke-accent-foreground/70 hover:stroke-accent-foreground'
              }`}
              onClick={() =>
                currentStep > 1 && setCurrentStep((prev) => prev - 1)
              }
            />

            {/* Progress Bar */}
            <ProgressBar
              totalSteps={totalSteps}
              currentStep={currentStep}
              className="flex-1"
            />

            {/* Next Step */}
            <CircleChevronRight
              className={`${
                currentStep === totalSteps || !canProceed
                  ? 'stroke-gray-300'
                  : 'cursor-pointer stroke-accent-foreground/70 hover:stroke-accent-foreground'
              }`}
              onClick={() =>
                currentStep < totalSteps &&
                canProceed &&
                setCurrentStep((prev) => prev + 1)
              }
            />
          </div>

          {/* Step content */}
          <div className="w-full max-w-md px-6 pb-6 pt-3">
            {steps[currentStep]}
          </div>
        </Card>
      </div>
    </div>
  );
};

export default OnboardingPage;
