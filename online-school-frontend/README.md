# Online School Frontend

A modern React.js frontend application for the Online School Management System.

## Features

- **Dashboard**: Overview of system statistics and quick actions
- **Students Management**: Full CRUD operations for student records
- **Teachers Management**: Complete teacher profile management
- **Courses Management**: Course creation and management
- **Classes Management**: Class scheduling and teacher assignment
- **Registrations Management**: Student course registrations and grade tracking

## Technology Stack

- **React 18** with TypeScript
- **Material-UI (MUI)** for modern UI components
- **React Router** for navigation
- **Axios** for API communication
- **MUI X Data Grid** for advanced data tables

## Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- Online School Backend running on http://localhost:8080

## Installation

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm start
```

The application will open at http://localhost:3000

## Available Scripts

- `npm start` - Runs the app in development mode
- `npm test` - Launches the test runner
- `npm run build` - Builds the app for production
- `npm run eject` - Ejects from Create React App (one-way operation)

## Project Structure

```
src/
├── components/          # Reusable UI components
│   └── Navbar.tsx      # Navigation bar component
├── pages/              # Main application pages
│   ├── Dashboard.tsx   # Dashboard with statistics
│   ├── Students.tsx    # Student management
│   ├── Teachers.tsx    # Teacher management
│   ├── Courses.tsx     # Course management
│   ├── Classes.tsx     # Class management
│   └── Registrations.tsx # Registration management
├── services/           # API service layer
│   └── api.ts         # API client and type definitions
├── App.tsx            # Main application component
└── index.tsx          # Application entry point
```

## API Integration

The frontend communicates with the Spring Boot backend through RESTful APIs:

- **Base URL**: http://localhost:8080/api
- **CORS**: Configured to allow requests from localhost:3000
- **Proxy**: Development proxy configured in package.json

## Features Overview

### Dashboard
- Real-time statistics display
- Quick overview of system status
- Navigation to different modules

### Student Management
- View all students in a data grid
- Add new students with validation
- Edit existing student information
- Delete students with confirmation
- Track class enrollments and registrations

### Teacher Management
- Complete teacher profile management
- Department-based organization
- Class assignment tracking
- Employee ID management

### Course Management
- Course creation and editing
- Credit and duration tracking
- Class and registration associations
- Detailed course descriptions

### Class Management
- Semester and year-based scheduling
- Teacher assignment
- Student capacity management
- Course associations

### Registration Management
- Student-course enrollment tracking
- Status management (Active, Completed, Dropped, Pending)
- Grade assignment and tracking
- Registration date management

## Development

### Adding New Features

1. Create new components in `src/components/`
2. Add new pages in `src/pages/`
3. Update API services in `src/services/api.ts`
4. Add new routes in `App.tsx`

### Styling

The application uses Material-UI's theming system. The main theme is configured in `App.tsx`.

### State Management

Currently using React's built-in state management. For larger applications, consider adding Redux or Zustand.

## Production Build

To create a production build:

```bash
npm run build
```

This creates a `build` folder with optimized production files.

## Deployment

The built application can be deployed to any static hosting service:

- Netlify
- Vercel
- AWS S3 + CloudFront
- GitHub Pages

Make sure to update the API base URL for production deployment.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is part of the Online School Management System.