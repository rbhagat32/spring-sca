type IMessage = {
  id: string;
  content: string;
  sender: IUser;
  createdAt: Date;
} | null;
