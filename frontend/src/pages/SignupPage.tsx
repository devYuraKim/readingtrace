import ReadingTraceIcon from '@/assets/readingtrace.svg';
import { SignupForm } from '@/components/SignupForm';

export default function SignupPage() {
  return (
    <div className="bg-muted flex min-h-svh flex-col items-center justify-center gap-6 p-6 md:p-10">
      <div className="flex w-full max-w-lg flex-col gap-6">
        <div className="flex items-center gap-1.5 self-center font-medium text-xl">
          <div className="bg-primary text-primary-foreground flex size-6 items-center justify-center rounded-sm">
            <img src={ReadingTraceIcon} alt="icon" className="size-4" />
          </div>
          ReadingTrace
        </div>
        <SignupForm />
      </div>
    </div>
  );
}
