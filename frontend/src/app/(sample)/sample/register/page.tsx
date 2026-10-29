"use client";

import { useEffect, useState, type FormEvent } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Field, FieldError, FieldGroup, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { register } from "@/lib/api/api-auth";
import { useAuth } from "@/components/hooks/use-auth";

export default function SampleRegisterPage() {
  const router = useRouter();
  const { isLoading, isLoggedIn } = useAuth();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirmation, setPasswordConfirmation] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  useEffect(() => {
    if (!isLoading && isLoggedIn) {
      router.replace("/sample/user/dashboard");
    }
  }, [isLoading, isLoggedIn, router]);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);

    if (password !== passwordConfirmation) {
      setError("Passwords do not match.");
      return;
    }

    setIsSubmitting(true);

    try {
      const response = await register({ name, email, password, passwordConfirmation });
      setSuccessMessage(response.message || "Registration successful. Please log in.");
    } catch (cause: unknown) {
      setError(cause instanceof Error ? cause.message : "Registration failed. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="flex min-h-full flex-1 items-center justify-center px-4 py-12">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle>Create an account</CardTitle>
          <CardDescription>Enter your details to get started.</CardDescription>
        </CardHeader>
        <CardContent>
          {successMessage ? (
            <FieldGroup className="gap-5">
              <p role="status">{successMessage}</p>
              <Button onPress={() => router.replace("/sample/login")}>Go to log in</Button>
            </FieldGroup>
          ) : (
            <form onSubmit={handleSubmit}>
              <FieldGroup className="gap-5">
                <Field>
                  <FieldLabel htmlFor="name">Name</FieldLabel>
                  <Input
                    id="name"
                    name="name"
                    type="text"
                    autoComplete="name"
                    value={name}
                    onChange={(event) => setName(event.target.value)}
                    required
                  />
                </Field>
                <Field>
                  <FieldLabel htmlFor="email">Email</FieldLabel>
                  <Input
                    id="email"
                    name="email"
                    type="email"
                    autoComplete="email"
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    required
                  />
                </Field>
                <Field>
                  <FieldLabel htmlFor="password">Password</FieldLabel>
                  <Input
                    id="password"
                    name="password"
                    type="password"
                    autoComplete="new-password"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    required
                  />
                </Field>
                <Field>
                  <FieldLabel htmlFor="passwordConfirmation">Confirm password</FieldLabel>
                  <Input
                    id="passwordConfirmation"
                    name="passwordConfirmation"
                    type="password"
                    autoComplete="new-password"
                    value={passwordConfirmation}
                    onChange={(event) => setPasswordConfirmation(event.target.value)}
                    required
                  />
                </Field>
                {error && <FieldError>{error}</FieldError>}
                <p className="text-muted-foreground text-center">
                  Already have an account?{" "}
                  <Link
                    href="/sample/login"
                    className="font-medium text-foreground underline underline-offset-4 hover:text-primary"
                  >
                    Log in
                  </Link>
                </p>
                <Button type="submit" isDisabled={isSubmitting || isLoading}>
                  {isSubmitting ? "Creating account..." : "Create account"}
                </Button>
              </FieldGroup>
            </form>
          )}
        </CardContent>
      </Card>
    </main>
  );
}
