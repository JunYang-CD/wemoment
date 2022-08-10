package gateway.controller;

import gateway.model.User;
import gateway.secure.utils.JwtTokenUtil;
import gateway.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class GatewayController {
    @Autowired
    @Qualifier("UserDetailsRepositoryReactiveAuthenticationManager")
    ReactiveAuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("feign_client_userservice")
    UserService userService;

    @RequestMapping("/gateway/login")
    public Mono<ResponseEntity> login(@RequestBody User user) {
        return Mono.from(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword())))
                .flatMap(auth -> {
                    if (auth.isAuthenticated()) {
                        return Mono.fromCallable(() -> userService.getUser(auth.getName()));
                    } else {
                        return Mono.empty();
                    }
                })
                .map(userAccount -> {
                    Map<String, Object> claims = new HashMap<>();
                    String token = "Bearer " + JwtTokenUtil.generateToken(claims, userAccount.getUuid(), 24 * 3600 * 1000);
                    MultiValueMap<String, String> headers = new HttpHeaders();
                    headers.add(HttpHeaders.AUTHORIZATION, token);
                    return new ResponseEntity(headers, HttpStatus.OK);
                })
                .log()
                .onErrorReturn(new ResponseEntity(HttpStatus.FORBIDDEN));
    }
}