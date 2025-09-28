package com.bookstore.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.exception.UserAlreadyExistsException;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.UserRepo;
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

  @Mock private UserRepo userRepo;

  @Mock private PasswordEncoder pswEncoder;

  @InjectMocks private UserService userService;

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
    user.setEmail("test@test.com");
    user.setPassword("rawPassword");
    user.setActive(true);
    user.setRoles("ROLE_USER");

    when(userRepo.findUserByUsername("newUser")).thenReturn(Optional.empty());
    when(pswEncoder.encode("rawPassword")).thenReturn("encodedPassword");

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

    verify(userRepo).findUserByUsername("newUser");
    verify(pswEncoder).encode("rawPassword");
    verify(userRepo).save(userCaptor.capture());

    User userToSave = userCaptor.getValue();
    assertEquals("newUser", userToSave.getUsername());
    assertEquals("encodedPassword", userToSave.getPassword());
  }

  @Test
  public void testSaveUserUserAlreadyExistsThrowsException() {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("existingUser");

    when(userRepo.findUserByUsername("existingUser")).thenReturn(Optional.of(new User()));

    assertThrows(
        UserAlreadyExistsException.class,
        () -> {
          userService.saveUser(userDTO);
        });

    verify(userRepo).findUserByUsername("existingUser");
    verifyNoMoreInteractions(userRepo);
  }
}
