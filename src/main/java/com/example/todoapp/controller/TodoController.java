package com.example.todoapp.controller;

import com.example.todoapp.dto.ResponseDTO;
import com.example.todoapp.dto.TodoDTO;
import com.example.todoapp.model.TodoEntity;
import com.example.todoapp.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * todo : 실습코드 2-20 과 같이 TodoController를 작성해보자.
 * 스스로 ResponseEntity를 리턴하는 HTTP GET testTodo() 메서드를 작성하자
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("todo")
public class TodoController {

    private final TodoService todoService;

    /**
     * TodoList item 목록 구하기
     * @param userId 사용자의 ID
     * @return 사용자의 Todoitem 을 리턴
     */
    @GetMapping("/testTodo/{userId}")
    public ResponseEntity<ResponseDTO<TodoDTO>> testTodo(@PathVariable String userId){

        TodoDTO tempTodoEntity = TodoDTO.builder().id(userId + "_1").title("test").done(false).build();
        TodoDTO tempTodoEntity2 = TodoDTO.builder().id(userId + "_2").title("test2").done(false).build();

        List<TodoDTO> todoList = Arrays.asList(tempTodoEntity, tempTodoEntity2);

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(todoList).build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/testService")
    public ResponseEntity<ResponseDTO<String>> testService() {
        String serviceResult = todoService.testService();

        List<String> stringList = new ArrayList<>();
        stringList.add(serviceResult);

        ResponseDTO<String> response = ResponseDTO.<String>builder().data(stringList).build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";

            TodoEntity entity = TodoDTO.toEntity(dto);

            // id를 null 로 초기화 한다. 생성 당시에는 id 가 없어야 하기 때문이다. (자동으로 채워주나?_
            entity.setId(null);

            // 4장에서 인증과 인가에서 수정할 예정
            entity.setUserId(temporaryUserId);

            List<TodoEntity> entities = todoService.create(entity);

            // 자바 스트림을 이용해 엔티티 리스트를 TodoDTO 리스트로 변환한다.

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response); //badrequest라는 보장이 없는데?
        }
    }
}