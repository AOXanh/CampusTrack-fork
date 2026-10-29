import { UserRole } from "./auth";

export interface UpdateUserProfileRequestDto {
  name?: string;
  email?: string;
}

export interface UpdateUserPasswordRequestDto {
  currentPassword: string;
  password: string;
  passwordConfirmation: string;
}

export interface UserProfileResponseDto {
  createdAt: string;
  id: number;
  name: string;
  email: string;
  userRole: UserRole;
}
