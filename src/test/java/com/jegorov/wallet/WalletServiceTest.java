package com.jegorov.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jegorov.wallet.engine.data.WalletEntity;
import com.jegorov.wallet.engine.dto.UserDto;
import com.jegorov.wallet.engine.exception.ElementNotFoundException;
import com.jegorov.wallet.engine.exception.InvalidArgumentException;
import com.jegorov.wallet.engine.repository.UserRepository;
import com.jegorov.wallet.engine.repository.WalletRepository;
import com.jegorov.wallet.engine.service.UserService;
import com.jegorov.wallet.engine.service.WalletService;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WalletServiceTest {

  @Autowired
  WalletService walletService;

  @Autowired
  UserService userService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WalletRepository walletRepository;


  @BeforeEach
  public void createWallet() {
    UUID walletId = walletService.createWallet();
    UserDto userDto = new UserDto();
    userDto.setUsername("user");
    userDto.setPassword("password");
    userService.createUser(userDto, walletId);
  }

  @AfterEach
  public void deleteWallet() {
    userRepository.deleteAll();
    walletRepository.deleteAll();
  }

  @Test
  public void putSomeMoneyTest() {
    WalletEntity walletEntity = walletRepository.findAll().iterator().next();
    assertEquals(0D,walletEntity.getFunds());
    walletService.putSomeMoney(walletEntity.getId(), 300.595D);
    assertEquals(300.59D,walletService.getBalance(walletEntity.getId()));
  }

  @Test
  public void withdrawSomeMoneyTest() {
    WalletEntity walletEntity = walletRepository.findAll().iterator().next();
    walletService.putSomeMoney(walletEntity.getId(), 300D);
    assertEquals(300D,walletService.getBalance(walletEntity.getId()));
    walletService.withdrawSomeMoney(walletEntity.getId(), 200D);
    assertEquals(100D,walletService.getBalance(walletEntity.getId()));
  }

  @Test
  public void getBalanceTest() {
    WalletEntity walletEntity = walletRepository.findAll().iterator().next();
    walletService.putSomeMoney(walletEntity.getId(), 300D);
    assertEquals(300D,walletService.getBalance(walletEntity.getId()));
  }

  @Test
  public void transferToOtherWalletTest() {
    WalletEntity walletEntity = walletRepository.findAll().iterator().next();
    WalletEntity otherWallet = createAndGetOtherUserWallet();
    walletService.putSomeMoney(walletEntity.getId(), 300D);
    assertEquals(300,walletService.getBalance(walletEntity.getId()));
    assertEquals(0,walletService.getBalance(otherWallet.getId()));
    walletService.transferByUsername(walletEntity.getId(), otherWallet.getId(), 200D);
    assertEquals(200D,walletService.getBalance(otherWallet.getId()));
    assertEquals(100D,walletService.getBalance(walletEntity.getId()));
  }


  @Test
  public void putNegativeValueTest() {
    WalletEntity walletEntity = walletRepository.findAll().iterator().next();
    try {
      walletService.putSomeMoney(walletEntity.getId(), -300D);
      fail();
    }
    catch (InvalidArgumentException e){
      assertTrue(e.getMessage().contains("not valid argument. Argument must be positive value"));
    }
  }

  @Test
  public void transferToInvalidWalletTest() {
    WalletEntity walletEntity = walletRepository.findAll().iterator().next();
    WalletEntity otherWallet = createAndGetOtherUserWallet();
    walletService.putSomeMoney(walletEntity.getId(), 300D);
    assertEquals(300,walletService.getBalance(walletEntity.getId()));
    assertEquals(0,walletService.getBalance(otherWallet.getId()));
    try {
      walletService.transferByUsername(walletEntity.getId(), UUID.randomUUID(), 200D);
      fail();
    }
    catch (ElementNotFoundException e){
      assertTrue(e.getMessage().contains("Wallet not found"));
    }
  }

  private WalletEntity createAndGetOtherUserWallet(){

    UUID walletId = walletService.createWallet();
    UserDto userDto = new UserDto();
    userDto.setUsername("otherUser");
    userDto.setPassword("password");
    userService.createUser(userDto, walletId);

    return walletRepository.findById(walletId).get();
  }
}
