package cnsa.demo.controller.user;

import cnsa.demo.DTO.ResponseDTO;
import cnsa.demo.DTO.User.UserDTO;
import cnsa.demo.DTO.User.UserIdDTO;
import cnsa.demo.service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseDTO saveUserInfo(@RequestBody UserDTO userDTO) {
        Long userId = userService.saveUserInfo(userDTO);

        ResponseDTO responseDTO = ResponseDTO.builder()
                .code(200)
                .message("sign in user " + userDTO.getEmail())
                .data(new UserIdDTO(userId))
                .build();

        return responseDTO;
    }
}
