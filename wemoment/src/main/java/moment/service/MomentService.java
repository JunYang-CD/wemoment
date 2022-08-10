package moment.service;

import moment.entity.CommentEntity;
import moment.entity.PostEntity;
import moment.model.CommentItem;
import moment.model.PostItem;
import moment.mq.MessageSource;
import moment.repo.MomentRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@EnableBinding(MessageSource.class)
public class MomentService {
    @Autowired
    private MomentRepo momentRepo;

    @StreamListener("user_update")
    public void onReceive(String msg) {
        System.out.println(msg);
    }

    @Autowired
    MessageSource messageSource;

    public List<PostItem> getMoments(String userUuid, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "post_time");
        Pageable pageRequest = PageRequest.of(page, 20, sort);

        Page<PostEntity> postEntityPage = momentRepo.findAllRelatedPosts(userUuid, pageRequest);
        List<PostEntity> postEntities = postEntityPage.getContent();
        List<PostItem> postItems = new ArrayList<>();
        for (PostEntity postEntity : postEntities) {
            PostItem postItem = new PostItem();
            BeanUtils.copyProperties(postEntity, postItem);
            List<CommentItem> commentItems = new ArrayList<>();
            for(CommentEntity commentEntity: postEntity.getComments()) {
                CommentItem commentItem = new CommentItem();
                BeanUtils.copyProperties(commentEntity, commentItem);
                commentItems.add(commentItem);
            }
            postItem.setComments(commentItems);
            postItems.add(postItem);
        }
        return postItems;
    }


    public PostItem addMoment(String ownerUuid, PostItem postItem) {
        PostEntity postEntity = new PostEntity();
        BeanUtils.copyProperties(postItem, postEntity);
        postEntity.setUuid(UUID.randomUUID().toString());
        postEntity.setOwnerUuid(ownerUuid);
        Date date = new Date();
        postEntity.setPostTime(new Timestamp(date.getTime()));
        try {
            PostEntity savedPostEntity = momentRepo.save(postEntity);
            BeanUtils.copyProperties(savedPostEntity, postItem);
            messageSource.postUpdateOutChannel().send(
                    MessageBuilder
                            .withPayload(postItem.getSubject())
                            .build());
            return postItem;
        } catch (Exception e) {
            return null;
        }
    }
}
