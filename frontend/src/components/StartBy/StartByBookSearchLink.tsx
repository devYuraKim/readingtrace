import { NavLink } from 'react-router-dom';

function StartByBookSearchLink() {
  return (
    <NavLink to="searchBook">
      <div className="mt-4 flex items-center gap-2 font-medium hover:underline cursor-pointer">
        <span>Or search for any book you like</span>
        <svg
          className="w-4 h-4"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M9 5l7 7-7 7"
          />
        </svg>
      </div>
    </NavLink>
  );
}

export default StartByBookSearchLink;
