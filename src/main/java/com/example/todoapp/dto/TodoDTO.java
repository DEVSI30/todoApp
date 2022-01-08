package com.example.todoapp.dto;

import com.example.todoapp.model.TodoEntity;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * Model -> DTO 로 하는 이유
 * 비즈니스 로직을 캡슐화 하기 위함
 * 클라이언트가 필요한 정보를 모델이 전부 포함하지 않는 경우가 많기 떄문
 * 서비스 로직과 관련된 내용을 담기 위해 (ex: errorMessage)
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
    private String id;
    private String title;
    private boolean done;

    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    // DTO -> Entity
    public static TodoEntity toEntity(final TodoDTO dto) {
        TodoEntity newEntity = new TodoEntity();
        BeanUtils.copyProperties(dto, newEntity);
        return newEntity;
    }
}
