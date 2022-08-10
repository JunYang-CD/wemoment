package moment.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.SubscribableChannel;

public interface MessageSource {

    @Input("user_update")
    SubscribableChannel userChannel();

    @Output("post_update")
    SubscribableChannel postUpdateOutChannel();
}
