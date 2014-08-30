package camelJms;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generating reply
 * 
 * @author raul
 */
public final class MessageXmlWriter {
	
	Logger logger = LoggerFactory.getLogger(MessageXmlWriter.class);
	
	public String makeReplyMessage(Message message) {
		
		String replyXml = "";
		
		try {
			
			XMLOutputFactory outputFactory     = XMLOutputFactory.newInstance();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    XMLEventWriter eventWriter         = outputFactory.createXMLEventWriter(outputStream);
		    XMLEventFactory eventFactory       = XMLEventFactory.newInstance();
		    XMLEvent end                       = eventFactory.createDTD("\n");
		    StartDocument startDocument        = eventFactory.createStartDocument();
		    eventWriter.add(startDocument);
		    // MESSAGE_REPLY <
		    eventWriter.add(eventFactory.createStartElement("", "", "message_reply"));
		    eventWriter.add(eventFactory.createAttribute("id", message.id));
		    eventWriter.add(end);
		    
		    eventWriter.add(eventFactory.createStartElement("", "", "status"));
		    eventWriter.add(eventFactory.createCharacters("delivered"));
		    eventWriter.add(eventFactory.createEndElement("", "", "status"));
		    eventWriter.add(end);
		    
		    // MESSAGE_REPLY </
		    eventWriter.add(eventFactory.createEndElement("", "", "message_reply"));
		    eventWriter.add(end);
		    eventWriter.add(eventFactory.createEndDocument());
		    eventWriter.close();
		    
		    replyXml = outputStream.toString("UTF-8");
		    logger.debug("XML: " + replyXml);
		} catch(XMLStreamException xse) {
			
			logger.error("Error generating XML: " + xse);
		} catch(UnsupportedEncodingException uee) {
			
			logger.error("Encoding problems: " + uee);
		}
		
		return replyXml;
	}

}
