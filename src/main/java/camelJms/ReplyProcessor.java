package camelJms;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camel processor to generate reply message
 * 
 * @author raul
 */
public final class ReplyProcessor implements Processor {
	
	Logger logger = LoggerFactory.getLogger(ReplyProcessor.class);
	
	MessageXmlParser messageXmlParser = new MessageXmlParser();
	MessageXmlWriter messageXmlWriter = new MessageXmlWriter();

	@Override
	public void process(Exchange exchange) throws Exception {

		logger.debug("ReplyProcessor started");
		
		String messageBody = exchange.getIn().getBody(String.class);
		logger.debug("Message body dump: " + messageBody);
		
		String reply = makeReply(messageBody);
		
		exchange.getIn().setBody(reply, String.class);
	}
	
	// Parsing message and generating reply
	private final String makeReply(String xmlMessage) {
		
		Message message = messageXmlParser.parseMessage(xmlMessage);
		if(message == null) {
			
			logger.error("Error parsing incoming message! Message: " + xmlMessage);
		}
		
		String reply = messageXmlWriter.makeReplyMessage(message);
		if(reply == null || reply.length() == 0) {
			logger.debug("Empty reply message.");
		}
		logger.debug("REPLY: " + reply);
		
		return reply;
	}
}
