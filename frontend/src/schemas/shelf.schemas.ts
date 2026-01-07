import z from 'zod';

export const shelfNameSchema = z.object({
  shelfName: z
    .string()
    .trim()
    .min(1, 'Bookshelf name cannot be empty')
    .transform((val) => val.replace(/\s+/g, ' ')),
});
