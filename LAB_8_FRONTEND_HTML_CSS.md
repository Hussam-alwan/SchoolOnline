# Lab 8: Frontend HTML & CSS - Web Basics and Responsive Design

## Overview

Lab 8 introduces frontend web development fundamentals using HTML5 and CSS3. This lab teaches how to create semantic HTML structures, style web pages with CSS, implement responsive design, use CSS Grid and Flexbox, design forms, and ensure accessibility. Students will learn modern web design practices and create professional-looking web interfaces.

## Learning Objectives

By completing this lab, you will understand:

- **HTML5 Semantics:** How to use semantic HTML elements for proper document structure
- **CSS Styling:** How to style web pages with CSS selectors, properties, and values
- **Responsive Design:** How to create layouts that work on different screen sizes
- **CSS Grid:** How to create complex layouts using CSS Grid
- **Flexbox:** How to create flexible and responsive layouts using Flexbox
- **Forms:** How to design and style HTML forms with proper validation
- **Typography:** How to work with fonts, text styling, and readability
- **Colors and Backgrounds:** How to use colors, gradients, and background images
- **Accessibility:** How to create accessible web pages for all users
- **CSS Animations:** How to add animations and transitions to web pages
- **Media Queries:** How to create responsive designs with media queries
- **Best Practices:** How to write clean, maintainable HTML and CSS code

## Technology Stack

- **HTML5:** Latest HTML specification
- **CSS3:** Latest CSS specification
- **CSS Grid:** Modern layout system
- **Flexbox:** Flexible box layout
- **Media Queries:** Responsive design
- **Fonts:** Google Fonts, system fonts
- **Icons:** Font Awesome, Material Icons
- **Browser DevTools:** Chrome, Firefox, Safari
- **Code Editor:** VS Code, WebStorm, Sublime Text

## Project Structure

```
frontend/
├── index.html                       # Main page
├── css/
│   ├── styles.css                   # Main stylesheet
│   ├── responsive.css               # Responsive styles
│   └── animations.css               # Animation styles
├── js/
│   └── main.js                      # JavaScript (minimal)
├── images/
│   ├── logo.png
│   ├── hero.jpg
│   └── icons/
├── pages/
│   ├── students.html                # Students page
│   ├── courses.html                 # Courses page
│   ├── about.html                   # About page
│   └── contact.html                 # Contact page
└── assets/
    ├── fonts/
    └── icons/
```

## Key Concepts

### 1. HTML5 Semantic Elements

Use semantic HTML for better structure and accessibility:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online School</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <header>
        <nav>
            <ul>
                <li><a href="/">Home</a></li>
                <li><a href="/students">Students</a></li>
                <li><a href="/courses">Courses</a></li>
            </ul>
        </nav>
    </header>
    
    <main>
        <section>
            <h1>Welcome to Online School</h1>
            <p>Learn from anywhere, anytime</p>
        </section>
    </main>
    
    <footer>
        <p>&copy; 2025 Online School. All rights reserved.</p>
    </footer>
</body>
</html>
```

### 2. CSS Selectors and Styling

Style elements using CSS selectors:

```css
/* Element selector */
body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    line-height: 1.6;
    color: #333;
}

/* Class selector */
.container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
}

/* ID selector */
#header {
    background-color: #007bff;
    color: white;
    padding: 1rem 0;
}

/* Attribute selector */
input[type="text"] {
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
}

/* Pseudo-class */
a:hover {
    color: #0056b3;
    text-decoration: underline;
}
```

### 3. Flexbox Layout

Create flexible layouts with Flexbox:

```css
.navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem;
}

.nav-links {
    display: flex;
    gap: 2rem;
    list-style: none;
}

.hero {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 500px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    text-align: center;
}
```

### 4. CSS Grid Layout

Create complex layouts with CSS Grid:

```css
.grid-container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 2rem;
    padding: 2rem;
}

.card {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    transition: transform 0.3s ease;
}

.card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
```

### 5. Responsive Design

Create responsive layouts with media queries:

```css
/* Mobile first approach */
.container {
    width: 100%;
    padding: 1rem;
}

/* Tablet */
@media (min-width: 768px) {
    .container {
        width: 750px;
        margin: 0 auto;
    }
}

/* Desktop */
@media (min-width: 1024px) {
    .container {
        width: 960px;
    }
}

/* Large desktop */
@media (min-width: 1200px) {
    .container {
        width: 1140px;
    }
}
```

### 6. Forms and Inputs

Design accessible forms:

```html
<form class="form">
    <div class="form-group">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" required>
    </div>
    
    <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
    </div>
    
    <div class="form-group">
        <label for="message">Message:</label>
        <textarea id="message" name="message" rows="5"></textarea>
    </div>
    
    <button type="submit" class="btn btn-primary">Submit</button>
</form>
```

### 7. CSS Animations

Add animations and transitions:

```css
@keyframes slideIn {
    from {
        opacity: 0;
        transform: translateX(-100%);
    }
    to {
        opacity: 1;
        transform: translateX(0);
    }
}

.animated-element {
    animation: slideIn 0.5s ease-out;
}

.button {
    transition: all 0.3s ease;
}

.button:hover {
    background-color: #0056b3;
    transform: scale(1.05);
}
```

### 8. Accessibility

Create accessible web pages:

```html
<!-- Use semantic HTML -->
<header role="banner">
    <nav aria-label="Main navigation">
        <ul>
            <li><a href="/">Home</a></li>
        </ul>
    </nav>
</header>

<!-- Use alt text for images -->
<img src="logo.png" alt="Online School Logo">

<!-- Use proper heading hierarchy -->
<h1>Main Title</h1>
<h2>Subtitle</h2>

<!-- Use labels for form inputs -->
<label for="email">Email:</label>
<input type="email" id="email" name="email">
```

## Getting Started

### Prerequisites

- Code editor (VS Code, WebStorm, etc.)
- Web browser (Chrome, Firefox, Safari, Edge)
- Basic understanding of HTML and CSS

### Creating Your First Page

1. Create an `index.html` file:
```bash
touch index.html
```

2. Create a `css` directory:
```bash
mkdir css
```

3. Create a `styles.css` file:
```bash
touch css/styles.css
```

4. Link CSS in HTML:
```html
<link rel="stylesheet" href="css/styles.css">
```

5. Open in browser:
```bash
open index.html
```

## Common Tasks

### Creating a Navigation Bar

```html
<nav class="navbar">
    <div class="logo">Online School</div>
    <ul class="nav-links">
        <li><a href="/">Home</a></li>
        <li><a href="/students">Students</a></li>
        <li><a href="/courses">Courses</a></li>
    </ul>
</nav>
```

```css
.navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #333;
    color: white;
    padding: 1rem 2rem;
}

.nav-links {
    display: flex;
    list-style: none;
    gap: 2rem;
}

.nav-links a {
    color: white;
    text-decoration: none;
    transition: color 0.3s;
}

.nav-links a:hover {
    color: #007bff;
}
```

### Creating a Card Component

```html
<div class="card">
    <img src="image.jpg" alt="Card image">
    <div class="card-body">
        <h3>Card Title</h3>
        <p>Card description goes here</p>
        <a href="#" class="btn">Learn More</a>
    </div>
</div>
```

```css
.card {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    transition: transform 0.3s ease;
}

.card:hover {
    transform: translateY(-5px);
}

.card-body {
    padding: 1.5rem;
}

.card h3 {
    margin-top: 0;
}
```

### Creating a Responsive Grid

```html
<div class="grid">
    <div class="grid-item">Item 1</div>
    <div class="grid-item">Item 2</div>
    <div class="grid-item">Item 3</div>
    <div class="grid-item">Item 4</div>
</div>
```

```css
.grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 2rem;
    padding: 2rem;
}

.grid-item {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
```

## Best Practices

1. **Use semantic HTML** - Use proper HTML elements for structure
2. **Mobile-first approach** - Design for mobile first, then enhance for larger screens
3. **Accessibility** - Ensure your site is accessible to all users
4. **Performance** - Optimize images and minimize CSS/JS
5. **Consistency** - Use consistent colors, fonts, and spacing
6. **Clean code** - Write organized, well-commented code
7. **Responsive design** - Test on different screen sizes
8. **User experience** - Focus on usability and readability
9. **SEO** - Use proper HTML structure and meta tags
10. **Testing** - Test in different browsers and devices

## Troubleshooting

### Issue: Styles not applying
**Solution:** Check CSS file is linked correctly, clear browser cache, check selector specificity.

### Issue: Layout breaking on mobile
**Solution:** Add media queries, use flexible units (rem, %), test on mobile devices.

### Issue: Images not displaying
**Solution:** Check image path is correct, verify file exists, check file permissions.

### Issue: Form not submitting
**Solution:** Check form has action attribute, verify input names are correct, check JavaScript.

### Issue: Fonts not loading
**Solution:** Check font file path, verify font format is supported, check @font-face syntax.

## Resources

- [MDN Web Docs](https://developer.mozilla.org/)
- [CSS-Tricks](https://css-tricks.com/)
- [HTML5 Specification](https://html.spec.whatwg.org/)
- [CSS Specification](https://www.w3.org/Style/CSS/)
- [Google Fonts](https://fonts.google.com/)
- [Font Awesome Icons](https://fontawesome.com/)
- [Can I Use](https://caniuse.com/)
- [Web Accessibility Guidelines](https://www.w3.org/WAI/)

## Lab Progression

This is **Lab 8** of the bootcamp curriculum:

- **Lab 1:** Java Fundamentals - Core Java and OOP ✅
- **Lab 2:** JUnit Testing - Advanced testing patterns ✅
- **Lab 3:** Spring Boot Basics - REST APIs and services ✅
- **Lab 4:** Database & SQL - SQL fundamentals (coming soon)
- **Lab 5:** ORM & JPA - Object-Relational Mapping (coming soon)
- **Lab 6:** Backend API - RESTful API development (coming soon)
- **Lab 7:** Maven & Build Tools - Build automation (coming soon)
- **Lab 8:** Frontend HTML & CSS - Web basics (current)
- **Lab 9:** ReactJS - Modern frontend (coming soon)
- **Lab 10:** Full Stack Integration - Complete application (coming soon)

## Switching Between Labs

```bash
# View all available branches
git branch -a

# Switch to Lab 8
git checkout lab/frontend-8-html-css

# Switch to other labs
git checkout lab/java-1-fundamentals
git checkout lab/junit-2-testing
git checkout lab/springboot-3-basics
git checkout lab/orm-5-jpa
git checkout lab/api-6-backend
git checkout lab/maven-7-build
```

## Next Steps

1. Create your first HTML page with semantic structure
2. Style it with CSS using Flexbox and Grid
3. Make it responsive with media queries
4. Add animations and transitions
5. Ensure accessibility compliance
6. Test on different browsers and devices
7. Optimize for performance
8. Deploy to a web server

---

**Last Updated:** November 22, 2025  
**Lab:** 8 - Frontend HTML & CSS  
**Status:** Ready for Development
