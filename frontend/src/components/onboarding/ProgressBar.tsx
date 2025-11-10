type ProgressBarProps = {
  totalSteps: number;
  currentStep: number;
  className: string;
};

const ProgressBar = ({
  totalSteps,
  currentStep,
  className,
}: ProgressBarProps) => {
  const percentage = Math.min(
    Math.max((currentStep / totalSteps) * 100, 0),
    100,
  );

  return (
    <div className={`flex items-center space-x-2 ${className}`}>
      {/* Bar */}
      <div className="flex-1 h-4 bg-gray-100 rounded-sm overflow-hidden">
        <div
          className="h-4 bg-black rounded-sm transition-all duration-300"
          style={{ width: `${percentage}%` }}
        />
      </div>

      {/* Step text */}
      <div className="text-sm text-gray-700 whitespace-nowrap">
        <span className="font-bold">{currentStep}</span> / {totalSteps}
      </div>
    </div>
  );
};

export default ProgressBar;
