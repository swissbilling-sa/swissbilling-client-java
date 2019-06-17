package ch.swissbilling.commercial.client.graphQL;

import java.util.List;

import ch.swissbilling.commercial.client.models.*;

public class TransactionsByBatchIdResponse {
	public String id;
	public String reference;
	public String date;
	public String shopId;
	public String language;
	public String displayName;
	public String reservationId;

	public TransactionDebtor transactionDebtor;

	public List<TransactionItem> transactionItems;

	public double amount;

	public TransactionStatus status;
	public GuaranteeType guaranteeType;
	public int installmentPlan;
}
