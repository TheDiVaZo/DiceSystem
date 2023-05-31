package org.thedivazo.dicesystem.dice.exception;

public class WeightArgumentException extends Exception {
    public WeightArgumentException() {
        super("Weight must be strictly greater than zero!");
    }
}
