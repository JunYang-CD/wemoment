package user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import user.entity.FriendRelationEntity;
import user.entity.UserEntity;

import java.util.List;

public interface FriendRelationRepo extends JpaRepository<FriendRelationEntity, Long> {
    @Query("SELECT f FROM FriendRelationEntity f" +
            " where" +
            " (f.owner = ?1 and f.friend = ?2)" +
            " or" +
            " (f.owner = ?2 and f.friend = ?1)")
    List<FriendRelationEntity> findTwoById(UserEntity user1, UserEntity user2);
}
