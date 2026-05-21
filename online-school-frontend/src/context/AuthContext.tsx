import React, { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { authService, LoginPayload, RegisterPayload, UserInfo, UserRole } from '../services/authService';

interface AuthContextValue {
  user: UserInfo | null;
  loading: boolean;
  isAuthenticated: boolean;
  hasRole: (...roles: UserRole[]) => boolean;
  login: (payload: LoginPayload) => Promise<void>;
  register: (payload: RegisterPayload) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserInfo | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  const loadUser = useCallback(async () => {
    if (!authService.getToken()) {
      setUser(null);
      setLoading(false);
      return;
    }
    try {
      const info = await authService.me();
      setUser(info);
    } catch {
      authService.logout();
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadUser();
  }, [loadUser]);

  const login = useCallback(async (payload: LoginPayload) => {
    await authService.login(payload);
    const info = await authService.me();
    setUser(info);
  }, []);

  const register = useCallback(async (payload: RegisterPayload) => {
    await authService.register(payload);
    const info = await authService.me();
    setUser(info);
  }, []);

  const logout = useCallback(() => {
    authService.logout();
    setUser(null);
  }, []);

  const hasRole = useCallback(
    (...roles: UserRole[]) => (user ? roles.includes(user.role) : false),
    [user]
  );

  const value = useMemo<AuthContextValue>(() => ({
    user,
    loading,
    isAuthenticated: !!user,
    hasRole,
    login,
    register,
    logout,
  }), [user, loading, hasRole, login, register, logout]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = (): AuthContextValue => {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return ctx;
};
