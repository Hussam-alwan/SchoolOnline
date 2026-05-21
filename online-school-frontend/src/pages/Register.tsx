import React, { useState } from 'react';
import {
  Box, Paper, Typography, TextField, Button, Alert, Link, Stack,
  MenuItem,
} from '@mui/material';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { UserRole } from '../services/authService';

const ROLES: UserRole[] = ['STUDENT', 'TEACHER', 'ADMIN'];

const Register: React.FC = () => {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState<UserRole>('STUDENT');
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await register({ username, password, email, role });
      navigate('/dashboard', { replace: true });
    } catch (err: any) {
      const msg = err.response?.data?.message ?? 'Registration failed';
      setError(msg);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', mt: 6 }}>
      <Paper sx={{ p: 4, width: '100%', maxWidth: 480 }} elevation={3}>
        <Typography variant="h5" gutterBottom>Create account</Typography>
        <Box component="form" onSubmit={handleSubmit} noValidate>
          <Stack spacing={2}>
            {error && <Alert severity="error">{error}</Alert>}
            <TextField
              label="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              autoFocus
              autoComplete="username"
              fullWidth
            />
            <TextField
              label="Email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              autoComplete="email"
              fullWidth
            />
            <TextField
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              autoComplete="new-password"
              helperText="At least 6 characters"
              fullWidth
            />
            <TextField
              label="Role"
              value={role}
              onChange={(e) => setRole(e.target.value as UserRole)}
              select
              required
              fullWidth
            >
              {ROLES.map((r) => (
                <MenuItem key={r} value={r}>{r}</MenuItem>
              ))}
            </TextField>
            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={submitting || !username || !email || !password}
            >
              {submitting ? 'Creating…' : 'Create account'}
            </Button>
            <Typography variant="body2" align="center">
              Already have an account?{' '}
              <Link component={RouterLink} to="/login">Sign in</Link>
            </Typography>
          </Stack>
        </Box>
      </Paper>
    </Box>
  );
};

export default Register;
