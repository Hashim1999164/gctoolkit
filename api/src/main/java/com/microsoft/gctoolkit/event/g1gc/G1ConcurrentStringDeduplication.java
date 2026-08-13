// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.
package com.microsoft.gctoolkit.event.g1gc;

import com.microsoft.gctoolkit.event.GCCause;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.time.DateTimeStamp;

/**
 * Concurrent string deduplication statistics from a G1 GC log line such as:
 * {@code 2015-10-17T21:10:23.673-0400: 3993.137: [GC concurrent-string-deduplication, 79.3K->792.0B(78.6K), avg 96.2%, 0.0024351 secs]}
 *
 * <p>String volume fields ({@link #getStartingStringVolume()}, {@link #getEndingStringVolume()}, and
 * {@link #getReduction()}) are expressed in kilobytes (KiB), matching the parser's conversion of
 * log sizes such as {@code B}, {@code K}, {@code M}, and {@code G} into K units.
 *
 * <p>{@link #getPercentReduction()} is the average reduction percentage from the log (for example,
 * {@code avg 96.2%} is represented as {@code 96.2}, not {@code 0.962}).
 */
public class G1ConcurrentStringDeduplication extends G1GCConcurrentEvent {

    /** Occupied string table size before deduplication, in kilobytes. */
    private double startingStringVolume;
    /** Occupied string table size after deduplication, in kilobytes. */
    private double endingStringVolume;
    /** Size removed by deduplication, in kilobytes. */
    private double reduction;
    /** Average reduction percentage where {@code 100.0} means 100%. */
    private double percentReduction;

    public G1ConcurrentStringDeduplication(DateTimeStamp timeStamp, double startingStringVolume, double endingStringVolume, double reduction, double percentReduction, double duration) {
        this(timeStamp, GCCause.GCCAUSE_NOT_SET, startingStringVolume, endingStringVolume, reduction, percentReduction, duration);
    }

    public G1ConcurrentStringDeduplication(DateTimeStamp timeStamp, GCCause cause, double startingStringVolume, double endingStringVolume, double reduction, double percentReduction, double duration) {
        super(timeStamp, GarbageCollectionTypes.ConcurrentStringDeduplication, cause, duration);
        this.startingStringVolume = startingStringVolume;
        this.endingStringVolume = endingStringVolume;
        this.reduction = reduction;
        this.percentReduction = percentReduction;
    }

    /** @return occupied string table size before deduplication, in kilobytes */
    public double getStartingStringVolume() {
        return startingStringVolume;
    }

    /** @return occupied string table size after deduplication, in kilobytes */
    public double getEndingStringVolume() {
        return endingStringVolume;
    }

    /** @return size removed by deduplication, in kilobytes */
    public double getReduction() {
        return reduction;
    }

    /**
     * @return average reduction percentage where {@code 100.0} means 100%
     */
    public double getPercentReduction() {
        return percentReduction;
    }


}
