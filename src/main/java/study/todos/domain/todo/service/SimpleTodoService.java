package study.todos.domain.todo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.repository.JpaTodoRepository;

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
    public SimpleTodoRes findTodo(Long id) {

        Todo foundTodo = jpaTodoRepository.findById(id)
                .orElseThrow(() -> new TodoException(TodoErrorCode.NOT_FOUND));

        return new SimpleTodoRes(foundTodo.getUserName(), foundTodo.getTitle(), foundTodo.getContent(), foundTodo.getCreatedAt(), foundTodo .getUpdatedAt());
    }


}
