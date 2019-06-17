package ch.swissbilling.commercial.client.graphQL;

import ch.swissbilling.commercial.client.models.ReservationDebtor;
import ch.swissbilling.commercial.client.models.ReservationStatus;

public class CreateReservationResponse {
	public String id;
	public String reference;
	public double requestedAmount;
	public String validityEndDate;
	public String shopId;
	public ReservationStatus status;
	
	public ReservationDebtor reservationDebtor;
}
