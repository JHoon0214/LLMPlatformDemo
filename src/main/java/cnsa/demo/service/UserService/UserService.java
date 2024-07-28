package cnsa.demo.service.UserService;

import cnsa.demo.DTO.User.UserDTO;
import cnsa.demo.domain.Role;
import cnsa.demo.domain.User;
import cnsa.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public Long saveUserInfo(UserDTO userDTO) {
        String userEmail = userDTO.getEmail();
        if(!EmailValidator.isValid(userEmail)) {
            throw new RuntimeException("Invalid email format " + userEmail);
        }

        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
        if(user.isPresent()) return user.get().getId();

        try {
            User newUser = userRepository.save(
                    User.builder()
                            .name(userDTO.getName())
                            .email(userDTO.getEmail())
                            .role(Role.USER)
                            .picture(userDTO.getPicture())
                            .build()
            );
            return newUser.getId();
        }
        catch (DataAccessException e) {
            throw new RuntimeException("Error saving user information", e);
        }
    }
}
