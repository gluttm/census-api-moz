package mz.co.truetech.resources;

import lombok.RequiredArgsConstructor;
import mz.co.truetech.dto.RoleRequest;
import mz.co.truetech.entity.Census;
import mz.co.truetech.entity.Role;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.enums.GenderConverter;
import mz.co.truetech.enums.Zone;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.pojos.RequestSuccessResponse;
import mz.co.truetech.service.CensusService;
import mz.co.truetech.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/census")
@RequiredArgsConstructor
public class CensusController extends GenderConverter {

	private final CensusService service;


	@GetMapping
	public ResponseEntity<Page<Census>> findAllBy(
			@RequestParam(name = "district") String district,
			@RequestParam(name = "year", required = false) Integer year,
			@RequestParam(name = "gender", required = false) String gender,
			@RequestParam(name = "zone", required = false) String zone,
			@RequestParam(name = "age", required = false) Integer age,
			Pageable pageable
	) throws ApiRequestException {
		Gender g = null;
		if (gender != null) {
			g = gender.equals("male") ? Gender.MALE : Gender.FAMALE;
		}
		Zone z = null;
		if (zone != null) {
			z = zone.equals("rural") ? Zone.RURAL : Zone.URBAN;
		}

		Page<Census> list = service.findAllBy(district, year, g, z, age, pageable);
		return ResponseEntity.ok().body(list);
	}

//	@GetMapping
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//	public ResponseEntity<Page<Census>> findAll(Pageable pageable) {
//		Page<Census> list = service.findAll(pageable);
//		return ResponseEntity.ok().body(list);
//	}
//
//	@GetMapping(value="/{id}")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//	public ResponseEntity<RoleRequest> findById(@PathVariable Long id) throws ApiRequestException {
//			RoleRequest obj = service.findById(id);
//			return new ResponseEntity<>(obj, HttpStatus.OK);
//	}
//
//	@PostMapping
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//	public ResponseEntity<RoleRequest> insert(@Valid @RequestBody RoleRequest roleRequest) throws ApiRequestException {
//		RoleRequest role = service.insert(roleRequest);
//		URI uri = ServletUriComponentsBuilder
//				.fromCurrentRequest().path("/{id}")
//				.buildAndExpand(role.getId())
//				.toUri();
//		return ResponseEntity.created(uri).body(role);
//	}
//
//	@DeleteMapping(value="/{id}")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//	public ResponseEntity<RequestSuccessResponse> delete(@PathVariable Long id) throws ApiRequestException {
//		RequestSuccessResponse result = service.delete(id);
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@PutMapping(value="/{id}")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//	public ResponseEntity<RoleRequest> update(@PathVariable Long id, @RequestBody RoleRequest roleRequest) throws ApiRequestException {
//		RoleRequest role = service.update(id, roleRequest);
//		return ResponseEntity.ok().body(role);
//	}
}
