package ch.swissbilling.commercial.client.endpoints;

public class LocalhostEndpoints implements IEnvironmentEndpoints {

	@Override
	public String getGraphQlAnonymous() {
		return "http://localhost:5001/api/graphqlanonymous";
	}

	@Override
	public String getGraphQlAuthorize() {
		return "http://localhost:5001/api/graphqlauthorize";
	}

	@Override
	public String getGraphQlManagement() {
		return "http://localhost:5001/api/graphqlmanagement";
	}

	@Override
	public String getTransactionDocument(String shopId) {
		return "http://localhost:5001/api/transaction/document?shopId=" + shopId;
	}

	@Override
	public String getTransactionBatchDocumentHubNegotiate() {
		return "http://localhost:5001/transactionBatchDocument/negotiate";
	}

	@Override
	public String getTransactionBatchDocumentHubWebSocket(String connectionId) {
		return "ws://localhost:5001/transactionBatchDocument?id=" + connectionId;
	}
}
