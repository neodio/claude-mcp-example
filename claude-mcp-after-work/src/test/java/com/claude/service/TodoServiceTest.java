package com.claude.service;

import com.claude.dto.TodoCreateRequest;
import com.claude.dto.TodoResponse;
import com.claude.dto.TodoUpdateRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo mockTodo;
    private TodoCreateRequest createRequest;
    private TodoUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        mockTodo = new Todo("Test Title", "Test Description");
        mockTodo.setId(1L);
        
        createRequest = new TodoCreateRequest("New Todo", "New Description");
        updateRequest = new TodoUpdateRequest("Updated Title", "Updated Description", true);
    }

    @Test
    @DisplayName("투두 생성 성공")
    void createTodo_Success() {
        // given
        when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo);

        // when
        TodoResponse response = todoService.createTodo(createRequest);

        // then
        assertNotNull(response);
        assertEquals(mockTodo.getId(), response.getId());
        assertEquals(mockTodo.getTitle(), response.getTitle());
        assertEquals(mockTodo.getDescription(), response.getDescription());
        assertEquals(mockTodo.getIsDone(), response.getIsDone());
        
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("전체 투두 조회 성공")
    void getAllTodos_Success() {
        // given
        Todo todo1 = new Todo("Title 1", "Description 1");
        todo1.setId(1L);
        Todo todo2 = new Todo("Title 2", "Description 2");
        todo2.setId(2L);
        
        List<Todo> mockTodos = Arrays.asList(todo1, todo2);
        when(todoRepository.findAll()).thenReturn(mockTodos);

        // when
        List<TodoResponse> responses = todoService.getAllTodos();

        // then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Title 1", responses.get(0).getTitle());
        assertEquals("Title 2", responses.get(1).getTitle());
        
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("ID로 투두 조회 성공")
    void getTodoById_Success() {
        // given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));

        // when
        TodoResponse response = todoService.getTodoById(1L);

        // then
        assertNotNull(response);
        assertEquals(mockTodo.getId(), response.getId());
        assertEquals(mockTodo.getTitle(), response.getTitle());
        
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("ID로 투두 조회 실패 - 존재하지 않는 ID")
    void getTodoById_NotFound() {
        // given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> todoService.getTodoById(999L));
        
        assertEquals("Todo not found with id: 999", exception.getMessage());
        verify(todoRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("투두 수정 성공")
    void updateTodo_Success() {
        // given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo);

        // when
        TodoResponse response = todoService.updateTodo(1L, updateRequest);

        // then
        assertNotNull(response);
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("투두 수정 실패 - 존재하지 않는 ID")
    void updateTodo_NotFound() {
        // given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> todoService.updateTodo(999L, updateRequest));
        
        assertEquals("Todo not found with id: 999", exception.getMessage());
        verify(todoRepository, times(1)).findById(999L);
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("투두 삭제 성공")
    void deleteTodo_Success() {
        // given
        when(todoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(todoRepository).deleteById(1L);

        // when
        assertDoesNotThrow(() -> todoService.deleteTodo(1L));

        // then
        verify(todoRepository, times(1)).existsById(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("투두 삭제 실패 - 존재하지 않는 ID")
    void deleteTodo_NotFound() {
        // given
        when(todoRepository.existsById(anyLong())).thenReturn(false);

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> todoService.deleteTodo(999L));
        
        assertEquals("Todo not found with id: 999", exception.getMessage());
        verify(todoRepository, times(1)).existsById(999L);
        verify(todoRepository, never()).deleteById(anyLong());
    }
}
