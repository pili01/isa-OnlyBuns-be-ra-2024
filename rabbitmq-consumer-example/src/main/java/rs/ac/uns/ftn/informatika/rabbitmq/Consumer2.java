package rs.ac.uns.ftn.informatika.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer2 {

	private static final Logger log = LoggerFactory.getLogger(Consumer2.class);

	@RabbitListener(
			bindings = @QueueBinding(value = @Queue(value = "${advertising.queue2}", durable = "true"),
			exchange = @Exchange(value = "${advertising.exchange}", type = ExchangeTypes.FANOUT, durable = "true")
	))
	public void handler(String message) {
		log.info("Consumer2 primio poruku: {}", message);
	}
}