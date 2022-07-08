package mz.co.truetech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mz.co.truetech.dto.PermissionDTO;
import mz.co.truetech.entity.Permission;
import mz.co.truetech.repository.PermissionRepository;

@Service
@Transactional
public class PermissionService {
	
	@Autowired
	private PermissionRepository permissionRepository;

	public PermissionDTO insert(PermissionDTO dto) {
		Permission p = permissionRepository.save(new Permission(null, dto.getName(), dto.getDisplayName()));
		return new PermissionDTO(p);
	}
	
	public Permission findById(Long id) {
		return permissionRepository.findById(id).get();
	}
	
	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}
	
	public void delete(Long id) {
		permissionRepository.deleteById(id);
	}
	
	public Permission update(Long id, Permission permission) {
		Permission p = permissionRepository.findById(id).get();
		p.setName(permission.getName());
		p.setDisplayName(permission.getDisplayName());
		return permissionRepository.save(p);
	}
}
