/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.geometry;

import com.ciebiada.reddot.material.Diffuse;
import com.ciebiada.reddot.material.Light;
import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.material.Phong;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Vec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OBJParser {

    public static List<TriangleRaw> parseObj(String filename, String dir) {
        List<TriangleRaw> list = new ArrayList<TriangleRaw>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(dir + filename));

            HashMap<String, Material> mtl = null;
            ArrayList<Vec> verts = new ArrayList<Vec>();
            ArrayList<Vec> norms = new ArrayList<Vec>();
            Vec[] normsLocal = null;
            int vertCount = 0;
            Material mat = null;
            boolean finishedVerts = false;
            String line;
            boolean smooth = false;
            while ((line = in.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens[0].equals("mtllib")) {
                    mtl = parseMtl(tokens[1], dir);
                } else if (tokens[0].equals("v")) {
                    if (finishedVerts) {
                        verts.clear();
                        finishedVerts = false;
                    }
                    verts.add(new Vec(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
                    ++vertCount;
                } else if (tokens[0].equals("vn")) {
                    norms.add(new Vec(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
                } else if (tokens[0].equals("usemtl")) {
                    mat = mtl.get(tokens[1]);
                } else if (tokens[0].equals("s")) {
                    if (tokens[1].equals("off") || tokens[1].equals("0")) {
                        smooth = false;
                    } else {
                        smooth = true;
                    }
                } else if (tokens[0].equals("f")) {
                    if (!finishedVerts) {
                        if (smooth)
                            normsLocal = new Vec[verts.size()];

                        finishedVerts = true;
                    }

                    String[] p0 = tokens[1].split("/");
                    String[] p1 = tokens[2].split("/");
                    String[] p2 = tokens[3].split("/");

                    int p0idx = Integer.valueOf(p0[0]) - (vertCount - verts.size()) - 1;
                    int p1idx = Integer.valueOf(p1[0]) - (vertCount - verts.size()) - 1;
                    int p2idx = Integer.valueOf(p2[0]) - (vertCount - verts.size()) - 1;

                    if (smooth) {
                        int n0idx = Integer.valueOf(p0[2]) - 1;
                        int n1idx = Integer.valueOf(p1[2]) - 1;
                        int n2idx = Integer.valueOf(p2[2]) - 1;
                        if (normsLocal[p0idx] == null) normsLocal[p0idx] = norms.get(n0idx);
                        if (normsLocal[p1idx] == null) normsLocal[p1idx] = norms.get(n1idx);
                        if (normsLocal[p2idx] == null) normsLocal[p2idx] = norms.get(n2idx);
                    }

                    list.add(new TriangleRaw(verts.get(p0idx), verts.get(p1idx), verts.get(p2idx), mat));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static HashMap<String, Material> parseMtl(String filename, String dir) {
        HashMap<String, Material> map = new HashMap<String, Material>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(dir + filename));

            String line;
            String key = null;
            int ns = 0;
            Col diffuse = null;
            Col specular = null;
            boolean glass = false;
            double ior = 1;

            while ((line = in.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens[0].equals("newmtl")) {
                    key = tokens[1];
                } else if (tokens[0].equals("Ns")) {
                    ns = (int) Math.floor(Double.valueOf(tokens[1]));
                } else if (tokens[0].equals("Kd")) {
                    diffuse = new Col(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3]));
                } else if (tokens[0].equals("Ks")) {
                    specular = new Col(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3]));
                } else if (tokens[0].equals("d")) {
                    double d = Double.valueOf(tokens[1]);
                    ior = d + 1.0f;
                    glass = (d < 1);
                } else if (tokens[0].equals("illum")) {
                    switch (Integer.valueOf(tokens[1])) {
                        case 0:
                            map.put(key, new Light(diffuse));
                            break;
                        case 1:
                            map.put(key, new Diffuse(diffuse));
                            break;
                        case 2:
                            map.put(key, new Phong(diffuse, specular, ns));
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
