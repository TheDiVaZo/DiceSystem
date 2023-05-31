package org.thedivazo.dicesystem.dice;

import lombok.SneakyThrows;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;

import java.util.Objects;

/**
 * A class that describes the side of dice
 * @param <T> The type of the object contained in the side
 */
public class Side<T> {
    private final T value;
    private final double weight;

    /**
     * @param value object contained in the side
     * @param weight Side weight in dice
     * @throws WeightArgumentException If the weight is zero or less.
     */
    public Side(T value, double weight) throws WeightArgumentException {
        if (weight <= 0) throw new WeightArgumentException();
        this.value = value;
        this.weight = weight;
    }

    public T value() {
        return value;
    }

    public double weight() {
        return weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Side) obj;
        return Objects.equals(this.value, that.value) && this.weight == that.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, weight);
    }

}
