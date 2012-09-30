
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

    private final static QName _ReceiveSpende_QNAME = new QName("http://ws.monitor.myaktion.dpunkt.de/", "receiveSpende");
    private final static QName _ReceiveSpendeResponse_QNAME = new QName("http://ws.monitor.myaktion.dpunkt.de/", "receiveSpendeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.dpunkt.myaktion.monitor.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReceiveSpende }
     * 
     */
    public ReceiveSpende createReceiveSpende() {
        return new ReceiveSpende();
    }

    /**
     * Create an instance of {@link ReceiveSpendeResponse }
     * 
     */
    public ReceiveSpendeResponse createReceiveSpendeResponse() {
        return new ReceiveSpendeResponse();
    }

    /**
     * Create an instance of {@link Spende }
     * 
     */
    public Spende createSpende() {
        return new Spende();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveSpende }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.monitor.myaktion.dpunkt.de/", name = "receiveSpende")
    public JAXBElement<ReceiveSpende> createReceiveSpende(ReceiveSpende value) {
        return new JAXBElement<ReceiveSpende>(_ReceiveSpende_QNAME, ReceiveSpende.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveSpendeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.monitor.myaktion.dpunkt.de/", name = "receiveSpendeResponse")
    public JAXBElement<ReceiveSpendeResponse> createReceiveSpendeResponse(ReceiveSpendeResponse value) {
        return new JAXBElement<ReceiveSpendeResponse>(_ReceiveSpendeResponse_QNAME, ReceiveSpendeResponse.class, null, value);
    }

}
