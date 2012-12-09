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

            double size = Double.valueOf(attributes.getValue("size"));

            if (type.equalsIgnoreCase("gaussian")) {
                double alpha = Double.valueOf(attributes.getValue("alpha"));
                filter = new Gaussian(size, alpha);
            } else if (type.equalsIgnoreCase("mitchell")) {
                double b = Double.valueOf(attributes.getValue("b"));
                double c = Double.valueOf(attributes.getValue("c"));
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
            Vec eye = new Vec(Double.valueOf(eyeStrs[0]), Double.valueOf(eyeStrs[1]), Double.valueOf(eyeStrs[2]));

            String[] lookAtStrs = attributes.getValue("lookAt").split(" ");
            Vec lookAt = new Vec(Double.valueOf(lookAtStrs[0]), Double.valueOf(lookAtStrs[1]), Double.valueOf(lookAtStrs[2]));

            double fov = Double.valueOf(attributes.getValue("fov"));

            if (type.equalsIgnoreCase("pinhole")) {
                scene.camera = new Pinhole(eye, lookAt.sub(eye).norm(), fov, (double) height / width);
            } else if (type.equalsIgnoreCase("thinLens")) {
                double lensSize = Double.valueOf(attributes.getValue("lensSize"));
                double distance = Double.valueOf(attributes.getValue("distance"));
                scene.camera = new ThinLens(eye, lookAt.sub(eye).norm(), fov, (double) height / width, lensSize, distance);
            }
        } else if (qName.equalsIgnoreCase("reddot")) {
            Integer threads = Integer.valueOf(attributes.getValue("threads"));

            for (int i = 0; i < threads; ++i) {
                scene.threads.add(new Trace(scene, i));
            }
        }
    }
}
