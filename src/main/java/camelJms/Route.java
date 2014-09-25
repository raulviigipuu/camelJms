package camelJms;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import camelJms.conf.AppConf;
import camelJms.conf.ConfChangeListener;

public final class Route {

	final ConfChangeListener confChangeListener;
	final CamelContext camelContext;

	Logger logger = LoggerFactory.getLogger(Route.class);

	public Route(final CamelContext camelContext, final ConfChangeListener confChangeListener) throws Exception {

		this.camelContext = camelContext;
		this.confChangeListener = confChangeListener;
	}

	public void loadRoutes() throws Exception {

		if(camelContext.hasComponent("activemq") != null) {
			camelContext.stopRoute("route1");
			camelContext.removeRoute("route1");
			camelContext.removeComponent("activemq");
		}

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(buildUrl());
		camelContext.addComponent("activemq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() {
				from("activemq:queue:" + AppConf.getInstance().getConfValue("in-queue")).
				routeId("route1").
				process(new ReplyProcessor()).
				to("activemq:queue:" + AppConf.getInstance().getConfValue("out-queue"));
			}
		});

	}

	// Constructing the ActiveMQ url
	private String buildUrl() {

		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("tcp://");
		urlBuilder.append(AppConf.getInstance().getConfValue("activemq-url"));
		urlBuilder.append(":");
		urlBuilder.append(AppConf.getInstance().getConfValue("activemq-port"));

		return urlBuilder.toString();
	}
}
