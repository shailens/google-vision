package com.gcp.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

/**
 * @author Shailendra
 */
public class ImageUtilities {
    private static final String[] IMAGE_FILE_EXTENSION = {".jpg",".jpeg",".tiff",".png"};


    public static void init(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     *
     * @param input
     */
    public static String convertImageToGreyScale(String input){
        init();
        Mat source = Imgcodecs.imread(input);
        Mat destination = new Mat();
        Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);
        long filePrefix = System.currentTimeMillis();
        String destinationFilePath = "C:\\workspace\\documents\\gray-images\\gray_"+filePrefix+IMAGE_FILE_EXTENSION[0];
        Imgcodecs.imwrite(destinationFilePath, destination);
        return destinationFilePath;
    }

    public static String convertImageToBinaryImage(String filePath){
        init();
        Mat gray = getGrayMat(filePath);
        Mat bw = new Mat();
        Core.bitwise_not(gray, gray);
        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, -2);
        long filePrefix = System.currentTimeMillis();
        String destinationFilePath = "C:\\workspace\\documents\\binary-images\\binary_" + filePrefix + IMAGE_FILE_EXTENSION[0];
        Imgcodecs.imwrite(destinationFilePath,bw);
        return destinationFilePath;
    }

    public static Mat getGrayMat(String filePath){
        Mat src = Imgcodecs.imread(filePath);
        Mat gray = new Mat();
        if (src.channels() == 3) {
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            gray = src;
        }
        return gray;
    }
}
