import { useState } from 'react';
import { Minus, Plus } from 'lucide-react';
import StepTitle from './StepTitle';
import TimeframeDropdown from './TimeframeDropdown';
import TimeframeSelect from './TimeframeSelect';
import UnitDropdown from './UnitDropdown';
import UnitSelect from './UnitSelect';

const Step3 = () => {
  const [count, setCount] = useState(0);
  const [errorMessage, setErrorMessage] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    if (value === '') {
      setCount('');
      return;
    }
    const numberValue = parseInt(value, 10);
    if (!isNaN(numberValue)) setCount(numberValue);
  };

  const handleDecrement = () => {
    if (count > 0) {
      setCount((prev) => prev - 1);
    } else {
      setErrorMessage("Can't go minus");
    }
  };

  const handleIncrement = () => {
    if (count <= 100_000_000) {
      setCount((prev) => prev + 1);
    }
    if (count > 100_000_000) {
      setErrorMessage("Bit too ambitious, don't you think?");
    }
  };

  return (
    <div className="flex flex-col items-center gap-4">
      <StepTitle title="Set Your Reading Goal" />

      <div className="flex items-center gap-5 bg-gray-100 rounded-lg p-10">
        <Minus
          className={`rounded-full p-1 cursor-pointer stroke-accent-foreground/70 hover:stroke-accent-foreground ${
            count === 0 || count === '' ? 'opacity-50 pointer-events-none' : ''
          }`}
          onClick={handleDecrement}
        />

        <input
          type="number"
          value={count}
          onChange={handleChange}
          min={0}
          className="text-center font-extrabold text-2xl bg-transparent outline-none [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
          style={{ width: `${Math.max(String(count || 0).length + 1, 3)}ch` }}
        />

        <Plus
          className="rounded-full p-1 cursor-pointer stroke-accent-foreground/70 hover:stroke-accent-foreground"
          onClick={handleIncrement}
        />
        <UnitDropdown />
        <TimeframeDropdown />
      </div>
      {errorMessage}

      <div className="flex items-center gap-5 bg-gray-100 rounded-lg p-10">
        <Minus
          className={`rounded-full p-1 cursor-pointer stroke-accent-foreground/70 hover:stroke-accent-foreground ${
            count === 0 || count === '' ? 'opacity-50 pointer-events-none' : ''
          }`}
          onClick={handleDecrement}
        />

        <input
          type="number"
          value={count}
          onChange={handleChange}
          min={0}
          className="text-center font-extrabold text-2xl bg-transparent outline-none [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
          style={{ width: `${Math.max(String(count || 0).length + 1, 3)}ch` }}
        />

        <Plus
          className="rounded-full p-1 cursor-pointer stroke-accent-foreground/70 hover:stroke-accent-foreground"
          onClick={handleIncrement}
        />
        <UnitSelect />
        <TimeframeSelect />
      </div>
    </div>
  );
};

export default Step3;
