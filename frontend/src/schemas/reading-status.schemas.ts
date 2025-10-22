import z from 'zod';

export const readingStatusFormSchema = z.object({
  userReadingStatusId: z.number().nullish(),
  status: z.enum(
    ['want-to-read', 'already-read', 'currently-reading', 'never-finished'],
    {
      message: 'Please select a reading status.',
    },
  ),
  visibility: z.enum(['public', 'private', 'friends'], {
    message: 'Please select a visibility option.',
  }),
  rating: z
    .number({ message: 'Please provide a rating.' })
    .min(0)
    .max(5)
    .nullish(),
  shelfId: z.number().nullish(),
  startDate: z.date().nullish(),
  endDate: z.date().nullish(),
});

export type ReadingStatusFormValues = z.infer<typeof readingStatusFormSchema>;
