package com.bookstore.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.exception.UserAlreadyExistsException;
import com.bookstore.app.model.User;
import com.bookstore.app.model.UserVerification;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.validator.VerificationCodeGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder pswEncoder;

    @Mock
    private VerificationService verificationService;

    @Mock
    private VerificationCodeGenerator verificationCodeGenerator;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("admin");
        when(userRepo.findUserByUsername("admin")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("admin");

        assertThat(result.getUsername()).isEqualTo("admin");
    }

    @Test
    public void testSaveUser() {
        UserDTO user = new UserDTO();
        user.setUsername("newUser");
        user.setEmail("newuser@example.com");
        user.setPassword("rawPassword");
        user.setActive(true);
        user.setRoles("ROLE_USER");

        mockStatic(VerificationCodeGenerator.class)
                .when(() -> VerificationCodeGenerator.generateVerificationCode())
                .thenReturn("verificationCode");

        when(pswEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        doNothing().when(verificationService).saveVerification(any(UserVerification.class));
        doNothing().when(emailService).sendVerificationEmail(anyString(), anyString());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        User savedUser = new User();
        savedUser.setUsername("newUser");
        savedUser.setEmail("newuser@example.com");
        savedUser.setRoles("ROLE_USER");
        savedUser.setActive(true);
        savedUser.setPassword("encodedPassword");

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        User result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertEquals("newuser@example.com", result.getEmail());
        assertEquals("ROLE_USER", result.getRoles());
        assertTrue(result.isActive());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepo).existsByUsername("newUser");
        verify(pswEncoder).encode("rawPassword");
        verify(verificationService).saveVerification(any(UserVerification.class));
        verify(emailService).sendVerificationEmail("newuser@example.com", "verificationCode");
        verify(userRepo).save(userCaptor.capture());

        User userToSave = userCaptor.getValue();
        assertEquals("newUser", userToSave.getUsername());
        assertEquals("encodedPassword", userToSave.getPassword());
    }

    @Test
    public void testSaveUserUserAlreadyExistsThrowsException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existingUser");

        when(userRepo.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.saveUser(userDTO);
        });

        verify(userRepo).existsByUsername("existingUser");
        verifyNoMoreInteractions(userRepo);
    }

    @Test
    public void testMakeAdmin() {
        List<Integer> userIds = Arrays.asList(1, 2, 3);
        User user1 = new User();
        user1.setId(1);
        user1.setRoles("ROLE_USER");
        User user2 = new User();
        user2.setId(2);
        user2.setRoles("ROLE_USER");
        User user3 = new User();
        user3.setId(3);
        user3.setRoles("ROLE_USER");

        when(userRepo.findById(1)).thenReturn(Optional.of(user1));
        when(userRepo.findById(2)).thenReturn(Optional.of(user2));
        when(userRepo.findById(3)).thenReturn(Optional.of(user3));

        userService.makeAdmin(userIds);

        assertThat(user1.getRoles()).isEqualTo("ROLE_ADMIN");
        assertThat(user2.getRoles()).isEqualTo("ROLE_ADMIN");
        assertThat(user3.getRoles()).isEqualTo("ROLE_ADMIN");

        verify(userRepo).findById(1);
        verify(userRepo).findById(2);
        verify(userRepo).findById(3);
        verify(userRepo).save(user1);
        verify(userRepo).save(user2);
        verify(userRepo).save(user3);
    }

    @Test
    public void testRemoveAdmin() {
        List<Integer> userIds = Arrays.asList(1, 2, 3);
        User user1 = new User();
        user1.setId(1);
        user1.setRoles("ROLE_ADMIN");
        User user2 = new User();
        user2.setId(2);
        user2.setRoles("ROLE_ADMIN");
        User user3 = new User();
        user3.setId(3);
        user3.setRoles("ROLE_ADMIN");

        when(userRepo.findById(1)).thenReturn(Optional.of(user1));
        when(userRepo.findById(2)).thenReturn(Optional.of(user2));
        when(userRepo.findById(3)).thenReturn(Optional.of(user3));

        userService.removeAdmin(userIds);

        assertThat(user1.getRoles()).isEqualTo("ROLE_USER");
        assertThat(user2.getRoles()).isEqualTo("ROLE_USER");
        assertThat(user3.getRoles()).isEqualTo("ROLE_USER");

        verify(userRepo).findById(1);
        verify(userRepo).findById(2);
        verify(userRepo).findById(3);
        verify(userRepo).save(user1);
        verify(userRepo).save(user2);
        verify(userRepo).save(user3);
    }

    @Test
    public void testBlockUsers() {
        List<Integer> userIds = Arrays.asList(1, 2, 3);
        User user1 = new User();
        user1.setId(1);
        user1.setActive(true);
        User user2 = new User();
        user2.setId(2);
        user2.setActive(true);
        User user3 = new User();
        user3.setId(3);
        user3.setActive(true);

        when(userRepo.findById(1)).thenReturn(Optional.of(user1));
        when(userRepo.findById(2)).thenReturn(Optional.of(user2));
        when(userRepo.findById(3)).thenReturn(Optional.of(user3));

        userService.blockUsers(userIds);

        assertThat(user1.isActive()).isFalse();
        assertThat(user2.isActive()).isFalse();
        assertThat(user3.isActive()).isFalse();

        verify(userRepo).findById(1);
        verify(userRepo).findById(2);
        verify(userRepo).findById(3);
        verify(userRepo).save(user1);
        verify(userRepo).save(user2);
        verify(userRepo).save(user3);
    }

    @Test
    public void testUnblockUsers() {
        List<Integer> userIds = Arrays.asList(1, 2, 3);
        User user1 = new User();
        user1.setId(1);
        user1.setActive(false);
        User user2 = new User();
        user2.setId(2);
        user2.setActive(false);
        User user3 = new User();
        user3.setId(3);
        user3.setActive(false);

        when(userRepo.findById(1)).thenReturn(Optional.of(user1));
        when(userRepo.findById(2)).thenReturn(Optional.of(user2));
        when(userRepo.findById(3)).thenReturn(Optional.of(user3));

        userService.unblockUsers(userIds);

        assertThat(user1.isActive()).isTrue();
        assertThat(user2.isActive()).isTrue();
        assertThat(user3.isActive()).isTrue();

        verify(userRepo).findById(1);
        verify(userRepo).findById(2);
        verify(userRepo).findById(3);
        verify(userRepo).save(user1);
        verify(userRepo).save(user2);
        verify(userRepo).save(user3);
    }

    @Test
    public void testDeleteUsersById() {
        List<Integer> userIds = Arrays.asList(1, 2, 3);

        userService.deleteUsersById(userIds);

        verify(userRepo).deleteById(1);
        verify(userRepo).deleteById(2);
        verify(userRepo).deleteById(3);

        verifyNoMoreInteractions(userRepo);
    }
}
