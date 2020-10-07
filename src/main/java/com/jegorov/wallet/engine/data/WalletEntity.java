package com.jegorov.wallet.engine.data;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "wallet")
@EqualsAndHashCode
public class WalletEntity implements Serializable {

  @Id @GeneratedValue
  UUID id;

  @Column(nullable = false)
  Double funds = 0D;
}
