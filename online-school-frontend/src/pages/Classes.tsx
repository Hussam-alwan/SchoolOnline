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
import { Class, Teacher, classService, teacherService } from '../services/api';

const Classes: React.FC = () => {
  const [classes, setClasses] = useState<Class[]>([]);
  const [teachers, setTeachers] = useState<Teacher[]>([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingClass, setEditingClass] = useState<Class | null>(null);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });
  const [formData, setFormData] = useState({
    name: '',
    semester: '',
    year: new Date().getFullYear(),
    maxCapacity: 30,
    teacherId: 0,
  });

  useEffect(() => {
    fetchClasses();
    fetchTeachers();
  }, []);

  const fetchClasses = async () => {
    try {
      setLoading(true);
      const response = await classService.getAll();
      setClasses(response.data);
    } catch (error) {
      showSnackbar('Failed to fetch classes', 'error');
    } finally {
      setLoading(false);
    }
  };

  const fetchTeachers = async () => {
    try {
      const response = await teacherService.getAll();
      setTeachers(response.data);
    } catch (error) {
      console.error('Failed to fetch teachers:', error);
    }
  };

  const showSnackbar = (message: string, severity: 'success' | 'error') => {
    setSnackbar({ open: true, message, severity });
  };

  const handleOpenDialog = (clazz?: Class) => {
    if (clazz) {
      setEditingClass(clazz);
      setFormData({
        name: clazz.name,
        semester: clazz.semester,
        year: clazz.year,
        maxCapacity: clazz.maxCapacity,
        teacherId: clazz.teacherId,
      });
    } else {
      setEditingClass(null);
      setFormData({
        name: '',
        semester: 'Fall',
        year: new Date().getFullYear(),
        maxCapacity: 30,
        teacherId: 0,
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingClass(null);
    setFormData({ name: '', semester: '', year: new Date().getFullYear(), maxCapacity: 30, teacherId: 0 });
  };

  const handleSubmit = async () => {
    try {
      if (editingClass) {
        await classService.update(editingClass.id!, formData);
        showSnackbar('Class updated successfully', 'success');
      } else {
        await classService.create(formData);
        showSnackbar('Class created successfully', 'success');
      }
      handleCloseDialog();
      fetchClasses();
    } catch (error) {
      showSnackbar('Failed to save class', 'error');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this class?')) {
      try {
        await classService.delete(id);
        showSnackbar('Class deleted successfully', 'success');
        fetchClasses();
      } catch (error) {
        showSnackbar('Failed to delete class', 'error');
      }
    }
  };

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'name', headerName: 'Class Name', width: 150 },
    { field: 'semester', headerName: 'Semester', width: 120 },
    { field: 'year', headerName: 'Year', width: 100, type: 'number' },
    { field: 'maxCapacity', headerName: 'Max Capacity', width: 120, type: 'number' },
    { field: 'teacherName', headerName: 'Teacher', width: 200 },
    {
      field: 'studentIds',
      headerName: 'Students',
      width: 120,
      renderCell: (params) => (
        <Chip 
          label={`${params.value?.length || 0}/${params.row.maxCapacity}`} 
          size="small" 
          color="primary" 
          variant="outlined"
        />
      ),
    },
    {
      field: 'courseIds',
      headerName: 'Courses',
      width: 120,
      renderCell: (params) => (
        <Chip 
          label={`${params.value?.length || 0} courses`} 
          size="small" 
          color="secondary" 
          variant="outlined"
        />
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
          Classes
        </Typography>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={() => handleOpenDialog()}
        >
          Add Class
        </Button>
      </Box>

      <Box sx={{ height: 600, width: '100%' }}>
        <DataGrid
          rows={classes}
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
          {editingClass ? 'Edit Class' : 'Add New Class'}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ pt: 2, display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label="Class Name"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              fullWidth
              required
              placeholder="e.g., CS-101-A"
            />
            <FormControl fullWidth required>
              <InputLabel>Semester</InputLabel>
              <Select
                value={formData.semester}
                label="Semester"
                onChange={(e) => setFormData({ ...formData, semester: e.target.value })}
              >
                <MenuItem value="Spring">Spring</MenuItem>
                <MenuItem value="Summer">Summer</MenuItem>
                <MenuItem value="Fall">Fall</MenuItem>
                <MenuItem value="Winter">Winter</MenuItem>
              </Select>
            </FormControl>
            <TextField
              label="Year"
              type="number"
              value={formData.year}
              onChange={(e) => setFormData({ ...formData, year: parseInt(e.target.value) || new Date().getFullYear() })}
              fullWidth
              required
              inputProps={{ min: 2020, max: 2030 }}
            />
            <TextField
              label="Max Capacity"
              type="number"
              value={formData.maxCapacity}
              onChange={(e) => setFormData({ ...formData, maxCapacity: parseInt(e.target.value) || 0 })}
              fullWidth
              required
              inputProps={{ min: 1, max: 100 }}
            />
            <FormControl fullWidth required>
              <InputLabel>Teacher</InputLabel>
              <Select
                value={formData.teacherId}
                label="Teacher"
                onChange={(e) => setFormData({ ...formData, teacherId: e.target.value as number })}
              >
                {teachers.map((teacher) => (
                  <MenuItem key={teacher.id} value={teacher.id}>
                    {teacher.name} ({teacher.department})
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained" disabled={!formData.teacherId}>
            {editingClass ? 'Update' : 'Create'}
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

export default Classes;