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
package com.nextgis.mobile.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.nextgis.mobile.util.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.nextgis.mobile.util.Constants.*;

public abstract class Layer {
    protected String mName;
    protected boolean mIsVisible;
    protected short mId;
    protected int mMaxZoom;
    protected int mMinZoom;
    protected File mPath;
    protected MapBase mMap;

    public Layer(){

    }

    public Layer(MapBase map, short id, File path, JSONObject config){
        mMap = map;
        mPath = path;
        mId = id;
        fillDetails(config);
    }

    public String getName() {
        return mName;
    }

    public void setName(String newName) {
        this.mName = newName;
        mMap.onLayerChanged(this);
    }

    protected void fillDetails(JSONObject config){
        try {
            mName = config.getString(JSON_NAME_KEY);
            mMaxZoom = config.getInt(JSON_MAXLEVEL_KEY);
            mMinZoom = config.getInt(JSON_MINLEVEL_KEY);
            setVisible( config.getBoolean(JSON_VISIBILITY_KEY) );
        } catch (JSONException e){
            reportError(e.getLocalizedMessage());
        }
    }

    protected File getAbsolutePath(){
        return mPath;
    }

    public String getRelativePath(){
        return mPath.getName();
    }

    public abstract Drawable getIcon();

    protected Context getContext(){
        return mMap.getContext();
    }

    protected void reportError(String errMsg){
        Log.d(TAG, errMsg);
        Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
    }

    public short getId(){
        return mId;
    }

    public abstract int getType();

    public abstract void changeProperties();

    public boolean getVisible(){
        return mIsVisible;
    }

    public void setVisible(boolean visible){
        mIsVisible = visible;
        mMap.onLayerChanged(this);
    }

    public boolean delete(){
        return mPath.delete();
    }

    public void save(){
        try {
            JSONObject rootConfig = new JSONObject();
            rootConfig.put(JSON_NAME_KEY, mName);
            rootConfig.put(JSON_TYPE_KEY, getType());
            rootConfig.put(JSON_MAXLEVEL_KEY, mMaxZoom);
            rootConfig.put(JSON_MINLEVEL_KEY, mMinZoom);
            rootConfig.put(JSON_VISIBILITY_KEY, getVisible());
            File outFile = new File(mPath, LAYER_CONFIG);
            FileUtil.writeToFile(outFile, rootConfig.toString());
        } catch (JSONException e){
            reportError(e.getLocalizedMessage());
        } catch (IOException e){
            reportError(e.getLocalizedMessage());
        }
    }
}
