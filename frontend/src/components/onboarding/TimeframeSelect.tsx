import { TIMEFRAME } from '@/constants/onboarding.constants';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select';

const TimeframeSelect = () => {
  return (
    <Select defaultValue={TIMEFRAME[0]}>
      <SelectTrigger className="w-30">
        <SelectValue />
      </SelectTrigger>
      <SelectContent>
        <SelectGroup>
          {TIMEFRAME.map((timeframe) => (
            <SelectItem key={timeframe} value={timeframe}>
              {timeframe}
            </SelectItem>
          ))}
        </SelectGroup>
      </SelectContent>
    </Select>
  );
};

export default TimeframeSelect;
