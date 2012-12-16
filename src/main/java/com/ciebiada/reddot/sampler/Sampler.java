/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.sampler;

import com.ciebiada.reddot.math.Sample;

import java.util.Random;

public abstract class Sampler {

    public static <T> T[] shuffle(T[] table, Random random) {
        for (int i = 0; i < table.length; ++i) {
            int pick = random.nextInt(table.length);

            T tmp = table[i];
            table[i] = table[pick];
            table[pick] = tmp;
        }

        return table;
    }

    public abstract double get1dSample();

    public abstract Sample getSample();

    public abstract void reset();
}