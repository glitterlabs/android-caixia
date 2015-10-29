package com.glitterlabs.caixia.util;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import com.parse.ParseGeoPoint;

import java.io.Serializable;


/**
 * Documentation ??
 */
public class GeoPointParse extends ParseGeoPoint implements Serializable{

    public GeoPointParse(double latitude, double longitude) {
        super(latitude, longitude);
    }
}
