package com.learningsystem.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.learningsystem.entity.Course;
import com.learningsystem.entity.Lesson;

@Repository
public class LessonRepository {
    private final Map<Long, Lesson> lessonStore = new ConcurrentHashMap<>();
    private Long lessonIdCounter = 1L;

    public Lesson save(Lesson lesson) {
        if (lesson.getId() == null) 
            lesson.setId(lessonIdCounter++);
        lessonStore.put(lesson.getId(), lesson);
        return lesson;
    }

    public Optional<Lesson> findById(Long id) {
        return Optional.ofNullable(lessonStore.get(id));
    }

    public List<Lesson> findByCourse(Course course) {
        List<Lesson> lessons = new ArrayList<>();
        for (Lesson lesson : lessonStore.values())
            if (lesson.getCourse().equals(course)) 
                lessons.add(lesson);
        return lessons;
    }

    public Map<Long, Lesson> findAll() {
        return lessonStore;
    }

    public void deleteById(Long id) {
        lessonStore.remove(id);
    }
}
