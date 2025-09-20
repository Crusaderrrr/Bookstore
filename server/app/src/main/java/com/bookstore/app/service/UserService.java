package com.bookstore.app.service;

import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.exception.UserAlreadyExistsException;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.UserRepo;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepo userRepo;
  private final PasswordEncoder pswEncoder;

  public UserService(UserRepo userRepo, PasswordEncoder pswEncoder) {
    this.userRepo = userRepo;
    this.pswEncoder = pswEncoder;
  }

  public void deleteUsersById(List<Integer> userIds) {
    userIds.forEach(this::deleteUserById);
  }

  public void deleteUserById(Integer userId) {
    userRepo.deleteById(userId);
  }


  public User findByUsername(String username) {
    return userRepo.findUserByUsername(username).get();
  }

  public User saveUser(UserDTO userDTO) {
    if (userRepo.findUserByUsername(userDTO.getUsername()).isPresent()) {
      throw new UserAlreadyExistsException(
          "User with name " + userDTO.getUsername() + " already exists");
    }
    User user = new User();
    user.setUsername(userDTO.getUsername());
    user.setEmail(userDTO.getEmail());
    user.setRoles(userDTO.getRoles());
    user.setActive(userDTO.isActive());
    user.setPassword(pswEncoder.encode(userDTO.getPassword()));
    return userRepo.save(user);
  }

  public Iterable<User> getAllUsers() {
    return userRepo.findAll();
  }
}
