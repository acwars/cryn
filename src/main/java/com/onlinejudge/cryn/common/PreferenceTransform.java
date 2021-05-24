package com.onlinejudge.cryn.common;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.Preference;

public interface PreferenceTransform extends Refreshable {

    float getTransformedValue(Preference pref) throws TasteException;

}