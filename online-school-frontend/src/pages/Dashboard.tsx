import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Card,
  CardContent,
  Box,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  People,
  Person,
  Book,
  Class,
  Assignment,
  TrendingUp,
} from '@mui/icons-material';
import { studentService, teacherService, courseService, classService, registrationService } from '../services/api';

interface DashboardStats {
  totalStudents: number | null;
  totalTeachers: number | null;
  totalCourses: number | null;
  totalClasses: number | null;
  totalRegistrations: number | null;
  activeRegistrations: number | null;
}

const countOr = <T,>(result: PromiseSettledResult<{ data: T[] }>): number | null =>
  result.status === 'fulfilled' ? result.value.data.length : null;

const Dashboard: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats>({
    totalStudents: 0,
    totalTeachers: 0,
    totalCourses: 0,
    totalClasses: 0,
    totalRegistrations: 0,
    activeRegistrations: 0,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        const [students, teachers, courses, classes, registrations] = await Promise.allSettled([
          studentService.getAll(),
          teacherService.getAll(),
          courseService.getAll(),
          classService.getAll(),
          registrationService.getAll(),
        ]);

        const activeRegistrations = registrations.status === 'fulfilled'
          ? registrations.value.data.filter((reg) => reg.status === 'ACTIVE').length
          : null;

        setStats({
          totalStudents: countOr(students),
          totalTeachers: countOr(teachers),
          totalCourses: countOr(courses),
          totalClasses: countOr(classes),
          totalRegistrations: countOr(registrations),
          activeRegistrations,
        });
      } catch (err) {
        setError('Failed to fetch dashboard statistics');
        console.error('Dashboard error:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  const statCards = [
    {
      title: 'Total Students',
      value: stats.totalStudents,
      icon: <People fontSize="large" />,
      color: '#1976d2',
    },
    {
      title: 'Total Teachers',
      value: stats.totalTeachers,
      icon: <Person fontSize="large" />,
      color: '#388e3c',
    },
    {
      title: 'Total Courses',
      value: stats.totalCourses,
      icon: <Book fontSize="large" />,
      color: '#f57c00',
    },
    {
      title: 'Total Classes',
      value: stats.totalClasses,
      icon: <Class fontSize="large" />,
      color: '#7b1fa2',
    },
    {
      title: 'Total Registrations',
      value: stats.totalRegistrations,
      icon: <Assignment fontSize="large" />,
      color: '#d32f2f',
    },
    {
      title: 'Active Registrations',
      value: stats.activeRegistrations,
      icon: <TrendingUp fontSize="large" />,
      color: '#0288d1',
    },
  ];

  if (loading) {
    return (
      <Container>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (error) {
    return (
      <Container>
        <Alert severity="error" sx={{ mt: 2 }}>
          {error}
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" component="h1" gutterBottom>
        Dashboard
      </Typography>
      <Typography variant="subtitle1" color="text.secondary" gutterBottom>
        Welcome to the Online School Management System
      </Typography>

      <Box 
        sx={{ 
          display: 'grid', 
          gridTemplateColumns: { 
            xs: '1fr', 
            sm: 'repeat(2, 1fr)', 
            md: 'repeat(3, 1fr)' 
          }, 
          gap: 3, 
          mt: 2 
        }}
      >
        {statCards.map((card, index) => (
          <Card
            key={index}
            sx={{
              height: '100%',
              display: 'flex',
              flexDirection: 'column',
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'translateY(-4px)',
                boxShadow: 4,
              },
            }}
          >
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="text.secondary" gutterBottom>
                    {card.title}
                  </Typography>
                  <Typography variant="h4" component="div" sx={{ color: card.color }}>
                    {card.value === null ? '—' : card.value}
                  </Typography>
                </Box>
                <Box sx={{ color: card.color }}>
                  {card.icon}
                </Box>
              </Box>
            </CardContent>
          </Card>
        ))}
      </Box>

      <Box sx={{ mt: 4 }}>
        <Typography variant="h6" gutterBottom>
          Quick Actions
        </Typography>
        <Box 
          sx={{ 
            display: 'grid', 
            gridTemplateColumns: { xs: '1fr', md: 'repeat(2, 1fr)' }, 
            gap: 2 
          }}
        >
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Recent Activity
              </Typography>
              <Typography color="text.secondary">
                • {stats.activeRegistrations ?? '—'} students are currently enrolled in courses
              </Typography>
              <Typography color="text.secondary">
                • {stats.totalClasses ?? '—'} classes are scheduled for this semester
              </Typography>
              <Typography color="text.secondary">
                • {stats.totalTeachers ?? '—'} teachers are managing courses
              </Typography>
            </CardContent>
          </Card>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                System Status
              </Typography>
              <Typography color="text.secondary">
                • All systems operational
              </Typography>
              <Typography color="text.secondary">
                • Database connection: Active
              </Typography>
              <Typography color="text.secondary">
                • Last updated: {new Date().toLocaleString()}
              </Typography>
            </CardContent>
          </Card>
        </Box>
      </Box>
    </Container>
  );
};

export default Dashboard;