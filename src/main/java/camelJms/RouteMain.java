package camelJms;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class
 * 
 * @author raul
 */
public final class RouteMain {
	
	Logger logger = LoggerFactory.getLogger(RouteMain.class);

	public static void main(String[] args) throws Exception {

		new RouteMain().init();
	}
	
	// Setting up MQ connection and Camel route
	private final void init() throws Exception {
		
		CamelContext camelContext = new DefaultCamelContext();
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		
		camelContext.addComponent("activemq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		camelContext.addRoutes(new RouteBuilder() {
			public void configure() {
				from("activemq:queue:TEST.in").process(new ReplyProcessor()).to("activemq:queue:TEST.out");
			}
		});
		
		camelContext.start();
		logger.info("Everything up and running!");
	}
}
