package com.jegorov.wallet.controller;

import com.jegorov.wallet.engine.dto.UserDto;
import com.jegorov.wallet.engine.service.UserService;
import com.jegorov.wallet.engine.service.WalletService;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

  final UserService userService;
  final WalletService walletService;

  public SecurityController(UserService userService, WalletService walletService) {
    this.userService = userService;
    this.walletService = walletService;
  }

  @PostMapping("/login")
  public UserDto login(@RequestBody UserDto userDto) {

    if (userService.login(userDto)) {
      return userDto;
    }
    UUID walletId = walletService.createWallet();
    userService.createUser(userDto, walletId);
    return userDto;
  }
}
