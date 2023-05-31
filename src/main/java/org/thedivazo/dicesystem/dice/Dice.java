package org.thedivazo.dicesystem.dice;

import org.jetbrains.annotations.Unmodifiable;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;

import java.util.List;

/**
 * An interface that provides methods for implementing its dice class
 * @param <T> Dice side type
 */
public interface Dice<T> {
    String getName();

    /**
     * @return unmodifiable list of sides
     */
    @Unmodifiable
    List<Side<T>> getSides();

    /**
     * @param value The value that dice contains
     * @return Returns the probability of a side with the given value from 0 to 1.
     * If there is no side with this value, 0 is returned.
     * If the dice has only this value, then 1 is returned
     */
    double getProbability(T value);

    /**
     * @return Throws dice and returns a random value, given the weight of the sides.
     */
    T roll();

    interface DiceBuilder<T> {
        DiceBuilder<T> setName(String name);
        DiceBuilder<T> setSides(List<Side<T>> sides);
        DiceBuilder<T> addSide(Side<T> side);
        DiceBuilder<T> addSide(T value, double weight) throws WeightArgumentException;
        DiceBuilder<T> clearSides();
        Dice<T> build();
    }
}
