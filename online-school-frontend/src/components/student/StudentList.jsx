import React, { useState, useMemo } from 'react';
import { Alert, Box, Button, TextField, InputAdornment, IconButton } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';
import StudentCard from './StudentCard';
import StudentForm from './StudentForm';
import LoadingSpinner from '../common/LoadingSpinner';
import useStudents from '../../hooks/useStudents';

export default function StudentList() {
  const { students, loading, error, addStudent, removeStudent } = useStudents();
  const [showForm, setShowForm] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  const filteredStudents = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();
    if (!term) return students;
    return students.filter(s =>
      s.name?.toLowerCase().includes(term) ||
      s.email?.toLowerCase().includes(term)
    );
  }, [students, searchTerm]);

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

      <Box sx={{ mb: 3, display: 'flex', gap: 2, flexWrap: 'wrap', alignItems: 'center' }}>
        <Button
          variant="contained"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? 'Cancel' : 'Add New Student'}
        </Button>
        <TextField
          size="small"
          placeholder="Search by name or email..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          sx={{ flexGrow: 1, minWidth: 240 }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon fontSize="small" />
              </InputAdornment>
            ),
            endAdornment: searchTerm && (
              <InputAdornment position="end">
                <IconButton size="small" onClick={() => setSearchTerm('')} aria-label="clear search">
                  <ClearIcon fontSize="small" />
                </IconButton>
              </InputAdornment>
            ),
          }}
        />
        {searchTerm && (
          <Button variant="outlined" onClick={() => setSearchTerm('')}>
            Clear
          </Button>
        )}
      </Box>

      {showForm && (
        <Box sx={{ mb: 3, p: 2, backgroundColor: '#f5f5f5', borderRadius: 1 }}>
          <StudentForm onSubmit={handleAddStudent} />
        </Box>
      )}

      {students.length === 0 ? (
        <Alert severity="info">No students found</Alert>
      ) : filteredStudents.length === 0 ? (
        <Alert severity="warning">No results found for "{searchTerm}"</Alert>
      ) : (
        filteredStudents.map(student => (
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
