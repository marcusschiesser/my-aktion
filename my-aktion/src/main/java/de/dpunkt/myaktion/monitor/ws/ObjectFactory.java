
package de.dpunkt.myaktion.monitor.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import de.dpunkt.myaktion.model.Spende;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.dpunkt.myaktion.monitor.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SendSpende_QNAME = new QName("http://ws.monitor.myaktion.dpunkt.de/", "sendSpende");
    private final static QName _SendSpendeResponse_QNAME = new QName("http://ws.monitor.myaktion.dpunkt.de/", "sendSpendeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.dpunkt.myaktion.monitor.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendSpendeResponse }
     * 
     */
    public SendSpendeResponse createSendSpendeResponse() {
        return new SendSpendeResponse();
    }

    /**
     * Create an instance of {@link SendSpende }
     * 
     */
    public SendSpende createSendSpende() {
        return new SendSpende();
    }

    /**
     * Create an instance of {@link Spende }
     * 
     */
    public Spende createSpende() {
        return new Spende();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSpende }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.monitor.myaktion.dpunkt.de/", name = "sendSpende")
    public JAXBElement<SendSpende> createSendSpende(SendSpende value) {
        return new JAXBElement<SendSpende>(_SendSpende_QNAME, SendSpende.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSpendeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.monitor.myaktion.dpunkt.de/", name = "sendSpendeResponse")
    public JAXBElement<SendSpendeResponse> createSendSpendeResponse(SendSpendeResponse value) {
        return new JAXBElement<SendSpendeResponse>(_SendSpendeResponse_QNAME, SendSpendeResponse.class, null, value);
    }

}
