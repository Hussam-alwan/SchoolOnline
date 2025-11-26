import React from 'react';
import { Box, Container, Typography } from '@mui/material';

export default function Footer() {
  return (
    <Box
      component="footer"
      sx={{
        py: 3,
        px: 2,
        mt: 'auto',
        backgroundColor: '#f5f5f5',
        borderTop: '1px solid #e0e0e0'
      }}
    >
      <Container maxWidth="lg">
        <Typography variant="body2" color="textSecondary" align="center">
          &copy; 2025 Online School Management System. All rights reserved.
        </Typography>
      </Container>
    </Box>
  );
}
