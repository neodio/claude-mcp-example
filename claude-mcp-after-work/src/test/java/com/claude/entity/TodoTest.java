package com.claude.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Todo Entity 테스트")
class TodoTest {
    
    @Test
    @DisplayName("Todo 기본 생성자 테스트")
    void defaultConstructor_Test() {
        // when
        Todo todo = new Todo();
        
        // then
        assertThat(todo.getId()).isNull();
        assertThat(todo.getTitle()).isNull();
        assertThat(todo.getDescription()).isNull();
        assertThat(todo.getIsDone()).isFalse();
        assertThat(todo.getCreatedAt()).isNotNull();
        assertThat(todo.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
    
    @Test
    @DisplayName("Todo 생성자 테스트")
    void constructor_Test() {
        // when
        Todo todo = new Todo("Test Title", "Test Description");
        
        // then
        assertThat(todo.getTitle()).isEqualTo("Test Title");
        assertThat(todo.getDescription()).isEqualTo("Test Description");
        assertThat(todo.getIsDone()).isFalse();
        assertThat(todo.getCreatedAt()).isNotNull();
        assertThat(todo.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
    
    @Test
    @DisplayName("Todo Setter 테스트")
    void setter_Test() {
        // given
        Todo todo = new Todo();
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 12, 0);
        
        // when
        todo.setId(1L);
        todo.setTitle("Test Title");
        todo.setDescription("Test Description");
        todo.setIsDone(true);
        todo.setCreatedAt(createdAt);
        
        // then
        assertThat(todo.getId()).isEqualTo(1L);
        assertThat(todo.getTitle()).isEqualTo("Test Title");
        assertThat(todo.getDescription()).isEqualTo("Test Description");
        assertThat(todo.getIsDone()).isTrue();
        assertThat(todo.getCreatedAt()).isEqualTo(createdAt);
    }
    
    @Test
    @DisplayName("생성일자 자동 설정 테스트")
    void createdAt_AutoSetting_Test() throws InterruptedException {
        // given
        LocalDateTime before = LocalDateTime.now();
        Thread.sleep(1); // 시간 차이를 만들기 위해
        
        // when
        Todo todo = new Todo("Test Title", "Test Description");
        
        // then
        assertThat(todo.getCreatedAt()).isAfter(before);
        assertThat(todo.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}
