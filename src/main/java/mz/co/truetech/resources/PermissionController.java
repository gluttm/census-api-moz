package mz.co.truetech.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mz.co.truetech.dto.PermissionDTO;
import mz.co.truetech.entity.Permission;
import mz.co.truetech.service.PermissionService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/permissions")
@ApiIgnore
public class PermissionController {
	
	@Autowired
	private PermissionService service;
	
	@GetMapping
	public ResponseEntity<List<Permission>> findAll() {
		List<Permission> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Permission> findById(@PathVariable Long id) {
		Permission obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<PermissionDTO> insert(@RequestBody @Valid PermissionDTO permission) {
		permission = service.insert(permission);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(permission.getId())
				.toUri();
		
		return ResponseEntity.created(uri).body(permission);
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Permission> update(@PathVariable Long id, @RequestBody Permission obj) {
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}
