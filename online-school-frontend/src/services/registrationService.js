import api from './api';

export const getRegistrationsByStudentId = async (studentId) => {
  const response = await api.get(`/registrations/student/${studentId}`);
  return response.data;
};
