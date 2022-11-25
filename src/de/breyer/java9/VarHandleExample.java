package de.breyer.java9;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class VarHandleExample {

    // variable handles should be declared as static final
    private static final VarHandle PUBLIC_INT;
    private static final VarHandle PRIVATE_INT;
    private static final VarHandle INT_ARRAY;

    // initialization of variable handles should be done in static block
    static {
        try {
            PUBLIC_INT = MethodHandles.lookup()
                    .findVarHandle(VarHandleExample.class, "publicInt", int.class);

            PRIVATE_INT = MethodHandles
                    .privateLookupIn(VarHandleExample.class, MethodHandles.lookup())
                    .findVarHandle(VarHandleExample.class, "privateInt", int.class);

            INT_ARRAY = MethodHandles.arrayElementVarHandle(int[].class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // variable to use
    public int publicInt = 1;
    private int privateInt = 1;

    public void doVarHandleStuff() {
        System.out.println("PUBLIC_INT coordinated types: " + PUBLIC_INT.coordinateTypes().size());
        System.out.println("PUBLIC_INT type: " + PUBLIC_INT.coordinateTypes().get(0));
        System.out.println("PUBLIC_INT value: " + PUBLIC_INT.get(this));
        System.out.println("PRIVATE_INT coordinated types: " + PRIVATE_INT.coordinateTypes().size());
        System.out.println("PRIVATE_INT type: " + PRIVATE_INT.coordinateTypes().get(0));
        //simple change of variable
        PRIVATE_INT.set(this, 24);
        System.out.println("PRIVATE_INT value: " + PRIVATE_INT.get(this));
        System.out.println("INT_ARRAY coordinated types: " + INT_ARRAY.coordinateTypes().size());
        System.out.println("INT_ARRAY type 0: " + INT_ARRAY.coordinateTypes().get(0));
        System.out.println("INT_ARRAY type 1: " + INT_ARRAY.coordinateTypes().get(1));

        // change value only if current value is equal something
        PUBLIC_INT.compareAndSet(this, 1, 300);
        System.out.println("PUBLIC_INT value compare to 1 and set 300: " + PUBLIC_INT.get(this));
        PUBLIC_INT.compareAndSet(this, 1, 301);
        System.out.println("PUBLIC_INT value compare to 1 and set 301: " + PUBLIC_INT.get(this));

        // get value and perform add afterwards
        int before = (int) PRIVATE_INT.getAndAdd(this, 200);
        System.out.println("getAndAdd old [" + before + "] and after [" + PRIVATE_INT.get(this) + "]");

    }
}
