package ch.swissbilling.commercial.client.graphQL;

import ch.swissbilling.commercial.client.models.TransactionDebtor;
import ch.swissbilling.commercial.client.models.TransactionStatus;

public class CreateTransactionResponse {
    public String id;
    public double amount;
    public String reference;
    public TransactionStatus status;
    public TransactionDebtor transactionDebtor;

    /***
     * Used for mapping json response
     */
    public CreateTransactionResponse(){}

    public CreateTransactionResponse(UpdateTransactionResponse updatedTransaction) {
        this.id = updatedTransaction.id;
        this.amount = updatedTransaction.amount;
        this.reference = updatedTransaction.reference;
        this.status = updatedTransaction.status;
        this.transactionDebtor = updatedTransaction.transactionDebtor;
    }
}
