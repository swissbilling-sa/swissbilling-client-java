package ch.swissbilling.framework.einvoice.services;

import ch.swissbilling.framework.einvoice.models.Envelope;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YellowbillInvoice2Service implements IYellowbillInvoice2Service {
    /***
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(YellowbillInvoice2Service.class.getName());

    public YellowbillInvoice2Service() {

    }

    @Override
    public Envelope Deserialize(File xmlFile) {

        try {
            var jaxbContext = JAXBContext.newInstance(Envelope.class);
            var jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            var envelope = (Envelope) jaxbUnmarshaller.unmarshal(xmlFile);

            return envelope;
        } catch (JAXBException e) {
            LOGGER.log(Level.SEVERE, "Failed to parse yellowbill xml", e);
        }

        return null;
    }
}
