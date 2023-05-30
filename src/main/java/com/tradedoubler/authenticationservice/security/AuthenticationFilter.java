package com.tradedoubler.authenticationservice.security;

import static com.tradedoubler.authenticationservice.enums.TokenType.AUTH;
import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.getAuthentication;
import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.getSubjectFromToken;
import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.isTokenValid;
import static org.springframework.util.StringUtils.hasText;

import com.tradedoubler.authenticationservice.model.User;
import com.tradedoubler.authenticationservice.services.UserService;
import com.tradedoubler.authenticationservice.utils.AuthenticationUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private final UserService userService;

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
      User user = userService.loadUserByUsername(username);
      SecurityContextHolder.getContext()
          .setAuthentication(getAuthentication(token, user));
    }
    catch (UsernameNotFoundException ignored) {
    }
  }
}


