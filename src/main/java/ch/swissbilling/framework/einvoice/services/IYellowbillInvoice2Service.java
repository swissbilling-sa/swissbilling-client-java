package ch.swissbilling.framework.einvoice.services;

import ch.swissbilling.framework.einvoice.models.Envelope;

import java.io.File;


public interface IYellowbillInvoice2Service {
    Envelope Deserialize(File xmlFile);
}
