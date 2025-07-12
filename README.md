<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <p>
     <h1>Java-Based Learning Management System (LMS)</h1>
A Java-based Learning Management System (LMS) built with Spring Boot as a RESTful API. The system manages online courses, assessments, and user roles for students and instructors. Core functionalities include course creation, user and role management, performance tracking, and assessment handling. It uses a MySQL database for persistent storage and has been thoroughly tested using Postman.    </p>
  <h2>Contributors</h2>
    <ul>
      <li><strong><a href="https://github.com/DuaA-A">Duaa Abd-Elati </a></strong> – Project Lead, Core functionality, JUnit Testing</li>
      <li><strong><a href="https://github.com/RaghadThabet">Raghad Thabet </a></strong> – Email Services</li>
      <li><strong><a href="http://github.com/Kz191kz">Ahmed Ameen</a></strong> – Database Designer</li>
      <li><strong><a href="http://github.com/Kz191kz">Rehab Ameen</a></strong> – Core functionality</li>
      <li><strong>Safaa Tawfiq </strong> – Research, Documentation & Coordination</li>
    </ul>
    <h2>Key Components</h2>
    <h3>1. User Management</h3>
    <ul>
        <li><strong>User Types:</strong> Admin, Instructor, Student</li>
        <li>
            <strong>Admin:</strong> Manages system settings, creates users, and manages courses.
        </li>
        <li>
            <strong>Instructor:</strong> Creates courses, manages content, adds assignments and quizzes, grades students, and removes students.
        </li>
        <li>
            <strong>Student:</strong> Enrolls in courses, accesses materials, submits assignments, and views grades.
        </li>
        <li><strong>Features:</strong>
            <ul>
                <li>User registration and login (role-based access)</li>
                <li>Profile management</li>
            </ul>
        </li>
    </ul>
    <h3>2. Course Management</h3>
    <ul>
        <li><strong>Course Creation:</strong> Allows instructors to create courses with details, upload media files, and organize lessons.</li>
        <li><strong>Enrollment Management:</strong> Students can enroll in courses, and admins/instructors can view enrolled students.</li>
        <li><strong>Attendance Management:</strong> OTP-based attendance tracking for lessons.</li>
    </ul>
    <h3>3. Assessment & Grading</h3>
    <ul>
        <li><strong>Quiz Creation:</strong> Supports various question types and randomized question selection.</li>
        <li><strong>Assignment Submission:</strong> Students can upload assignments for instructor review.</li>
        <li><strong>Grading and Feedback:</strong> Automated quiz feedback and manual assignment feedback.</li>
    </ul>
    <h3>4. Performance Tracking</h3>
    <p>
        Instructors can track student progress, quiz scores, assignment submissions, and attendance.
    </p>
    <h3>5. Notifications</h3>
    <ul>
        <li>System notifications for enrollments, grades, and updates.</li>
        <li>Unread and read notification views.</li>
        <li>Instructors receive notifications for student enrollments.</li>
    </ul>
    <h3>Bonus Features</h3>
    <ul>
        <li><strong>Role-Based Access Control:</strong> Authentication and authorization using Spring Security.</li>
        <li><strong>Performance Analytics:</strong> Visual reports (charts) and Excel exports for student performance.</li>
        <li><strong>Email Notifications:</strong> Similar to system notifications for updates and grades.</li>
    </ul>
    <h2>Technical Requirements</h2>
    <ul>
        <li><strong>Backend:</strong> Java with Spring Boot for RESTful APIs</li>
        <li><strong>Database:</strong> MySQL, PostgreSQL, or SQLite</li>
        <li><strong>Testing:</strong> JUnit for unit testing</li>
        <li><strong>Version Control:</strong> Git</li>
    </ul>
    
</body>
</html>
