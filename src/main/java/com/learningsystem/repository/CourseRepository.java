package com.learningsystem.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.learningsystem.entity.Course;
import com.learningsystem.entity.User;

@Repository
public class CourseRepository {
    private final Map<Long, Course> courseStore = new ConcurrentHashMap<>();
    private long currentId = 1;

    public Course save(Course course) {
        if (course.getId() == null) 
            course.setId(currentId++);
        courseStore.put(course.getId(), course);
        return course;
    }

    public Optional<Course> findById(Long id){
        return Optional.ofNullable(courseStore.get(id));
    }

    public List<Course> findAll() {
        return new ArrayList<>(courseStore.values());
    }

    public void deleteById(Long id) {
        courseStore.remove(id);
    }

    public List<Course> findByInstructor(User instructor) {
        return courseStore.values().stream()
                .filter(course -> course.getInstructor().equals(instructor))
                .collect(Collectors.toList());
    }
}
