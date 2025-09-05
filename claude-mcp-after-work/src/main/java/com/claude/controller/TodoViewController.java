package com.claude.controller;

import com.claude.dto.TodoCreateDto;
import com.claude.dto.TodoResponseDto;
import com.claude.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class TodoViewController {
    
    private final TodoService todoService;
    
    @Autowired
    public TodoViewController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    // 투두 목록 페이지
    @GetMapping
    public String todoList(Model model) {
        List<TodoResponseDto> todos = todoService.getAllTodos();
        model.addAttribute("todos", todos);
        return "todos/list";
    }
    
    // 투두 등록 폼 페이지
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("todoCreateDto", new TodoCreateDto());
        return "todos/create";
    }
    
    // 투두 등록 처리
    @PostMapping("/create")
    public String createTodo(@ModelAttribute TodoCreateDto todoCreateDto, 
                           RedirectAttributes redirectAttributes) {
        try {
            todoService.createTodo(todoCreateDto);
            redirectAttributes.addFlashAttribute("message", "투두가 성공적으로 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/todos/create";
        }
        return "redirect:/todos";
    }
    
    // 투두 완료 상태 토글
    @PostMapping("/{id}/toggle")
    public String toggleTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            TodoResponseDto todo = todoService.getTodoById(id);
            
            // isDone 상태를 반전
            com.claude.dto.TodoUpdateDto updateDto = new com.claude.dto.TodoUpdateDto();
            updateDto.setTitle(todo.getTitle());
            updateDto.setDescription(todo.getDescription());
            updateDto.setIsDone(!todo.getIsDone());
            
            todoService.updateTodo(id, updateDto);
            redirectAttributes.addFlashAttribute("message", "투두 상태가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/todos";
    }
    
    // 투두 삭제
    @PostMapping("/{id}/delete")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            todoService.deleteTodo(id);
            redirectAttributes.addFlashAttribute("message", "투두가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/todos";
    }
    
    // 루트 경로에서 투두 목록으로 리다이렉트
    @GetMapping("/")
    public String home() {
        return "redirect:/todos";
    }
}
