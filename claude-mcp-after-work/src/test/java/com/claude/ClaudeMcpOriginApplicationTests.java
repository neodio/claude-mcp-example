package com.claude;

import com.claude.dto.TodoCreateDto;
import com.claude.dto.TodoResponseDto;
import com.claude.dto.TodoUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("통합 테스트")
class ClaudeMcpOriginApplicationTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("Spring Context 로드 테스트")
    void contextLoads() {
        // Spring Boot 애플리케이션이 정상적으로 로드되는지 확인
    }
    
    @Test
    @DisplayName("투두 전체 플로우 통합 테스트")
    void todoFullFlow_IntegrationTest() throws Exception {
        // 1. 투두 생성
        TodoCreateDto createDto = new TodoCreateDto("Integration Test Todo", "Integration Test Description");
        
        MvcResult createResult = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Todo"))
                .andExpect(jsonPath("$.description").value("Integration Test Description"))
                .andExpect(jsonPath("$.isDone").value(false))
                .andExpect(jsonPath("$.createdAt").exists())
                .andReturn();
        
        String createResponse = createResult.getResponse().getContentAsString();
        TodoResponseDto createdTodo = objectMapper.readValue(createResponse, TodoResponseDto.class);
        Long todoId = createdTodo.getId();
        
        // 2. 생성된 투두 단건 조회
        mockMvc.perform(get("/api/todos/" + todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value("Integration Test Todo"));
        
        // 3. 전체 투두 목록 조회 (최소 1개 이상 존재)
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(1)));
        
        // 4. 투두 수정
        TodoUpdateDto updateDto = new TodoUpdateDto("Updated Integration Test", "Updated Description", true);
        
        mockMvc.perform(put("/api/todos/" + todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value("Updated Integration Test"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.isDone").value(true));
        
        // 5. 투두 삭제
        mockMvc.perform(delete("/api/todos/" + todoId))
                .andExpect(status().isNoContent());
        
        // 6. 삭제된 투두 조회 시 404 확인
        mockMvc.perform(get("/api/todos/" + todoId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("유효하지 않은 요청 테스트")
    void invalidRequest_Test() throws Exception {
        // 1. 제목 없이 투두 생성 시도
        TodoCreateDto invalidCreateDto = new TodoCreateDto(null, "Description without title");
        
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateDto)))
                .andExpect(status().isBadRequest());
        
        // 2. 존재하지 않는 투두 조회
        mockMvc.perform(get("/api/todos/999999"))
                .andExpect(status().isNotFound());
        
        // 3. 존재하지 않는 투두 수정
        TodoUpdateDto updateDto = new TodoUpdateDto("Title", "Description", true);
        
        mockMvc.perform(put("/api/todos/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
        
        // 4. 존재하지 않는 투두 삭제
        mockMvc.perform(delete("/api/todos/999999"))
                .andExpect(status().isNotFound());
    }
}
