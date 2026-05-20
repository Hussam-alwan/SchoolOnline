import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Divider,
  List,
  ListItem,
  ListItemText,
  Stack,
  Typography,
} from '@mui/material';
import LoadingSpinner from '../common/LoadingSpinner';
import StudentForm from './StudentForm';
import {
  getStudentById,
  updateStudent,
  deleteStudent,
} from '../../services/studentService';
import { getRegistrationsByStudentId } from '../../services/registrationService';

export default function StudentDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [student, setStudent] = useState(null);
  const [registrations, setRegistrations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editing, setEditing] = useState(false);

  useEffect(() => {
    let cancelled = false;

    const load = async () => {
      try {
        setLoading(true);
        setError(null);
        const studentData = await getStudentById(id);
        if (cancelled) return;
        setStudent(studentData);

        try {
          const regs = await getRegistrationsByStudentId(id);
          if (!cancelled) setRegistrations(regs);
        } catch {
          if (!cancelled) setRegistrations([]);
        }
      } catch (err) {
        if (!cancelled) {
          setError(
            err.response?.status === 404
              ? 'Student not found'
              : err.message || 'Failed to load student'
          );
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    load();
    return () => {
      cancelled = true;
    };
  }, [id]);

  const handleUpdate = async (formData) => {
    const updated = await updateStudent(id, formData);
    setStudent(updated);
    setEditing(false);
  };

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete this student?')) return;
    try {
      await deleteStudent(id);
      navigate('/students');
    } catch (err) {
      setError(err.message || 'Failed to delete student');
    }
  };

  if (loading) return <LoadingSpinner message="Loading student..." />;

  if (error) {
    return (
      <Box>
        <Button onClick={() => navigate('/students')} sx={{ mb: 2 }}>
          ← Back to Students
        </Button>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  if (!student) return null;

  return (
    <Box>
      <Button onClick={() => navigate('/students')} sx={{ mb: 2 }}>
        ← Back to Students
      </Button>

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'start',
              mb: 2,
            }}
          >
            <Box>
              <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold' }}>
                {student.name}
              </Typography>
              <Typography color="textSecondary">{student.email}</Typography>
            </Box>
            <Chip
              label={`GPA: ${student.gpa?.toFixed(2) || '0.00'}`}
              color="primary"
            />
          </Box>

          <Divider sx={{ my: 2 }} />

          <Stack spacing={1}>
            <Typography>
              <strong>ID:</strong> {student.id}
            </Typography>
            {student.studentId && (
              <Typography>
                <strong>Student ID:</strong> {student.studentId}
              </Typography>
            )}
            <Typography>
              <strong>Status:</strong> {student.status || 'ACTIVE'}
            </Typography>
            {student.enrollmentDate && (
              <Typography>
                <strong>Enrolled:</strong> {student.enrollmentDate}
              </Typography>
            )}
          </Stack>

          <Box sx={{ mt: 3, display: 'flex', gap: 1 }}>
            <Button
              variant="contained"
              onClick={() => setEditing((prev) => !prev)}
            >
              {editing ? 'Cancel' : 'Edit'}
            </Button>
            <Button variant="outlined" color="error" onClick={handleDelete}>
              Delete
            </Button>
          </Box>
        </CardContent>
      </Card>

      {editing && (
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6" sx={{ mb: 2 }}>
              Edit Student
            </Typography>
            <StudentForm onSubmit={handleUpdate} initialData={student} />
          </CardContent>
        </Card>
      )}

      <Card>
        <CardContent>
          <Typography variant="h6" sx={{ mb: 2 }}>
            Enrolled Courses ({registrations.length})
          </Typography>
          {registrations.length === 0 ? (
            <Alert severity="info">No course enrollments found</Alert>
          ) : (
            <List dense>
              {registrations.map((reg) => (
                <ListItem key={reg.id} divider>
                  <ListItemText
                    primary={reg.courseName || `Course #${reg.courseId}`}
                    secondary={
                      <>
                        Status: {reg.status || 'N/A'}
                        {reg.grade ? ` · Grade: ${reg.grade}` : ''}
                      </>
                    }
                  />
                </ListItem>
              ))}
            </List>
          )}
        </CardContent>
      </Card>
    </Box>
  );
}
