package in.misk.timed.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SlowConsumer extends SpringRouteBuilder {

    public static final String QUEUE = "embedded-from:a.queue";
    private static final String QUEUE2 = "embedded-from:another.queue";

    @Override
    public void configure() throws Exception {
        //@formatter:off
            onException(Exception.class)
                 .useOriginalMessage()
                 .log("Exception: ${body}")
                 .handled(true)
                 .to(QUEUE);
                
            // Slow consumer, limited to 1 message a second
            from(QUEUE+"?maxConcurrentConsumers=20")
                .log(".........SLOW")
                .throttle(1)
                .to(QUEUE2)
                .stop();
    
            // Messages are sent here by the slow consumer
            from(QUEUE2+"?maxConcurrentConsumers=20")
                .throttle(1)
                .log("...........................QUEUE2")
                .stop();
            //@formatter:on
    }
}
