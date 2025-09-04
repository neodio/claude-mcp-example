package com.claude.integration;

import com.claude.dto.TodoCreateRequest;
import com.claude.dto.TodoUpdateRequest;
import com.claude.entity.Todo;
import com.claude.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TodoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    @DisplayName("투두 CRUD 전체 플로우 테스트")
    void todoFullCrudFlow() throws Exception {
        // 1. 투두 생성
        TodoCreateRequest createRequest = new TodoCreateRequest("Integration Test Todo", "Test Description");
        
        String response = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Todo"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.isDone").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 생성된 투두의 ID 추출
        Long todoId = objectMapper.readTree(response).get("id").asLong();

        // 2. 단건 조회
        mockMvc.perform(get("/api/todos/" + todoId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value("Integration Test Todo"));

        // 3. 전체 조회
        mockMvc.perform(get("/api/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(todoId));

        // 4. 투두 수정
        TodoUpdateRequest updateRequest = new TodoUpdateRequest("Updated Title", "Updated Description", true);
        
        mockMvc.perform(put("/api/todos/" + todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.isDone").value(true));

        // 5. 투두 삭제
        mockMvc.perform(delete("/api/todos/" + todoId))
                .andDo(print())
                .andExpect(status().isNoContent());

        // 6. 삭제 확인
        mockMvc.perform(get("/api/todos/" + todoId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("여러 투두 생성 및 조회 테스트")
    void multipleTodosTest() throws Exception {
        // given - 여러 투두 생성
        for (int i = 1; i <= 3; i++) {
            TodoCreateRequest createRequest = new TodoCreateRequest("Todo " + i, "Description " + i);
            mockMvc.perform(post("/api/todos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated());
        }

        // when & then - 전체 조회
        mockMvc.perform(get("/api/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value("Todo 1"))
                .andExpect(jsonPath("$[1].title").value("Todo 2"))
                .andExpect(jsonPath("$[2].title").value("Todo 3"));
    }

    @Test
    @DisplayName("데이터베이스 직접 접근 테스트")
    void databaseDirectAccessTest() throws Exception {
        // given - Repository를 통해 직접 투두 생성
        Todo todo = new Todo("Database Test Todo", "Test Description");
        Todo savedTodo = todoRepository.save(todo);

        // when & then - API를 통해 조회
        mockMvc.perform(get("/api/todos/" + savedTodo.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTodo.getId()))
                .andExpect(jsonPath("$.title").value("Database Test Todo"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.isDone").value(false));
    }
}
