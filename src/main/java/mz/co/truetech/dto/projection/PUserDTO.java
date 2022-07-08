package mz.co.truetech.dto.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.co.truetech.entity.Role;
import mz.co.truetech.entity.User;
import mz.co.truetech.enums.Gender;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor
public class PUserDTO {
    private Long id;
    private String firstName;
    private String lastname;
    private String username;
    private String email;
    private Gender gender;
    private Boolean isLocked;
    private Set<Role> roles = new HashSet<>();

    public PUserDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        firstName = user.getFirstName();
        lastname = user.getLastname();
        gender = user.getGender();
        email = user.getEmail();
        roles = user.getRoles();
        isLocked = user.getIsLocked();
    }
}
