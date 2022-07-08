package mz.co.truetech.pojos;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mz.co.truetech.entity.User;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.UserStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
public class UserRoleRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long id;
	@NotNull(message = "{notnull.username}")
	@Getter
	@Setter
	private String username;
	@NotNull(message = "{notnull.password}")
	@Getter
	@Setter
	private String password;
	@NotNull(message = "{notnull.password}")
	@Getter
	@Setter
	private String passwordConfirm;
	@NotNull(message = "{notnull.firstName}")
	@Getter
	@Setter
	private String firstName;
	@NotNull(message = "{notnull.lastname}")
	@Getter
	@Setter
	private String lastname;
	@NotNull(message = "{notnull.gender}")
	@Getter
	@Setter
	private Gender gender;
	@Getter
	@Setter
	private Boolean isLocked;
	@Email(message = "{notvalid.email}")
	@NotNull(message = "{notnull.email}")
	@Getter
	@Setter
	private String email;
	@Getter
	@Setter
	private List<Long> rolesIds = new ArrayList<>();
	
	public UserRoleRequest(User user) {
		id = user.getId();
		username = user.getUsername();
		password = user.getPassword();
		firstName = user.getFirstName();
		lastname = user.getLastname();
		gender = user.getGender();
		isLocked = user.getIsLocked();
		email = user.getEmail();
	}
}
