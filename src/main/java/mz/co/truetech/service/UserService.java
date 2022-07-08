package mz.co.truetech.service;

import mz.co.truetech.dto.projection.PUserDTO;
import mz.co.truetech.entity.Role;
import mz.co.truetech.entity.User;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.pojos.RequestSuccessResponse;
import mz.co.truetech.pojos.UserRoleRequest;
import mz.co.truetech.repository.RoleRepository;
import mz.co.truetech.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

	private final UserRepository repository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final Environment environment;

	public UserService(UserRepository repository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, Environment environment) {
		this.repository = repository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.environment = environment;
	}

	public Page<PUserDTO> findAll(Pageable pageable) {
		Page<User> page = repository.findAll(pageable);
		repository.findAllUsers(page.stream().collect(Collectors.toList()));
		return page.map(PUserDTO::new);
	}

	public PUserDTO findById(Long id, Authentication authentication) throws ApiRequestException {
		Optional<User> loggedUserOptional = getLoggedUserData(authentication);
		if (loggedUserOptional.isPresent()) {
			if (loggedUserOptional.get().getId().equals(id)
					|| authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
				Optional<User> obj = repository.findById(id);
				if (obj.isEmpty()) {
					throw new ApiRequestException(environment.getProperty("notfound.user"));
				}
				return new PUserDTO(obj.get());
			} else {
				throw new ApiRequestException(environment.getProperty("permission.error"));
			}
		}
		throw new ApiRequestException(environment.getProperty("permission.error"));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = repository.findByUsername(username);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(environment.getProperty("notfound.user"));
		}
		return new AppUserDetails(user.get());
	}

	public PUserDTO save(UserRoleRequest userPojo) throws ApiRequestException {
		if (userPojo.getRolesIds().isEmpty()) {
			throw new ApiRequestException(environment.getProperty("notnull.roles"));
		}
		if (userPojo.getPassword().compareTo(userPojo.getPasswordConfirm()) != 0) {
			throw new ApiRequestException(environment.getProperty("match.password"));
		}
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,30}$";
		boolean passMatchs = Pattern.matches(regex, userPojo.getPassword());
		if (userPojo.getPassword().length() < 8 || !passMatchs) {
			throw new ApiRequestException(environment.getProperty("notvalid.password"));
		}
		Optional<User> u = repository.findByUsername(userPojo.getUsername());
		if (u.isPresent()) {
			throw new ApiRequestException(environment.getProperty("exists.user"));
		}
		userPojo.setIsLocked(false);
		userPojo.setPassword(passwordEncoder.encode(userPojo.getPassword()));

		User user = repository.save(new User(userPojo));
		for (Long id : userPojo.getRolesIds()) {
			Optional<Role> r = roleRepository.findById(id);
			if (r.isEmpty()) {
				throw new ApiRequestException(environment.getProperty("notfound.role"));
			}
			addRoleToUser(user.getUsername(), r.get().getName());
		}
		return new PUserDTO(user);
	}

	public PUserDTO update(Long id, UserRoleRequest userDTO, Authentication authentication) throws ApiRequestException {
		Optional<User> loggedUserOptional = getLoggedUserData(authentication);
		if (loggedUserOptional.isEmpty()) {
			throw new ApiRequestException(environment.getProperty("permission.error"));
		}
		if (loggedUserOptional.get().getId().compareTo(id) == 0
				|| authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			if (userDTO.getRolesIds().isEmpty()) {
				throw new ApiRequestException(environment.getProperty("notnull.roles"));
			}
			Optional<User> u = repository.findById(id);
			if (u.isEmpty()) {
				throw new ApiRequestException(environment.getProperty("notfound.user"));
			}
			u.get().setFirstName(userDTO.getFirstName());
			u.get().setLastname(userDTO.getLastname());
			u.get().setUsername(userDTO.getUsername());
			u.get().setEmail(userDTO.getEmail());
			u.get().setGender(userDTO.getGender());
			u.get().setPassword(passwordEncoder.encode(userDTO.getPassword()));
			u.get().setCreatedAt(Instant.now());
			u.get().setEmailVerified(Instant.now());
			u.get().setIsLocked(userDTO.getIsLocked());
			u.get().setPasswordChangedAt(Instant.now());
			u.get().getRoles().clear();
			for (Long roleId : userDTO.getRolesIds()) {
				roleRepository.findById(roleId).ifPresent((x) -> u.get().getRoles().add(x));
			}
			return new PUserDTO(repository.save(u.get()));
		} else {
			throw new ApiRequestException(environment.getProperty("permission.error"));
		}
	}

	public void addRoleToUser(String username, String roleName) throws ApiRequestException {
		Optional<User> user = repository.findByUsername(username);
		Optional<Role> role = roleRepository.findByName(roleName);
		if (user.isEmpty() || role.isEmpty()) {
			throw new ApiRequestException(environment.getProperty("notvalid.user"));
		}
		user.get().getRoles().add(role.get());
	}

	public Optional<User> getLoggedUserData(Authentication authentication) {
		return repository.findByUsername(authentication.getName());
	}

	public RequestSuccessResponse delete(Long id) throws ApiRequestException {
		Optional<User> role = repository.findById(id);
		if (role.isEmpty()) {
			throw new ApiRequestException(environment.getProperty("notfound.user"));
		}
		RequestSuccessResponse resp = new RequestSuccessResponse(
				String.format("%s ", environment.getProperty("msg.deleted")),
				HttpStatus.OK);
		repository.deleteById(id);
		return  resp;
	}
}