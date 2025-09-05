package com.claude.service;

import com.claude.dto.TodoCreateDto;
import com.claude.dto.TodoResponseDto;
import com.claude.dto.TodoUpdateDto;
import com.claude.entity.Todo;
import com.claude.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {
    
    private final TodoRepository todoRepository;
    
    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    
    // 투두 생성
    public TodoResponseDto createTodo(TodoCreateDto createDto) {
        if (createDto.getTitle() == null || createDto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        
        Todo todo = new Todo(createDto.getTitle(), createDto.getDescription());
        Todo savedTodo = todoRepository.save(todo);
        return new TodoResponseDto(savedTodo);
    }
    
    // 전체 투두 조회
    public List<TodoResponseDto> getAllTodos() {
        return todoRepository.findAll()
                .stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }
    
    // 단건 투두 조회
    public TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + id));
        return new TodoResponseDto(todo);
    }
    
    // 투두 수정
    public TodoResponseDto updateTodo(Long id, TodoUpdateDto updateDto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + id));
        
        if (updateDto.getTitle() != null && !updateDto.getTitle().trim().isEmpty()) {
            todo.setTitle(updateDto.getTitle());
        }
        
        if (updateDto.getDescription() != null) {
            todo.setDescription(updateDto.getDescription());
        }
        
        if (updateDto.getIsDone() != null) {
            todo.setIsDone(updateDto.getIsDone());
        }
        
        Todo updatedTodo = todoRepository.save(todo);
        return new TodoResponseDto(updatedTodo);
    }
    
    // 투두 삭제
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new IllegalArgumentException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }
}
