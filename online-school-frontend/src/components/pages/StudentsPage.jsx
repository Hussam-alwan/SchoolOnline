import React from 'react';
import { Box, Typography } from '@mui/material';
import StudentList from '../student/StudentList';

export default function StudentsPage() {
  return (
    <Box>
      <Typography variant="h4" component="h1" sx={{ mb: 3, fontWeight: 'bold' }}>
        Students
      </Typography>
      <StudentList />
    </Box>
  );
}
