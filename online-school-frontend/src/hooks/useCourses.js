import { useState, useEffect } from 'react';
import {
  getCourses,
  createCourse,
  updateCourse,
  deleteCourse
} from '../services/courseService';

export default function useCourses() {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      setLoading(true);
      const data = await getCourses();
      setCourses(data);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const addCourse = async (course) => {
    try {
      const newCourse = await createCourse(course);
      setCourses([...courses, newCourse]);
      return newCourse;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const updateCourseData = async (id, course) => {
    try {
      const updated = await updateCourse(id, course);
      setCourses(courses.map(c => c.id === id ? updated : c));
      return updated;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const removeCourse = async (id) => {
    try {
      await deleteCourse(id);
      setCourses(courses.filter(c => c.id !== id));
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  return {
    courses,
    loading,
    error,
    fetchCourses,
    addCourse,
    updateCourseData,
    removeCourse
  };
}
