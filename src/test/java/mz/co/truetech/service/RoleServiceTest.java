package mz.co.truetech.service;

import java.util.ArrayList;
import java.util.HashSet;

import mz.co.truetech.dto.PermissionDTO;

import mz.co.truetech.dto.RoleRequest;
import mz.co.truetech.entity.Role;
import mz.co.truetech.exceptions.ApiRequestException;
import mz.co.truetech.pojos.RequestSuccessResponse;
import mz.co.truetech.repository.PermissionRepository;
import mz.co.truetech.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RoleService.class})
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @MockBean
    private PermissionRepository permissionRepository;

    private RoleService underTest;
    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Environment environment = null;
        underTest = new RoleService(roleRepository, null, environment);
    }

    /**
     * Method under test: {@link RoleService#insert(RoleRequest)}
     */
    @Test
    @DisplayName("Role insertion without permissions.")
    void testIfInsertNormalBehaviour() throws ApiRequestException {
        Role role = new Role();
        role.setDisplayName("Display Name");
        role.setId(123L);
        role.setName("Name");
        role.setUsers(new HashSet<>());
        when(this.roleRepository.selectExistsDisplayName((String) org.mockito.Mockito.any())).thenReturn(true);
        when(this.roleRepository.selectExistsName((String) org.mockito.Mockito.any())).thenReturn(true);
        assertThrows(ApiRequestException.class, () -> underTest.insert(new RoleRequest()));
        verify(this.roleRepository).selectExistsDisplayName((String) org.mockito.Mockito.any());
        verify(this.roleRepository).selectExistsName((String) org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link RoleService#insert(RoleRequest)}
     */
    @Test
    @DisplayName("Will Thrown if role already exists.")
    void testInsertRoleAlreadyExists() throws ApiRequestException {
        Role role = new Role();
        role.setDisplayName("Display Name");
        role.setId(123L);
        role.setName("Name");
        role.setUsers(new HashSet<>());
        when(this.roleRepository.selectExistsDisplayName((String) org.mockito.Mockito.any())).thenReturn(true);
        when(this.roleRepository.selectExistsName((String) org.mockito.Mockito.any())).thenReturn(true);
        RoleRequest roleRequest = mock(RoleRequest.class);
        when(roleRequest.getDisplayName()).thenReturn("Display Name");
        when(roleRequest.getName()).thenReturn("Name");
        assertThrows(ApiRequestException.class, () -> underTest.insert(roleRequest));
        verify(this.roleRepository).selectExistsDisplayName((String) org.mockito.Mockito.any());
        verify(this.roleRepository).selectExistsName((String) org.mockito.Mockito.any());
        verify(roleRequest).getDisplayName();
        verify(roleRequest).getName();
    }

    /**
     * Method under test: {@link RoleService#insert(RoleRequest)}
     */
    @Test
    @DisplayName("More deep insertion tests 1.")
    void testInsertVerifications1() throws ApiRequestException {
        Role role = new Role();
        role.setDisplayName("Display Name");
        role.setId(123L);
        role.setName("Name");
        role.setUsers(new HashSet<>());
        when(this.roleRepository.selectExistsDisplayName((String) org.mockito.Mockito.any())).thenReturn(false);
        when(this.roleRepository.selectExistsName((String) org.mockito.Mockito.any())).thenReturn(false);
        when(this.roleRepository.save((Role) org.mockito.Mockito.any())).thenReturn(role);
        RoleRequest roleRequest = mock(RoleRequest.class);
        when(roleRequest.getPermissions()).thenReturn(new HashSet<>());
        when(roleRequest.getDisplayName()).thenReturn("Display Name");
        when(roleRequest.getName()).thenReturn("Name");
        RoleRequest actualInsertResult = underTest.insert(roleRequest);
        assertEquals("Display Name", actualInsertResult.getDisplayName());
        assertTrue(actualInsertResult.getPermissions().isEmpty());
        assertEquals("Name", actualInsertResult.getName());
        assertEquals(123L, actualInsertResult.getId().longValue());
        verify(this.roleRepository).selectExistsDisplayName((String) org.mockito.Mockito.any());
        verify(this.roleRepository).selectExistsName((String) org.mockito.Mockito.any());
        verify(this.roleRepository).save((Role) org.mockito.Mockito.any());
        verify(roleRequest, atLeast(1)).getDisplayName();
        verify(roleRequest, atLeast(1)).getName();
        verify(roleRequest).getPermissions();
    }

    /**
     * Method under test: {@link RoleService#insert(RoleRequest)}
     */
    @Test
    @DisplayName("More deep insertion tests 1.")
    void testInsertVerifications2() throws ApiRequestException {
        Role role = new Role();
        role.setDisplayName("Display Name");
        role.setId(123L);
        role.setName("Name");
        role.setUsers(new HashSet<>());
        when(this.roleRepository.selectExistsDisplayName((String) org.mockito.Mockito.any())).thenReturn(false);
        when(this.roleRepository.selectExistsName((String) org.mockito.Mockito.any())).thenReturn(false);
        when(this.roleRepository.save((Role) org.mockito.Mockito.any())).thenReturn(role);

        HashSet<PermissionDTO> permissionDTOSet = new HashSet<>();
        permissionDTOSet.add(new PermissionDTO());
        RoleRequest roleRequest = mock(RoleRequest.class);
        when(roleRequest.getPermissions()).thenReturn(permissionDTOSet);
        when(roleRequest.getDisplayName()).thenReturn("Display Name");
        when(roleRequest.getName()).thenReturn("Name");
        RoleRequest actualInsertResult = underTest.insert(roleRequest);
        assertEquals("Display Name", actualInsertResult.getDisplayName());
        assertTrue(actualInsertResult.getPermissions().isEmpty());
        assertEquals("Name", actualInsertResult.getName());
        assertEquals(123L, actualInsertResult.getId().longValue());
        verify(this.roleRepository).selectExistsDisplayName((String) org.mockito.Mockito.any());
        verify(this.roleRepository).selectExistsName((String) org.mockito.Mockito.any());
        verify(this.roleRepository).save((Role) org.mockito.Mockito.any());
        verify(roleRequest, atLeast(1)).getDisplayName();
        verify(roleRequest, atLeast(1)).getName();
        verify(roleRequest, atLeast(1)).getPermissions();
    }

    /**
     * Method under test: {@link RoleService#findById(Long)}
     */
    @Test
    void testFindById() throws ApiRequestException {
        Role role = new Role();
        role.setDisplayName("Display Name");
        role.setId(123L);
        role.setName("Name");
        role.setUsers(new HashSet<>());
        Optional<Role> ofResult = Optional.of(role);
        when(this.roleRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(ofResult);
        RoleRequest actualFindByIdResult = underTest.findById(123L);
        assertEquals("Display Name", actualFindByIdResult.getDisplayName());
        assertTrue(actualFindByIdResult.getPermissions().isEmpty());
        assertEquals("Name", actualFindByIdResult.getName());
        assertEquals(123L, actualFindByIdResult.getId().longValue());
        verify(this.roleRepository).findById((Long) org.mockito.Mockito.any());
    }

    @Test
    @DisplayName("Check returned message in case of duplication.")
    void willThrowWhenRoleExists() throws ApiRequestException {
        Role role = new Role(null, "ROLE_ADMIN2", "Administrador3");
        //when
        given(roleRepository.selectExistsName(role.getName())).willReturn(true);
        given(roleRepository.selectExistsDisplayName(role.getDisplayName())).willReturn(true);

        assertThatThrownBy(() -> underTest.insert(new RoleRequest(role)))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("O cargo ja existe");

        verify(roleRepository, never()).save(any());
    }

    /**
     * Method under test: {@link RoleService#findAll(org.springframework.data.domain.Pageable)}
     */
    @Test
    void testFindAll() {
        PageImpl<Role> pageImpl = new PageImpl<>(new ArrayList<>());
        when(this.roleRepository.findAll((org.springframework.data.domain.Pageable) org.mockito.Mockito.any()))
                .thenReturn(pageImpl);
        Page<Role> actualFindAllResult = underTest.findAll(null);
        assertSame(pageImpl, actualFindAllResult);
        assertTrue(actualFindAllResult.toList().isEmpty());
        verify(this.roleRepository).findAll((org.springframework.data.domain.Pageable) org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link RoleService#delete(Long)}
     */
    @Test
    void testDelete() throws ApiRequestException {
        Role role = new Role();
        role.setDisplayName("Display Name");
        role.setId(123L);
        role.setName("Name");
        role.setUsers(new HashSet<>());
        Optional<Role> ofResult = Optional.of(role);
        doNothing().when(this.roleRepository).deleteById((Long) org.mockito.Mockito.any());
        when(this.roleRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(ofResult);
        RequestSuccessResponse actualDeleteResult = this.roleService.delete(123L);
        assertEquals(HttpStatus.OK, actualDeleteResult.getHttpStatus());
        assertEquals("Apagado com sucesso. ", actualDeleteResult.getMsg());
        verify(this.roleRepository).findById((Long) org.mockito.Mockito.any());
        verify(this.roleRepository).deleteById((Long) org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link RoleService#delete(Long)}
     */
    @Test
    void testDelete2() throws ApiRequestException {
        doNothing().when(this.roleRepository).deleteById((Long) org.mockito.Mockito.any());
        when(this.roleRepository.findById((Long) org.mockito.Mockito.any())).thenReturn(Optional.empty());
        assertThrows(ApiRequestException.class, () -> this.roleService.delete(123L));
        verify(this.roleRepository).findById((Long) org.mockito.Mockito.any());
    }

    @Test
    @Disabled
    void delete() {
    }

    @Test
    @Disabled
    void update() {
    }

    @Test
    @Disabled
    void findAllByIds() {
    }
}