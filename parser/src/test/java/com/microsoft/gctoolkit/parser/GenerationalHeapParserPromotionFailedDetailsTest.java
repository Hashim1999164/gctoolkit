// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.
package com.microsoft.gctoolkit.parser;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Locks the behavior of GenerationalHeapParser#parNewPromotionFailedDetails, which collects the
 * per-thread promotion failure sizes found on a "ParNew (N: promotion failure size = X) ..." line.
 * These characterizations pin the exact values, order, and count produced for zero, one, and many
 * promotion failure size blocks so that the underlying collection strategy can be safely changed.
 */
public class GenerationalHeapParserPromotionFailedDetailsTest {

    private int[] promotionFailureSizesOf(GenerationalHeapParser parser) throws ReflectiveOperationException {
        Field field = GenerationalHeapParser.class.getDeclaredField("promotionFailureSizesForwardReference");
        field.setAccessible(true);
        return (int[]) field.get(parser);
    }

    @Test
    public void singlePromotionFailureSizeIsCaptured() throws ReflectiveOperationException {
        GenerationalHeapParser parser = new GenerationalHeapParser();
        String line = "2015-05-26T12:23:33.483-0700: 4.484: [ParNew (0: promotion failure size = 8)  (promotion failed): 19136K->19136K(19136K), 0.0300000 secs]";

        parser.parNewPromotionFailedDetails(null, line);

        assertArrayEquals(new int[]{8}, promotionFailureSizesOf(parser));
    }

    @Test
    public void multiplePromotionFailureSizesArePreservedInOrder() throws ReflectiveOperationException {
        GenerationalHeapParser parser = new GenerationalHeapParser();
        String line = "2017-04-16T12:35:12.797-0700: 142237.056: [ParNew (0: promotion failure size = 3)  (1: promotion failure size = 3)  (2: promotion failure size = 3)  (3: promotion failure size = 3)  (4: promotion failure size = 3)  (5: promotion failure size = 3)  (6: promotion failure size = 3)  (7: promotion failure size = 131074)  (8: promotion failure size = 3)  (9: promotion failure size = 3)  (10: promotion failure size = 3)  (11: promotion failure size = 3)  (12: promotion failure size = 3)  (13: promotion failure size = 3)  (14: promotion failure size = 3)  (15: promotion failure size = 3)  (16: promotion failure size = 3)  (17: promotion failure size = 3)  (18: promotion failure size = 3)  (19: promotion failure size = 3)  (20: promotion failure size = 3)  (21: promotion failure size = 3)  (22: promotion failure size = 3)  (23: promotion failure size = 3)  (24: promotion failure size = 3)  (25: promotion failure size = 4)  (26: promotion failure size = 3)  (27: promotion failure size = 3)  (promotion failed): 1611303K->1607853K(1800000K), 0.4413433 secs]";

        parser.parNewPromotionFailedDetails(null, line);

        int[] expected = {3, 3, 3, 3, 3, 3, 3, 131074, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3};
        assertArrayEquals(expected, promotionFailureSizesOf(parser));
    }

    @Test
    public void noPromotionFailureSizeBlockYieldsEmptyArrayInsteadOfThrowing() throws ReflectiveOperationException {
        GenerationalHeapParser parser = new GenerationalHeapParser();
        String line = "2014-10-24T06:04:47.413-0400: 748196.080: [ParNew (promotion failed): 19136K->19136K(19136K), 0.0300000 secs]";

        assertDoesNotThrow(() -> parser.parNewPromotionFailedDetails(null, line));

        assertArrayEquals(new int[0], promotionFailureSizesOf(parser));
    }
}
