package com.jegorov.wallet.controller;

import com.jegorov.wallet.engine.dto.TransferDto;
import com.jegorov.wallet.engine.service.UserService;
import com.jegorov.wallet.engine.service.WalletService;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

  final WalletService walletService;

  final UserService userService;

  public WalletController(
      WalletService walletService, UserService userService) {
    this.walletService = walletService;
    this.userService = userService;
  }

  @GetMapping("/balance")
  public Double balance() {
    UUID uuid = userService.getCurrentWalletId();
    return walletService.getBalance(uuid);
  }

  @PostMapping("/withdraw")
  public Double withdraw(@RequestBody Double amount) {
    UUID walletId = userService.getCurrentWalletId();
    return walletService.withdrawSomeMoney(walletId, amount);
  }

  @PostMapping("/put")
  public void put(@RequestBody Double amount) {
    UUID walletId = userService.getCurrentWalletId();
    walletService.putSomeMoney(walletId, amount);
  }

  @PostMapping("/transfer")
  public void transferByLogin(@RequestBody TransferDto transferDto) {
    UUID walletId = userService.getCurrentWalletId();
    UUID otherWalletId = userService.getWalletIdByUsername(transferDto.getUsername());
    walletService.transferByUsername(walletId, otherWalletId, transferDto.getAmount());
  }
}
