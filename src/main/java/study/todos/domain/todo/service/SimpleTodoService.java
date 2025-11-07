package study.todos.domain.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.dto.UpdateTodoReq;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.repository.JpaTodoRepository;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class SimpleTodoService implements TodoService{

    private final JpaTodoRepository jpaTodoRepository;

    public SimpleTodoService(JpaTodoRepository jpaTodoRepository){
        this.jpaTodoRepository = jpaTodoRepository;
    }

    @Override
    @Transactional
    public SimpleTodoRes saveTodo(SimpleTodoReq req) {

        Todo todo = new Todo(req.userName(), req.title(), req.content());

        Todo saveTodo = jpaTodoRepository.save(todo);

        return new SimpleTodoRes(saveTodo.getUserName(), saveTodo.getTitle(), saveTodo.getContent(), saveTodo.getCreatedAt(), saveTodo.getUpdatedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public SimpleTodoRes findTodo(Long id) {

        Todo foundTodo = jpaTodoRepository.findById(id)
                .orElseThrow(() -> new TodoException(TodoErrorCode.NOT_FOUND));

        return new SimpleTodoRes(foundTodo.getUserName(), foundTodo.getTitle(), foundTodo.getContent(), foundTodo.getCreatedAt(), foundTodo .getUpdatedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimpleTodoRes> findTodos(Pageable pageable) {

        Page<Todo> todos = jpaTodoRepository.findAll(pageable);

        List<SimpleTodoRes> responses = todos.getContent().stream().map(SimpleTodoRes::toDto).toList();

        return new PageImpl<>( responses, pageable, todos.getTotalElements());
    }

    @Override
    @Transactional
    public SimpleTodoRes updateTodo(Long todoId, UpdateTodoReq req){

        Todo foundTodo = jpaTodoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TodoErrorCode.NOT_FOUND));

        foundTodo.updateTodo(req);

        return new SimpleTodoRes(foundTodo.getUserName(), foundTodo.getTitle(), foundTodo.getContent(), foundTodo.getCreatedAt(), foundTodo .getUpdatedAt());
    }

    @Override
    @Transactional
    public Map<String, String> deleteTodo(Long todoId) {

        Todo foundTodo = jpaTodoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TodoErrorCode.NOT_FOUND));

        jpaTodoRepository.delete(foundTodo);

        return Map.of("message" , "일정이 삭제되었습니다.");
    }

}
