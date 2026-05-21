import React, { useState } from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  IconButton,
  Menu,
  MenuItem,
  Avatar,
  Chip,
} from '@mui/material';
import {
  School,
  Dashboard,
  People,
  Person,
  Book,
  Class,
  Assignment,
  Logout,
} from '@mui/icons-material';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { UserRole } from '../services/authService';

interface MenuItemDef {
  label: string;
  path: string;
  icon: React.ReactNode;
  roles?: UserRole[];
}

const ALL_ITEMS: MenuItemDef[] = [
  { label: 'Dashboard', path: '/dashboard', icon: <Dashboard /> },
  { label: 'Students', path: '/students', icon: <People /> },
  { label: 'Teachers', path: '/teachers', icon: <Person />, roles: ['ADMIN', 'TEACHER'] },
  { label: 'Courses', path: '/courses', icon: <Book /> },
  { label: 'Classes', path: '/classes', icon: <Class /> },
  { label: 'Registrations', path: '/registrations', icon: <Assignment /> },
];

const Navbar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, isAuthenticated, logout, hasRole } = useAuth();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const visibleItems = ALL_ITEMS.filter((item) =>
    !item.roles || hasRole(...item.roles)
  );

  const handleLogout = () => {
    setAnchorEl(null);
    logout();
    navigate('/login');
  };

  return (
    <AppBar position="static" elevation={2}>
      <Toolbar>
        <School sx={{ mr: 2 }} />
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Online School Management
        </Typography>
        <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
          {isAuthenticated && visibleItems.map((item) => (
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

          {isAuthenticated && user ? (
            <>
              <Chip
                label={user.role}
                size="small"
                color="secondary"
                sx={{ ml: 1, color: 'white', fontWeight: 600 }}
              />
              <IconButton
                color="inherit"
                onClick={(e) => setAnchorEl(e.currentTarget)}
                sx={{ ml: 1 }}
                size="small"
              >
                <Avatar sx={{ width: 32, height: 32 }}>
                  {user.username.charAt(0).toUpperCase()}
                </Avatar>
              </IconButton>
              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={() => setAnchorEl(null)}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
              >
                <MenuItem disabled>
                  <Box>
                    <Typography variant="body2" fontWeight={600}>{user.username}</Typography>
                    <Typography variant="caption" color="text.secondary">{user.email}</Typography>
                  </Box>
                </MenuItem>
                <MenuItem onClick={handleLogout}>
                  <Logout fontSize="small" sx={{ mr: 1 }} />
                  Logout
                </MenuItem>
              </Menu>
            </>
          ) : (
            <>
              <Button color="inherit" onClick={() => navigate('/login')}>Login</Button>
              <Button color="inherit" onClick={() => navigate('/register')}>Register</Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
