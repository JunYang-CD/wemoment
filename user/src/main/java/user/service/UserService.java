package user.service;

import model.FriendRelation;
import model.User;
import model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import user.entity.FriendRelationEntity;
import user.entity.UserEntity;
import user.mq.MessageSource;
import user.repo.FriendRelationRepo;
import user.repo.UserRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableBinding(MessageSource.class)
public class UserService {
    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRepo userRepo;

    @Autowired
    FriendRelationRepo friendRelationRepo;

    Logger logger = LoggerFactory.getLogger(getClass());

    public void sendMessage(String message) {
        messageSource.userChannel().send(
                MessageBuilder.withPayload(message).build());
    }

    public boolean addUser(User user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setUuid(UUID.randomUUID().toString());
        try {
            userRepo.save(userEntity);
            return true;
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean createOrUpdateFriendRelation(String uuid1, String uuid2, String status) {
        UserEntity userEntity1, userEntity2;
        List<UserEntity> userEntities = userRepo.findTwoByUuid(uuid1, uuid2);
        if (userEntities == null || userEntities.size() != 2) {
            logger.error("add friend failed due to user no exists");
            return false;
        }
        userEntity1 = userEntities.get(0);
        userEntity2 = userEntities.get(1);

        FriendRelationEntity friendRelationEntity1, friendRelationEntity2;

        List<FriendRelationEntity> friendEntities = friendRelationRepo.findTwoById(userEntity1, userEntity2);
        if (friendEntities == null || friendEntities.size() == 0) {
            friendRelationEntity1 = new FriendRelationEntity();
            friendRelationEntity1.setFriend(userEntity2);
            friendRelationEntity1.setOwner(userEntity1);

            friendRelationEntity2 = new FriendRelationEntity();
            friendRelationEntity2.setFriend(userEntity1);
            friendRelationEntity2.setOwner(userEntity2);
        } else {
            friendRelationEntity1 = friendEntities.get(0);
            friendRelationEntity2 = friendEntities.get(1);
        }

        friendRelationEntity1.setStatus(status);
        friendRelationRepo.save(friendRelationEntity1);

        friendRelationEntity2.setStatus(status);
        friendRelationRepo.save(friendRelationEntity2);

        return true;
    }

    public User getUserByIdentity(String identity) {
        Optional<UserEntity> userOptional = userRepo.findByIdentity(identity);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            return user;
        }
        return null;
    }

    public UserInfo getFriends(String identity) {
        Optional<UserEntity> userOptional = userRepo.findByIdentity(identity);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(userEntity, userInfo);
            List<FriendRelation> friendRelations = new ArrayList<>();
            for (FriendRelationEntity friendRelationEntity : userEntity.getFriends()) {
                FriendRelation friendRelation = new FriendRelation();

                UserEntity friendEntity = friendRelationEntity.getFriend();
                UserInfo friendInfo = new UserInfo();
                BeanUtils.copyProperties(friendEntity, friendInfo);

                friendRelation.setStatus(friendRelationEntity.getStatus());
                friendRelation.setNickName(friendRelationEntity.getNickName());
                friendRelation.setFriendInfo(friendInfo);

                friendRelations.add(friendRelation);
            }
            userInfo.setFriendRelations(friendRelations);
            return userInfo;
        }
        return null;
    }
}
