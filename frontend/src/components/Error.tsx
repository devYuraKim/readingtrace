import React from 'react';

function Error({ message }: { message: string }) {
  return <p className="gap-2 text-sm text-destructive">{message}</p>;
}

export default Error;
