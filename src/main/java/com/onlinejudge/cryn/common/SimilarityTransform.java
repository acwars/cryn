package com.onlinejudge.cryn.common;

import org.apache.mahout.cf.taste.common.Refreshable;

/**
 * <p>
 * Implementations encapsulate some transformation on similarity values between two things, where things might
 * be IDs of users or items or something else.
 * </p>
 */
public interface SimilarityTransform extends Refreshable {

    /**
     * @param value
     *          original similarity between thing1 and thing2 (should be in [-1,1])
     * @return transformed similarity (should be in [-1,1])
     */
    double transformSimilarity(long id1, long id2, double value);

}
