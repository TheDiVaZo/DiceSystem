package org.thedivazo.dicesystem.dice;

import org.jetbrains.annotations.Unmodifiable;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;

import java.util.List;

public interface Dice<T> {
    String getName();

    @Unmodifiable
    List<Side<T>> getSides();

    double getProbability(T value);

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
