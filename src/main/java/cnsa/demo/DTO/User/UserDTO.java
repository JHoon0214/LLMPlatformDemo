package cnsa.demo.DTO.User;

import cnsa.demo.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class UserDTO implements Serializable {

    private String name;
    private String email;
    private String picture;

    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
