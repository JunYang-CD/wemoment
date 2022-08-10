package gateway.secure;

import gateway.user.UserService;
import gateway.user.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {
    @Autowired
    @Qualifier("feign_client_userservice")
    UserService userService;

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(() ->
        {
            // This is a blocking call
            UserAccount userAccount = userService.getUser(username);
            if (userAccount != null &&
                    (userAccount.getUuid().contentEquals(username)
                            || userAccount.getPhone().contentEquals(username))) {
                return ((UserDetails) new User(
                        userAccount.getUuid(),
                        passwordEncoder.encode(userAccount.getCredential()),
                        true, true, true, true, new ArrayList<>()
                ));
            }
            return null;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
