package mz.co.truetech.repository;

import mz.co.truetech.entity.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @DisplayName("It should select the role by name.")
    void findRoleByNameSuccess() {
        // Given
        String roleName = "ROLE_ADMIN2";
        Role role = new Role(null, roleName, "Administrador");
        underTest.save(role);
        //when
        role = underTest.findByName(roleName).get();
        // Then
        assertThat(role.getId()).isNotNull();
    }

    @Test
    @DisplayName("It should not find the role.")
    void findRoleByNameFail() {
        // Given
        String roleName = "WrongName";

        //when
        Optional<Role> role = underTest.findByName("WrongName");
        // Then
        assertTrue(role.isEmpty());
    }
}