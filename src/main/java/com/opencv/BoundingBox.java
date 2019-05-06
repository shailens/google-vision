package com.opencv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

public class BoundingBox {

    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat.dump());
    }

    public void boxExtraction(File inputFile, File croppedDirectory){



    }

    public Mat readImage(String imagePath){
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        Imgcodecs imageCodecs = new Imgcodecs();
        Mat matrix = imageCodecs.imread(imagePath);
        return matrix;
    }
}
