package org.ecsoya.fabric;

import com.google.gson.Gson;

public class Car {

	private String color;
	private String make;
	private String model;
	private String owner;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static Car fromJSON(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Car.class);
	}

	public static void main(String[] args) {
		Car demo = new Car();
		demo.color = "Red";
		demo.make = "Toto";
		demo.model = "Nano";
		demo.owner = "Valeria";

		String json = demo.toString();
		System.out.println(json);

		System.out.println(Car.fromJSON(json));

		String value = "{\"color1\":\"Red\",\"make1\":\"Toto\",\"model\":\"Nano\",\"owner\":\"Valeria\"}";

		System.out.println(Car.fromJSON(value));
	}
}
