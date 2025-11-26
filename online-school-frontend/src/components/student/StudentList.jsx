import React, { useState } from 'react';
import { Alert, Box, Button } from '@mui/material';
import StudentCard from './StudentCard';
import StudentForm from './StudentForm';
import LoadingSpinner from '../common/LoadingSpinner';
import useStudents from '../../hooks/useStudents';

export default function StudentList() {
  const { students, loading, error, addStudent, removeStudent } = useStudents();
  const [showForm, setShowForm] = useState(false);

  const handleAddStudent = async (formData) => {
    try {
      await addStudent(formData);
      setShowForm(false);
    } catch (err) {
      console.error('Failed to add student:', err);
    }
  };

  const handleDeleteStudent = async (id) => {
    if (window.confirm('Are you sure you want to delete this student?')) {
      try {
        await removeStudent(id);
      } catch (err) {
        console.error('Failed to delete student:', err);
      }
    }
  };

  if (loading) return <LoadingSpinner message="Loading students..." />;

  return (
    <Box>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Box sx={{ mb: 3 }}>
        <Button
          variant="contained"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? 'Cancel' : 'Add New Student'}
        </Button>
      </Box>

      {showForm && (
        <Box sx={{ mb: 3, p: 2, backgroundColor: '#f5f5f5', borderRadius: 1 }}>
          <StudentForm onSubmit={handleAddStudent} />
        </Box>
      )}

      {students.length === 0 ? (
        <Alert severity="info">No students found</Alert>
      ) : (
        students.map(student => (
          <StudentCard
            key={student.id}
            student={student}
            onDelete={handleDeleteStudent}
          />
        ))
      )}
    </Box>
  );
}
