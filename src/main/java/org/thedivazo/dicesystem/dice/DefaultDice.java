package org.thedivazo.dicesystem.dice;

import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;

import java.util.*;
import java.util.stream.Stream;


/**
 * This class represents an implementation of the {@link Dice<T>} interface and is immutable.
 * The class has 2 builders ({@link DefaultDice#builder(String)} and {@link DefaultDice#toBuilder()}).
 * The first builder allows you to generate an object from scratch,
 * and the second builder allows you to generate a new object based on an existing object.
 * @param <T> Dice side type
 */
public final class DefaultDice<T> implements Dice<T> {
    private static final Random generatorRandom = new Random();

    @Getter
    private final String name;

    @Getter
    @Nullable
    private final String permission;

    // cumulative sum
    private final double cumSum;

    // unmodifiable sides
    /**
     * When creating dice, the list with sides is copied,
     * after which, based on the weight of the sides,
     * a cumulative sum is calculated using which the probability is calculated.
     * @param name Name of Dice
     * @param sides list with sides
     */
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
        this.permission = null;
    }

    private DefaultDice(String name, String permission, List<Side<T>> sides) {
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
        this.permission = permission;
    }

    @Getter
    @Unmodifiable
    private final List<Side<T>> sides;

    private final double[] cumProbability;


    public double getProbability(Object value) {
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

    /**
     * @param name Name of dice
     * @param <T> Dice side type
     * @return Returns a builder that creates a new dice object
     */
    public static <T> DiceBuilder<T> builder(String name) {
        return new DiceBuilder<>(name);
    }


    /**
     * @return Returns a builder that creates a new dice object based on an existing dice object.
     */
    public DiceBuilder<T> toBuilder() {
        return new DiceBuilder<>(this);
    }


    /**
     * @param <T> Dice side type
     */
    public static class DiceBuilder<T> implements Dice.DiceBuilder<T> {
        private String name;
        private String permission;
        private List<Side<T>> sides = new ArrayList<>();

        /**
         * @param name Name of Dice
         */
        public DiceBuilder(String name) {
            this.name = name;
        }

        /**
         * @param dice The dice to base the new dice on
         */
        public DiceBuilder(DefaultDice<T> dice) {
            this.name = dice.getName();
            this.sides = new ArrayList<>(dice.getSides());
        }

        /**
         * @param name set name of dice
         * @return current DiceBuilder
         */
        public DiceBuilder<T> setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * @param permission the permission to use dice
         * @return current DiceBuilder
         */
        public DiceBuilder<T> setPermission(String permission) {
            this.permission = permission;
            return this;
        }

        /**
         * @param sides set sides of dice
         * @return current DiceBuilder
         */
        public DiceBuilder<T> setSides(List<Side<T>> sides) {
            this.sides = sides;
            return this;
        }

        /**
         * @param side add side to dice
         * @return current DiceBuilder
         */
        public DiceBuilder<T> addSide(Side<T> side) {
            sides.add(side);
            return this;
        }

        /**
         * @param value The value that the side will contain
         * @param weight Side weight. The default weight is one. The more weight, the more likely that side will fall out.
         * @return current DiceBuilder
         * @throws WeightArgumentException If the weight is 0 or less.
         */
        public DiceBuilder<T> addSide(T value, double weight) throws WeightArgumentException {
            sides.add(new Side<>(value, weight));
            return this;
        }

        /**
         * Clean the sides of the dice
         * @return current DiceBuilder
         */
        public DiceBuilder<T> clearSides() {
            sides.clear();
            return this;
        }

        /**
         * @return builds a new dice object based on the given data.
         * All data is copied, and after copying are immutable.
         */
        public DefaultDice<T> build() {
            return new DefaultDice<>(name, sides);
        }
    }


}
