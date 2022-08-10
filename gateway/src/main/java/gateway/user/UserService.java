package gateway.user;

import gateway.user.model.UserAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
@Qualifier("feign_client_userservice")
public interface UserService {
    @RequestMapping("/user/{identity}")
    UserAccount getUser(@RequestParam String identity);
}
