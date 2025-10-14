//static
export const BookCollection = [
  {
    bookId: '1',
    title: 'A Clockwork Orange',
    authors: 'Anthony Burgess',
    imageLinks: '/a_clockwork_orange.jpg',
    publisher: '',
    publishedDate: '',
    description: '',
    isbn10: '',
    isbn13: '',
  },
  {
    bookId: '2',
    title: 'Cats Cradle',
    authors: 'Kurt Vonnegut',
    imageLinks: '/cats_cradle.jpg',
    publisher: '',
    publishedDate: '',
    description: '',
    isbn10: '',
    isbn13: '',
  },
  {
    bookId: '3',
    title: 'Metamorphosis',
    authors: 'Franz Kafka',
    imageLinks: '/metamorphosis.jpg',
    publisher: '',
    publishedDate: '',
    description: '',
    isbn10: '',
    isbn13: '',
  },
  {
    bookId: '4',
    title: 'Of Human Bondage',
    authors: 'W. Somerset Maugham',
    imageLinks: '/of_human_bondage.jpg',
    description: `A masterpiece of modern literature that mirrors Maugham's own career. 
      
      Of Human Bondage is the first and most autobiographical of Maugham's novels. It is the story of Philip Carey, an orphan eager for life, love and adventure. After a few months studying in Heidelberg, and a brief spell in Paris as a would-be artist, Philip settles in London to train as a doctor. And that is where he meets Mildred, the loud but irresistible waitress with whom he plunges into a formative, tortured and masochistic affair which very nearly ruins him.`,
    publishedDate: 'February 23, 2010',
    publisher: 'Random House',
    isbn10: '1407016458',
    isbn13: '9781407016450',
  },
  {
    bookId: '5',
    title: 'Slaughterhouse Five',
    authors: 'Kurt Vonnegut',
    imageLinks: '/slaughterhouse_five.jpg',
    publisher: '',
    publishedDate: '',
    description: '',
    isbn10: '',
    isbn13: '',
  },
  {
    bookId: '6',
    title: 'Steeppenwolf',
    authors: 'Hermann Hesse',
    imageLinks: '/steppenwolf.jpg',
    publisher: '',
    publishedDate: '',
    description: '',
    isbn10: '',
    isbn13: '',
  },
  {
    bookId: '7',
    title: 'The Bhagavad Gita',
    authors: 'N/A',
    imageLinks: '/the_bhagavad_gita.jpg',
    publisher: '',
    publishedDate: '',
    description: '',
    isbn10: '',
    isbn13: '',
  },
  {
    bookId: '8',
    title: 'The Castle',
    authors: 'Franz Kafka',
    imageLinks: '/the_castle.jpg',
    publisher: '',
    publishedDate: '',
    description: '',
    isbn10: '',
    isbn13: '',
  },
  {
    bookId: '9',
    title: 'The Trial',
    authors: 'Franz Kafka',
    publisher: 'Penguin Books Ltd (UK)',
    publishedDate: '2024',
    imageLinks: '/the_trial.jpg',
    description: '',
    isbn10: '',
    isbn13: '',
  },
];

export type bookType = {
  bookId: string;
  title: string;
  authors: string;
  imageLinks: string;
  publisher: string;
  publishedDate: string;
  description: string;
  isbn10: string;
  isbn13: string;
};
