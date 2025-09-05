package com.claude.dto;

import com.claude.entity.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Todo DTO 테스트")
class TodoDtoTest {
    
    @Test
    @DisplayName("TodoCreateDto 테스트")
    void todoCreateDto_Test() {
        // when
        TodoCreateDto dto = new TodoCreateDto("Test Title", "Test Description");
        
        // then
        assertThat(dto.getTitle()).isEqualTo("Test Title");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
    }
    
    @Test
    @DisplayName("TodoUpdateDto 테스트")
    void todoUpdateDto_Test() {
        // when
        TodoUpdateDto dto = new TodoUpdateDto("Updated Title", "Updated Description", true);
        
        // then
        assertThat(dto.getTitle()).isEqualTo("Updated Title");
        assertThat(dto.getDescription()).isEqualTo("Updated Description");
        assertThat(dto.getIsDone()).isTrue();
    }
    
    @Test
    @DisplayName("TodoResponseDto Entity 생성자 테스트")
    void todoResponseDto_FromEntity_Test() {
        // given
        Todo todo = new Todo("Test Title", "Test Description");
        todo.setId(1L);
        todo.setIsDone(true);
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 12, 0);
        todo.setCreatedAt(createdAt);
        
        // when
        TodoResponseDto dto = new TodoResponseDto(todo);
        
        // then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("Test Title");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getIsDone()).isTrue();
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
    }
    
    @Test
    @DisplayName("TodoResponseDto 일반 생성자 테스트")
    void todoResponseDto_Constructor_Test() {
        // given
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 12, 0);
        
        // when
        TodoResponseDto dto = new TodoResponseDto(1L, "Test Title", "Test Description", false, createdAt);
        
        // then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("Test Title");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getIsDone()).isFalse();
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
    }
}
