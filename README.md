# Simple Apache Camel JMS route demo

Java, Maven, ActiveMQ

Send one jms message and get result back from another queue.
There is also some bare hands XML parsing and generating(**StAX**) involved and the result is a **executable JAR** with all the dependencies.
Changes in application config will apply automatically without restarting.

## Running

		mvn package
		java -jar target/camelJms-jar-with-dependencies.jar

## Conf

Configuration loading order:
<ol>
<li>From environment variable: <strong>camelJms-conf</strong></li>
<li>From command line argument: <strong>--conf=/some/path/to/config.properties</strong></li>
<li>From default location: <strong>src/main/resources/config.properties</strong>.</li>
</ol>

## Example messages

### Incoming
		<message id="123" timestamp="1409425388043">
			<content>Something something</content>
		</message>

### outgoing

		<message_reply id="123">
			<status>delivered</status>
		</message_reply>