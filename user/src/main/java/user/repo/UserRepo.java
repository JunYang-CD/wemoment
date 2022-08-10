package user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUuid(String uuid);

    @Query("SELECT u FROM UserEntity u WHERE u.uuid = ?1 OR u.uuid = ?2")
    List<UserEntity> findTwoByUuid(String uuid1, String uuid2);

    @Query("SELECT u FROM UserEntity u where u.uuid = ?1 OR u.phone = ?1")
    Optional<UserEntity> findByIdentity(String identity);
}
