import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

export default function NotFoundPage() {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '400px',
        textAlign: 'center'
      }}
    >
      <Typography variant="h1" sx={{ mb: 2, fontWeight: 'bold' }}>
        404
      </Typography>
      <Typography variant="h5" sx={{ mb: 3 }}>
        Page Not Found
      </Typography>
      <Typography variant="body1" color="textSecondary" sx={{ mb: 4 }}>
        The page you are looking for does not exist.
      </Typography>
      <Button
        variant="contained"
        component={RouterLink}
        to="/"
      >
        Go Home
      </Button>
    </Box>
  );
}
