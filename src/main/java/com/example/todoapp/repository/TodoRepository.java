package com.example.todoapp.repository;

import com.example.todoapp.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    // p131 오타 발견 Long -> String,, 원래는 Long으로 하셨나 봄
    List<TodoEntity> findAllByUserId(String userId);
}
