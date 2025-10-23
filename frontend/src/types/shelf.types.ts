export type Shelf = {
  shelfId: number;
  userId: number;
  name: string;
  slug: string;
  description: string;
  bookCount: number;
  isDefault: boolean;
  defaultShelfId: number;
  isHidden: boolean;
  orderIndex: number;
};
