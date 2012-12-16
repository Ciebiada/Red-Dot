/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;

import com.ciebiada.reddot.camera.Camera;
import com.ciebiada.reddot.filter.Filter;
import com.ciebiada.reddot.primitive.BVH;
import com.ciebiada.reddot.primitive.OBJParser;
import com.ciebiada.reddot.primitive.Primitive;
import com.ciebiada.reddot.tracer.Tracer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scene {

    public Film film;
    public Camera camera;
    public Filter filter;
    public Primitive bvh;
    public Primitive[] lights;
    public List<Tracer> threads = new ArrayList<Tracer>();

    public Scene(String filename) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        SceneHandler handler = new SceneHandler(this);
        saxParser.parse(filename, handler);

        File file = new File(filename);
        String dir = file.getParent();

        List<Primitive> primitives = OBJParser.parseObj(handler.fileName, dir + "/");

        List<Primitive> lightsList = new ArrayList<Primitive>();
        for (Primitive primitive : primitives)
            if (primitive.getMat().isEmissive())
                lightsList.add(primitive);

        lights = lightsList.toArray(new Primitive[0]);
        bvh = BVH.build(primitives.toArray(new Primitive[0]), 0, primitives.size());
    }

    public void render() {
        for (Thread thread : threads)
            thread.start();
    }
}
