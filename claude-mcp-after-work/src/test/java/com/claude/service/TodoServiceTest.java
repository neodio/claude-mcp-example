package com.claude.service;

import com.claude.dto.TodoCreateDto;
import com.claude.dto.TodoResponseDto;
import com.claude.dto.TodoUpdateDto;
import com.claude.entity.Todo;
import com.claude.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoService 테스트")
class TodoServiceTest {
    
    @Mock
    private TodoRepository todoRepository;
    
    @InjectMocks
    private TodoService todoService;
    
    private Todo testTodo;
    private TodoCreateDto createDto;
    private TodoUpdateDto updateDto;
    
    @BeforeEach
    void setUp() {
        testTodo = new Todo("Test Title", "Test Description");
        testTodo.setId(1L);
        
        createDto = new TodoCreateDto("New Todo", "New Description");
        updateDto = new TodoUpdateDto("Updated Title", "Updated Description", true);
    }
    
    @Test
    @DisplayName("투두 생성 성공")
    void createTodo_Success() {
        // given
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);
        
        // when
        TodoResponseDto result = todoService.createTodo(createDto);
        
        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getIsDone()).isFalse();
        
        verify(todoRepository).save(any(Todo.class));
    }
    
    @Test
    @DisplayName("투두 생성 실패 - 제목이 null")
    void createTodo_Fail_TitleIsNull() {
        // given
        createDto.setTitle(null);
        
        // when & then
        assertThatThrownBy(() -> todoService.createTodo(createDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title is required");
        
        verify(todoRepository, never()).save(any(Todo.class));
    }
    
    @Test
    @DisplayName("투두 생성 실패 - 제목이 빈 문자열")
    void createTodo_Fail_TitleIsEmpty() {
        // given
        createDto.setTitle("");
        
        // when & then
        assertThatThrownBy(() -> todoService.createTodo(createDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title is required");
        
        verify(todoRepository, never()).save(any(Todo.class));
    }
    
    @Test
    @DisplayName("전체 투두 조회 성공")
    void getAllTodos_Success() {
        // given
        Todo todo2 = new Todo("Second Todo", "Second Description");
        todo2.setId(2L);
        
        List<Todo> todos = Arrays.asList(testTodo, todo2);
        when(todoRepository.findAll()).thenReturn(todos);
        
        // when
        List<TodoResponseDto> result = todoService.getAllTodos();
        
        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Title");
        assertThat(result.get(1).getTitle()).isEqualTo("Second Todo");
        
        verify(todoRepository).findAll();
    }
    
    @Test
    @DisplayName("단건 투두 조회 성공")
    void getTodoById_Success() {
        // given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
        
        // when
        TodoResponseDto result = todoService.getTodoById(1L);
        
        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Title");
        
        verify(todoRepository).findById(1L);
    }
    
    @Test
    @DisplayName("단건 투두 조회 실패 - 존재하지 않는 ID")
    void getTodoById_Fail_NotFound() {
        // given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());
        
        // when & then
        assertThatThrownBy(() -> todoService.getTodoById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todo not found with id: 999");
        
        verify(todoRepository).findById(999L);
    }
    
    @Test
    @DisplayName("투두 수정 성공")
    void updateTodo_Success() {
        // given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);
        
        // when
        TodoResponseDto result = todoService.updateTodo(1L, updateDto);
        
        // then
        assertThat(result).isNotNull();
        verify(todoRepository).findById(1L);
        verify(todoRepository).save(testTodo);
    }
    
    @Test
    @DisplayName("투두 수정 실패 - 존재하지 않는 ID")
    void updateTodo_Fail_NotFound() {
        // given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());
        
        // when & then
        assertThatThrownBy(() -> todoService.updateTodo(999L, updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todo not found with id: 999");
        
        verify(todoRepository).findById(999L);
        verify(todoRepository, never()).save(any(Todo.class));
    }
    
    @Test
    @DisplayName("투두 삭제 성공")
    void deleteTodo_Success() {
        // given
        when(todoRepository.existsById(1L)).thenReturn(true);
        
        // when
        todoService.deleteTodo(1L);
        
        // then
        verify(todoRepository).existsById(1L);
        verify(todoRepository).deleteById(1L);
    }
    
    @Test
    @DisplayName("투두 삭제 실패 - 존재하지 않는 ID")
    void deleteTodo_Fail_NotFound() {
        // given
        when(todoRepository.existsById(999L)).thenReturn(false);
        
        // when & then
        assertThatThrownBy(() -> todoService.deleteTodo(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todo not found with id: 999");
        
        verify(todoRepository).existsById(999L);
        verify(todoRepository, never()).deleteById(any());
    }
}
