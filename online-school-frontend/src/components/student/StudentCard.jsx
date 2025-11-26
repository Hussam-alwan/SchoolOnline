import React from 'react';
import {
  Card,
  CardContent,
  CardActions,
  Typography,
  Button,
  Box,
  Chip
} from '@mui/material';

export default function StudentCard({ student, onEdit, onDelete }) {
  return (
    <Card sx={{ mb: 2, '&:hover': { boxShadow: 4 } }}>
      <CardContent>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
          <Box>
            <Typography variant="h6" component="div">
              {student.name}
            </Typography>
            <Typography sx={{ mb: 1.5 }} color="textSecondary">
              {student.email}
            </Typography>
          </Box>
          <Chip
            label={`GPA: ${student.gpa?.toFixed(2) || '0.00'}`}
            color="primary"
            variant="outlined"
          />
        </Box>
        <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
          <Typography variant="body2">
            <strong>ID:</strong> {student.id}
          </Typography>
          <Typography variant="body2">
            <strong>Status:</strong> {student.status || 'ACTIVE'}
          </Typography>
        </Box>
      </CardContent>
      <CardActions>
        <Button size="small" onClick={() => onEdit?.(student)}>
          Edit
        </Button>
        <Button size="small" color="error" onClick={() => onDelete?.(student.id)}>
          Delete
        </Button>
      </CardActions>
    </Card>
  );
}
