package de.breyer.java8;

import java.lang.annotation.Repeatable;

@Repeatable(Names.class)
public @interface Name {

    String firstname();
    String lastname();
}
