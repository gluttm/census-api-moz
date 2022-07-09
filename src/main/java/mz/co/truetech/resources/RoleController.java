package mz.co.truetech.resources;

import java.net.URI;

import mz.co.truetech.pojos.RequestSuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mz.co.truetech.dto.RoleRequest;
import mz.co.truetech.entity.Role;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.service.RoleService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

	@Autowired
	private RoleService service;

	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Page<Role>> findAll(Pageable pageable) {
		Page<Role> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<RoleRequest> findById(@PathVariable Long id) throws ApiRequestException {
			RoleRequest obj = service.findById(id);
			return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<RoleRequest> insert(@Valid @RequestBody RoleRequest roleRequest) throws ApiRequestException {
		RoleRequest role = service.insert(roleRequest);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(role.getId())
				.toUri();		
		return ResponseEntity.created(uri).body(role);
	}
	
	@DeleteMapping(value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<RequestSuccessResponse> delete(@PathVariable Long id) throws ApiRequestException {
		RequestSuccessResponse result = service.delete(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<RoleRequest> update(@PathVariable Long id, @RequestBody RoleRequest roleRequest) throws ApiRequestException {
		RoleRequest role = service.update(id, roleRequest);
		return ResponseEntity.ok().body(role);
	}
}
