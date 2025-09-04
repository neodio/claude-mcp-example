package com.claude.controller;

import com.claude.dto.TodoCreateRequest;
import com.claude.dto.TodoResponse;
import com.claude.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class TodoWebController {

    private final TodoService todoService;

    @Autowired
    public TodoWebController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 투두 목록 페이지
    @GetMapping
    public String list(Model model) {
        List<TodoResponse> todos = todoService.getAllTodos();
        model.addAttribute("todos", todos);
        return "todos/list";
    }

    // 투두 등록 폼 페이지
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("todoCreateRequest", new TodoCreateRequest());
        return "todos/create";
    }

    // 투두 등록 처리
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute TodoCreateRequest todoCreateRequest,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "todos/create";
        }
        
        try {
            todoService.createTodo(todoCreateRequest);
            redirectAttributes.addFlashAttribute("message", "투두가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "투두 등록 중 오류가 발생했습니다.");
        }
        
        return "redirect:/todos";
    }

    // 투두 완료 상태 토글
    @PostMapping("/{id}/toggle")
    public String toggleComplete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            TodoResponse todo = todoService.getTodoById(id);
            // isDone 상태를 반대로 변경
            todoService.updateTodo(id, new com.claude.dto.TodoUpdateRequest(
                    todo.getTitle(), 
                    todo.getDescription(), 
                    !todo.getIsDone()
            ));
            redirectAttributes.addFlashAttribute("message", "투두 상태가 변경되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "투두 상태 변경 중 오류가 발생했습니다.");
        }
        
        return "redirect:/todos";
    }

    // 투두 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            todoService.deleteTodo(id);
            redirectAttributes.addFlashAttribute("message", "투두가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "투두 삭제 중 오류가 발생했습니다.");
        }
        
        return "redirect:/todos";
    }

    // 메인 페이지에서 투두 목록으로 리다이렉트
    @GetMapping("/")
    public String home() {
        return "redirect:/todos";
    }
}

@Controller
class HomeController {
    
    @GetMapping("/")
    public String index() {
        return "redirect:/todos";
    }
}
