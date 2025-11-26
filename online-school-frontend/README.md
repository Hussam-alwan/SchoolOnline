# Online School Frontend - React Application

This is the React frontend for the Online School Management System. It provides a modern, responsive user interface for managing students, courses, and classes.

## Features

- **Student Management:** View, create, and manage student records
- **Course Management:** Manage courses with descriptions and credit hours
- **Responsive Design:** Works seamlessly on desktop, tablet, and mobile devices
- **Material-UI Components:** Professional and accessible UI components
- **API Integration:** Seamless integration with the backend API
- **Error Handling:** Comprehensive error handling and user feedback

## Technology Stack

- **React 18:** Modern React with hooks
- **React Router 6:** Client-side routing
- **Material-UI (MUI):** Professional UI component library
- **Axios:** HTTP client for API calls
- **Vite:** Fast build tool and dev server

## Prerequisites

- Node.js 16+ and npm/yarn
- Backend API running on `http://localhost:8080`

## Installation

1. Navigate to the frontend directory:
```bash
cd online-school-frontend
```

2. Install dependencies:
```bash
npm install
```

3. Create environment file:
```bash
cp .env.example .env
```

4. Update `.env` with your API URL (default is already set):
```
VITE_API_URL=http://localhost:8080/api
```

## Development

Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

## Building

Build for production:
```bash
npm run build
```

Preview production build:
```bash
npm run preview
```

## Project Structure

```
src/
├── components/
│   ├── common/           # Shared components (Header, Footer, etc.)
│   ├── pages/            # Page components
│   ├── student/          # Student-related components
│   └── course/           # Course-related components
├── hooks/                # Custom React hooks
├── services/             # API service layer
├── App.jsx               # Main app component
├── index.jsx             # Entry point
└── index.css             # Global styles
```

## API Integration

The frontend communicates with the backend API at `http://localhost:8080/api`. Key endpoints:

- `GET /api/students` - Get all students
- `POST /api/students` - Create a new student
- `PUT /api/students/{id}` - Update a student
- `DELETE /api/students/{id}` - Delete a student
- `GET /api/courses` - Get all courses
- `POST /api/courses` - Create a new course
- `PUT /api/courses/{id}` - Update a course
- `DELETE /api/courses/{id}` - Delete a course

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint
- `npm run test` - Run tests

## Troubleshooting

### CORS Errors
Ensure the backend has CORS enabled and the API URL in `.env` is correct.

### API Connection Issues
Check that the backend is running on `http://localhost:8080` and the API URL in `.env` matches.

### Styles Not Applying
Clear browser cache and restart the development server.

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

This project is part of the Online School Management System bootcamp curriculum.
