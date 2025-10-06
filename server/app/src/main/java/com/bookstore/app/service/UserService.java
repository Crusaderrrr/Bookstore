package com.bookstore.app.service;

import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.exception.UserAlreadyExistsException;
import com.bookstore.app.model.User;
import com.bookstore.app.model.UserVerification;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.validator.VerificationCodeGenerator;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final VerificationService verificationService;
  private final UserRepo userRepo;
  private final PasswordEncoder pswEncoder;
  private final EmailService emailService;
  private final VerificationCodeGenerator verificationCodeGenerator;

  public UserService(
      UserRepo userRepo,
      PasswordEncoder pswEncoder,
      EmailService emailService,
      VerificationCodeGenerator verificationCodeGenerator,
      VerificationService verificationService) {
    this.userRepo = userRepo;
    this.pswEncoder = pswEncoder;
    this.emailService = emailService;
    this.verificationCodeGenerator = verificationCodeGenerator;
    this.verificationService = verificationService;
  }

  public Optional<User> findByUserId(int id) {
    return userRepo.findById(id);
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
    if (userRepo.existsByUsername(userDTO.getUsername())) {
      throw new UserAlreadyExistsException(
          "User with name " + userDTO.getUsername() + " already exists");
    }
    User user = new User();
    user.setUsername(userDTO.getUsername());
    user.setEmail(userDTO.getEmail());
    user.setRoles(userDTO.getRoles());
    user.setActive(userDTO.isActive());
    user.setPassword(pswEncoder.encode(userDTO.getPassword()));

    String verificationCode = verificationCodeGenerator.generateVerificationCode();
    UserVerification verification = new UserVerification();
    verification.setEmail(user.getEmail());
    verification.setVerificationCode(verificationCode);
    verificationService.saveVerification(verification);

    emailService.sendVerificationEmail(user.getEmail(), verificationCode);

    return userRepo.save(user);
  }

  public User saveUser(User user) {
    return userRepo.save(user);
  }

  public Iterable<User> getAllUsers() {
    return userRepo.findAll();
  }

  public void makeAdmin(List<Integer> userIds) {
    userIds.forEach(
        (id) -> {
          User user = userRepo.findById(id).get();
          user.setRoles("ROLE_ADMIN");
          userRepo.save(user);
        });
  }

  public void removeAdmin(List<Integer> userIds) {
    userIds.forEach(
        (id) -> {
          User user = userRepo.findById(id).get();
          user.setRoles("ROLE_USER");
          userRepo.save(user);
        });
  }

  public void blockUsers(List<Integer> userIds) {
    userIds.forEach(
        (id) -> {
          User user = userRepo.findById(id).get();
          user.setActive(false);
          userRepo.save(user);
        });
  }

  public void unblockUsers(List<Integer> userIds) {
    userIds.forEach(
        (id) -> {
          User user = userRepo.findById(id).get();
          user.setActive(true);
          userRepo.save(user);
        });
  }
}
