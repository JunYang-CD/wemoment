package moment.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("user-service")
public interface UserService {
    @RequestMapping("/user/{id}")
    String getUser(@PathVariable String id);
}
