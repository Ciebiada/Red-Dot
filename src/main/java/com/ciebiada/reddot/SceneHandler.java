/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;

import com.ciebiada.reddot.camera.Pinhole;
import com.ciebiada.reddot.camera.ThinLens;
import com.ciebiada.reddot.filter.*;
import com.ciebiada.reddot.math.Vec;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SceneHandler extends DefaultHandler {

    int width, height;
    Filter filter;
    String fileName;
    Scene scene;

    public SceneHandler(Scene scene) {
        this.scene = scene;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("image")) {
            width = Integer.valueOf(attributes.getValue("width"));
            height = Integer.valueOf(attributes.getValue("height"));
        } else if (qName.equalsIgnoreCase("filter")) {
            String type = attributes.getValue("type");

            float size = Float.valueOf(attributes.getValue("size"));

            if (type.equalsIgnoreCase("gaussian")) {
                float alpha = Float.valueOf(attributes.getValue("alpha"));
                filter = new Gaussian(size, alpha);
            } else if (type.equalsIgnoreCase("mitchell")) {
                float b = Float.valueOf(attributes.getValue("b"));
                float c = Float.valueOf(attributes.getValue("c"));
                filter = new Mitchell(size, b, c);
            } else if (type.equalsIgnoreCase("tent")) {
                filter = new Tent(size);
            } else if (type.equalsIgnoreCase("box")) {
                filter = new Box(size);
            }
        } else if (qName.equalsIgnoreCase("obj")) {
            fileName = attributes.getValue("filename");
        } else if (qName.equalsIgnoreCase("camera")) {
            String type = attributes.getValue("type");

            String[] eyeStrs = attributes.getValue("eye").split(" ");
            Vec eye = new Vec(Float.valueOf(eyeStrs[0]), Float.valueOf(eyeStrs[1]), Float.valueOf(eyeStrs[2]));

            String[] lookAtStrs = attributes.getValue("lookAt").split(" ");
            Vec lookAt = new Vec(Float.valueOf(lookAtStrs[0]), Float.valueOf(lookAtStrs[1]), Float.valueOf(lookAtStrs[2]));

            float fov = Float.valueOf(attributes.getValue("fov"));

            if (type.equalsIgnoreCase("pinhole")) {
                scene.camera = new Pinhole(eye, lookAt.sub(eye).norm(), fov, (float) height / width);
            } else if (type.equalsIgnoreCase("thinLens")) {
                float lensSize = Float.valueOf(attributes.getValue("lensSize"));
                float distance = Float.valueOf(attributes.getValue("distance"));
                scene.camera = new ThinLens(eye, lookAt.sub(eye).norm(), fov, (float) height / width, lensSize, distance);
            }
        } else if (qName.equalsIgnoreCase("reddot")) {
            Integer threads = Integer.valueOf(attributes.getValue("threads"));

            for (int i = 0; i < threads; ++i) {
                scene.threads.add(new Trace(scene, i));
            }
        }
    }
}
