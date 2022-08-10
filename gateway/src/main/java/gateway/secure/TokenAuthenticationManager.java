package gateway.secure;

import gateway.secure.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
@Primary
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        Claims claims = JwtTokenUtil.parseJwtRsa256(authentication.getPrincipal().toString());
        Collection<? extends GrantedAuthority> roles = (Collection<? extends GrantedAuthority>) claims.get("roles");
        return Mono.just(new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                roles
        ));
    }
}
