import {
  StatusSlug,
  VisibilitySlug,
} from '@/constants/reading-status.constants';
import z from 'zod';

export const readingStatusFormSchema = z.object({
  userReadingStatusId: z.number().nullish(),
  status: z.enum(
    [
      StatusSlug.ALREADY_READ,
      StatusSlug.WANT_TO_READ,
      StatusSlug.CURRENTLY_READING,
      StatusSlug.PAUSED_READING,
      StatusSlug.NEVER_FINISHED,
    ],
    {
      message: 'Please select a reading status.',
    },
  ),
  visibility: z.enum(
    [VisibilitySlug.PUBLIC, VisibilitySlug.FRIENDS, VisibilitySlug.PRIVATE],
    {
      message: 'Please select a visibility option.',
    },
  ),
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
