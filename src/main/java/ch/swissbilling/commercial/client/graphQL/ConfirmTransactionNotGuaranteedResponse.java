package ch.swissbilling.commercial.client.graphQL;

import ch.swissbilling.commercial.client.models.TransactionDebtor;
import ch.swissbilling.commercial.client.models.TransactionStatus;

public class ConfirmTransactionNotGuaranteedResponse {
	public String id;
	public double amount;
	public String reference;
	public TransactionStatus status;
	public TransactionDebtor transactionDebtor;
}
