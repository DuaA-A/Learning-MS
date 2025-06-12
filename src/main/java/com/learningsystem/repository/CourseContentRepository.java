package com.learningsystem.repository;

import org.springframework.stereotype.Repository;
import com.learningsystem.entity.Course;
import com.learningsystem.entity.CourseContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CourseContentRepository {
    
    private final Map<Long, CourseContent> contentStore = new ConcurrentHashMap<>();
    private Long contentIdCounter = 1L;

    public CourseContent save(CourseContent content) {
        if (content.getId() == null) 
            content.setId(contentIdCounter++); 
        contentStore.put(content.getId(), content);
        return content;
    }

    public Optional<CourseContent> findById(Long id) {
        return Optional.ofNullable(contentStore.get(id));
    }

    public List<CourseContent> findByCourse(Course course) {
        List<CourseContent> contentList = new ArrayList<>();
        for (CourseContent content : contentStore.values()) {
            if (content.getCourse().equals(course)) {
                contentList.add(content);
            }
        }
        return contentList;
    }

    public void deleteById(Long id) {
        contentStore.remove(id);
    }

    public Map<Long, CourseContent> findAll() {
        return contentStore;
    }

    public List<CourseContent> findAllByCourseId(Long courseId) {
        List<CourseContent> contentList = new ArrayList<>();
        for (CourseContent content : contentStore.values()) {
            if (content.getCourse().getId().equals(courseId)) {
                contentList.add(content);
            }
        }
        return contentList;
    }
}
