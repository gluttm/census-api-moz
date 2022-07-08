package mz.co.truetech.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.co.truetech.entity.Permission;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data @AllArgsConstructor @NoArgsConstructor
public class PermissionDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull(message = "{email.notempty}")
	private String name;
	private String displayName;
	
	public PermissionDTO(Permission permission) {
		id = permission.getId();
		name = permission.getName();
		displayName = permission.getDisplayName();
	}

}
