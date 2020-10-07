package com.jegorov.wallet.engine.service;

import com.jegorov.wallet.engine.data.WalletEntity;
import com.jegorov.wallet.engine.exception.ElementNotFoundException;
import com.jegorov.wallet.engine.exception.InsufficientlyMoneyException;
import com.jegorov.wallet.engine.exception.InvalidArgumentException;
import com.jegorov.wallet.engine.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  private static final String USER_NOT_FOUND_MESSAGE = "Wallet not found";
  private static final String INSUFFICIENTLY_MONEY_IN_WALLET_MESSAGE = "Insufficiently money in wallet";
  private static final String NEGATIVE_VALUE_MESSAGE = "not valid argument. Argument must be positive value";

  final WalletRepository walletRepository;

  public WalletService(
      WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public WalletEntity createWalletEntity() {
    return new WalletEntity();
  }

  public void putSomeMoney(UUID id, Double amount) {
    if (amount <= 0) {
      throw new InvalidArgumentException(NEGATIVE_VALUE_MESSAGE);
    }
    WalletEntity walletEntity = walletRepository.findById(id)
        .orElseThrow(() -> new ElementNotFoundException(USER_NOT_FOUND_MESSAGE));

    Double newAmount = walletEntity.getFunds() + formatDouble(amount);
    walletEntity.setFunds(newAmount);
    walletRepository.save(walletEntity);
  }

  public double withdrawSomeMoney(UUID id, Double amount) {
    if (amount < 0) {
      throw new InvalidArgumentException(NEGATIVE_VALUE_MESSAGE);
    }
    WalletEntity walletEntity = walletRepository.findById(id)
        .orElseThrow(() -> new ElementNotFoundException(USER_NOT_FOUND_MESSAGE));

    withdraw(walletEntity, amount);
    return amount;
  }

  public double getBalance(UUID id) {
    WalletEntity accountEntity = walletRepository.findById(id)
        .orElseThrow(() -> new ElementNotFoundException(USER_NOT_FOUND_MESSAGE));

    return accountEntity.getFunds();
  }

  public void transferByUsername(UUID walletId, UUID toUserWalletId, Double amount) {
    WalletEntity walletEntity = walletRepository.findById(walletId)
        .orElseThrow(() -> new ElementNotFoundException(USER_NOT_FOUND_MESSAGE));

    WalletEntity toUserWalletEntity = walletRepository.findById(toUserWalletId)
        .orElseThrow(() -> new ElementNotFoundException(USER_NOT_FOUND_MESSAGE));

    withdraw(walletEntity, amount);
    putToWallet(toUserWalletEntity, amount);
  }

  public UUID createWallet() {
    WalletEntity walletEntity = createWalletEntity();
    walletRepository.save(walletEntity);
    return walletEntity.getId();
  }

  private void putToWallet(WalletEntity walletEntity, Double amount) {
    Double newAmount = BigDecimal.valueOf(walletEntity.getFunds())
        .add(BigDecimal.valueOf(formatDouble(amount))).doubleValue();
    walletEntity.setFunds(newAmount);
    walletRepository.save(walletEntity);
  }

  private void withdraw(WalletEntity walletEntity, Double amount) {
    if (walletEntity.getFunds() < amount) {
      throw new InsufficientlyMoneyException(INSUFFICIENTLY_MONEY_IN_WALLET_MESSAGE);
    }
    Double newAmount = BigDecimal.valueOf(walletEntity.getFunds())
        .subtract(BigDecimal.valueOf(formatDouble(amount))).doubleValue();
    walletEntity.setFunds(newAmount);
    walletRepository.save(walletEntity);
  }

  private Double formatDouble(Double amount) {
    return Math.floor(amount * 100) / 100.0;
  }

}
