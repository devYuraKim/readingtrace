import GoogleLogo from '@/assets/google.png';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation } from '@tanstack/react-query';
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

const formSchema = z.object({
  email: z.email('Enter a valid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters long'),
});

export function LoginForm({
  className,
  ...props
}: React.ComponentProps<'div'>) {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: '',
      password: '',
    },
  });

  function onSubmit(values: z.infer<typeof formSchema>) {
    mutateAsync(values)
      .then(() => {
        toast.success('Logged in successfully');
      })
      .catch((error) => {
        toast.error(error.response.data.message);
      });
  }

  const navigate = useNavigate();
  const setAuth = useAuthStore((state) => state.setAuth);

  const { mutateAsync } = useMutation({
    mutationFn: async (values: z.infer<typeof formSchema>) => {
      return await apiClient.post('/auth/login', {
        email: values.email,
        password: values.password,
      });
    },
    onSuccess: (res) => {
      const accessToken = res.headers['authorization'];
      const user = res.data;
      setAuth(user, accessToken);
      navigate(`/${user.userId}`);
      console.log(useAuthStore.getState());
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
                        <div className="flex items-center">
                          <FormLabel>Password</FormLabel>
                          <NavLink
                            to="#"
                            className="ml-auto text-sm underline-offset-4 hover:underline"
                          >
                            Forgot your password?
                          </NavLink>
                        </div>
                        <FormControl>
                          <Input
                            type="password"
                            placeholder="Password must be at least 6 characters"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
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
