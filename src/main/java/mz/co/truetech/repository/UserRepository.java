package mz.co.truetech.repository;

import java.util.List;
import java.util.Optional;

import mz.co.truetech.dto.projection.PUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mz.co.truetech.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	@Query("SELECT u FROM User u " +
			"JOIN FETCH u.roles WHERE u IN :users")
	List<PUserDTO> findAllUsers(List<User> users);

}
//@Query("SELECT new " +
//		"mz.co.truetech.dto.projection" +
//		".PUserDTO(" +
//		"u.id, u.firstName, u.lastname, " +
//		"u.username, u.email, u.gender, " +
//		"u.createdAt, u.roles) FROM User u " +
//		"JOIN FETCH u.roles WHERE u IN :users")