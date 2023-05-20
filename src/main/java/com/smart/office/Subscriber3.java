package com.smart.office;

import org.eclipse.paho.client.mqttv3.*;

public class Subscriber3 {
	public static void main(String[] args) {
		String lightTopic = "floor/light/ID";
		String windowTopic = "floor/window/location";
		String broker = "tcp://localhost:1883";
		String clientId = "Subscriber3";

		try {
			MqttClient client = new MqttClient(broker, clientId);
			client.setCallback(new MqttCallback() {
				@Override
				public void connectionLost(Throwable cause) {
					System.out.println("Connection lost: " + cause.getMessage());
				}

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println("Received message on topic: " + topic);
					System.out.println("Message: " + message.toString());
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					// Not used in this example
				}
			});

			client.connect();

			client.subscribe(lightTopic);
			client.subscribe(windowTopic);

			System.out.println("Subscribed to topics: " + lightTopic + ", " + windowTopic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
