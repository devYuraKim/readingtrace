import { useEffect, useState } from 'react';
import { OnboardingStepProps } from '@/types/props.types';
import StepTitle from './StepTitle';

const genresList = [
  'Action',
  'Adventure',
  'Romance',
  'Comedy',
  'Drama',
  'Fantasy',
  'Sci-Fi',
  'Mystery',
  'Thriller',
  'Horror',
  'Historical Fiction',
  'Young Adult',
  "Children's",
  'Dystopian',
  'Comics',
  'Biography & Memoir',
  'Self-Help',
  'Science & Technology',
  'Business & Economics',
  'Travel',
  'History',
  'Philosophy',
  'Psychology',
  'Sociology',
];

const Step2 = ({ setCanProceed }: OnboardingStepProps) => {
  const [selectedGenres, setSelectedGenres] = useState([]);
  const minSelection = 3;
  const maxSelection = 5;

  useEffect(() => {
    const content = localStorage.getItem('ob_favoredGenres');
    if (content) setSelectedGenres(content.split(','));
  }, []);

  useEffect(() => {
    localStorage.setItem('ob_favoredGenres', selectedGenres.toString());
  }, [selectedGenres]);

  const handleClick = (genre) => {
    if (selectedGenres.includes(genre)) {
      setSelectedGenres((prev) => prev.filter((g) => g !== genre));
    } else if (selectedGenres.length < maxSelection) {
      setSelectedGenres((prev) => [...prev, genre]);
    }
  };

  if (selectedGenres.length >= minSelection) {
    setCanProceed(true);
    localStorage.setItem('on_step', '2');
  } else {
    setCanProceed(false);
    localStorage.setItem('on_step', '1');
  }

  return (
    <div>
      <StepTitle title="Select Your Favorite Genres (3-5)" />
      <div className="flex flex-wrap gap-2 gap-y-3">
        {genresList.map((genre) => {
          const isSelected = selectedGenres.includes(genre);
          const isDisabled =
            !isSelected && selectedGenres.length >= maxSelection;

          return (
            <div
              key={genre}
              onClick={() => !isDisabled && handleClick(genre)}
              style={{
                fontSize: '0.8rem',
                padding: '7px 9px',
                borderRadius: '8px',
                border: isSelected ? '2px solid black' : '2px solid #f5f5f5',
                backgroundColor: isSelected ? 'black' : '#f5f5f5',
                color: isSelected ? 'white' : 'black',
                cursor: isDisabled ? 'not-allowed' : 'pointer',
                opacity: isDisabled ? 0.5 : 1,
                userSelect: 'none',
              }}
            >
              {genre}
            </div>
          );
        })}

        <div className="mt-5 m-auto">
          {selectedGenres.length < minSelection && (
            <span className="text-sm text-red-700">
              Please select at least {minSelection} genres.
            </span>
          )}
        </div>
      </div>
    </div>
  );
};

export default Step2;
