package mz.co.truetech.resources;

import lombok.RequiredArgsConstructor;
import mz.co.truetech.dto.DistrictDTO;
import mz.co.truetech.entity.District;
import mz.co.truetech.entity.Permission;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.service.DistrictService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/districts")
@RequiredArgsConstructor
public class DistrictController {
	
	private final DistrictService service;
	
	@GetMapping
	public ResponseEntity<List<District>> findAll() {
		List<District> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<District>> findAllByName(@RequestParam String name) {
		List<District> list = service.findAllByName(name.toUpperCase(Locale.ROOT));
		System.out.println(name);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<DistrictDTO> findById(@PathVariable Long id) throws ApiRequestException {
		DistrictDTO obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<DistrictDTO> insert(@RequestBody @Valid DistrictDTO districtDTO) {
		districtDTO = service.save(districtDTO);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(districtDTO.getId())
				.toUri();
		
		return ResponseEntity.created(uri).body(districtDTO);
	}
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		//service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Permission> update(@PathVariable Long id, @RequestBody Permission obj) {
		//obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}
