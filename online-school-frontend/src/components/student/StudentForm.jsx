import React, { useState } from 'react';
import { TextField, Button, Box, Alert } from '@mui/material';

export default function StudentForm({ onSubmit, initialData = null }) {
  const [formData, setFormData] = useState(
    initialData || {
      name: '',
      email: '',
      gpa: 0
    }
  );
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'gpa' ? parseFloat(value) : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!formData.name.trim()) {
      setError('Name is required');
      return;
    }

    if (!formData.email.trim()) {
      setError('Email is required');
      return;
    }

    try {
      await onSubmit(formData);
      setFormData({ name: '', email: '', gpa: 0 });
    } catch (err) {
      setError(err.message || 'Failed to submit form');
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit}>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <TextField
        fullWidth
        label="Name"
        name="name"
        value={formData.name}
        onChange={handleChange}
        margin="normal"
        required
      />

      <TextField
        fullWidth
        label="Email"
        name="email"
        type="email"
        value={formData.email}
        onChange={handleChange}
        margin="normal"
        required
      />

      <TextField
        fullWidth
        label="GPA"
        name="gpa"
        type="number"
        value={formData.gpa}
        onChange={handleChange}
        margin="normal"
        inputProps={{ step: '0.01', min: '0', max: '4' }}
      />

      <Button
        type="submit"
        variant="contained"
        sx={{ mt: 2 }}
      >
        Submit
      </Button>
    </Box>
  );
}
