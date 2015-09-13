package com.glitterlabs.caixia.util;

import com.parse.ParseGeoPoint;

import java.io.Serializable;

/**
 * Created by mohinish on 8/30/15.
 */
public class GeoPointParse extends ParseGeoPoint implements Serializable{

    public GeoPointParse(double latitude, double longitude) {
        super(latitude, longitude);
    }
}
