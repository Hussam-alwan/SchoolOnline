import React from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
} from '@mui/material';
import {
  School,
  Dashboard,
  People,
  Person,
  Book,
  Class,
  Assignment,
} from '@mui/icons-material';
import { useNavigate, useLocation } from 'react-router-dom';

const Navbar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
    { label: 'Dashboard', path: '/dashboard', icon: <Dashboard /> },
    { label: 'Students', path: '/students', icon: <People /> },
    { label: 'Teachers', path: '/teachers', icon: <Person /> },
    { label: 'Courses', path: '/courses', icon: <Book /> },
    { label: 'Classes', path: '/classes', icon: <Class /> },
    { label: 'Registrations', path: '/registrations', icon: <Assignment /> },
  ];

  return (
    <AppBar position="static" elevation={2}>
      <Toolbar>
        <School sx={{ mr: 2 }} />
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Online School Management
        </Typography>
        <Box sx={{ display: 'flex', gap: 1 }}>
          {menuItems.map((item) => (
            <Button
              key={item.path}
              color="inherit"
              startIcon={item.icon}
              onClick={() => navigate(item.path)}
              sx={{
                backgroundColor: location.pathname === item.path ? 'rgba(255, 255, 255, 0.1)' : 'transparent',
                '&:hover': {
                  backgroundColor: 'rgba(255, 255, 255, 0.2)',
                },
              }}
            >
              {item.label}
            </Button>
          ))}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;