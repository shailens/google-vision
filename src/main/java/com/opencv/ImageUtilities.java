package com.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

public class ImageUtilities {


    public Mat createBinaryImage(String imagePath) {
        /**
         * Load Image
         */
        Mat src = Imgcodecs.imread(imagePath);
        /**
         * Transform source image to gray if it is not already
         */
        Mat gray = createGrayImage(src);
        /**
         * binary image
         */
        Mat bw = new Mat();
        Core.bitwise_not(gray, gray);
        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, -2);

        return bw;
    }

    public Mat createGrayImage(Mat src) {
        Mat gray = new Mat();
        if (src.channels() == 3) {
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            gray = src;
        }
        return gray;
    }
}
