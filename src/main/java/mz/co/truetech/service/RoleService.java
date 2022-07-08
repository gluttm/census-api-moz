package mz.co.truetech.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import mz.co.truetech.pojos.RequestSuccessResponse;
import mz.co.truetech.utils.RequestsUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mz.co.truetech.dto.RoleRequest;
import mz.co.truetech.entity.Permission;
import mz.co.truetech.entity.Role;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.repository.PermissionRepository;
import mz.co.truetech.repository.RoleRepository;

@Service
@Transactional
public class RoleService {
	
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	private final Environment environment;

	public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository, Environment environment) {
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;
		this.environment = environment;
	}

	public RoleRequest insert(RoleRequest roleRequest) throws ApiRequestException
	{
		Boolean roleByName = roleRepository.selectExistsName(roleRequest.getName());
		Boolean roleByDisplayName = roleRepository.selectExistsDisplayName(roleRequest.getDisplayName());
		if (roleByName || roleByDisplayName) {
			throw new ApiRequestException(environment.getProperty("exists.role"));
		}
		Role role = new Role();
		role.setName(roleRequest.getName());
		role.setDisplayName(roleRequest.getDisplayName());
		if (roleRequest.getPermissions().size() > 0) {
			Set<Long> checkedIds = RequestsUtil.idsChecker(roleRequest.getPermissions());
			role.getPermissions().addAll(findAllByIds(checkedIds));
		}
		Role savedRole = roleRepository.save(role);
		return new RoleRequest(savedRole);
	}
	
	public RoleRequest findById(Long id) throws ApiRequestException {
		if (id == null) {
			throw new ApiRequestException(environment.getProperty("notfound.role"));
		}
		Optional<Role> role = roleRepository.findById(id);
		return new RoleRequest(role.orElse(new Role()));
	}
	
	public Page<Role> findAll(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}
	
	public RequestSuccessResponse delete(Long id) throws ApiRequestException {
		Optional<Role> role = roleRepository.findById(id);
		if (role.isEmpty()) {
			throw new ApiRequestException(environment.getProperty("notfound.role"));
		}
		RequestSuccessResponse resp = new RequestSuccessResponse(
				String.format("%s ", environment.getProperty("msg.deleted")),
				HttpStatus.OK);
		roleRepository.deleteById(id);
		return  resp;
	}
	
	public RoleRequest update(Long id, RoleRequest roleRequest) throws ApiRequestException {
		Set<Long> checkedIds = RequestsUtil.idsChecker(roleRequest.getPermissions());
		if (checkedIds.isEmpty()) {
			throw new ApiRequestException(environment.getProperty("notnull.permissions"));
		}
		Optional<Role> role = roleRepository.findById(id);
		if (role.isEmpty()) {
			throw new ApiRequestException(environment.getProperty("notfound.role"));
		}
		role.get().setName(roleRequest.getName());
		role.get().setDisplayName(roleRequest.getDisplayName());
		role.get().getPermissions().clear();
		role.get().getPermissions().addAll(findAllByIds(checkedIds));
		return new RoleRequest(roleRepository.save(role.get()));
	}

	public Set<Permission> findAllByIds(Set<Long> ids) {
		Set<Permission> permissions = new HashSet<>();
		ids.stream().map(p -> {
			Optional<Permission> permission = permissionRepository.findById(p);
			permission.ifPresent(permissions::add);
			return permission;
		}).collect(Collectors.toSet());
		return permissions;
	}
}
