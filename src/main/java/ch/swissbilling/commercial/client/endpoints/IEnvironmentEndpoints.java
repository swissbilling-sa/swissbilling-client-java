package ch.swissbilling.commercial.client.endpoints;

public interface IEnvironmentEndpoints {
	String getGraphQlAnonymous();
	String getGraphQlAuthorize();
	String getGraphQlManagement();
	String getTransactionDocument(String shopId);
	String getTransactionBatchDocumentHubNegotiate();
	String getTransactionBatchDocumentHubWebSocket(String connectionId);
}
