package com.example.todoapp.service;

import com.example.todoapp.model.TodoEntity;
import com.example.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public String testService() {
        // entity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        // entity 저장
        todoRepository.save(entity);
        // entity 검색
        Optional<TodoEntity> byId = todoRepository.findById(entity.getId());
        if (!byId.isPresent()) {
            return null;
        }
        TodoEntity savedEntity = byId.get();

        return savedEntity.getTitle();
    }

    public List<TodoEntity> create(final TodoEntity entity){
        // Validations 커지면 TodoValidator.java로 분리
        validateTodoEntity(entity);

        todoRepository.save(entity);

        log.info("Entity id : {} is saved", entity.getId());


        return todoRepository.findByUserId(entity.getUserId());
    }

    private void validateTodoEntity(final TodoEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null.");
        }

        if (entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> retrieve(final String userId) {
        return todoRepository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity) {
        validateTodoEntity(entity);

        final Optional<TodoEntity> orginal = todoRepository.findById(entity.getId());

        orginal.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            todoRepository.save(todo);
        });

        return retrieve(entity.getUserId());
    }
}
