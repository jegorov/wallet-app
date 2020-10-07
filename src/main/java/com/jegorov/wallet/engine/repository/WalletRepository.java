package com.jegorov.wallet.engine.repository;

import com.jegorov.wallet.engine.data.WalletEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<WalletEntity, UUID> {
}
