package com.smart.office;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class RoomPublisher {
	public static void main(String[] args) {
		String topicTemperature = "floor/room/temperature";
		String topicHumidity = "floor/room/humidity";
		String broker = "tcp://localhost:1883";
		String clientId = "RoomPublisher";
		boolean cleanSession = false;
		try {
			MqttClient client = new MqttClient(broker, clientId);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(cleanSession);
			if (!cleanSession) {
				connOpts.setWill("floor/room/status", "Offline".getBytes(), 2, true);
			}
			client.connect(connOpts);
			while (true) {
				double temperature = getRandomTemperature();
				double humidity = getRandomHumidity();

				String temperatureMessage = String.format("%.2f C", temperature);
				String humidityMessage = String.format("%.2f%%", humidity);

				publishMessage(client, topicTemperature, temperatureMessage);
				publishMessage(client, topicHumidity, humidityMessage);

				Thread.sleep(1000); // Wait for 1 second
			}
		} catch (MqttException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void publishMessage(MqttClient client, String topic, String message) throws MqttException {
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());
		client.publish(topic, mqttMessage);
		System.out.println("Published message: " + message + " on topic: " + topic);
	}

	private static double getRandomTemperature() {
		return Math.random() * 10 + 15; // Generate random temperature between 15 and 25
	}

	private static double getRandomHumidity() {
		return Math.random() * 30 + 40; // Generate random humidity between 40% and 70%
	}
}
