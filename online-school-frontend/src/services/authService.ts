import api, { TOKEN_STORAGE_KEY } from './api';

export type UserRole = 'ADMIN' | 'TEACHER' | 'STUDENT';

export interface AuthResponse {
  token: string;
  tokenType: string;
  expiresInMs: number;
  username: string;
  email: string;
  role: UserRole;
}

export interface UserInfo {
  id: number;
  username: string;
  email: string;
  role: UserRole;
}

export interface LoginPayload {
  username: string;
  password: string;
}

export interface RegisterPayload {
  username: string;
  password: string;
  email: string;
  role: UserRole;
}

export const authService = {
  async login(payload: LoginPayload): Promise<AuthResponse> {
    const { data } = await api.post<AuthResponse>('/auth/login', payload);
    localStorage.setItem(TOKEN_STORAGE_KEY, data.token);
    return data;
  },

  async register(payload: RegisterPayload): Promise<AuthResponse> {
    const { data } = await api.post<AuthResponse>('/auth/register', payload);
    localStorage.setItem(TOKEN_STORAGE_KEY, data.token);
    return data;
  },

  async me(): Promise<UserInfo> {
    const { data } = await api.get<UserInfo>('/auth/me');
    return data;
  },

  logout(): void {
    localStorage.removeItem(TOKEN_STORAGE_KEY);
  },

  getToken(): string | null {
    return localStorage.getItem(TOKEN_STORAGE_KEY);
  },
};

export default authService;
