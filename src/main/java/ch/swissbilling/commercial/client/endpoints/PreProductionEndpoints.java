package ch.swissbilling.commercial.client.endpoints;

public class PreProductionEndpoints implements IEnvironmentEndpoints {

	@Override
	public String getGraphQlAnonymous() {
		 return "https://core-pp.swissbilling.ch/api/graphqlanonymous";
	}

	@Override
	public String getGraphQlAuthorize() {
		return "https://core-pp.swissbilling.ch/api/graphqlauthorize";
	}

	@Override
	public String getGraphQlManagement() {
		return "https://core-pp.swissbilling.ch/api/graphqlmanagement";
	}

	@Override
	public String getTransactionDocument(String shopId) {
		return "https://core-pp.swissbilling.ch/api/transaction/document?shopId=" + shopId;
	}

	@Override
	public String getTransactionBatchDocumentHubNegotiate() {
		return "https://core-pp.swissbilling.ch/transactionBatchDocument/negotiate";
	}

	@Override
	public String getTransactionBatchDocumentHubWebSocket(String connectionId) {
		return "wss://core-pp.swissbilling.ch/transactionBatchDocument?id=" + connectionId;
	}
}
