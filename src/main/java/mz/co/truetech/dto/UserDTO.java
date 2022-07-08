package mz.co.truetech.dto;

import lombok.Data;
import mz.co.truetech.entity.User;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.UserStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull(message = "{notnull.username}")
	private String username;
	@NotNull(message = "{notnull.password}")
	private String password;
	@NotNull(message = "{notnull.password}")
	private String passwordConfirm;
	@NotNull(message = "{notnull.firstName}")
	private String firstName;
	@NotNull(message = "{notnull.lastname}")
	private String lastname;
	@NotNull(message = "{notnull.gender}")
	private Gender gender;
	private UserStatus userStatus;
	@Email(message = "{notvalid.email}")
	@NotNull(message = "{notnull.email}")
	private String email;
	private Instant emailVerified;
	private Instant createdAt;
	private Instant passwordChangedAt;
	private Set<RoleRequest> roles = new HashSet<>();
	
	public UserDTO(User user) {
		id = user.getId();
		username = user.getUsername();
		password = user.getPassword();
		firstName = user.getFirstName();
		lastname = user.getLastname();
		gender = user.getGender();
		email = user.getEmail();
		emailVerified = user.getEmailVerified();
		createdAt = user.getCreatedAt();
		passwordChangedAt = user.getPasswordChangedAt();
		user.getRoles()
				.stream().map(x -> this.roles.add(new RoleRequest(x))).collect(Collectors.toSet());
	}

}
