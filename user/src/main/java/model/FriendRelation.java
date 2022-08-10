package model;

import lombok.Data;

@Data
public class FriendRelation {
    private String nickName;
    private String status;
    private UserInfo friendInfo;
}
