import React from 'react';
import { Box, Typography, Button, Container, Grid, Card, CardContent } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

export default function HomePage() {
  const features = [
    {
      title: 'Student Management',
      description: 'Manage student information, track GPA, and monitor progress'
    },
    {
      title: 'Course Management',
      description: 'Create and manage courses with credit hours and descriptions'
    },
    {
      title: 'Class Management',
      description: 'Organize classes and manage student enrollments'
    }
  ];

  return (
    <Box>
      <Box
        sx={{
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          color: 'white',
          py: 8,
          mb: 6,
          borderRadius: 2,
          textAlign: 'center'
        }}
      >
        <Typography variant="h3" component="h1" sx={{ mb: 2, fontWeight: 'bold' }}>
          Welcome to Online School
        </Typography>
        <Typography variant="h6" sx={{ mb: 4 }}>
          Manage your educational institution with ease
        </Typography>
        <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
          <Button
            variant="contained"
            color="inherit"
            component={RouterLink}
            to="/students"
            sx={{ backgroundColor: 'white', color: '#667eea' }}
          >
            View Students
          </Button>
          <Button
            variant="outlined"
            color="inherit"
            component={RouterLink}
            to="/courses"
          >
            View Courses
          </Button>
        </Box>
      </Box>

      <Container maxWidth="lg">
        <Typography variant="h4" component="h2" sx={{ mb: 4, textAlign: 'center' }}>
          Key Features
        </Typography>

        <Grid container spacing={3} sx={{ mb: 6 }}>
          {features.map((feature, index) => (
            <Grid item xs={12} sm={6} md={4} key={index}>
              <Card sx={{ height: '100%', '&:hover': { boxShadow: 4 } }}>
                <CardContent>
                  <Typography variant="h6" component="h3" sx={{ mb: 2 }}>
                    {feature.title}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    {feature.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>

        <Box sx={{ textAlign: 'center', py: 4 }}>
          <Typography variant="h5" sx={{ mb: 3 }}>
            Get Started
          </Typography>
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
            <Button
              variant="contained"
              component={RouterLink}
              to="/students"
            >
              Manage Students
            </Button>
            <Button
              variant="contained"
              component={RouterLink}
              to="/courses"
            >
              Manage Courses
            </Button>
          </Box>
        </Box>
      </Container>
    </Box>
  );
}
