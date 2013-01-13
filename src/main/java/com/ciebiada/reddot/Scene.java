/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;

import com.ciebiada.reddot.camera.Camera;
import com.ciebiada.reddot.geometry.KDTree;
import com.ciebiada.reddot.geometry.OBJParser;
import com.ciebiada.reddot.geometry.TriangleRaw;
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
    public KDTree kdtree;
    public TriangleRaw[] lights;

    public int threadCount;

    public Scene(String filename) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        SceneHandler handler = new SceneHandler(this);
        saxParser.parse(filename, handler);

        File file = new File(filename);
        String dir = file.getParent();

        List<TriangleRaw> tris = OBJParser.parseObj(handler.fileName, dir + "/");

        List<TriangleRaw> lightsList = new ArrayList<TriangleRaw>();
        for (TriangleRaw tri : tris)
            if (tri.getMat().isEmissive())
                lightsList.add(tri);

        lights = lightsList.toArray(new TriangleRaw[0]);

//        kdtree = new ListAccel(tris);
//        kdtree = new BVHAccel(tris);
        kdtree = new KDTree(tris.toArray(new TriangleRaw[tris.size()]));
    }
}
