package com.jegorov.wallet;

import com.jegorov.wallet.engine.dto.UserDto;
import com.jegorov.wallet.engine.repository.UserRepository;
import com.jegorov.wallet.engine.repository.WalletRepository;
import com.jegorov.wallet.engine.service.UserService;
import com.jegorov.wallet.engine.service.WalletService;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

  @Autowired
  WalletService walletService;

  @Autowired
  UserService userService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WalletRepository walletRepository;

  @AfterEach
  public void deleteWallet() {
    userRepository.deleteAll();
    walletRepository.deleteAll();
  }

  @Test
  public void userNotExistloginTest() {
    UserDto userDto = new UserDto();
    userDto.setUsername("user");
    userDto.setPassword("password");
    boolean isExist = userService.login(userDto);
    assertFalse( isExist);
  }

  @Test
  public void userExistLoginTest() {
    createUser();
    UserDto userDto = new UserDto();
    userDto.setUsername("user");
    userDto.setPassword("password");
    boolean isExist = userService.login(userDto);
    assertTrue( isExist);
  }

  @Test
  public void userLoginFailTest() {
    createUser();
    UserDto userDto = new UserDto();
    userDto.setUsername("user");
    userDto.setPassword("passwordFail");
    try {
      userService.login(userDto);
      fail();
    } catch (AuthenticationCredentialsNotFoundException e) {
      assertTrue(e.getMessage().contains("Invalid password exists"));
    }
  }

  @Test
  public void createUserTest() {
    createUser();
    assertTrue(userRepository.findByUsername("user").isPresent());
  }

  private void createUser() {
    UUID walletId = walletService.createWallet();
    UserDto userDto = new UserDto();
    userDto.setUsername("user");
    userDto.setPassword("password");
    userService.createUser(userDto, walletId);
  }

}
