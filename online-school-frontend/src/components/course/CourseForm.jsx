import React, { useState } from 'react';
import { TextField, Button, Box, Alert } from '@mui/material';

export default function CourseForm({ onSubmit, initialData = null }) {
  const [formData, setFormData] = useState(
    initialData || {
      name: '',
      description: '',
      credits: 3
    }
  );
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'credits' ? parseInt(value) : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!formData.name.trim()) {
      setError('Course name is required');
      return;
    }

    try {
      await onSubmit(formData);
      setFormData({ name: '', description: '', credits: 3 });
    } catch (err) {
      setError(err.message || 'Failed to submit form');
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit}>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <TextField
        fullWidth
        label="Course Name"
        name="name"
        value={formData.name}
        onChange={handleChange}
        margin="normal"
        required
      />

      <TextField
        fullWidth
        label="Description"
        name="description"
        value={formData.description}
        onChange={handleChange}
        margin="normal"
        multiline
        rows={3}
      />

      <TextField
        fullWidth
        label="Credits"
        name="credits"
        type="number"
        value={formData.credits}
        onChange={handleChange}
        margin="normal"
        inputProps={{ min: '1', max: '4' }}
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
