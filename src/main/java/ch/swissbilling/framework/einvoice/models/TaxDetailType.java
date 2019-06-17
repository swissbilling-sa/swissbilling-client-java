//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.26 at 06:45:41 PM EET 
//


package ch.swissbilling.framework.einvoice.models;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxDetailType", propOrder = {
    "rate",
    "amount",
    "baseAmountExclusiveTax",
    "baseAmountInclusiveTax"
})
public class TaxDetailType {

    @XmlElement(name = "Rate", required = true)
    protected BigDecimal rate;
    @XmlElement(name = "Amount", required = true)
    protected BigDecimal amount;
    @XmlElement(name = "BaseAmountExclusiveTax", required = true)
    protected BigDecimal baseAmountExclusiveTax;
    @XmlElement(name = "BaseAmountInclusiveTax")
    protected BigDecimal baseAmountInclusiveTax;

    /**
     * Gets the value of the rate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * Sets the value of the rate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRate(BigDecimal value) {
        this.rate = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the baseAmountExclusiveTax property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseAmountExclusiveTax() {
        return baseAmountExclusiveTax;
    }

    /**
     * Sets the value of the baseAmountExclusiveTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseAmountExclusiveTax(BigDecimal value) {
        this.baseAmountExclusiveTax = value;
    }

    /**
     * Gets the value of the baseAmountInclusiveTax property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseAmountInclusiveTax() {
        return baseAmountInclusiveTax;
    }

    /**
     * Sets the value of the baseAmountInclusiveTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseAmountInclusiveTax(BigDecimal value) {
        this.baseAmountInclusiveTax = value;
    }

}