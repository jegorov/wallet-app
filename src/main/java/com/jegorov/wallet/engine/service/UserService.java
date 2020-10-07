package com.jegorov.wallet.engine.service;

import com.jegorov.wallet.engine.data.UserEntity;
import com.jegorov.wallet.engine.dto.UserDto;
import com.jegorov.wallet.engine.exception.IllegalUserException;
import com.jegorov.wallet.engine.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

  final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserEntity createUserEntity() {
    return new UserEntity();
  }

  public UUID getCurrentWalletId() {
    return getCurrentUser().getWalletId();
  }

  public UserEntity getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return ((UserEntity) authentication.getPrincipal());
  }

  public UUID getWalletIdByUsername(String login) {
    if (StringUtils.isEmpty(login)) {
      throw new IllegalArgumentException("login cannot be null");
    }
    if (getCurrentUser().getUsername().equals(login)) {
      throw new IllegalArgumentException("login cannot be the same as current user");
    }
    Optional<UserEntity> user = userRepository.findByUsername(login);
    if (user.isEmpty()) {
      throw new IllegalUserException("user is not valid");
    }
    return user.get().getWalletId();
  }

  public boolean login(UserDto dto) {
    if (StringUtils.isEmpty(dto.getPassword())) {
      throw new IllegalArgumentException("Password cannot be null");
    }
    Optional<UserEntity> user = userRepository.findByUsername(dto.getUsername());

    if (user.isEmpty()) {
      return false;
    }
    if (user.get().getPassword().equals(dto.getPassword())) {
      return true;
    }
    throw new AuthenticationCredentialsNotFoundException("Invalid password exists");
  }


  public void createUser(UserDto userDto, UUID walletId) {

    UserEntity userEntity = createUserEntity();
    userEntity.setUsername(userDto.getUsername());
    userEntity.setPassword(userDto.getPassword());
    userEntity.setWalletId(walletId);

    userRepository.save(userEntity);
  }
}
