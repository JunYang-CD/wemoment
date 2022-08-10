package notification.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface MessageSource {

    @Input("user_update")
    SubscribableChannel userChannel();

    @Input("post_update")
    SubscribableChannel postUpdateOutChannel();
}
