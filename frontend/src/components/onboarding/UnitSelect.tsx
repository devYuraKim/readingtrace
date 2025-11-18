import { UNIT } from '@/constants/onboarding.constants';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select';

const UnitSelect = () => {
  return (
    <Select defaultValue={UNIT[0]}>
      <SelectTrigger className="w-30">
        <SelectValue />
      </SelectTrigger>
      <SelectContent align="center">
        <SelectGroup>
          {UNIT.map((unit) => (
            <SelectItem key={unit} value={unit}>
              {unit}
            </SelectItem>
          ))}
        </SelectGroup>
      </SelectContent>
    </Select>
  );
};

export default UnitSelect;
