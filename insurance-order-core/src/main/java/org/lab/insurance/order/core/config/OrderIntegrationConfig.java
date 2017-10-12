package org.lab.insurance.order.core.config;

import org.lab.insurance.domain.core.insurance.Order;
import org.lab.insurance.order.core.service.MarketOrderGeneratorProcessor;
import org.lab.insurance.order.core.service.OrderFeesProcessor;
import org.lab.insurance.order.core.service.OrderValorizationScheduler;
import org.lab.insurance.order.core.service.ValueDateProcessor;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.handler.LoggingHandler.Level;

@Configuration
@ComponentScan("org.lab.insurance.order.core")
public class OrderIntegrationConfig extends AbstractOrderDslConfig {

	@Autowired
	private MarketOrderGeneratorProcessor marketOrderGeneratorProcessor;

	@Autowired
	private OrderFeesProcessor orderFeesProcessor;

	@Autowired
	private OrderValorizationScheduler valorizationScheduler;

	@Autowired
	private ValueDateProcessor valueDateProcessor;

	@Bean
	Queue orderInitializationQueue() {
		return new Queue(env.getProperty("queues.order.creation"), true, false, false);
	}

	// @Bean
	// MessageChannel paymentCreationChannel() {
	// return MessageChannels.direct().get();
	// }

	//@formatter:off
	@Bean
	IntegrationFlow orderCreationFlow() {
		return IntegrationFlows //
			.from(Amqp
				.inboundGateway(connectionFactory, amqpTemplate, orderInitializationQueue()))
			.log(Level.INFO, "Processing order request")
			.transform(Transformers.fromJson(Order.class))
			.handle(Order.class, (request, headers) -> orderMongoAdapter.read(request.getId(), Order.class))
			.handle(Order.class, (request, headers) -> stateMachineProcessor.process(request, Order.States.PROCESSING.name(), true))
			.handle(Order.class, (request, headers) -> orderFeesProcessor.process(request))
			.handle(Order.class, (request, headers) -> valueDateProcessor.process(request))
			.handle(Order.class, (request, headers) -> marketOrderGeneratorProcessor.process(request))
			.handle(Order.class, (request, headers) -> valorizationScheduler.process(request))
			.handle(Order.class, (request, headers) -> stateMachineProcessor.process(request, Order.States.PROCESSED.name(), true))
			.handle(Order.class, (request, headers) -> orderMongoAdapter.save(request))
			.transform(Transformers.toJson(mapper()))
			//TODO
			.bridge(null)
			.get();
	}
	//@formatter:on
}