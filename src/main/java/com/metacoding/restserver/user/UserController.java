package com.metacoding.restserver.user;

import com.metacoding.restserver._core.auth.LoginUser;
import com.metacoding.restserver._core.util.Resp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO reqDTO, Errors errors) {
        LoginUser loginUser = userService.로그인(reqDTO);

        // 세션에 저장안한다. stateless 서버라서.
        return  ResponseEntity.ok()
                .header("Authorization", loginUser.getJwt())
                .body(Resp.success("loginUser"));
    }

}