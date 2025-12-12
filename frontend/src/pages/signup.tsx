import { useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "sonner";

import { ImageUpload } from "@/components/custom/image-upload";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useUser } from "@/context/user-provider";

export function SignUpPage() {
  const { submitting, signup } = useUser();
  const [file, setFile] = useState<File | null>(null);

  const handleSubmit = async (
    e: React.MouseEvent<HTMLButtonElement> | React.KeyboardEvent<HTMLFormElement | HTMLInputElement>
  ) => {
    e.preventDefault();
    if (submitting) return;

    const form = e.currentTarget.closest("form");
    if (!form) return;

    const formData = new FormData(form);
    const name = formData.get("name") as string;
    const email = formData.get("email") as string;
    const password = formData.get("password") as string;

    try {
      await signup(name, email, password, file!);
    } catch (err: any) {
      toast.error(err?.response?.data?.message || "Sign Up failed. Try again.");
    }
  };

  return (
    <div className="flex flex-col gap-6">
      <Card className="overflow-hidden p-0">
        <CardContent className="grid p-0 md:grid-cols-2">
          <div className="bg-muted relative hidden md:block">
            <img
              src="ducati.jpg"
              alt="Image"
              className="absolute inset-0 h-full w-full object-cover dark:brightness-[0.5]"
            />
          </div>

          <form
            className="p-6 md:p-8"
            onKeyDown={(e) => {
              if (e.key === "Enter") handleSubmit(e);
            }}
          >
            <div className="flex flex-col gap-6">
              <div className="flex flex-col items-center text-center">
                <h1 className="text-2xl font-bold">Welcome</h1>
                <p className="text-muted-foreground text-balance">
                  Create a new <span className="underline">Scalable Chat App</span> account
                </p>
              </div>

              <div className="grid gap-3">
                <Label htmlFor="name">Name</Label>
                <Input id="name" name="name" type="text" required />
              </div>

              <div className="grid gap-3">
                <Label htmlFor="email">Email</Label>
                <Input id="email" name="email" type="email" required />
              </div>

              <div className="grid gap-3">
                <Label htmlFor="password">Password</Label>
                <Input id="password" name="password" type="password" required />
              </div>

              <ImageUpload onFileChange={setFile} />

              <Button type="button" className="w-full" onClick={handleSubmit} disabled={submitting}>
                Sign up
              </Button>

              <div className="text-center text-sm">
                Already have an account?{" "}
                <Link to="/login" className="underline underline-offset-4">
                  Log in
                </Link>
              </div>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
