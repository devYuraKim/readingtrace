import React from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';

const MainPage = () => {
  const handleUserInfo = () => {
    apiClient.get(`/user/${useAuthStore.getState().user?.id}`).then((res) => {
      console.log(res);
    });
  };

  const handleSomethingElse = () => {
    apiClient.get('/user/11').then((res) => {
      console.log(res);
    });
  };

  return (
    <div>
      After login
      <button onClick={handleUserInfo}>USER INFO</button>
      <button onClick={handleSomethingElse}>SOMETHING ELSE</button>
    </div>
  );
};

export default MainPage;
