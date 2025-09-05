package com.claude.repository;

import com.claude.entity.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("TodoRepository 테스트")
class TodoRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private TodoRepository todoRepository;
    
    @Test
    @DisplayName("투두 저장 및 조회")
    void saveTodo_Success() {
        // given
        Todo todo = new Todo("Test Title", "Test Description");
        
        // when
        Todo savedTodo = todoRepository.save(todo);
        
        // then
        assertThat(savedTodo.getId()).isNotNull();
        assertThat(savedTodo.getTitle()).isEqualTo("Test Title");
        assertThat(savedTodo.getDescription()).isEqualTo("Test Description");
        assertThat(savedTodo.getIsDone()).isFalse();
        assertThat(savedTodo.getCreatedAt()).isNotNull();
    }
    
    @Test
    @DisplayName("ID로 투두 조회")
    void findById_Success() {
        // given
        Todo todo = new Todo("Test Title", "Test Description");
        Todo savedTodo = entityManager.persistAndFlush(todo);
        
        // when
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());
        
        // then
        assertThat(foundTodo).isPresent();
        assertThat(foundTodo.get().getTitle()).isEqualTo("Test Title");
        assertThat(foundTodo.get().getDescription()).isEqualTo("Test Description");
    }
    
    @Test
    @DisplayName("존재하지 않는 ID로 투두 조회")
    void findById_NotFound() {
        // when
        Optional<Todo> foundTodo = todoRepository.findById(999L);
        
        // then
        assertThat(foundTodo).isEmpty();
    }
    
    @Test
    @DisplayName("전체 투두 조회")
    void findAll_Success() {
        // given
        Todo todo1 = new Todo("First Todo", "First Description");
        Todo todo2 = new Todo("Second Todo", "Second Description");
        
        entityManager.persistAndFlush(todo1);
        entityManager.persistAndFlush(todo2);
        
        // when
        List<Todo> todos = todoRepository.findAll();
        
        // then
        assertThat(todos).hasSize(2);
        assertThat(todos).extracting(Todo::getTitle)
                .contains("First Todo", "Second Todo");
    }
    
    @Test
    @DisplayName("투두 삭제")
    void deleteTodo_Success() {
        // given
        Todo todo = new Todo("Test Title", "Test Description");
        Todo savedTodo = entityManager.persistAndFlush(todo);
        Long todoId = savedTodo.getId();
        
        // when
        todoRepository.deleteById(todoId);
        entityManager.flush();
        
        // then
        Optional<Todo> deletedTodo = todoRepository.findById(todoId);
        assertThat(deletedTodo).isEmpty();
    }
    
    @Test
    @DisplayName("투두 수정")
    void updateTodo_Success() {
        // given
        Todo todo = new Todo("Original Title", "Original Description");
        Todo savedTodo = entityManager.persistAndFlush(todo);
        
        // when
        savedTodo.setTitle("Updated Title");
        savedTodo.setDescription("Updated Description");
        savedTodo.setIsDone(true);
        Todo updatedTodo = todoRepository.save(savedTodo);
        
        // then
        assertThat(updatedTodo.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedTodo.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedTodo.getIsDone()).isTrue();
    }
    
    @Test
    @DisplayName("투두 존재 여부 확인")
    void existsById_Success() {
        // given
        Todo todo = new Todo("Test Title", "Test Description");
        Todo savedTodo = entityManager.persistAndFlush(todo);
        
        // when
        boolean exists = todoRepository.existsById(savedTodo.getId());
        boolean notExists = todoRepository.existsById(999L);
        
        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
