package org.lab.insurance.portfolio.core.config;

import org.lab.insurance.domain.core.IntegrationConstants.Queues;
import org.lab.insurance.domain.core.contract.Contract;
import org.lab.insurance.portfolio.core.service.PortfolioInitializacionService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.integration.support.json.JsonObjectMapper;

@Configuration
@ComponentScan("org.lab.insurance.portfolio.core")
public class PortfolioIntegrationConfig {

	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private PortfolioInitializacionService initializationService;
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Bean
	public JsonObjectMapper<?, ?> mapper() {
		return new Jackson2JsonObjectMapper();
	}

	@Bean
	public Queue portfolioInitializationRequest() {
		return new Queue(Queues.PortfolioInitialization, true, false, false);
	}

	//@formatter:off
	@Bean
	public IntegrationFlow portfolioInitializacionFlow() {
		return IntegrationFlows //
			.from(Amqp
				.inboundGateway(connectionFactory, amqpTemplate, portfolioInitializationRequest()))
			.log(Level.INFO, "Processing portfolio initialization request")
			.transform(Transformers.fromJson(Contract.class))
			.handle(Contract.class, (request, headers) -> initializationService.initialize(request))
			.transform(Transformers.toJson(mapper()))
			.get();
	}
	//@formatter:on
}