package moment.service;

import moment.entity.CommentEntity;
import moment.entity.PostEntity;
import moment.model.CommentItem;
import moment.repo.CommentRepo;
import moment.repo.MomentRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService implements Serializable {
    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private MomentRepo postRepo;

    public CommentItem addComment(CommentItem commentItem) {
        CommentEntity commentEntity = new CommentEntity();
        BeanUtils.copyProperties(commentItem, commentEntity);
        commentEntity.setUuid(UUID.randomUUID().toString());

        Optional<PostEntity> postEntityOptional = postRepo.findByUuid(commentItem.getPostUuid());
        if (postEntityOptional.isPresent()) {
            PostEntity postEntity = postEntityOptional.get();
            commentEntity.setPostEntity(postEntity);
            try {
                BeanUtils.copyProperties(commentItem, commentRepo.save(commentEntity));
                return commentItem;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
