package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${advertising.exchange}")
    private String advertisingExchange;

    @Value("${advertising.queue1}")
    private String queue1;

    @Value("${advertising.queue2}")
    private String queue2;

    @Value("${advertising.queue3}")
    private String queue3;

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(advertisingExchange, true, false);
    }

    @Bean
    public Queue queue1() {
        return new Queue(queue1, true);
    }

    @Bean
    public Queue queue2() {
        return new Queue(queue2, true);
    }

    @Bean
    public Queue queue3() {
        return new Queue(queue3, true);
    }

    @Bean
    public Binding bindingQueue1(FanoutExchange fanoutExchange, Queue queue1) {
        return BindingBuilder.bind(queue1).to(fanoutExchange);
    }

    @Bean
    public Binding bindingQueue2(FanoutExchange fanoutExchange, Queue queue2) {
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }

    @Bean
    public Binding bindingQueue3(FanoutExchange fanoutExchange, Queue queue3) {
        return BindingBuilder.bind(queue3).to(fanoutExchange);
    }
}
