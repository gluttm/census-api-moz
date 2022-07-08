package mz.co.truetech.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.co.truetech.dto.UserDTO;
import mz.co.truetech.dto.projection.PUserDTO;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.UserStatus;
import mz.co.truetech.pojos.UserRoleRequest;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor 
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "first_name")
	private String firstName;
	@Column (name = "lastname")
	private String lastname;
	@Column(name = "gender")
	private Gender gender;
	@Column(name = "is_locked")
	private Boolean isLocked;
	@Column(name = "email")
	private String email;
	@Column(name = "email_verified_at")
	private Instant emailVerified;
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;
	@Column(name = "password_changed_at")
	private Instant passwordChangedAt;
	
	@ManyToMany(
			cascade= {
					CascadeType.DETACH, 
					CascadeType.MERGE,
					CascadeType.PERSIST,
					CascadeType.REFRESH
					},
			fetch = FetchType.EAGER)
	@JoinTable(
			name="roles_users", 
			joinColumns = @JoinColumn(name="user_id"), 
			inverseJoinColumns = @JoinColumn(name="role_id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Set<Role> roles = new HashSet<>();
	
	public User(UserRoleRequest userRoleRequest) {
		id = userRoleRequest.getId();
		username = userRoleRequest.getUsername();
		password = userRoleRequest.getPassword();
		firstName = userRoleRequest.getFirstName();
		lastname = userRoleRequest.getLastname();
		gender = userRoleRequest.getGender();
		isLocked = userRoleRequest.getIsLocked();
		email = userRoleRequest.getEmail();
	}
	
	public User(UserDTO userDto) {
		id = userDto.getId();
		username = userDto.getUsername();
		password = userDto.getPassword();
		firstName = userDto.getFirstName();
		lastname = userDto.getLastname();
		gender = userDto.getGender();
		email = userDto.getEmail();
		emailVerified = userDto.getEmailVerified();
		createdAt = userDto.getCreatedAt();
		passwordChangedAt = userDto.getPasswordChangedAt();
	}

	public User(PUserDTO userDto) {
		id = userDto.getId();
		username = userDto.getUsername();
		firstName = userDto.getFirstName();
		lastname = userDto.getLastname();
		gender = userDto.getGender();
		email = userDto.getEmail();
		//createdAt = userDto.getCreatedAt();
	}
	
}
