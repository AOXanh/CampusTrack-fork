export enum UserRole {
  ADMIN = "ADMIN",
  WORKER = "WORKER",
  STUDENT = "STUDENT",
}

export interface LoginUserRequestDto {
  email: string;
  password: string;
}

export interface LoginUserResponseDto {
  message?: string | null;
  name?: string | null;
  email?: string | null;
  token?: string | null;
}

export interface RegisterUserRequestDto {
  name: string;
  email: string;
  password: string;
  passwordConfirmation: string;
  role?: UserRole;
}

export interface RegisterUserResponseDto {
  message?: string | null;
}

