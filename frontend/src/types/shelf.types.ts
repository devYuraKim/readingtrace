export type CustomShelf = {
  shelfId: number;
  userId: number;
  name: string;
  slug: string;
  description: string;
  bookCount: number;
  isHidden: boolean;
  orderIndex: number;
};

export type DefaultShelf = {
  userId: number;
  name: string;
  slug: string;
  bookCount: number;
  orderIndex: number;
};
