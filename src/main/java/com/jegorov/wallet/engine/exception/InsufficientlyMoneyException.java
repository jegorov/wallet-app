package com.jegorov.wallet.engine.exception;

public class InsufficientlyMoneyException extends RuntimeException {

  public InsufficientlyMoneyException(String message) {
    super(message);
  }
}
