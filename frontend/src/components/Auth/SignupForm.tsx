import { useState } from 'react';
import { apiClient } from '@/queries/axios';
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

const signupFormSchema = z
  .object({
    email: z.email('Enter a valid email address'),
    password: z
      .string()
      .regex(/^\S*$/, { message: 'Password cannot contain spaces' })
      .trim()
      .min(6, { message: 'Password must be at least 6 characters' }),
    confirmPassword: z.string().trim(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: 'Passwords do not match',
    path: ['confirmPassword'], // Assign the error to the confirmPassword field
  });

type signupForm = z.infer<typeof signupFormSchema>;

export function SignupForm({
  className,
  ...props
}: React.ComponentProps<'div'>) {
  const [showPassword, setShowPassword] = useState(false);

  const navigate = useNavigate();

  const form = useForm<signupForm>({
    resolver: zodResolver(signupFormSchema),
    defaultValues: {
      email: '',
      password: '',
      confirmPassword: '',
    },
    mode: 'onBlur',
  });

  function onSubmit(values: signupForm) {
    mutateAsync(values)
      .then(() => {
        toast.success('Account created successfully', {
          duration: 1500,
          closeButton: false,
        });
        navigate('/login');
      })
      .catch((error) => {
        const errorMessage =
          error.response?.data?.message ||
          'An unexpected error occurred during signup.';
        toast.error(errorMessage);
      });
  }

  const { mutateAsync } = useMutation({
    mutationFn: async (values: signupForm) => {
      await apiClient.post('/auth/signup', {
        email: values.email,
        password: values.password,
      });
    },
  });

  const togglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };

  return (
    <div className={cn('flex flex-col gap-6', className)} {...props}>
      <Card>
        <CardHeader className="text-center">
          <CardTitle className="text-xl">Create your account</CardTitle>
          <CardDescription>
            Join and connect with our community of readers
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
                          <div className="grid gap-3">
                            <FormLabel>Email</FormLabel>
                            <FormControl>
                              <Input
                                type="email"
                                placeholder="Enter your email"
                                {...field}
                              />
                            </FormControl>
                            <FormMessage />
                          </div>
                        </FormItem>
                      )}
                    />
                  </div>
                  <div className="grid gap-3">
                    <FormField
                      control={form.control}
                      name="password"
                      render={({ field }) => (
                        <FormItem>
                          <div className="grid gap-3">
                            <FormLabel>Password</FormLabel>
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
                                  showPassword
                                    ? 'Hide password'
                                    : 'Show password'
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
                  </div>
                  <div className="grid gap-3">
                    <FormField
                      control={form.control}
                      name="confirmPassword"
                      render={({ field }) => (
                        <FormItem>
                          <div className="grid gap-3">
                            <FormLabel>Confirm Password</FormLabel>
                            <div className="relative">
                              <FormControl>
                                <Input
                                  type={showPassword ? 'text' : 'password'}
                                  placeholder="Must match your  password"
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
                                  showPassword
                                    ? 'Hide password'
                                    : 'Show password'
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
                  </div>
                  <Button type="submit" className="w-full mt-4 cursor-pointer">
                    Sign up
                  </Button>
                </div>
                <div className="text-center text-sm">
                  Already have an account?{' '}
                  <NavLink to="/login" className="underline underline-offset-4">
                    Log in
                  </NavLink>
                </div>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
      <div className="text-muted-foreground *:[a]:hover:text-primary text-center text-xs text-balance *:[a]:underline *:[a]:underline-offset-4">
        By clicking continue, you agree to our <a href="#">Terms of Service</a>{' '}
        and <a href="#">Privacy Policy</a>.
      </div>
    </div>
  );
}
