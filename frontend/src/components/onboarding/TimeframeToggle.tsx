import { TIMEFRAME } from '@/constants/onboarding.constants';
import { ToggleGroup, ToggleGroupItem } from '../ui/toggle-group';

const TimeframeToggle = ({ setTimeframe }) => {
  const handleToggleChange = (timeframe: string) => {
    setTimeframe(timeframe);
  };

  return (
    <ToggleGroup type="single" variant="outline" className="m-auto">
      {TIMEFRAME.map((timeframe) => (
        <ToggleGroupItem
          value={timeframe}
          key={timeframe}
          className="first:!rounded-xs last:!rounded-xs cursor-pointer text-xs"
          onClick={() => handleToggleChange(timeframe)}
        >
          {timeframe}
        </ToggleGroupItem>
      ))}
    </ToggleGroup>
  );
};

export default TimeframeToggle;
