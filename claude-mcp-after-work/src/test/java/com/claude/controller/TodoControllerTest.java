package com.claude.controller;

import com.claude.dto.TodoCreateDto;
import com.claude.dto.TodoResponseDto;
import com.claude.dto.TodoUpdateDto;
import com.claude.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoController 테스트")
class TodoControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private TodoService todoService;
    
    @InjectMocks
    private TodoController todoController;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }
    
    private TodoResponseDto createTodoResponseDto(Long id, String title, String description, Boolean isDone) {
        TodoResponseDto dto = new TodoResponseDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setIsDone(isDone);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
    
    @Test
    @DisplayName("투두 생성 성공")
    void createTodo_Success() throws Exception {
        // given
        TodoCreateDto createDto = new TodoCreateDto("Test Title", "Test Description");
        TodoResponseDto responseDto = createTodoResponseDto(1L, "Test Title", "Test Description", false);
        
        when(todoService.createTodo(any(TodoCreateDto.class))).thenReturn(responseDto);
        
        // when & then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.isDone").value(false));
        
        verify(todoService).createTodo(any(TodoCreateDto.class));
    }
    
    @Test
    @DisplayName("투두 생성 실패 - 제목 누락")
    void createTodo_Fail_TitleRequired() throws Exception {
        // given
        TodoCreateDto createDto = new TodoCreateDto(null, "Test Description");
        
        when(todoService.createTodo(any(TodoCreateDto.class)))
                .thenThrow(new IllegalArgumentException("Title is required"));
        
        // when & then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
        
        verify(todoService).createTodo(any(TodoCreateDto.class));
    }
    
    @Test
    @DisplayName("전체 투두 조회 성공")
    void getAllTodos_Success() throws Exception {
        // given
        List<TodoResponseDto> todos = Arrays.asList(
                createTodoResponseDto(1L, "First Todo", "First Description", false),
                createTodoResponseDto(2L, "Second Todo", "Second Description", true)
        );
        
        when(todoService.getAllTodos()).thenReturn(todos);
        
        // when & then
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("First Todo"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Second Todo"));
        
        verify(todoService).getAllTodos();
    }
    
    @Test
    @DisplayName("단건 투두 조회 성공")
    void getTodoById_Success() throws Exception {
        // given
        TodoResponseDto responseDto = createTodoResponseDto(1L, "Test Title", "Test Description", false);
        
        when(todoService.getTodoById(1L)).thenReturn(responseDto);
        
        // when & then
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.isDone").value(false));
        
        verify(todoService).getTodoById(1L);
    }
    
    @Test
    @DisplayName("단건 투두 조회 실패 - 존재하지 않는 ID")
    void getTodoById_Fail_NotFound() throws Exception {
        // given
        when(todoService.getTodoById(999L))
                .thenThrow(new IllegalArgumentException("Todo not found with id: 999"));
        
        // when & then
        mockMvc.perform(get("/api/todos/999"))
                .andExpect(status().isNotFound());
        
        verify(todoService).getTodoById(999L);
    }
    
    @Test
    @DisplayName("투두 수정 성공")
    void updateTodo_Success() throws Exception {
        // given
        TodoUpdateDto updateDto = new TodoUpdateDto("Updated Title", "Updated Description", true);
        TodoResponseDto responseDto = createTodoResponseDto(1L, "Updated Title", "Updated Description", true);
        
        when(todoService.updateTodo(eq(1L), any(TodoUpdateDto.class))).thenReturn(responseDto);
        
        // when & then
        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.isDone").value(true));
        
        verify(todoService).updateTodo(eq(1L), any(TodoUpdateDto.class));
    }
    
    @Test
    @DisplayName("투두 수정 실패 - 존재하지 않는 ID")
    void updateTodo_Fail_NotFound() throws Exception {
        // given
        TodoUpdateDto updateDto = new TodoUpdateDto("Updated Title", "Updated Description", true);
        
        when(todoService.updateTodo(eq(999L), any(TodoUpdateDto.class)))
                .thenThrow(new IllegalArgumentException("Todo not found with id: 999"));
        
        // when & then
        mockMvc.perform(put("/api/todos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
        
        verify(todoService).updateTodo(eq(999L), any(TodoUpdateDto.class));
    }
    
    @Test
    @DisplayName("투두 삭제 성공")
    void deleteTodo_Success() throws Exception {
        // given
        doNothing().when(todoService).deleteTodo(1L);
        
        // when & then
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
        
        verify(todoService).deleteTodo(1L);
    }
    
    @Test
    @DisplayName("투두 삭제 실패 - 존재하지 않는 ID")
    void deleteTodo_Fail_NotFound() throws Exception {
        // given
        doThrow(new IllegalArgumentException("Todo not found with id: 999"))
                .when(todoService).deleteTodo(999L);
        
        // when & then
        mockMvc.perform(delete("/api/todos/999"))
                .andExpect(status().isNotFound());
        
        verify(todoService).deleteTodo(999L);
    }
}
