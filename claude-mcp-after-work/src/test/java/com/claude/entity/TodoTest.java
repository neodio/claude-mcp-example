package com.claude.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    @DisplayName("Todo 기본 생성자 테스트")
    void defaultConstructor() {
        // when
        Todo todo = new Todo();

        // then
        assertNull(todo.getId());
        assertNull(todo.getTitle());
        assertNull(todo.getDescription());
        assertNull(todo.getIsDone());
    }

    @Test
    @DisplayName("Todo 매개변수 생성자 테스트")
    void parameterConstructor() {
        // when
        Todo todo = new Todo("Test Title", "Test Description");

        // then
        assertNull(todo.getId()); // ID는 아직 할당되지 않음
        assertEquals("Test Title", todo.getTitle());
        assertEquals("Test Description", todo.getDescription());
        assertEquals(false, todo.getIsDone()); // 기본값 false
    }

    @Test
    @DisplayName("Todo Setter 테스트")
    void setterTest() {
        // given
        Todo todo = new Todo();

        // when
        todo.setId(1L);
        todo.setTitle("Updated Title");
        todo.setDescription("Updated Description");
        todo.setIsDone(true);

        // then
        assertEquals(1L, todo.getId());
        assertEquals("Updated Title", todo.getTitle());
        assertEquals("Updated Description", todo.getDescription());
        assertEquals(true, todo.getIsDone());
    }

    @Test
    @DisplayName("Todo 필드 기본값 테스트")
    void defaultValuesTest() {
        // when
        Todo todo = new Todo("Title", "Description");

        // then
        assertEquals(false, todo.getIsDone()); // isDone 기본값은 false
    }

    @Test
    @DisplayName("Todo null 값 처리 테스트")
    void nullValuesTest() {
        // when
        Todo todo = new Todo(null, null);

        // then
        assertNull(todo.getTitle());
        assertNull(todo.getDescription());
        assertEquals(false, todo.getIsDone()); // isDone은 기본값 유지
    }
}
