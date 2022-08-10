package user.controller;

import model.CreateFriendRelationRequest;
import model.UpdateFriendRelationRequest;
import model.User;
import model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/add")
    public String addUser(@RequestBody User user) {
        if (userService.addUser(user)) {
            return user.getName();
        } else {
            return "{}";
        }
    }

    @GetMapping("/user/{identity}")
    public User getUserByIdentity(@PathVariable String identity) {
        return userService.getUserByIdentity(identity);
    }

    @PostMapping("/user/friends")
    public List<String> addFriends(@RequestHeader("uuid") String curUuid, @RequestBody CreateFriendRelationRequest createFriendRelationRequest) {
        List<String> addedFriends = new ArrayList<>();
        for (String uuid : createFriendRelationRequest.getUuids()) {
            if (userService.createOrUpdateFriendRelation(curUuid, uuid, "apply")) {
                addedFriends.add(uuid);
            }
        }
        return addedFriends;
    }

    @PutMapping("/user/friends")
    public String acceptOrRejectFriend(@RequestHeader("uuid") String curUuid, @RequestBody UpdateFriendRelationRequest req) {
        if (userService.createOrUpdateFriendRelation(curUuid, req.getUuid(), req.getStatus())) {
            return req.getUuid();
        }
        return "{}";
    }

    @DeleteMapping("/user/friends")
    public List<String> deleteFriend() {
        return null;
    }

    @GetMapping("/user/friends")
    public UserInfo getAllFriends(@RequestHeader("uuid") String userIdentity) {
        return userService.getFriends(userIdentity);
    }

}
