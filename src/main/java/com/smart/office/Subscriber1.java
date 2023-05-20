package com.smart.office;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Subscriber1 {
	public static void main(String[] args) {
		String topic = "floor/room/temperature";
		String broker = "tcp://localhost:1883";
		String clientId = "Subscriber1";

		try {
			MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			client.connect(connOpts);

			client.subscribe(topic, (t, message) -> {
				System.out.println("Received message on topic: " + t);
				System.out.println("Temperature: " + new String(message.getPayload()));
			});

			System.out.println("Subscribed to topic: " + topic);
			System.out.println("Connected...");

			// Keep the main thread running to allow the subscriber to receive messages
			while (true) {
				Thread.sleep(1000); // Delay to prevent the thread from consuming excessive CPU
			}
		} catch (MqttException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
