type IUser = {
  id: string;
  name: string;
  email: string;
  avatarId: string;
  avatarUrl: string;
  roles: string[];
  createdAt: Date;
  updatedAt: Date;
} | null;
