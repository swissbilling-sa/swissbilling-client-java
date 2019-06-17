package ch.swissbilling.commercial.client.endpoints;

public class ProductionEndpoints implements IEnvironmentEndpoints {

	@Override
	public String getGraphQlAnonymous() {
		 return "https://core.swissbilling.ch/api/graphqlanonymous";
	}

	@Override
	public String getGraphQlAuthorize() {
		return "https://core.swissbilling.ch/api/graphqlauthorize";
	}

	@Override
	public String getGraphQlManagement() {
		return "https://core.swissbilling.ch/api/graphqlmanagement";
	}

	@Override
	public String getTransactionDocument(String shopId) {
		return "https://core.swissbilling.ch/api/transaction/document?shopId=" + shopId;
	}

	@Override
	public String getTransactionBatchDocumentHubNegotiate() {
		return "https://core.swissbilling.ch/transactionBatchDocument/negotiate";
	}

	@Override
	public String getTransactionBatchDocumentHubWebSocket(String connectionId) {
		return "wss://core.swissbilling.ch/transactionBatchDocument?id=" + connectionId;
	}
}
