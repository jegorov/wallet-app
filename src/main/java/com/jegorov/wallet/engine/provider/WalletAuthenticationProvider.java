package com.jegorov.wallet.engine.provider;

import com.jegorov.wallet.engine.data.UserEntity;
import com.jegorov.wallet.engine.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class WalletAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    Optional<UserEntity> user = userRepository.findByUsername(username);

    if (user.isPresent()) {

      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("ROLE"));

      return new UsernamePasswordAuthenticationToken(user.get(), password, authorities);
    }
    throw new BadCredentialsException("User not found");

  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}