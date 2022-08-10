package moment.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import moment.model.PostItem;
import moment.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class MomentController {
    @Autowired
    MomentService momentService;


    @Autowired
    EurekaClient eurekaClient;

    @GetMapping("/moments2")
    public String getMoments2() {
        List<InstanceInfo> instances = eurekaClient
                .getApplication("user-service")
                .getInstances();
        if (instances != null && instances.size() > 0) {
            log.debug(instances.get(0).getAppName());
            log.debug(instances.get(0).getHostName() + ":" + instances.get(0).getPort());
            return instances.get(0).getAppName();
        }
        return "";
    }

    @PostMapping("/moments/moment")
    public PostItem createPost(@RequestHeader("uuid") String uuid, @RequestBody PostItem postItem) {
        return momentService.addMoment(uuid, postItem);
    }

    @GetMapping("/moments/moment")
    public List<PostItem> getMoments(@RequestHeader("uuid") String uuid, @RequestParam int page) {
        return momentService.getMoments(uuid, page);
    }
}
