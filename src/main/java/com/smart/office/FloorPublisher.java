package com.smart.office;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class FloorPublisher {
	public static void main(String[] args) {
		String topicLight = "floor/light/ID";
		String topicWindow = "floor/window/location";
		String broker = "tcp://localhost:1883";
		String clientId = "FloorPublisher";
		int numMessages = 200; // Number of messages to publish
		boolean cleanSession = false;
		try {
			MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(cleanSession);

			if (!cleanSession) {
				connOpts.setWill("floor/publisher/status", "Offline".getBytes(), 2, true);
			}
			client.connect(connOpts);
			for (int i = 0; i < numMessages; i++) {
				String lightStatus = getRandomLightStatus();
				String windowStatus = getRandomWindowStatus();

				publishMessage(client, topicLight, lightStatus);
				publishMessage(client, topicWindow, windowStatus);
				Thread.sleep(500); // Wait for 500 milliseconds
			}
			client.disconnect();
			System.out.println("Disconnected");
			client.close();
		} catch (MqttException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void publishMessage(MqttClient client, String topic, String message) throws MqttException {
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());
		client.publish(topic, mqttMessage);
		System.out.println("Published message: " + message + " on topic: " + topic);
	}

	private static String getRandomLightStatus() {
		return Math.random() < 0.5 ? "ON" : "OFF"; // Randomly generate light status
	}

	private static String getRandomWindowStatus() {
		return Math.random() < 0.5 ? "OPEN" : "CLOSE"; // Randomly generate window status
	}
}
