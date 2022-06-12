export class NotificationDto {
  id: number;
  userId: number;
  isViewed: boolean;
  author: string;
  action: string;
  authorLink: string;
  actionLink: string;
  isMessage: boolean;
}
