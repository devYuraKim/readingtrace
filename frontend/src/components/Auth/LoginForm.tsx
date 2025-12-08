import { useState } from 'react';
import GoogleLogo from '@/assets/google.png';
import { apiClient } from '@/queries/axios';
import { useAuthStore, User, UserProfile } from '@/store/useAuthStore';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation } from '@tanstack/react-query';
import { Eye, EyeOff } from 'lucide-react';
import { useForm } from 'react-hook-form';
import { NavLink, useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { z } from 'zod';
import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { cn } from '@/lib/utils';

const loginFormSchema = z.object({
  email: z.email('Enter a valid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters long'),
});

type loginForm = z.infer<typeof loginFormSchema>;

export function LoginForm({
  className,
  ...props
}: React.ComponentProps<'div'>) {
  const [showPassword, setShowPassword] = useState(false);

  const togglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };

  const form = useForm<loginForm>({
    resolver: zodResolver(loginFormSchema),
    defaultValues: {
      email: '',
      password: '',
    },
    mode: 'onBlur',
  });

  function onSubmit(values: loginForm) {
    mutate(values);
  }

  const navigate = useNavigate();
  const setAuth = useAuthStore((state) => state.setAuth);

  const { mutate } = useMutation({
    mutationFn: async (values: loginForm) => {
      return await apiClient.post('/auth/login', {
        email: values.email,
        password: values.password,
      });
    },
    onSuccess: (res) => {
      const accessToken = res.headers['authorization'];
      const authenticatedUser = res.data;

      const user: User = {
        userId: authenticatedUser.userId,
        email: authenticatedUser.email,
        roles: authenticatedUser.roles,
      };
      const userProfile: UserProfile = {
        nickname: authenticatedUser.nickname,
        profileImageUrl: authenticatedUser.profileImageUrl,
        readingGoalCount: authenticatedUser.readingGoalCount,
        readingGoalUnit: authenticatedUser.readingGoalUnit,
        readingGoalTimeframe: authenticatedUser.readingGoalTimeframe,
        favoredGenres: authenticatedUser.favoredGenres,
        isOnboardingCompleted: authenticatedUser.isOnboardingCompleted,
      };

      setAuth(user, userProfile, accessToken);
      navigate(`/users/${user.userId}`);
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const handleGoogleLogin = async () => {
    try {
      //TODO: check if URI works in deployment
      window.location.href =
        'http://localhost:8080/oauth2/authorization/google';
    } catch (error) {
      console.log(error);
    }
  };

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
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)}>
              <div className="grid gap-6">
                <div className="grid gap-6">
                  <div className="grid gap-3">
                    <FormField
                      control={form.control}
                      name="email"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Email</FormLabel>
                          <FormControl>
                            <Input
                              type="email"
                              placeholder="Enter your email"
                              {...field}
                            />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                  <div className="grid gap-3"></div>
                  <FormField
                    control={form.control}
                    name="password"
                    render={({ field }) => (
                      <FormItem>
                        <div className="grid gap-3">
                          <div className="flex items-center">
                            <FormLabel>Password</FormLabel>
                            <NavLink
                              to="#"
                              className="ml-auto text-sm underline-offset-4 hover:underline"
                            >
                              Forgot your password?
                            </NavLink>
                          </div>
                          <div className="relative">
                            <FormControl>
                              <Input
                                type={showPassword ? 'text' : 'password'}
                                placeholder="Must be at least 6 characters"
                                {...field}
                              />
                            </FormControl>
                            <Button
                              type="button"
                              variant="ghost"
                              size="sm"
                              className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent cursor-pointer"
                              onClick={togglePasswordVisibility}
                              aria-label={
                                showPassword ? 'Hide password' : 'Show password'
                              }
                            >
                              {showPassword ? (
                                <Eye className="h-4 w-4 text-muted-foreground" />
                              ) : (
                                <EyeOff className="h-4 w-4 text-muted-foreground" />
                              )}
                            </Button>
                          </div>
                          <FormMessage />
                        </div>
                      </FormItem>
                    )}
                  />

                  <Button type="submit" className="w-full mt-3 cursor-pointer">
                    Log in
                  </Button>
                </div>

                <div className="after:border-border relative text-center text-sm after:absolute after:inset-0 after:top-1/2 after:z-0 after:flex after:items-center after:border-t">
                  <span className="bg-card text-muted-foreground relative z-10 px-2">
                    Or continue with
                  </span>
                </div>
              </div>
            </form>
          </Form>

          <div className="flex flex-col gap-4 my-5">
            <Button
              variant="outline"
              className="w-full cursor-pointer"
              onClick={handleGoogleLogin}
            >
              <img width="15" height="15" alt="Google logo" src={GoogleLogo} />
              Login with Google
            </Button>
          </div>
          <div className="text-center text-sm">
            Don&apos;t have an account?{' '}
            <NavLink to="/signup" className="underline underline-offset-4">
              Sign up{' '}
            </NavLink>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
