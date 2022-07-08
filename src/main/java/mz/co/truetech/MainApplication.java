package mz.co.truetech;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mz.co.truetech.dto.DistrictDTO;
import mz.co.truetech.dto.ProvinceDTO;
import mz.co.truetech.entity.*;
import mz.co.truetech.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import mz.co.truetech.dto.PermissionDTO;
import mz.co.truetech.dto.RoleRequest;
import mz.co.truetech.enums.Gender;
import mz.co.truetech.jwt.JwtConfig;
import mz.co.truetech.pojos.UserRoleRequest;


@SpringBootApplication(scanBasePackages="mz.co.truetech")
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	
	@Bean
	public JwtConfig jwtConfig () {
		return new JwtConfig();
	}

//	@Bean
//	CommandLineRunner run(UserService userService,
//						  JwtConfig jwtConfig, ProvinceService provinceService,
//						  RoleService roleService, DistrictService districtService,
//						  PermissionService permissionService, PasswordEncoder passwordEncoder) {
//
//		return args -> {
//			System.out.println(jwtConfig);
//			UserRoleRequest uObj1 = new UserRoleRequest();
//			uObj1.setUsername("glu");
//			uObj1.setPassword("!Ttm0000");
//			uObj1.setFirstName("Glu");
//			uObj1.setLastname("TTM");
//			uObj1.setPasswordConfirm("!Ttm0000");
//
//			uObj1.setGender(Gender.MALE);
//
//			UserRoleRequest uObj2 = new UserRoleRequest();
//			uObj2.setUsername("ayder");
//			uObj2.setFirstName("Ayder");
//			uObj2.setLastname("Glu");
//
//			uObj2.setPasswordConfirm("!Ttm0000");
//			uObj2.setPassword("!Ttm0000");
//
//			PermissionDTO p1 = permissionService.insert(new PermissionDTO(0L, "read:user", "Visualizar usuarios"));
//			PermissionDTO p2 = permissionService.insert(new PermissionDTO(0L, "write:user", "Salvar usuarios"));
//			PermissionDTO p5 = permissionService.insert(new PermissionDTO(0L, "edit:user", "Modificar usuarios"));
//			PermissionDTO p6 = permissionService.insert(new PermissionDTO(0L, "delete:user", "Apagar usuarios"));
//			PermissionDTO p3 = permissionService.insert(new PermissionDTO(0L, "read:role", "Visualizar cargos"));
//			PermissionDTO p4 = permissionService.insert(new PermissionDTO(0L, "write:role", "Salvar cargos"));
//
//			Set<PermissionDTO> permissions = new HashSet<>();
//			permissions.add(p4);
//			permissions.add(p2);
//			permissions.add(p3);
//			permissions.add(p1);
//			permissions.add(p5);
//			permissions.add(p6);
//
//			RoleRequest r1 = roleService.insert(new RoleRequest(null, "ADMIN", "Administrador", permissions));
//			RoleRequest r2 = roleService.insert(new RoleRequest(null, "MANAGER", "Gerente", permissions));
//			List<Long> roles = new ArrayList<>();
//			List<Long> managerRole = new ArrayList<>();
//			roles.add(r1.getId());
//			uObj1.setRolesIds(roles);
//
//			managerRole.add(r2.getId());
//			uObj2.setRolesIds(managerRole);
//			User u1 = new User( userService.save(uObj1) );
//			User u2 = new User( userService.save(uObj2) );
//
//			//Set<District> districts = new HashSet<>();
//
//			//Set<Census> censuses = new HashSet<>();
//			ProvinceDTO prov1 = provinceService.save(new ProvinceDTO(null, "Tete", "p_tete"));
//
//			districtService.save(new DistrictDTO(null, "Angonia", "d_angonia", new Province(prov1)));
//			districtService.save(new DistrictDTO(null, "Mutarara", "d_mutarara", new Province(prov1)));
//			districtService.save(new DistrictDTO(null, "Magoe", "d_magoe", new Province(prov1)));
//
//
//
//		};
//	}
}
