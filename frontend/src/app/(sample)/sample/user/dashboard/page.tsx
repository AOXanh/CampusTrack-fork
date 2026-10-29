"use client";

import { useCallback, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/components/hooks/use-auth";
import { getProfile, type UserProfileResponseDto } from "@/lib/api/api-profile";
import { logout } from "@/lib/api/api-auth";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "@/components/ui/card";

export default function UserDashboardPage() {
  const router = useRouter();
  const { isLoading: isAuthLoading, isLoggedIn } = useAuth();
  const [profile, setProfile] = useState<UserProfileResponseDto | null>(null);
  const [isLoadingProfile, setIsLoadingProfile] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const loadProfile = useCallback(async () => {
    setIsLoadingProfile(true);
    setError(null);

    try {
      const data = await getProfile();
      setProfile(data);
    } catch (cause: unknown) {
      setError(
        cause instanceof Error ? cause.message : "Failed to load user profile."
      );
    } finally {
      setIsLoadingProfile(false);
    }
  }, []);

  useEffect(() => {
    if (!isAuthLoading && !isLoggedIn) {
      router.replace("/sample/login");
      return;
    }

    if (isLoggedIn) {
      void loadProfile();
    }
  }, [isAuthLoading, isLoggedIn, loadProfile, router]);

  async function handleLogout() {
    await logout();
    router.replace("/sample/login");
  }

  const isLoading = isAuthLoading || isLoadingProfile;

  return (
    <main className="flex min-h-full flex-1 items-center justify-center px-4 py-12">
      <Card className="w-full max-w-xl">
        <CardHeader>
          <h1 className="text-4xl font-semibold">User Dashboard</h1>
          <p className="text-muted-foreground">
            Your authenticated profile information.
          </p>
        </CardHeader>
        <CardContent>
          {isLoading && (
            <p className="text-muted-foreground">Loading profile information...</p>
          )}

          {!isLoading && error && (
            <div className="space-y-4">
              <p className="text-destructive font-medium">{error}</p>
              <Button onPress={loadProfile}>Retry</Button>
            </div>
          )}

          {!isLoading && !error && !profile && (
            <p className="text-muted-foreground">No profile data available.</p>
          )}

          {!isLoading && !error && profile && (
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
              <div>
                <p className="text-muted-foreground">User ID</p>
                <p className="font-semibold">{profile.id}</p>
              </div>
              <div>
                <p className="text-muted-foreground">Role</p>
                <p className="font-semibold">{profile.userRole}</p>
              </div>
              <div>
                <p className="text-muted-foreground">Name</p>
                <p className="font-semibold">{profile.name}</p>
              </div>
              <div>
                <p className="text-muted-foreground">Email</p>
                <p className="font-semibold">{profile.email}</p>
              </div>
              <div className="sm:col-span-2">
                <p className="text-muted-foreground">Created At</p>
                <p className="font-semibold">
                  {Number.isNaN(new Date(profile.createdAt).getTime())
                    ? profile.createdAt
                    : new Date(profile.createdAt).toLocaleString(undefined, {
                        dateStyle: "medium",
                        timeStyle: "short",
                      })}
                </p>
              </div>
            </div>
          )}
        </CardContent>
        {!isLoading && profile && (
          <CardFooter className="flex justify-end">
            <Button variant="outline" onPress={handleLogout}>
              Log out
            </Button>
          </CardFooter>
        )}
      </Card>
    </main>
  );
}
