package com.jegorov.wallet.engine.repository;

import com.jegorov.wallet.engine.data.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

  Optional<UserEntity> findByUsername(String username);
}
