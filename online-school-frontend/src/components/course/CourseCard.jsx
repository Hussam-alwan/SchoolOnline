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

export default function CourseCard({ course, onEdit, onDelete }) {
  return (
    <Card sx={{ mb: 2, '&:hover': { boxShadow: 4 } }}>
      <CardContent>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
          <Box>
            <Typography variant="h6" component="div">
              {course.name}
            </Typography>
            <Typography sx={{ mb: 1.5 }} color="textSecondary">
              {course.description}
            </Typography>
          </Box>
          <Chip
            label={`Credits: ${course.credits || 0}`}
            color="primary"
            variant="outlined"
          />
        </Box>
        <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
          <Typography variant="body2">
            <strong>ID:</strong> {course.id}
          </Typography>
          <Typography variant="body2">
            <strong>Status:</strong> {course.status || 'ACTIVE'}
          </Typography>
        </Box>
      </CardContent>
      <CardActions>
        <Button size="small" onClick={() => onEdit?.(course)}>
          Edit
        </Button>
        <Button size="small" color="error" onClick={() => onDelete?.(course.id)}>
          Delete
        </Button>
      </CardActions>
    </Card>
  );
}
