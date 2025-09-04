package com.claude.dto;

import com.claude.entity.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoResponseTest {

    @Test
    @DisplayName("TodoResponse 기본 생성자 테스트")
    void defaultConstructor() {
        // when
        TodoResponse response = new TodoResponse();

        // then
        assertNull(response.getId());
        assertNull(response.getTitle());
        assertNull(response.getDescription());
        assertNull(response.getIsDone());
    }

    @Test
    @DisplayName("TodoResponse 매개변수 생성자 테스트")
    void parameterConstructor() {
        // when
        TodoResponse response = new TodoResponse(1L, "Test Title", "Test Description", false);

        // then
        assertEquals(1L, response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Description", response.getDescription());
        assertEquals(false, response.getIsDone());
    }

    @Test
    @DisplayName("TodoResponse Entity 생성자 테스트")
    void entityConstructor() {
        // given
        Todo todo = new Todo("Entity Title", "Entity Description");
        todo.setId(1L);
        todo.setIsDone(true);

        // when
        TodoResponse response = new TodoResponse(todo);

        // then
        assertEquals(1L, response.getId());
        assertEquals("Entity Title", response.getTitle());
        assertEquals("Entity Description", response.getDescription());
        assertEquals(true, response.getIsDone());
    }

    @Test
    @DisplayName("TodoResponse Setter 테스트")
    void setterTest() {
        // given
        TodoResponse response = new TodoResponse();

        // when
        response.setId(2L);
        response.setTitle("Setter Title");
        response.setDescription("Setter Description");
        response.setIsDone(true);

        // then
        assertEquals(2L, response.getId());
        assertEquals("Setter Title", response.getTitle());
        assertEquals("Setter Description", response.getDescription());
        assertEquals(true, response.getIsDone());
    }

    @Test
    @DisplayName("TodoResponse null Entity 처리 테스트")
    void nullEntityTest() {
        // given
        Todo todo = new Todo(null, null);
        todo.setId(null);
        todo.setIsDone(null);

        // when
        TodoResponse response = new TodoResponse(todo);

        // then
        assertNull(response.getId());
        assertNull(response.getTitle());
        assertNull(response.getDescription());
        assertNull(response.getIsDone());
    }
}
