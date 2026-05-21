import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { Box } from '@mui/material';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';
import { AuthProvider } from './context/AuthContext';
import Dashboard from './pages/Dashboard';
import Students from './pages/Students';
import Teachers from './pages/Teachers';
import Courses from './pages/Courses';
import Classes from './pages/Classes';
import Registrations from './pages/Registrations';
import Login from './pages/Login';
import Register from './pages/Register';
import Forbidden from './pages/Forbidden';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
    background: {
      default: '#f5f5f5',
    },
  },
  typography: {
    h4: {
      fontWeight: 600,
    },
    h6: {
      fontWeight: 500,
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <AuthProvider>
          <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
            <Navbar />
            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
              <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/forbidden" element={<Forbidden />} />

                <Route path="/" element={<Navigate to="/dashboard" replace />} />
                <Route
                  path="/dashboard"
                  element={<ProtectedRoute><Dashboard /></ProtectedRoute>}
                />
                <Route
                  path="/students"
                  element={<ProtectedRoute><Students /></ProtectedRoute>}
                />
                <Route
                  path="/teachers"
                  element={
                    <ProtectedRoute roles={['ADMIN', 'TEACHER']}>
                      <Teachers />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/courses"
                  element={<ProtectedRoute><Courses /></ProtectedRoute>}
                />
                <Route
                  path="/classes"
                  element={<ProtectedRoute><Classes /></ProtectedRoute>}
                />
                <Route
                  path="/registrations"
                  element={<ProtectedRoute><Registrations /></ProtectedRoute>}
                />
              </Routes>
            </Box>
          </Box>
        </AuthProvider>
      </Router>
    </ThemeProvider>
  );
}

export default App;
