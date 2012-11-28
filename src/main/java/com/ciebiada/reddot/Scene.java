/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;

import com.ciebiada.reddot.camera.Camera;
import com.ciebiada.reddot.primitive.BVH;
import com.ciebiada.reddot.primitive.OBJParser;
import com.ciebiada.reddot.primitive.Primitive;
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

    public Primitive bvh;

    public List<Primitive> lights = new ArrayList<Primitive>();

    public List<Trace> threads = new ArrayList<Trace>();

    public Scene(String filename) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        SceneHandler handler = new SceneHandler(this);
        saxParser.parse(filename, handler);

        film = new Film(handler.width, handler.height, handler.filter);

        File file = new File(filename);
        String dir = file.getParent();

        List<Primitive> prims = OBJParser.parseObj(handler.fileName, dir + "/");

        for (Primitive shape: prims)
            if (shape.getMat().isEmissive())
                lights.add(shape);

        bvh = BVH.build(prims.toArray(new Primitive[0]), 0, prims.size());
    }

    public void render() {
        for (Thread thread : threads)
            thread.start();
    }
}
