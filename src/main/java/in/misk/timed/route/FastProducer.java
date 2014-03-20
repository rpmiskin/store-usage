package in.misk.timed.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FastProducer extends SpringRouteBuilder {
    public static final String retryQueue = "direct:retry-queue";
    // The message that will be sent on the route
    private static final Object payLoad = new byte[1024 * 128];

    @Override
    public void configure() throws Exception {
        //@formatter:off
        
        onException(Exception.class)
            .useOriginalMessage()
            //.log("Exception - FastProducer")
            .handled(true)
//            .delay(10000)
//            .to(retryQueue)
            .stop();
            // Timed route to send the original message
            from("timer://foo?period=10", retryQueue)
               .setBody().constant(payLoad)
               .to(SlowConsumer.QUEUE);
    
            //@formatter:on
    }
}
