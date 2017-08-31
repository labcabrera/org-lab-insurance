package org.lab.insurance.portfolio.core.config;

import org.lab.insurance.domain.contract.Contract;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.amqp.AmqpOutboundEndpointSpec;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.integration.support.json.JsonObjectMapper;

@Configuration
public class IntegrationConfig {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Bean
	public JsonObjectMapper<?, ?> mapper() {
		return new Jackson2JsonObjectMapper();
	}

	@Bean
	public IntegrationFlow flow() {

		AmqpOutboundEndpointSpec outbound = Amqp.outboundGateway(amqpTemplate)
				.routingKey(Queues.PORTFOLIO_INITIALIZATION_REQUEST);

		return IntegrationFlows.from(MessageChannels.publishSubscribe(Channels.PORTFOLIO_INITIALIZATION_REQUEST)) //
				.transform(Transformers.toJson(mapper())) //
				.handle(outbound) //
				.transform(Transformers.fromJson(Contract.class, mapper())) //
				.channel(MessageChannels.direct(Channels.PORTFOLIO_INITIALIZATION_RESPONSE)) //
				.get();
	}

	// TODO mover a common
	public interface Queues {
		String PORTFOLIO_INITIALIZATION_REQUEST = "queue.portfolio-init.request";
		String PORTFOLIO_INITIALIZATION_RESPONSE = "queue.portfolio-init.response";
	}

	public interface Channels {
		String PORTFOLIO_INITIALIZATION_REQUEST = "channel.portfolio.init.request";
		String PORTFOLIO_INITIALIZATION_RESPONSE = "channel.portfolio.init.reponse";
	}
}