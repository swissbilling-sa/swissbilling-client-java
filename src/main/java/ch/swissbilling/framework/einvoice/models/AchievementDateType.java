//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.26 at 06:45:41 PM EET 
//


package ch.swissbilling.framework.einvoice.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AchievementDateType", propOrder = {
    "startDateAchievement",
    "endDateAchievement"
})
public class AchievementDateType {

    @XmlElement(name = "StartDateAchievement", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDateAchievement;
    @XmlElement(name = "EndDateAchievement", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDateAchievement;

    /**
     * Gets the value of the startDateAchievement property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDateAchievement() {
        return startDateAchievement;
    }

    /**
     * Sets the value of the startDateAchievement property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDateAchievement(XMLGregorianCalendar value) {
        this.startDateAchievement = value;
    }

    /**
     * Gets the value of the endDateAchievement property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDateAchievement() {
        return endDateAchievement;
    }

    /**
     * Sets the value of the endDateAchievement property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDateAchievement(XMLGregorianCalendar value) {
        this.endDateAchievement = value;
    }

}