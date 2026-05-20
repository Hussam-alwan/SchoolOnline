import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';

const NAV_ITEMS = [
  { label: 'Home', path: '/' },
  { label: 'Students', path: '/students' },
  { label: 'Courses', path: '/courses' },
  { label: 'Teachers', path: '/teachers' },
];

export default function Header() {
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path) =>
    path === '/' ? location.pathname === '/' : location.pathname.startsWith(path);

  return (
    <AppBar position="static" sx={{ mb: 4 }}>
      <Toolbar>
        <Typography
          variant="h6"
          component="div"
          onClick={() => navigate('/')}
          sx={{ flexGrow: 1, fontWeight: 'bold', cursor: 'pointer' }}
        >
          Online School
        </Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          {NAV_ITEMS.map(({ label, path }) => (
            <Button
              key={path}
              color="inherit"
              onClick={() => navigate(path)}
              sx={{
                backgroundColor: isActive(path) ? 'rgba(255,255,255,0.15)' : 'transparent',
                '&:hover': { backgroundColor: 'rgba(255,255,255,0.25)' },
              }}
            >
              {label}
            </Button>
          ))}
        </Box>
      </Toolbar>
    </AppBar>
  );
}
