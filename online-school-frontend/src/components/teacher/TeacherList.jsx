import React, { useState } from 'react';
import { Alert, Box, Button } from '@mui/material';
import TeacherCard from './TeacherCard';
import TeacherForm from './TeacherForm';
import LoadingSpinner from '../common/LoadingSpinner';
import useTeachers from '../../hooks/useTeachers';

export default function TeacherList() {
  const { teachers, loading, error, addTeacher, removeTeacher } = useTeachers();
  const [showForm, setShowForm] = useState(false);

  const handleAddTeacher = async (formData) => {
    try {
      await addTeacher(formData);
      setShowForm(false);
    } catch (err) {
      console.error('Failed to add teacher:', err);
    }
  };

  const handleDeleteTeacher = async (id) => {
    if (window.confirm('Are you sure you want to delete this teacher?')) {
      try {
        await removeTeacher(id);
      } catch (err) {
        console.error('Failed to delete teacher:', err);
      }
    }
  };

  if (loading) return <LoadingSpinner message="Loading teachers..." />;

  return (
    <Box>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Box sx={{ mb: 3 }}>
        <Button
          variant="contained"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? 'Cancel' : 'Add New Teacher'}
        </Button>
      </Box>

      {showForm && (
        <Box sx={{ mb: 3, p: 2, backgroundColor: '#f5f5f5', borderRadius: 1 }}>
          <TeacherForm onSubmit={handleAddTeacher} />
        </Box>
      )}

      {teachers.length === 0 ? (
        <Alert severity="info">No teachers found</Alert>
      ) : (
        teachers.map(teacher => (
          <TeacherCard
            key={teacher.id}
            teacher={teacher}
            onDelete={handleDeleteTeacher}
          />
        ))
      )}
    </Box>
  );
}
