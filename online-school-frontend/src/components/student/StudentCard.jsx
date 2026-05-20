import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  CardActionArea,
  CardContent,
  CardActions,
  Typography,
  Button,
  Box,
  Chip,
} from '@mui/material';

export default function StudentCard({ student, onEdit, onDelete }) {
  const navigate = useNavigate();

  const stop = (handler) => (e) => {
    e.stopPropagation();
    handler?.();
  };

  return (
    <Card sx={{ mb: 2, '&:hover': { boxShadow: 4 } }}>
      <CardActionArea onClick={() => navigate(`/students/${student.id}`)}>
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
      </CardActionArea>
      <CardActions>
        <Button size="small" onClick={stop(() => onEdit?.(student))}>
          Edit
        </Button>
        <Button size="small" color="error" onClick={stop(() => onDelete?.(student.id))}>
          Delete
        </Button>
      </CardActions>
    </Card>
  );
}
