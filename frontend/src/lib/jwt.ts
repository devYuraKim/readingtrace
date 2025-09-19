import { jwtDecode } from 'jwt-decode';

type JWTPayload = {
  sub: string; // email
  userId: number;
  roles: string[];
  iat: number;
  exp: number;
  iss: string;
};

export const decodeAccessToken = (token: string) => {
  try {
    const payload = jwtDecode<JWTPayload>(token);
    return {
      userId: payload.userId,
      email: payload.sub,
      roles: payload.roles,
    };
  } catch (err) {
    console.error('Failed to decode JWT', err);
    return null;
  }
};
