package ch.swissbilling.commercial.client.graphQL.arguments;

public class TransactionsByBatchIdAuthorizeQueryArgs {
    public String transactionBatchDocumentId;

    public TransactionsByBatchIdAuthorizeQueryArgs(String transactionBatchDocumentId){
        this.transactionBatchDocumentId = transactionBatchDocumentId;
    }
}
