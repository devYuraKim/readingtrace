import { ToggleGroup, ToggleGroupItem } from '../ui/toggle-group';

const PERIOD = ['DAY', 'WEEK', 'MONTH', 'YEAR'];

const PeriodToggle = () => {
  return (
    <ToggleGroup type="single" variant="outline" className="m-auto">
      {PERIOD.map((e) => (
        <ToggleGroupItem
          value={e}
          key={e}
          className="first:!rounded-xs last:!rounded-xs cursor-pointer text-xs"
        >
          {e}
        </ToggleGroupItem>
      ))}
    </ToggleGroup>
  );
};

export default PeriodToggle;
