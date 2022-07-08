package mz.co.truetech.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @NoArgsConstructor @Data @Table(name="permissions")
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(unique=true, name = "name")
	private String name;
	@Column(unique=true, name = "display_name")
	private String displayName;

	public Permission(Long id, String name, String displayName) {
		super();
		this.id = id;
		this.name = name;
		this.displayName = displayName;
	}

	@ManyToMany(mappedBy = "permissions")
	@JsonIgnore
	private Set<Role> roles;
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
}
