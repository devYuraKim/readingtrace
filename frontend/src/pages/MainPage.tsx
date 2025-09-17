import React from 'react';
import { useAuthStore } from '@/store/useAuthStore';

const MainPage = () => {
  return <div>hello, {useAuthStore.getState().user?.email}</div>;
};

export default MainPage;
