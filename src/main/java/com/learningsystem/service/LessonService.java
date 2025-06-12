package com.learningsystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learningsystem.entity.Course;
import com.learningsystem.entity.Lesson;
import com.learningsystem.repository.CourseRepository;
import com.learningsystem.repository.LessonRepository;

@Service
public class LessonService {
    
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    private final Map<Long, Lesson> lessons = new HashMap<>();
    private final Map<Long, Course> courses; 
    private final AtomicLong lessonIdGenerator = new AtomicLong(1);

    public LessonService(Map<Long, Course> courses) {
        this.courses = courses;
    }

    public Lesson addLessonToCourse(Long courseId, Lesson lesson) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return lessonRepository.findAll().values().stream()
                .filter(lesson -> lesson.getCourse().equals(course))
                .collect(Collectors.toList());
    }
}
