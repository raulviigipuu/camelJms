package camelJms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parsing xml > Message
 * 
 * @author raul
 */
public final class MessageXmlParser {

	static final String MESSAGE   = "message";
	static final String ID        = "id";
	static final String TIMESTAMP = "timestamp";
	static final String CONTENT   = "content";

	Logger logger = LoggerFactory.getLogger(MessageXmlParser.class);

	public Message parseMessage(String xmlMessage) {

		Message message = new Message();
		try {

			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream inStream         = new ByteArrayInputStream(xmlMessage.getBytes(StandardCharsets.UTF_8));
			XMLEventReader eventReader   = inputFactory.createXMLEventReader(inStream);

			while (eventReader.hasNext()) {

				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {

					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == MESSAGE) {

						message.id = startElement.getAttributeByName(new QName(ID)).getValue();
						message.timestamp = startElement.getAttributeByName(new QName(TIMESTAMP)).getValue();
					} else if (startElement.getName().getLocalPart() == CONTENT) {
						event = eventReader.nextEvent();
						message.content = event.asCharacters().getData();
					}
				}

				if (event.isEndElement()) {

					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == MESSAGE) {
						// only one message expected
						break;
					}
				}
			}

		} catch (XMLStreamException xse) {

			logger.error("Xml parsing error: " + xse);
		}

		return message.isValid() ? message : null;
	}
}
