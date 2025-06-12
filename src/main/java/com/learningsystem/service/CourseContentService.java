package com.learningsystem.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.learningsystem.entity.Course;
import com.learningsystem.entity.CourseContent;
import com.learningsystem.repository.CourseContentRepository;
import com.learningsystem.repository.CourseRepository;

@Service
public class CourseContentService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseContentRepository courseContentRepository;

    public List<CourseContent> getContentByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return courseContentRepository.findByCourse(course);
    }

    public void deleteContent(Long contentId) {
        if (courseContentRepository.findById(contentId).isEmpty()) {
            throw new RuntimeException("Content not found");
        }
        courseContentRepository.deleteById(contentId);
    }

    public Course getCourseDetails(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
    public CourseContent addContent(Long courseId, String title, String type, String url) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        CourseContent content = new CourseContent();
        content.setCourse(course);
        content.setTitle(title);
        content.setType(type);
        content.setUrl(url);
        return courseContentRepository.save(content);
    }

    public void deleteContent(Long courseId, Long contentId) {
        System.out.println("Fetching course with ID: " + courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course with ID " + courseId + " not found"));
        System.out.println("Fetching content with ID: " + contentId);
        CourseContent content = courseContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content with ID " + contentId + " not found"));
        if (!content.getCourse().equals(course)) 
            throw new IllegalStateException("Content does not belong to the specified course");
        System.out.println("Deleting content with ID: " + contentId + " from course: " + courseId);
        courseContentRepository.deleteById(contentId);
    }
    

    public CourseContent updateContent(Long courseId, Long contentId, CourseContent contentRequest) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        CourseContent content = courseContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));
        if (!content.getCourse().equals(course)) 
            throw new RuntimeException("Content does not belong to the specified course");
        content.setTitle(contentRequest.getTitle());
        content.setType(contentRequest.getType());
        content.setUrl(contentRequest.getUrl());
        return courseContentRepository.save(content);
    }
}
