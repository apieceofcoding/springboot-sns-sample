package com.apiece.springboot_sns_sample.api;

import com.apiece.springboot_sns_sample.api.auth.AuthLoginRequest;
import com.apiece.springboot_sns_sample.api.auth.AuthLoginResponse;
import com.apiece.springboot_sns_sample.api.auth.AuthSessionInfo;
import com.apiece.springboot_sns_sample.api.auth.AuthSessionsResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SessionRegistry sessionRegistry;

    @PostMapping("/api/v1/login")
    public AuthLoginResponse login(@RequestBody AuthLoginRequest request, HttpServletRequest httpRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        HttpSession session = httpRequest.getSession(true);
        Object principal = requireNonNull(authentication.getPrincipal());
        sessionRegistry.registerNewSession(session.getId(), principal);

        String username = ((UserDetails) principal).getUsername();
        return new AuthLoginResponse(session.getId(), username);
    }

    @GetMapping("/api/v1/sessions")
    public AuthSessionsResponse getAllSessions() {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        List<AuthSessionInfo> sessions = allPrincipals.stream()
                .flatMap(principal -> sessionRegistry.getAllSessions(principal, false).stream())
                .map(sessionInfo -> AuthSessionInfo.from(
                        sessionInfo.getSessionId(),
                        sessionInfo.getPrincipal(),
                        sessionInfo.getLastRequest(),
                        sessionInfo.isExpired()
                ))
                .collect(Collectors.toList());

        return new AuthSessionsResponse(sessions.size(), sessions);
    }

}
