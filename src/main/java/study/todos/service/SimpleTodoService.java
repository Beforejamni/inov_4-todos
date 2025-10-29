package study.todos.service;

import org.springframework.stereotype.Service;
import study.todos.Todo;
import study.todos.repository.TodoRepository;

@Service
public class SimpleTodoService implements TodoService{

    private final TodoRepository todoRepository;

    public SimpleTodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Todo saveTodo(CreateTodoReq req) {
        Todo todo = Todo.createTodo(req.userName(), req.title(), req.content());

        todoRepository.save(todo);

        return todo;
    }
}
