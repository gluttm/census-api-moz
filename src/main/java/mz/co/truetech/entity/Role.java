package mz.co.truetech.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.NoArgsConstructor;

@Entity @NoArgsConstructor @Table(name="roles")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(unique=true, name = "name")
	private String name;
	@Column(unique=true, name = "display_name")
	private String displayName;
	
	public Role(Long id, String name, String displayName) {
		this.id = id;
		this.name = name;
		this.displayName = displayName;
	}
	
	@ManyToMany(
			cascade= {
					CascadeType.DETACH, 
					CascadeType.MERGE,
					CascadeType.PERSIST,
					CascadeType.REFRESH
					},
			fetch = FetchType.EAGER)
	@JoinTable(
			name="permissions_roles", 
			joinColumns = @JoinColumn(name="role_id"), 
			inverseJoinColumns = @JoinColumn(name="permission_id"))
	private final Set<Permission> permissions = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<>();

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

	public Set<Permission> getPermissions() {
		return permissions;
	}



	public Set<User> getUsers() {
		return users;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions.addAll(permissions);
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", displayName=" + displayName + ", permissions=" + permissions
				+ ", users=" + users + "]";
	}

	public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
		Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getName())).collect(Collectors.toSet());

		permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name));
		return permissions;

	}

}
