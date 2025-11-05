package study.todos.domain.todo.service;

import org.springframework.stereotype.Service;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.repository.JpaTodoRepository;

@Service
public class SimpleTodoService implements TodoService{

    private final JpaTodoRepository jpaTodoRepository;

    public SimpleTodoService(JpaTodoRepository jpaTodoRepository){
        this.jpaTodoRepository = jpaTodoRepository;
    }

    @Override
    public SimpleTodoRes saveTodo(SimpleTodoReq req) {

        Todo todo = new Todo(req.userName(), req.title(), req.content());

        jpaTodoRepository.save(todo);

        return new SimpleTodoRes(todo.getUserName(), todo.getTitle(), todo.getContent(), todo.getCreatedAt(), todo.getUpdatedAt());
    }
}
