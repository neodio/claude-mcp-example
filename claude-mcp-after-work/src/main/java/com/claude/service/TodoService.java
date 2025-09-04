package com.claude.service;

import com.claude.dto.TodoCreateRequest;
import com.claude.dto.TodoResponse;
import com.claude.dto.TodoUpdateRequest;
import com.claude.entity.Todo;
import com.claude.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {
    
    private final TodoRepository todoRepository;
    
    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    
    // 투두 생성
    public TodoResponse createTodo(TodoCreateRequest request) {
        Todo todo = new Todo(request.getTitle(), request.getDescription());
        Todo savedTodo = todoRepository.save(todo);
        return new TodoResponse(savedTodo);
    }
    
    // 전체 투두 조회
    public List<TodoResponse> getAllTodos() {
        return todoRepository.findAll()
                .stream()
                .map(TodoResponse::new)
                .collect(Collectors.toList());
    }
    
    // 단건 투두 조회
    public TodoResponse getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        return new TodoResponse(todo);
    }
    
    // 투두 수정
    public TodoResponse updateTodo(Long id, TodoUpdateRequest request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        if (request.getIsDone() != null) {
            todo.setIsDone(request.getIsDone());
        }
        
        Todo updatedTodo = todoRepository.save(todo);
        return new TodoResponse(updatedTodo);
    }
    
    // 투두 삭제
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }
}
