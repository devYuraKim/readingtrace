import { useEffect, useRef, useState } from 'react';
import { Minus, Plus } from 'lucide-react';
import { Button } from '../ui/button';
import StepTitle from './StepTitle';
import TimeframeDropdown from './TimeframeDropdown';
import UnitDropdown from './UnitDropdown';

const Step3 = () => {
  const [count, setCount] = useState(0);

  const inputRef = useRef(null);

  useEffect(() => {
    inputRef?.current.focus();

    const goalCount = localStorage.getItem('on_goalCount');
    if (goalCount) setCount(Number(goalCount));
  }, []);

  useEffect(() => {
    localStorage.setItem('on_goalCount', count.toString());
  }, [count]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    if (value === '') {
      setCount('');
      return;
    }
    const numberValue = parseInt(value, 10);
    if (!isNaN(numberValue)) {
      setCount(numberValue);
    }
  };

  const handleDecrement = () => {
    setCount((prev) => prev - 1);
  };

  const handleIncrement = () => {
    setCount((prev) => prev + 1);
  };

  return (
    <div className="flex flex-col items-center">
      <StepTitle title="Set Your Reading Goal" />

      <div className="flex items-center bg-[#f5f5f5] rounded-lg gap-5 p-10 py-7">
        <div className="flex items-center gap-1">
          <Minus
            className={`rounded-full p-1 cursor-pointer stroke-accent-foreground/70 hover:stroke-black hover:stroke-5 ${
              count === 0 || count === ''
                ? 'opacity-50 pointer-events-none'
                : ''
            }`}
            onClick={handleDecrement}
          />

          <input
            ref={inputRef}
            type="number"
            value={count}
            onChange={handleChange}
            min={0}
            className="text-center font-extrabold text-2xl bg-transparent outline-none [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none "
            style={{ width: `${Math.max(String(count || 0).length, 0)}ch` }}
          />

          <Plus
            className="rounded-full p-1 cursor-pointer stroke-accent-foreground/70  hover:stroke-black hover:stroke-5"
            onClick={handleIncrement}
          />
        </div>

        <UnitDropdown isPlural={count >= 2} />
        <TimeframeDropdown />
      </div>

      <div className="mt-10">
        {count == 0 && "Let's set up a goal!"}
        {count < 0 && (
          <span className="text-sm text-red-700">
            Please enter a non-negative number
          </span>
        )}
        {count > 100_000_000 && (
          <span className="text-sm text-red-700">
            Please enter a number under 100,000,000
          </span>
        )}
        {count > 0 && count <= 100_000_000 && <Button>DONE</Button>}
      </div>
    </div>
  );
};

export default Step3;
