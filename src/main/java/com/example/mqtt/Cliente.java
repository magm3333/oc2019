package com.example.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Cliente implements MqttCallback{
	private MqttClient cliente;
	
	public Cliente(String uri, String nombreCliente, String topico, String mensaje ) throws MqttException {
		MqttConnectOptions opciones=new MqttConnectOptions();
		opciones.setConnectionTimeout(0);
		cliente=new MqttClient(uri, nombreCliente);
		cliente.connect(opciones);
		cliente.setCallback(this);
		if(mensaje==null) {
			cliente.subscribe(topico);
		} else {
			MqttMessage mqttMsg=new MqttMessage();
			mqttMsg.setPayload(mensaje.getBytes());
			cliente.publish(topico, mqttMsg);
		}
	}

	public static void main(String[] args) throws MqttException {
		String nc="cliente"+(int)(Math.random()*1000000);
		new Cliente("tcp://nf1.misinergia.com:1883",nc,"micasa/habitacion1/#",null);
		//Comando mosquitto para pub
		//mosquitto_pub -t "micasa/habitacion1/luz1" -m "off"
		
		//new Cliente("tcp://localhost:1883",nc,"micasa/habitacion1/luz1","on");
		// Comando mosquitto para sub
		//  mosquitto_sub -t "micasa/habitacion1/luz1"
	}
	//Calidad de servicio
	// 0 (como mucho uno) <- defecto
	// 1 (al menos uno) 
	// 2 (exactamente uno)

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Se desconectó el cliente");
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.printf("Se recibió '%s' sobre '%s'%n",new String(message.getPayload()),topic);
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("Se envió un mensaje");
		
	}
}
