# Lab 9: React Frontend - Homework Assignment

## Overview
Practice building React components, managing state with hooks, and integrating with REST APIs by extending the Online School Management System frontend.

---

## Exercise 1: Teacher Management Feature
Create a complete Teacher management feature similar to the existing Student management.

### Tasks:
1. Create `teacherService.js` in `src/services/` with CRUD operations
2. Create `useTeachers.js` custom hook in `src/hooks/`
3. Create Teacher components:
   - `TeacherList.jsx` - Display all teachers
   - `TeacherCard.jsx` - Individual teacher card
   - `TeacherForm.jsx` - Form to add/edit teachers
4. Create `TeachersPage.jsx` in `src/components/pages/`
5. Add a route for `/teachers` in `App.jsx`
6. Add "Teachers" link to the Header navigation

### Expected Deliverables:
- All files created and properly organized
- Teachers can be viewed, added, and deleted
- Loading and error states handled
- Form validation for required fields

---

## Exercise 2: Enhanced Student Details
Add a detailed view for individual students.

### Tasks:
1. Create `StudentDetails.jsx` component that shows:
   - Full student information
   - List of courses the student is enrolled in
   - Edit and delete buttons
2. Add a route `/students/:id` in `App.jsx`
3. Make student cards clickable to navigate to details page
4. Add a "Back to Students" button

### Expected Deliverables:
- Student details page with all information
- Navigation works correctly
- Edit functionality opens a form
- Proper error handling if student not found

---

## Exercise 3: Search and Filter
Add search functionality to the Students page.

### Tasks:
1. Add a search input field above the student list
2. Implement filtering logic to search by:
   - Student name
   - Email
3. Display "No results found" when search returns empty
4. Add a "Clear" button to reset the search

### Expected Deliverables:
- Search input with proper styling
- Real-time filtering as user types
- Case-insensitive search
- Clear button resets the list

---

## Exercise 4: Form Validation
Enhance the StudentForm with better validation.

### Tasks:
1. Add validation for:
   - Name: Required, minimum 2 characters
   - Email: Required, valid email format
   - Age: Required, between 16 and 100
2. Display error messages below each field
3. Disable submit button when form is invalid
4. Show success message after successful submission

### Expected Deliverables:
- All fields validated on blur and submit
- Clear error messages displayed
- Submit button disabled when invalid
- Success feedback to user

---

## Exercise 5: Loading States and Error Handling
Improve user experience with better feedback.

### Tasks:
1. Add a loading spinner when fetching data
2. Show error alerts when API calls fail
3. Add retry button for failed requests
4. Implement optimistic UI updates (update UI before API confirms)
5. Add toast notifications for success/error messages

### Expected Deliverables:
- Loading indicators during API calls
- User-friendly error messages
- Retry functionality works
- Smooth user experience

---

## Bonus Challenges

### Bonus 1: Dark Mode Toggle
Add a dark mode toggle to the Header that switches between light and dark themes using Material-UI's theme provider.

### Bonus 2: Pagination
Implement pagination for the student list (10 students per page) with previous/next buttons.

### Bonus 3: Course Enrollment
Create a feature where you can enroll students in courses:
- Add "Enroll" button on student details page
- Show a dialog with available courses
- Call the registration API endpoint
- Display enrolled courses on student details

### Bonus 4: Responsive Design
Ensure all components work well on mobile devices:
- Use Material-UI Grid system
- Test on different screen sizes
- Adjust card layouts for mobile

---

## Submission Checklist

- [ ] All exercises completed and tested
- [ ] Code follows React best practices
- [ ] Components are properly organized in folders
- [ ] Custom hooks used for data fetching
- [ ] Error handling implemented
- [ ] Loading states displayed
- [ ] Forms have validation
- [ ] Navigation works correctly
- [ ] Backend API integration working
- [ ] No console errors or warnings
- [ ] Code is clean and readable

---

## Testing Your Work

1. Start the backend server:
   ```bash
   mvn spring-boot:run
   ```

2. Start the frontend development server:
   ```bash
   cd online-school-frontend
   npm run dev
   ```

3. Test all CRUD operations:
   - Create new records
   - View lists and details
   - Update existing records
   - Delete records

4. Test error scenarios:
   - Stop the backend and verify error handling
   - Submit invalid form data
   - Navigate to non-existent routes

---

## Common Pitfalls to Avoid

1. **Not handling async operations properly** - Always use try/catch with async/await
2. **Forgetting to update state immutably** - Use spread operator or array methods
3. **Missing dependency arrays in useEffect** - Can cause infinite loops
4. **Not validating props** - Use PropTypes or TypeScript
5. **Hardcoding API URLs** - Use environment variables
6. **Ignoring loading states** - Always show feedback during API calls
7. **Poor error messages** - Give users clear, actionable error information

---

## Resources

- [React Documentation](https://react.dev/)
- [React Router Documentation](https://reactrouter.com/)
- [Material-UI Documentation](https://mui.com/)
- [Axios Documentation](https://axios-http.com/)
- [React Hooks Guide](https://react.dev/reference/react)

---

## Tips for Success

- Start with Exercise 1 and work sequentially
- Test each feature before moving to the next
- Use browser DevTools to debug
- Check the Network tab for API calls
- Use React DevTools extension
- Keep components small and focused
- Reuse existing components when possible
- Follow the existing code patterns in the project
