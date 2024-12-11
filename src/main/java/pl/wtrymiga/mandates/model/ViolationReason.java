package pl.wtrymiga.mandates.model;

public enum ViolationReason {

	SPEEDING("Przekroczenie prędkości"), NO_PARKING("Zakaz parkowania"), RED_LIGHT("Przejazd na czerwonym świetle"),
	OTHER("Inne");

	private final String name;

	ViolationReason(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
