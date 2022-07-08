package mz.co.truetech.dto;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mz.co.truetech.entity.Role;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor @AllArgsConstructor
public class RoleRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull(message = "{notnull.roleName}")
	@NotEmpty(message = "{notnull.roleName}")
	private String name;

	private String displayName;

	@NotNull(message = "{notnull.permissions}")
	@NotEmpty(message = "{notnull.permissions}")
	private Set<PermissionDTO> permissions = new HashSet<>();

	public RoleRequest(Role role) {
		this.id = role.getId();
		this.name = role.getName();
		this.displayName = role.getDisplayName();
		if (role.getPermissions().size() > 0) {
			role.getPermissions().stream()
					.map(x -> this.permissions.add(new PermissionDTO(x))).collect(Collectors.toSet());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Set<PermissionDTO> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<PermissionDTO> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "RoleRequest [id=" + id + ", name=" + name + ", displayName=" + displayName + ", permissions="
				+ permissions + "]";
	}
	
	
}
