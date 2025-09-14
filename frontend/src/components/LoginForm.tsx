import { NavLink } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { cn } from '@/lib/utils';

export function LoginForm({
  className,
  ...props
}: React.ComponentProps<'div'>) {
  return (
    <div className={cn('flex flex-col gap-6', className)} {...props}>
      <Card>
        <CardHeader className="text-center">
          <CardTitle className="text-xl">Welcome back</CardTitle>
          <CardDescription>
            Log in to continue your reading journey
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form>
            <div className="grid gap-6">
              <div className="grid gap-6">
                <div className="grid gap-3">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="example@mail.com"
                    required
                  />
                </div>
                <div className="grid gap-3">
                  <div className="flex items-center">
                    <Label htmlFor="password">Password</Label>
                    <a
                      href="#"
                      className="ml-auto text-sm underline-offset-4 hover:underline"
                    >
                      Forgot your password?
                    </a>
                  </div>
                  <Input id="password" type="password" required />
                </div>
                <Button type="submit" className="w-full">
                  Log in
                </Button>
              </div>

              <div className="after:border-border relative text-center text-sm after:absolute after:inset-0 after:top-1/2 after:z-0 after:flex after:items-center after:border-t">
                <span className="bg-card text-muted-foreground relative z-10 px-2">
                  Or continue with
                </span>
              </div>

              <div className="flex flex-col gap-4">
                <Button variant="outline" className="w-full">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="non"
                    viewBox="0 0 24 24"
                  >
                    <path
                      fill="#E64234"
                      fill-rule="evenodd"
                      d="M12 6.038c1.454 0 2.76.5 3.784 1.48l2.84-2.839C16.91 3.081 14.67 2.1 12 2.1a9.9 9.9 0 0 0-8.847 5.455l3.308 2.564C7.24 7.779 9.422 6.038 12 6.038"
                      clip-rule="evenodd"
                    ></path>
                    <path
                      fill="#F9BA00"
                      fill-rule="evenodd"
                      d="M6.46 13.881a6 6 0 0 1-.31-1.88c0-.653.112-1.288.31-1.882V7.554H3.153a9.9 9.9 0 0 0 0 8.893z"
                      clip-rule="evenodd"
                    ></path>
                    <path
                      fill="#31A752"
                      fill-rule="evenodd"
                      d="M12 21.9c2.673 0 4.914-.886 6.552-2.398l-3.199-2.485c-.886.594-2.02.945-3.353.945-2.578 0-4.76-1.741-5.54-4.08H3.154v2.565A9.9 9.9 0 0 0 12 21.9"
                      clip-rule="evenodd"
                    ></path>
                    <path
                      fill="#3D82F0"
                      fill-rule="evenodd"
                      d="M21.504 12.226c0-.702-.063-1.378-.18-2.026H12v3.83h5.328a4.56 4.56 0 0 1-1.975 2.988v2.484h3.199c1.872-1.724 2.952-4.261 2.952-7.276"
                      clip-rule="evenodd"
                    ></path>
                  </svg>
                  Login with Google
                </Button>
              </div>
              <div className="text-center text-sm">
                Don&apos;t have an account?{' '}
                <NavLink to="/signup" className="underline underline-offset-4">
                  Sign up{' '}
                </NavLink>
              </div>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
