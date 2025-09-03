import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Button,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Alert,
  Snackbar,
  Chip,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import { DataGrid, GridColDef, GridActionsCellItem } from '@mui/x-data-grid';
import { Add, Edit, Delete, Visibility } from '@mui/icons-material';
import { Registration, Student, Course, registrationService, studentService, courseService } from '../services/api';

const Registrations: React.FC = () => {
  const [registrations, setRegistrations] = useState<Registration[]>([]);
  const [students, setStudents] = useState<Student[]>([]);
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingRegistration, setEditingRegistration] = useState<Registration | null>(null);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
  const [formData, setFormData] = useState({
    studentId: 0,
    courseId: 0,
    registrationDate: '',
    status: 'ACTIVE' as 'ACTIVE' | 'COMPLETED' | 'DROPPED' | 'PENDING',
    grade: '',
  });

  useEffect(() => {
    fetchRegistrations();
    fetchStudents();
    fetchCourses();
  }, []);

  const fetchRegistrations = async () => {
    try {
      setLoading(true);
      const response = await registrationService.getAll();
      setRegistrations(response.data);
    } catch (error) {
      showSnackbar('Failed to fetch registrations', 'error');
    } finally {
      setLoading(false);
    }
  };

  const fetchStudents = async () => {
    try {
      const response = await studentService.getAll();
      setStudents(response.data);
    } catch (error) {
      console.error('Failed to fetch students:', error);
    }
  };

  const fetchCourses = async () => {
    try {
      const response = await courseService.getAll();
      setCourses(response.data);
    } catch (error) {
      console.error('Failed to fetch courses:', error);
    }
  };

  const showSnackbar = (message: string, severity: 'success' | 'error') => {
    setSnackbar({ open: true, message, severity });
  };

  const handleOpenDialog = (registration?: Registration) => {
    if (registration) {
      setEditingRegistration(registration);
      setFormData({
        studentId: registration.studentId,
        courseId: registration.courseId,
        registrationDate: registration.registrationDate,
        status: registration.status,
        grade: registration.grade || '',
      });
    } else {
      setEditingRegistration(null);
      setFormData({
        studentId: 0,
        courseId: 0,
        registrationDate: new Date().toISOString().split('T')[0],
        status: 'ACTIVE',
        grade: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingRegistration(null);
    setFormData({ studentId: 0, courseId: 0, registrationDate: '', status: 'ACTIVE', grade: '' });
  };

  const handleSubmit = async () => {
    try {
      const submitData = {
        ...formData,
        grade: formData.grade || undefined,
      };

      if (editingRegistration) {
        await registrationService.update(editingRegistration.id!, submitData);
        showSnackbar('Registration updated successfully', 'success');
      } else {
        await registrationService.create(submitData);
        showSnackbar('Registration created successfully', 'success');
      }
      handleCloseDialog();
      fetchRegistrations();
    } catch (error) {
      showSnackbar('Failed to save registration', 'error');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this registration?')) {
      try {
        await registrationService.delete(id);
        showSnackbar('Registration deleted successfully', 'success');
        fetchRegistrations();
      } catch (error) {
        showSnackbar('Failed to delete registration', 'error');
      }
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'ACTIVE': return 'success';
      case 'COMPLETED': return 'primary';
      case 'DROPPED': return 'error';
      case 'PENDING': return 'warning';
      default: return 'default';
    }
  };

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'studentName', headerName: 'Student', width: 200 },
    { field: 'courseName', headerName: 'Course', width: 250 },
    { 
      field: 'registrationDate', 
      headerName: 'Registration Date', 
      width: 150,
      valueFormatter: (params: any) => new Date(params.value).toLocaleDateString(),
    },
    {
      field: 'status',
      headerName: 'Status',
      width: 120,
      renderCell: (params) => (
        <Chip 
          label={params.value} 
          size="small" 
          color={getStatusColor(params.value) as any}
          variant="outlined"
        />
      ),
    },
    { 
      field: 'grade', 
      headerName: 'Grade', 
      width: 100,
      renderCell: (params) => (
        params.value ? (
          <Chip label={params.value} size="small" color="info" />
        ) : (
          <Typography variant="body2" color="text.secondary">-</Typography>
        )
      ),
    },
    {
      field: 'actions',
      type: 'actions',
      headerName: 'Actions',
      width: 150,
      getActions: (params) => [
        <GridActionsCellItem
          icon={<Visibility />}
          label="View"
          onClick={() => handleOpenDialog(params.row)}
        />,
        <GridActionsCellItem
          icon={<Edit />}
          label="Edit"
          onClick={() => handleOpenDialog(params.row)}
        />,
        <GridActionsCellItem
          icon={<Delete />}
          label="Delete"
          onClick={() => handleDelete(params.row.id)}
        />,
      ],
    },
  ];

  return (
    <Container maxWidth="lg">
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4" component="h1">
          Registrations
        </Typography>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={() => handleOpenDialog()}
        >
          Add Registration
        </Button>
      </Box>

      <Box sx={{ height: 600, width: '100%' }}>
        <DataGrid
          rows={registrations}
          columns={columns}
          loading={loading}
          pageSizeOptions={[5, 10, 25]}
          initialState={{
            pagination: {
              paginationModel: { page: 0, pageSize: 10 },
            },
          }}
          disableRowSelectionOnClick
        />
      </Box>

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {editingRegistration ? 'Edit Registration' : 'Add New Registration'}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ pt: 2, display: 'flex', flexDirection: 'column', gap: 2 }}>
            <FormControl fullWidth required>
              <InputLabel>Student</InputLabel>
              <Select
                value={formData.studentId}
                label="Student"
                onChange={(e) => setFormData({ ...formData, studentId: e.target.value as number })}
              >
                {students.map((student) => (
                  <MenuItem key={student.id} value={student.id}>
                    {student.name} ({student.studentId})
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl fullWidth required>
              <InputLabel>Course</InputLabel>
              <Select
                value={formData.courseId}
                label="Course"
                onChange={(e) => setFormData({ ...formData, courseId: e.target.value as number })}
              >
                {courses.map((course) => (
                  <MenuItem key={course.id} value={course.id}>
                    {course.name} ({course.credits} credits)
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <TextField
              label="Registration Date"
              type="date"
              value={formData.registrationDate}
              onChange={(e) => setFormData({ ...formData, registrationDate: e.target.value })}
              fullWidth
              required
              InputLabelProps={{ shrink: true }}
            />
            <FormControl fullWidth required>
              <InputLabel>Status</InputLabel>
              <Select
                value={formData.status}
                label="Status"
                onChange={(e) => setFormData({ ...formData, status: e.target.value as any })}
              >
                <MenuItem value="ACTIVE">Active</MenuItem>
                <MenuItem value="COMPLETED">Completed</MenuItem>
                <MenuItem value="DROPPED">Dropped</MenuItem>
                <MenuItem value="PENDING">Pending</MenuItem>
              </Select>
            </FormControl>
            <TextField
              label="Grade (Optional)"
              value={formData.grade}
              onChange={(e) => setFormData({ ...formData, grade: e.target.value })}
              fullWidth
              placeholder="e.g., A, B+, C, etc."
              helperText="Leave empty if not graded yet"
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button 
            onClick={handleSubmit} 
            variant="contained" 
            disabled={!formData.studentId || !formData.courseId}
          >
            {editingRegistration ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert
          onClose={() => setSnackbar({ ...snackbar, open: false })}
          severity={snackbar.severity}
          sx={{ width: '100%' }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default Registrations;