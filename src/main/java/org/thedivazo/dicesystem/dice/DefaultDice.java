package org.thedivazo.dicesystem.dice;

import lombok.*;
import org.jetbrains.annotations.Unmodifiable;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;

import java.util.*;
import java.util.stream.Stream;

public final class DefaultDice<T> implements Dice<T> {
    private static final Random generatorRandom = new Random();

    @Getter
    private final String name;

    private final double cumSum;

    @Getter
    @Unmodifiable
    private final List<Side<T>> sides;

    private final double[] cumProbability;

    @SafeVarargs
    private DefaultDice(String name, Side<T>... sides) {
        this.name = name;
        this.sides = List.of(sides);
        this.cumProbability = new double[sides.length];
        double localCumSum = 0;
        for (int i = 0; i < sides.length; i++) {
            Side<T> side = sides[i];
            localCumSum += side.weight();
            cumProbability[i] = localCumSum;
        }
        this.cumSum = localCumSum;
    }

    private DefaultDice(String name, List<Side<T>> sides) {
        this.name = name;
        this.sides = List.copyOf(sides);
        this.cumProbability = new double[sides.size()];
        double localCumSum = 0;
        for (int i = 0; i < sides.size(); i++) {
            Side<T> side = sides.get(i);
            localCumSum += side.weight();
            cumProbability[i] = localCumSum;
        }
        this.cumSum = localCumSum;
    }

    public double getProbability(T value) {
        Stream<Side<T>> filtered = sides.stream().filter(side -> side.value().equals(value));
        return filtered.mapToDouble(Side::weight).sum() / cumSum;
    }

    public T roll() {
        double random = generatorRandom.nextDouble(cumSum);
        for (int i = 0; i < cumProbability.length; i++) {
            if(random <= cumProbability[i]) return sides.get(i).value();
        }
        return sides.get(sides.size()-1).value();
    }

    public static <T> DiceBuilder<T> builder(String name) {
        return new DiceBuilder<>(name);
    }

    public DiceBuilder<T> toBuilder() {
        return new DiceBuilder<>(this);
    }

    public static class DiceBuilder<T> implements Dice.DiceBuilder<T> {
        private String name;
        private List<Side<T>> sides = new ArrayList<>();

        public DiceBuilder(String name) {
            this.name = name;
        }

        public DiceBuilder(DefaultDice<T> dice) {
            this.name = dice.getName();
            this.sides = new ArrayList<>(dice.getSides());
        }

        public DiceBuilder<T> setName(String name) {
            this.name = name;
            return this;
        }

        public DiceBuilder<T> setSides(List<Side<T>> sides) {
            this.sides = sides;
            return this;
        }

        public DiceBuilder<T> addSide(Side<T> side) {
            sides.add(side);
            return this;
        }

        public DiceBuilder<T> addSide(T value, double weight) throws WeightArgumentException {
            sides.add(new Side<>(value, weight));
            return this;
        }

        public DiceBuilder<T> clearSides() {
            sides.clear();
            return this;
        }

        public DefaultDice<T> build() {
            return new DefaultDice<>(name, sides);
        }
    }


}
