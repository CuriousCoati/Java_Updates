package de.breyer.java16;

public class ImmutableCar {

    private final String name;
    private final String manufacturer;

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public ImmutableCar(String name, String manufacturer) {
        this.name = name;
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "ImmutableCar{" +
                "name='" + getName() + '\'' +
                ", manufacturer='" + getManufacturer() + '\'' +
                '}';
    }
}
