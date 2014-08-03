/******************************************************************************
 * Project:  NextGIS mobile
 * Purpose:  Mobile GIS for Android.
 * Author:   Dmitry Baryshnikov (aka Bishop), polimax@mail.ru
 ******************************************************************************
 *   Copyright (C) 2014 NextGIS
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ****************************************************************************/
package com.nextgis.mobile.datasource;

public class GeoEnvelope {
    protected double mMinX;
    protected double mMaxX;
    protected double mMinY;
    protected double mMaxY;

    public GeoEnvelope(){
        mMinX = 0;
        mMaxX = 0;
        mMinY = 0;
        mMaxY = 0;
    }

    public GeoEnvelope(double minX, double maxX, double minY, double maxY){
        mMinX = minX;
        mMaxX = maxX;
        mMinY = minY;
        mMaxY = maxY;
    }

    public GeoEnvelope(GeoEnvelope env){
        mMinX = env.mMinX;
        mMaxX = env.mMaxX;
        mMinY = env.mMinY;
        mMaxY = env.mMaxY;
    }

    public final boolean isInit(){
        return mMinX != 0 || mMinY != 0 || mMaxX != 0 || mMaxY != 0;
    }

    public final GeoPoint getCenter(){
        double x = mMinX + width() / 2.0;
        double y = mMinY + height() / 2.0;
        return new GeoPoint(x, y);
    }

    public final double width(){
        return mMaxX - mMinX;
    }

    public final double height(){
        return mMaxY - mMinY;
    }

    public void adjust(double ratio){
        double w = width() / 2.0;
        double h = height() / 2.0;
        double centerX = mMinX + w;
        double centerY = mMinY + h;

        double envRatio = w / h;

        if(envRatio == ratio)
            return;

        if(ratio > envRatio) //increase width
        {
            w = h * ratio;
            mMaxX = centerX + w;
            mMinX = centerX - w;
        }
        else				//increase height
        {
            h = w / ratio;
            mMaxY = centerY + h;
            mMinY = centerY - h;
        }
    }

    public void merge( final GeoEnvelope other ) {
        if( isInit() ){
            mMinX = Math.min(mMinX, other.mMinX);
            mMaxX = Math.max(mMaxX, other.mMaxX);
            mMinY = Math.min(mMinY, other.mMinY);
            mMaxY = Math.max(mMaxY, other.mMaxY);
        }
        else{
            mMinX = other.mMinX;
            mMaxX = other.mMaxX;
            mMinY = other.mMinY;
            mMaxY = other.mMaxY;
        }
    }

    public void merge( double dfX, double dfY ) {
        if( isInit() ){
            mMinX = Math.min(mMinX, dfX);
            mMaxX = Math.max(mMaxX, dfX);
            mMinY = Math.min(mMinY, dfY);
            mMaxY = Math.max(mMaxY, dfY);
        }
        else{
            mMinX = mMaxX = dfX;
            mMinY = mMaxY = dfY;
        }
    }

    public void intersect( final GeoEnvelope other ) {
        if(intersects(other)){
            if( isInit() )
            {
                mMinX = Math.max(mMinX, other.mMinX);
                mMaxX = Math.min(mMaxX, other.mMaxX);
                mMinY = Math.max(mMinY, other.mMinY);
                mMaxY = Math.min(mMaxY, other.mMaxY);
            }
            else{
                mMinX = other.mMinX;
                mMaxX = other.mMaxX;
                mMinY = other.mMinY;
                mMaxY = other.mMaxY;
            }
        }
        else{
            mMinX = 0;
            mMaxX = 0;
            mMinY = 0;
            mMaxY = 0;
        }
    }

    public final boolean intersects(final GeoEnvelope other){
        return mMinX <= other.mMaxX && mMaxX >= other.mMinX && mMinY <= other.mMaxY && mMaxY >= other.mMinY;
    }

    public final boolean contains(final GeoEnvelope other){
        return mMinX <= other.mMinX && mMinY <= other.mMinY && mMaxX >= other.mMaxX && mMaxY >= other.mMaxY;
    }
}
