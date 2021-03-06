package com.example.todoapp.controller;

import com.example.todoapp.dto.ResponseDTO;
import com.example.todoapp.dto.UserDTO;
import com.example.todoapp.model.UserEntity;
import com.example.todoapp.security.TokenProvider;
import com.example.todoapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword())) // 책에 가입할 때 암호화 하는 부분이 빠져있는데요?...
                    .build();
            // BeanUtils.copyProperties 가 더 짧은데,, userDTO 에서 뭐가 user로 넘어가지를 확인하기 귀찮음?
//            UserEntity user = new UserEntity();
//            BeanUtils.copyProperties(userDTO, user, "token");

            UserEntity registeredUser = userService.create(user);

            UserDTO responseDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .email(registeredUser.getEmail())
                    .username(registeredUser.getUsername())
                    .password(registeredUser.getPassword())
                    .build();

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

        if (user != null) {
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }
        else{
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error("Login Failed.")
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}










