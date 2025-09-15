import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { RouterProvider } from 'react-router-dom';
import { Toaster } from '@/components/ui/sonner';
import router from './router/router';
import './App.css';
import { apiClient } from './queries/axios';

const queryClient = new QueryClient();
queryClient.prefetchQuery({
  queryKey: ['csrf-token'],
  queryFn: () => {
    console.log(document.cookie);
    return apiClient.get('/auth/csrf');
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Toaster
        richColors
        closeButton
        duration={5000}
        position="top-center"
        toastOptions={{
          closeButtonAriaLabel: 'Dismiss toast',
          classNames: {
            icon: '!order-1 ',
            content: '!order-2 !flex-1',
            closeButton: '!order-3 !static !translate-y-2/7',
            title: '!font-normal',
            description: '!font-thin',
          },
        }}
      />
      <RouterProvider router={router} />
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
}

export default App;
