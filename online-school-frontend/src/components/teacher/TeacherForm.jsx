import React, { useState } from 'react';
import { TextField, Button, Box, Alert } from '@mui/material';

export default function TeacherForm({ onSubmit, initialData = null }) {
  const [formData, setFormData] = useState(
    initialData || {
      name: '',
      email: '',
      employeeId: '',
      department: '',
      hireDate: ''
    }
  );
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
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
    if (!formData.employeeId.trim()) {
      setError('Employee ID is required');
      return;
    }
    if (!formData.department.trim()) {
      setError('Department is required');
      return;
    }
    if (!formData.hireDate) {
      setError('Hire date is required');
      return;
    }

    try {
      await onSubmit(formData);
      setFormData({
        name: '',
        email: '',
        employeeId: '',
        department: '',
        hireDate: ''
      });
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
        label="Employee ID"
        name="employeeId"
        value={formData.employeeId}
        onChange={handleChange}
        margin="normal"
        required
        inputProps={{ minLength: 3, maxLength: 20 }}
      />

      <TextField
        fullWidth
        label="Department"
        name="department"
        value={formData.department}
        onChange={handleChange}
        margin="normal"
        required
      />

      <TextField
        fullWidth
        label="Hire Date"
        name="hireDate"
        type="date"
        value={formData.hireDate}
        onChange={handleChange}
        margin="normal"
        required
        InputLabelProps={{ shrink: true }}
      />

      <Button type="submit" variant="contained" sx={{ mt: 2 }}>
        Submit
      </Button>
    </Box>
  );
}
