import React, { useState } from 'react';
import { Alert, Box, Button } from '@mui/material';
import CourseCard from './CourseCard';
import CourseForm from './CourseForm';
import LoadingSpinner from '../common/LoadingSpinner';
import useCourses from '../../hooks/useCourses';

export default function CourseList() {
  const { courses, loading, error, addCourse, removeCourse } = useCourses();
  const [showForm, setShowForm] = useState(false);

  const handleAddCourse = async (formData) => {
    try {
      await addCourse(formData);
      setShowForm(false);
    } catch (err) {
      console.error('Failed to add course:', err);
    }
  };

  const handleDeleteCourse = async (id) => {
    if (window.confirm('Are you sure you want to delete this course?')) {
      try {
        await removeCourse(id);
      } catch (err) {
        console.error('Failed to delete course:', err);
      }
    }
  };

  if (loading) return <LoadingSpinner message="Loading courses..." />;

  return (
    <Box>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Box sx={{ mb: 3 }}>
        <Button
          variant="contained"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? 'Cancel' : 'Add New Course'}
        </Button>
      </Box>

      {showForm && (
        <Box sx={{ mb: 3, p: 2, backgroundColor: '#f5f5f5', borderRadius: 1 }}>
          <CourseForm onSubmit={handleAddCourse} />
        </Box>
      )}

      {courses.length === 0 ? (
        <Alert severity="info">No courses found</Alert>
      ) : (
        courses.map(course => (
          <CourseCard
            key={course.id}
            course={course}
            onDelete={handleDeleteCourse}
          />
        ))
      )}
    </Box>
  );
}
