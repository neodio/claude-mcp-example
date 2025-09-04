package com.claude.controller;

import com.claude.dto.TodoCreateRequest;
import com.claude.dto.TodoResponse;
import com.claude.dto.TodoUpdateRequest;
import com.claude.exception.GlobalExceptionHandler;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    private ObjectMapper objectMapper;
    private TodoResponse mockResponse;
    private TodoCreateRequest createRequest;
    private TodoUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        // GlobalExceptionHandler를 포함하여 MockMvc 설정
        mockMvc = MockMvcBuilders.standaloneSetup(todoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        
        mockResponse = new TodoResponse(1L, "Test Title", "Test Description", false);
        createRequest = new TodoCreateRequest("New Todo", "New Description");
        updateRequest = new TodoUpdateRequest("Updated Title", "Updated Description", true);
    }

    @Test
    @DisplayName("POST /api/todos - 투두 생성 성공")
    void createTodo_Success() throws Exception {
        // given
        when(todoService.createTodo(any(TodoCreateRequest.class))).thenReturn(mockResponse);

        // when & then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.isDone").value(false));

        verify(todoService, times(1)).createTodo(any(TodoCreateRequest.class));
    }

    @Test
    @DisplayName("POST /api/todos - 투두 생성 실패 (제목 누락)")
    void createTodo_ValidationError() throws Exception {
        // given
        TodoCreateRequest invalidRequest = new TodoCreateRequest("", "Description");

        // when & then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());

        verify(todoService, never()).createTodo(any(TodoCreateRequest.class));
    }

    @Test
    @DisplayName("GET /api/todos - 전체 투두 조회 성공")
    void getAllTodos_Success() throws Exception {
        // given
        TodoResponse response1 = new TodoResponse(1L, "Title 1", "Description 1", false);
        TodoResponse response2 = new TodoResponse(2L, "Title 2", "Description 2", true);
        List<TodoResponse> responses = Arrays.asList(response1, response2);
        
        when(todoService.getAllTodos()).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Title 2"));

        verify(todoService, times(1)).getAllTodos();
    }

    @Test
    @DisplayName("GET /api/todos/{id} - 단건 투두 조회 성공")
    void getTodoById_Success() throws Exception {
        // given
        when(todoService.getTodoById(1L)).thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/todos/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(todoService, times(1)).getTodoById(1L);
    }

    @Test
    @DisplayName("GET /api/todos/{id} - 단건 투두 조회 실패 (존재하지 않는 ID)")
    void getTodoById_NotFound() throws Exception {
        // given
        when(todoService.getTodoById(999L)).thenThrow(new RuntimeException("Todo not found with id: 999"));

        // when & then
        mockMvc.perform(get("/api/todos/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Todo not found with id: 999"));

        verify(todoService, times(1)).getTodoById(999L);
    }

    @Test
    @DisplayName("PUT /api/todos/{id} - 투두 수정 성공")
    void updateTodo_Success() throws Exception {
        // given
        TodoResponse updatedResponse = new TodoResponse(1L, "Updated Title", "Updated Description", true);
        when(todoService.updateTodo(eq(1L), any(TodoUpdateRequest.class))).thenReturn(updatedResponse);

        // when & then
        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.isDone").value(true));

        verify(todoService, times(1)).updateTodo(eq(1L), any(TodoUpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /api/todos/{id} - 투두 수정 실패 (제목 누락)")
    void updateTodo_ValidationError() throws Exception {
        // given
        TodoUpdateRequest invalidRequest = new TodoUpdateRequest("", "Description", true);

        // when & then
        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());

        verify(todoService, never()).updateTodo(anyLong(), any(TodoUpdateRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/todos/{id} - 투두 삭제 성공")
    void deleteTodo_Success() throws Exception {
        // given
        doNothing().when(todoService).deleteTodo(1L);

        // when & then
        mockMvc.perform(delete("/api/todos/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).deleteTodo(1L);
    }

    @Test
    @DisplayName("DELETE /api/todos/{id} - 투두 삭제 실패 (존재하지 않는 ID)")
    void deleteTodo_NotFound() throws Exception {
        // given
        doThrow(new RuntimeException("Todo not found with id: 999"))
                .when(todoService).deleteTodo(999L);

        // when & then
        mockMvc.perform(delete("/api/todos/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Todo not found with id: 999"));

        verify(todoService, times(1)).deleteTodo(999L);
    }
}
