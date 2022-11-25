package de.breyer.java14;

import java.time.DayOfWeek;

public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.switchExpressions();

        // G1 Garbage Collector is now NUMA-aware, to improve performance

        // JFR data is now exposed and can be used for continuously monitoring
    }

    private void switchExpressions() {
        System.out.println("### switch expressions ###");

        // with java 14 the enhanced switch expressions are now standardized
        System.out.println("yield switch: " + isWeekendSwitchWithYield(DayOfWeek.FRIDAY));
        System.out.println("new switch: " + isWeekendEnhancedSwitch(DayOfWeek.FRIDAY));
        System.out.println("yield switch: " + isWeekendSwitchWithYield(DayOfWeek.SUNDAY));
        System.out.println("new switch: " + isWeekendEnhancedSwitch(DayOfWeek.SUNDAY));

        System.out.println();
    }

    private boolean isWeekendSwitchWithYield(DayOfWeek day) {
        return switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                yield false;
            case SATURDAY:
            case SUNDAY:
                yield true;
        };
    }

    private boolean isWeekendEnhancedSwitch(DayOfWeek day) {
        return switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> false;
            case SATURDAY, SUNDAY -> true;
        };
    }

}
