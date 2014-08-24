package lando.systems.ld30.desktop;

import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Ian McNamara <ian.mcnamara@doit.wisc.edu>
 * Date: 8/23/14 @ 11:41 PM
 */
public class SVGConverter {

//    private final String SVN_DIRECTORY_STRING = "../../art/svg-to-parse";
    private final String patternStr = "polygon points=\"([\\d\\.\\s\\,]*)\"";
    private Pattern p = Pattern.compile(patternStr);
    private Matcher m;

    public void run(String svnProcessingDir) throws Exception {
        File svnDirectory = new File(svnProcessingDir);
        File[] svgFiles = svnDirectory.listFiles();
        for (File svgFile : svgFiles) {
            if (!svgFile.isDirectory() &&
                    svgFile.getName().contains(".svg")) {
                this.processSVGFile(svgFile);
            }
        }
    }

    private void processSVGFile(File svgFile) throws Exception {
        System.out.println("Received file: " + svgFile.getName());
        String name = svgFile.getName();
        name = name.replace(".svn", "");
        // Read the file to get the string.
        String svgString = "";
        Scanner s = new Scanner(svgFile);
        while (s.hasNext()) {
            svgString += " " + s.next();
        }
        // Get the polygon point string(s)
        ArrayList<String> polygonPointStrings = new ArrayList<String>();
        String thisPolygonPointString;
        m = p.matcher(svgString);
        while (m.find()) {
            thisPolygonPointString = m.group(1);
            thisPolygonPointString = thisPolygonPointString.trim();
            polygonPointStrings.add(thisPolygonPointString);
        }
        System.out.println("Found " + polygonPointStrings.size() + " polygon string(s).");
        // Convert the polygonString into an array of Vector2 objects
        Vector2[][] polygonVectors = new Vector2[polygonPointStrings.size()][];
        for (int i = 0; i < polygonPointStrings.size(); i++) {
            polygonVectors[i] = convertPolygonPointStringToVector2Array(polygonPointStrings.get(i));
        }
        System.out.println("here");

        // TODO: Take this array of Vector2 arrays and output it into a file so that it can be accessed as a
        // variable thing.

        // Use the file name to name each polygon vector collection
        /**
         * E.g.
         *
         * for file worldWallA
         *      for polygon 1 in said file output
         *
         *      Vector2[] worldWallA01Vectors = new Vector2[4] {new Vector2(x,y), new Vector2(x,y), etc.}
         */



    }

    private Vector2[] convertPolygonPointStringToVector2Array(String polygonPointString) {
        Vector2[] points;
        String[] pointPairs = polygonPointString.split(" ");
        points = new Vector2[pointPairs.length];
        String[] v;
        for (int i = 0; i < pointPairs.length; i++) {
            v = pointPairs[i].split(",");
            points[i] = new Vector2(Float.parseFloat(v[0]), Float.parseFloat(v[1]));
        }
        return points;
    }

//    private static void processSVGFiles() {
//        System.out.println("SVG");
//
//        FileReader fr;
//        Scanner scanner;
//        String fileString;
//        String patternStr = "polygon points=\"([\\d\\.\\s\\,]*)\"";
//        Pattern p = Pattern.compile(patternStr);
//        ArrayList<File> svgFiles = new ArrayList<File>();
//        ArrayList<String> fileStrings = new ArrayList<String>();
//        Matcher m;
//        String patternCapture;
//        ArrayList<String> patternCaptures = new ArrayList<String>();
//
//        try {
//
//            final File svnToParseDir = new File("../../art/svg-to-parse");
//            for (final File thisFile : svnToParseDir.listFiles()) {
//                if (!thisFile.isDirectory() &&
//                        thisFile.getName().contains(".svg")) {
//                    svgFiles.add(thisFile);
//                }
//            }
//            String temp;
//            for (final File thisFile : svgFiles) {
//                fileString = "";
//                scanner = new Scanner(thisFile);
//                while (scanner.hasNext()) {
//                    fileString += " " + scanner.next();
//                }
//                fileStrings.add(fileString);
//            }
//
//            for (String thisFileString : fileStrings) {
//                m = p.matcher(thisFileString);
//                while (m.find()) {
//                    patternCapture = m.group(1);
//                    patternCapture = patternCapture.trim();
//                    patternCaptures.add(patternCapture);
//                }
//            }
//
//
//
//        } catch (Exception e) {
//            System.out.println("Failed to automatically parse/convert svg files.");
//        }
//
//        for (String s : patternCaptures) {
//            System.out.println(s);
//        }
//
//
//
//    }

}
