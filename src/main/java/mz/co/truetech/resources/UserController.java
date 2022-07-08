package mz.co.truetech.resources;

import java.net.URI;
import java.security.Principal;

import mz.co.truetech.dto.projection.PUserDTO;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.pojos.RequestSuccessResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mz.co.truetech.dto.UserDTO;
import mz.co.truetech.pojos.UserRoleRequest;
import mz.co.truetech.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@ApiIgnore
public class UserController {
	
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<PUserDTO> insert(@RequestBody @Valid UserRoleRequest user) throws ApiRequestException {
		PUserDTO dto = userService.save(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand("/{id}").toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'edit:user')")
	public ResponseEntity<PUserDTO> update(@RequestBody @Valid UserRoleRequest user, @PathVariable Long id, @CurrentSecurityContext(expression = "authentication")
			Authentication authentication) throws  ApiRequestException {
		PUserDTO dto = userService.update(id, user, authentication);
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public Page<PUserDTO> findAll(Pageable pageable) {
		return userService.findAll(pageable);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'read:user')")
	public ResponseEntity<PUserDTO> findById(@PathVariable Long id, @CurrentSecurityContext(expression = "authentication")
											 Authentication authentication) throws ApiRequestException {
		PUserDTO userDTO = userService.findById(id, authentication);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@DeleteMapping(value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<RequestSuccessResponse> delete(@PathVariable Long id) throws ApiRequestException {
		RequestSuccessResponse result = userService.delete(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}

