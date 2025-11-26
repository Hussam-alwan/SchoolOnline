import React from 'react';
import { Box, Typography } from '@mui/material';
import CourseList from '../course/CourseList';

export default function CoursesPage() {
  return (
    <Box>
      <Typography variant="h4" component="h1" sx={{ mb: 3, fontWeight: 'bold' }}>
        Courses
      </Typography>
      <CourseList />
    </Box>
  );
}
