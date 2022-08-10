package moment.repo;

import moment.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MomentRepo extends JpaRepository<PostEntity, Long> {

    @Query("SELECT p from PostEntity p where p.ownerUuid = ?1")
    Page<PostEntity> findAllPosts(String ownerUuid, Pageable pageable);

    @Query(value = "SELECT * FROM post_entity WHERE owner_uuid = ?1" +
            " or owner_uuid IN " +
            "( SELECT friend_uuid FROM friend_entity f WHERE owner_uuid = ?1)",
            nativeQuery = true)
    Page<PostEntity> findAllRelatedPosts(String ownerUuid, Pageable pageable);

    Optional<PostEntity> findByUuid(String uuid);
}
