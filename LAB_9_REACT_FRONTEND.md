# Lab 9: ReactJS - Modern Frontend Development

## Overview

Lab 9 introduces modern frontend development using React, a powerful JavaScript library for building user interfaces. This lab teaches how to build component-based applications, manage state with hooks, handle side effects, integrate with backend APIs, implement routing, and use Material-UI for professional UI components. Students will learn best practices for building scalable, maintainable React applications.

## Learning Objectives

By completing this lab, you will understand:

- **React Components:** How to create functional and class components
- **JSX:** How to write JSX syntax and render dynamic content
- **State Management:** How to use useState hook to manage component state
- **Side Effects:** How to use useEffect hook for API calls and lifecycle management
- **Props:** How to pass data between components
- **Event Handling:** How to handle user interactions and events
- **Conditional Rendering:** How to render components conditionally
- **Lists and Keys:** How to render lists efficiently with keys
- **Forms:** How to handle form inputs and submissions
- **API Integration:** How to fetch data from backend APIs
- **Routing:** How to implement client-side routing with React Router
- **Material-UI:** How to use Material-UI components for professional UI
- **Custom Hooks:** How to create reusable logic with custom hooks
- **Context API:** How to manage global state with Context
- **Performance:** How to optimize React applications
- **Testing:** How to test React components

## Technology Stack

- **React 18:** Latest React version
- **React Router 6:** Client-side routing
- **Material-UI (MUI):** Professional UI component library
- **Axios:** HTTP client for API calls
- **React Hooks:** useState, useEffect, useContext, useReducer
- **JavaScript ES6+:** Modern JavaScript features
- **CSS-in-JS:** Styled components or Material-UI styling
- **Node.js:** JavaScript runtime
- **npm/yarn:** Package managers
- **Vite:** Fast build tool (optional)
- **Jest:** Testing framework (optional)

## Project Structure

```
online-school-frontend/
├── public/
│   ├── index.html
│   ├── favicon.ico
│   └── manifest.json
├── src/
│   ├── components/
│   │   ├── common/
│   │   │   ├── Header.jsx
│   │   │   ├── Footer.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   └── LoadingSpinner.jsx
│   │   ├── pages/
│   │   │   ├── HomePage.jsx
│   │   │   ├── StudentsPage.jsx
│   │   │   ├── CoursesPage.jsx
│   │   │   ├── ClassesPage.jsx
│   │   │   └── NotFoundPage.jsx
│   │   ├── student/
│   │   │   ├── StudentList.jsx
│   │   │   ├── StudentCard.jsx
│   │   │   ├── StudentForm.jsx
│   │   │   └── StudentDetail.jsx
│   │   ├── course/
│   │   │   ├── CourseList.jsx
│   │   │   ├── CourseCard.jsx
│   │   │   └── CourseForm.jsx
│   │   └── class/
│   │       ├── ClassList.jsx
│   │       ├── ClassCard.jsx
│   │       └── ClassForm.jsx
│   ├── hooks/
│   │   ├── useStudents.js
│   │   ├── useCourses.js
│   │   ├── useClasses.js
│   │   └── useFetch.js
│   ├── services/
│   │   ├── api.js
│   │   ├── studentService.js
│   │   ├── courseService.js
│   │   └── classService.js
│   ├── context/
│   │   ├── AuthContext.jsx
│   │   └── NotificationContext.jsx
│   ├── styles/
│   │   ├── theme.js
│   │   ├── global.css
│   │   └── variables.css
│   ├── utils/
│   │   ├── constants.js
│   │   ├── helpers.js
│   │   └── validators.js
│   ├── App.jsx
│   ├── App.css
│   ├── index.jsx
│   └── index.css
├── package.json
├── package-lock.json
├── .env
├── .env.example
├── .gitignore
├── vite.config.js (if using Vite)
└── README.md
```

## Getting Started

### Prerequisites

- Node.js 16+ and npm/yarn
- Basic understanding of JavaScript ES6+
- Familiarity with HTML and CSS
- Understanding of React basics

### Setup Instructions

1. **Navigate to frontend directory:**
```bash
cd online-school-frontend
```

2. **Install dependencies:**
```bash
npm install
```

3. **Create environment file:**
```bash
cp .env.example .env
```

4. **Update .env file:**
```
VITE_API_URL=http://localhost:8080/api
VITE_APP_NAME=Online School
```

5. **Start development server:**
```bash
npm run dev
```

6. **Build for production:**
```bash
npm run build
```

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint
- `npm run test` - Run tests

## Lab Progression

This is **Lab 9** of the bootcamp curriculum:

- **Lab 1:** Java Fundamentals - Core Java and OOP ✅
- **Lab 2:** JUnit Testing - Advanced testing patterns ✅
- **Lab 3:** Spring Boot Basics - REST APIs and services ✅
- **Lab 4:** Database & SQL - SQL fundamentals ✅
- **Lab 5:** ORM & JPA - Object-Relational Mapping ✅
- **Lab 6:** Backend API - RESTful API development ✅
- **Lab 7:** Maven & Build Tools - Build automation ✅
- **Lab 8:** Frontend HTML & CSS - Web basics ✅
- **Lab 9:** ReactJS - Modern frontend (current)
- **Lab 10:** Full Stack Integration - Complete application (coming soon)

## Next Steps

1. Set up React project with Vite
2. Install required dependencies
3. Create component structure
4. Implement API service layer
5. Build student management features
6. Add routing and navigation
7. Implement Material-UI theme
8. Add error handling and loading states
9. Test components and API integration
10. Deploy to production

---

**Last Updated:** November 25, 2025  
**Lab:** 9 - ReactJS Frontend  
**Status:** Ready for Development
