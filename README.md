#Simple Apache Camel JMS route demo

Java, Maven, ActiveMQ

There is also some bare hands XML parsing and generating(**StAX**) involved and the result is a **executable JAR** with all the dependencies.

## Running

		mvn package
		java -jar target/camelJms-jar-with-dependencies.jar

## Example messages

### Incoming
		<message id="123" timestamp="1409425388043">
			<content>Something something</content>
		</message>

### outgoing

		<message_reply id="123">
			<status>delivered</status>
		</message_reply>