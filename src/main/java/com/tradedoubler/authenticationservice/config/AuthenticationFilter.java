package com.tradedoubler.authenticationservice.config;

import static com.tradedoubler.authenticationservice.enums.TokenType.AUTH;
import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.getAuthentication;
import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.getSubjectFromToken;
import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.isTokenValid;
import static org.springframework.util.StringUtils.hasText;

import com.tradedoubler.authenticationservice.entity.User;
import com.tradedoubler.authenticationservice.utils.AuthenticationUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String token = AuthenticationUtils.getTokenFromRequest(request);
    if (hasText(token) && isTokenValid(token, AUTH)) {
      setSecurityContext(token);
    }

    chain.doFilter(request, response);
  }

  private void setSecurityContext(String token) {
    try {
      String username = getSubjectFromToken(token);
      User user = (User) userDetailsService.loadUserByUsername(username);
      SecurityContextHolder.getContext()
          .setAuthentication(getAuthentication(token, user));
    }
    catch (UsernameNotFoundException ignored) {
    }
  }
}


