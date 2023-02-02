package com.wly.ecomm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * for rounding scale <= 0 use epsilon to allow for precious inaccuracy
 * But for rounding scale >= 1, this shouldn't be needed
 */
class MathUtilsTest {

    private final static double x = 12345678.12345678;
    private final static double epsilon = .00000001;

    @Test
    void round_scale0() {
        assertTrue(Math.abs(MathUtils.round(x, 0) - 12345678) < epsilon);
    }

    @Test
    void round_scale1() {
        assertEquals(12345678.1, MathUtils.round(x, 1));
    }

    @Test
    void round_scale2() {
        assertEquals(12345678.12, MathUtils.round(x, 2));
    }

    @Test
    void round_scale3() {
        assertEquals(12345678.123, MathUtils.round(x, 3));
    }

    @Test
    void round_scale4() {
        assertEquals(12345678.1235, MathUtils.round(x, 4));
    }

    @Test
    void round_scale5() {
        assertEquals(12345678.12346, MathUtils.round(x, 5));
    }

    @Test
    void round_scale6() {
        assertEquals(12345678.123457, MathUtils.round(x, 6));
    }

    @Test
    void round_scale7() {
        assertEquals(12345678.123457, MathUtils.round(x, 7));
    }

    @Test
    void round_scale_negative1() {
        assertTrue(Math.abs(MathUtils.round(x, -1) - 12345680) < epsilon);
    }

    @Test
    void round_scale_negative2() {
        assertTrue(Math.abs(MathUtils.round(x, -2) - 12345700) < epsilon);
    }

    @Test
    void round_scale_negative3() {
        assertTrue(Math.abs(MathUtils.round(x, -3) - 12346000) < epsilon);
    }

    @Test
    void round_scale_negative4() {
        assertTrue(Math.abs(MathUtils.round(x, -4) - 12350000) < epsilon);
    }

    @Test
    void round_scale_negative5() {
        assertTrue(Math.abs(MathUtils.round(x, -5) - 12300000) < epsilon);
    }

    @Test
    void round_scale_negative6() {
        assertTrue(Math.abs(MathUtils.round(x, -6) - 12000000) < epsilon);
    }

    @Test
    void round_scale_negative7() {
        assertTrue(Math.abs(MathUtils.round(x, -7) - 12000000) < epsilon);
    }

    @Test
    void getPrice() {
        for (int i = 0; i < 100_000; i++) {
            double price = MathUtils.getPrice();
            assertTrue(price >= 200);
            assertTrue(price < 1201);
        }
    }

    @Test
    void getOffPercentage() {
        for (int i = 0; i < 100_1000; i++) {
            double price = MathUtils.getOffPercentage();
            assertTrue(price >= 0);
            assertTrue(price <= 100.0);
        }
    }
}

