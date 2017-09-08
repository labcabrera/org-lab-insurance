package org.lab.insurance.domain;

public interface IntegrationConstants {

	public interface Queues {
		String ContractCreation = "contract-create.request";
		String ContractApprobation = "contract-approbation.request";
		String PortfolioInitialization = "portfolio-create.request";
		String PortfolioInitializationResponse = "portfolio-create.response";
		String PortfolioInitializationError = "portfolio-create.error";
		String OrderCreationRequest = "order-create.request";
		String ContractInitialDocRequest = "contract-create-doc.request";
		String InitialPaymentReception = "initial-payment-reception.request";
	}

	public interface Channels {
		String ContractCreationRequest = "contract-create-channel.request";
		String ContractCreationResponse = "contract-create-channel.reponse";
		String ContractApprobationRequest = "contract-approbation-channel.request";
		String ContractApprobationResponse = "contract-approbation-channel.response";
		String PortfolioInitializationRequest = "portfolio-initialization.request";
		String PortfolioInitializationResponse = "portfolio-iinitialization.response";
		String InitialPaymentReceptionRequest = "initial-payment-reception.request";
		String InitialPaymentReceptionResponse = "initial-payment-reception.response";
	}

}
