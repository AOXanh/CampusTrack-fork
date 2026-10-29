"use client";

import { useCallback, useEffect, useState } from "react";
import { getProfile } from "@/lib/api/api-profile";
import type { UserProfileResponseDto } from "@/types/profile";

export interface UseAuthReturn {
  user: UserProfileResponseDto | null;
  isLoading: boolean;
  isLoggedIn: boolean;
  isAuthenticated: boolean;
  error: Error | null;
  checkAuth: () => Promise<UserProfileResponseDto | null>;
}

export function useAuth(): UseAuthReturn {
  const [user, setUser] = useState<UserProfileResponseDto | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  const checkAuth = useCallback(async (): Promise<UserProfileResponseDto | null> => {
    setIsLoading(true);
    setError(null);

    const token =
      typeof window !== "undefined"
        ? localStorage.getItem("token") ?? sessionStorage.getItem("token")
        : null;

    if (!token) {
      setUser(null);
      setIsLoading(false);
      return null;
    }

    try {
      const profile = await getProfile();
      setUser(profile);
      return profile;
    } catch (err: unknown) {
      setUser(null);
      const errorInstance =
        err instanceof Error ? err : new Error("Failed to validate authentication");
      setError(errorInstance);
      return null;
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    let ignore = false;

    const syncAuth = async () => {
      const token =
        typeof window !== "undefined"
          ? localStorage.getItem("token") ?? sessionStorage.getItem("token")
          : null;

      if (!token) {
        setIsLoading(false);
        return;
      }

      try {
        const profile = await getProfile();
        if (!ignore) {
          setUser(profile);
        }
      } catch (err: unknown) {
        if (!ignore) {
          setUser(null);
          setError(
            err instanceof Error ? err : new Error("Failed to validate authentication")
          );
        }
      } finally {
        if (!ignore) {
          setIsLoading(false);
        }
      }
    };

    void syncAuth();

    return () => {
      ignore = true;
    };
  }, []);

  const isLoggedIn = user !== null;

  return {
    user,
    isLoading,
    isLoggedIn,
    isAuthenticated: isLoggedIn,
    error,
    checkAuth,
  };
}

export const useAuthStatus = useAuth;
export default useAuth;
