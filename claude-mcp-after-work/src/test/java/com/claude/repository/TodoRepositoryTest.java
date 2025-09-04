package com.claude.repository;

import com.claude.entity.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    @DisplayName("투두 저장 및 조회 테스트")
    void saveAndFindTodo() {
        // given
        Todo todo = new Todo("Test Title", "Test Description");

        // when
        Todo savedTodo = todoRepository.save(todo);

        // then
        assertNotNull(savedTodo.getId());
        assertEquals("Test Title", savedTodo.getTitle());
        assertEquals("Test Description", savedTodo.getDescription());
        assertEquals(false, savedTodo.getIsDone());
    }

    @Test
    @DisplayName("ID로 투두 조회 테스트")
    void findById() {
        // given
        Todo todo = new Todo("Test Title", "Test Description");
        Todo savedTodo = entityManager.persistAndFlush(todo);

        // when
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());

        // then
        assertTrue(foundTodo.isPresent());
        assertEquals("Test Title", foundTodo.get().getTitle());
        assertEquals("Test Description", foundTodo.get().getDescription());
    }

    @Test
    @DisplayName("전체 투두 조회 테스트")
    void findAll() {
        // given
        Todo todo1 = new Todo("Title 1", "Description 1");
        Todo todo2 = new Todo("Title 2", "Description 2");
        entityManager.persist(todo1);
        entityManager.persist(todo2);
        entityManager.flush();

        // when
        List<Todo> todos = todoRepository.findAll();

        // then
        assertEquals(2, todos.size());
        assertEquals("Title 1", todos.get(0).getTitle());
        assertEquals("Title 2", todos.get(1).getTitle());
    }

    @Test
    @DisplayName("투두 수정 테스트")
    void updateTodo() {
        // given
        Todo todo = new Todo("Original Title", "Original Description");
        Todo savedTodo = entityManager.persistAndFlush(todo);

        // when
        savedTodo.setTitle("Updated Title");
        savedTodo.setDescription("Updated Description");
        savedTodo.setIsDone(true);
        Todo updatedTodo = todoRepository.save(savedTodo);

        // then
        assertEquals("Updated Title", updatedTodo.getTitle());
        assertEquals("Updated Description", updatedTodo.getDescription());
        assertEquals(true, updatedTodo.getIsDone());
    }

    @Test
    @DisplayName("투두 삭제 테스트")
    void deleteTodo() {
        // given
        Todo todo = new Todo("Test Title", "Test Description");
        Todo savedTodo = entityManager.persistAndFlush(todo);
        Long todoId = savedTodo.getId();

        // when
        todoRepository.deleteById(todoId);

        // then
        Optional<Todo> deletedTodo = todoRepository.findById(todoId);
        assertFalse(deletedTodo.isPresent());
    }

    @Test
    @DisplayName("존재 여부 확인 테스트")
    void existsById() {
        // given
        Todo todo = new Todo("Test Title", "Test Description");
        Todo savedTodo = entityManager.persistAndFlush(todo);

        // when & then
        assertTrue(todoRepository.existsById(savedTodo.getId()));
        assertFalse(todoRepository.existsById(999L));
    }
}
