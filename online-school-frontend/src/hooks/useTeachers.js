import { useState, useEffect } from 'react';
import {
  getTeachers,
  createTeacher,
  updateTeacher,
  deleteTeacher
} from '../services/teacherService';

export default function useTeachers() {
  const [teachers, setTeachers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchTeachers();
  }, []);

  const fetchTeachers = async () => {
    try {
      setLoading(true);
      const data = await getTeachers();
      setTeachers(data);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const addTeacher = async (teacher) => {
    try {
      const newTeacher = await createTeacher(teacher);
      setTeachers([...teachers, newTeacher]);
      return newTeacher;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const updateTeacherData = async (id, teacher) => {
    try {
      const updated = await updateTeacher(id, teacher);
      setTeachers(teachers.map(t => t.id === id ? updated : t));
      return updated;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const removeTeacher = async (id) => {
    try {
      await deleteTeacher(id);
      setTeachers(teachers.filter(t => t.id !== id));
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  return {
    teachers,
    loading,
    error,
    fetchTeachers,
    addTeacher,
    updateTeacherData,
    removeTeacher
  };
}