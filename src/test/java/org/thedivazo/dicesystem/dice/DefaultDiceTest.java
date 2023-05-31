package org.thedivazo.dicesystem.dice;

import org.junit.jupiter.api.Test;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultDiceTest {

    DefaultDice<String> defaultDice4 = DefaultDice.<String>builder("Test1")
            .addSide("1", 1)
            .addSide("2", 1)
            .addSide("3", 1)
            .addSide("4", 1)
            .build();

    DefaultDice<String> defaultDice4BigWeight = DefaultDice.<String>builder("Test1")
            .addSide("1", 10)
            .addSide("2", 10)
            .addSide("3", 10)
            .addSide("4", 10)
            .build();

    DefaultDice<String> doubleDefaultDice4 = DefaultDice.<String>builder("Test2")
            .addSide("1", 1)
            .addSide("1", 1)
            .addSide("2", 1)
            .addSide("2", 1)
            .build();

    DefaultDice<String> unevenDefaultDice = DefaultDice.<String>builder("Test3")
            .addSide("1", 3)
            .addSide("2", 7)
            .addSide("3", 1)
            .addSide("4", 0.5)
            .build();

    DefaultDiceTest() throws WeightArgumentException {
    }


    @Test
    void getProbability() {
        assertEquals(defaultDice4.getProbability("1"), 1d/4d);
        assertEquals(defaultDice4.getProbability("4"), 1d/4d);

        assertEquals(defaultDice4BigWeight.getProbability("1"), 1d/4d);
        assertEquals(defaultDice4BigWeight.getProbability("4"), 1d/4d);

        assertEquals(doubleDefaultDice4.getProbability("1"), 1d/2d);
        assertEquals(doubleDefaultDice4.getProbability("2"), 1d/2d);

        assertEquals(unevenDefaultDice.getProbability("1"), 3d/11.5);
        assertEquals(unevenDefaultDice.getProbability("4"), 0.5d/11.5d);
    }

    @Test
    void roll() {
        Map<String, Integer> counter = new HashMap<>();
        for(int i = 0; i < 100000; i++) {
            String t = unevenDefaultDice.roll();
            if(counter.containsKey(t)) counter.put(t, counter.get(t)+1);
            else counter.put(t, 1);
        }

        for (String s : counter.keySet()) {
            System.out.printf("key: '%s' -> %s%n", s, counter.get(s));
        }
    }
}