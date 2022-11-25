package de.breyer.java8;

@Name(firstname = "Max", lastname = "Mustermann")
@Name(firstname = "Martina", lastname = "Musterfrau")
@Name(firstname = "Peter", lastname = "Müller")
@Name(firstname = "Richard", lastname = "Löwenherz")
public class NameGenerator {

    public static void listAll() {
        Name[] names = NameGenerator.class.getAnnotationsByType(Name.class);
        for (Name name: names) {
            System.out.println("Name: " + name.firstname() + " " + name.lastname());
        }
    }
}
