package com.opencv;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

public class MorphologyRun {
    private int threshold = 100;
    private Random rng = new Random(12345);

    public void run(String[] args) {
        // Check number of arguments
        if (args.length == 0) {
            System.out.println("Not enough parameters!");
            System.out.println("Program Arguments: [image_path]");
            System.exit(-1);
        }
        /**
         *  Load the image
         */
        Mat src = Imgcodecs.imread(args[0]);
        // Check if image is loaded fine
        if (src.empty()) {
            System.out.println("Error opening image: " + args[0]);
            System.exit(-1);
        }
        /**
         * Show source image
         */
        HighGui.imshow("src", src);

        /**
         * Transform source image to gray if it is not already
         */
        Mat gray = new Mat();
        if (src.channels() == 3) {
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        } else {
            gray = src;
        }
        /**
         * Show gray image
         */
        showWaitDestroy("gray", gray);
        /**
         * Apply adaptiveThreshold at the bitwise_not of gray
         */
        Mat bw = new Mat();
        Core.bitwise_not(gray, gray);
        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, -2);
        /**
         * Show binary image
         */
        showWaitDestroy("binary", bw);
        /**
         * Create the images that will use to extract the horizontal and vertical lines
         */
        Mat horizontal = bw.clone();
        Mat vertical = bw.clone();
        /**
         *  Specify size on horizontal axis
         */
        int horizontal_size = horizontal.cols() / 30;
        /**
         * Create structure element for extracting horizontal lines through morphology operations
         */
        Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontal_size, 1));
        /**
         * Apply morphology operations
         */
        Imgproc.erode(horizontal, horizontal, horizontalStructure);
        Imgproc.dilate(horizontal, horizontal, horizontalStructure);
        /**
         * Show extracted horizontal lines
         */
        showWaitDestroy("horizontal", horizontal);
        /**
         * Specify size on vertical axis
         */
        int vertical_size = vertical.rows() / 30;
        /**
         * Create structure element for extracting vertical lines through morphology operations
         */
        Mat verticalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, vertical_size));

        /**
         * Apply morphology operations
         */
        Imgproc.erode(vertical, vertical, verticalStructure);
        Imgproc.dilate(vertical, vertical, verticalStructure);
        /**
         * Show extracted vertical lines
         */
        showWaitDestroy("vertical", vertical);
        /*// Inverse vertical image
        Core.bitwise_not(vertical, vertical);
        showWaitDestroy("vertical_bit", vertical);

        // Inverse horizontal image
        Core.bitwise_not(horizontal, horizontal);
        showWaitDestroy("horizontal_bit", horizontal);*/
        /**
         * Convert to Gray scale to add channels.
         */
        /*Imgproc.cvtColor(horizontal, horizontal, Imgproc.COLOR_GRAY2BGR);
        Imgproc.cvtColor(vertical, vertical, Imgproc.COLOR_GRAY2BGR);
*/
        /**
         * Extraxt Boxes From Image. Merge fails if the channels < 3
         */

        Mat mergedImg = mergeImages(horizontal, vertical);

        /**
         * Find Contours from merged image
         */
        List<MatOfPoint> contours = findContours(mergedImg);

        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];

        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
            contours.get(i).rows();
        }

        Arrays.sort(boundRect, new Comparator<Rect>() {
            @Override
            public int compare(Rect o1, Rect o2) {
                if(o1.tl().y < o2.tl().y)return -1;
                else if(o1.tl().y > o2.tl().y)return 1;
                else return 0;
            }
        });

        int idx = 1;
        for (Rect rect : boundRect) {

            int x = rect.x;
            int y = rect.y;
            int h = rect.height;
            int w = rect.width;
            if (w > 40 && h > 30 && w > 3*h) {
                System.out.println("Creating cropped images for:[" +" x= "+x+"  y= "+y+"  w= "+w +"  h= "+h +"]");
                Rect rectCrop = new Rect(x, y, w, h);
                Mat croppedImage = new Mat(src, rectCrop);
                Imgcodecs.imwrite("C:\\workspace\\documents\\IndusInd-PNB\\Cropped\\img-"+idx+".png",croppedImage);
            }
            idx++;
        }

        System.exit(0);
    }


    private void showWaitDestroy(String winname, Mat img) {
        HighGui.imshow(winname, img);
        HighGui.moveWindow(winname, 500, 0);
        HighGui.waitKey(0);
        HighGui.destroyWindow(winname);
    }

    public Mat mergeImages(Mat img1, Mat img2) {
        double alpha = 0.5;
        double beta;
        Mat dst = new Mat();
        System.out.println(" Simple Linear Blender ");
        System.out.println("-----------------------");
        System.out.println("* Enter alpha [0.0-1.0]: ");
        beta = (1.0 - alpha);
        Core.addWeighted(img1, alpha, img2, beta, 0.0, dst);
        HighGui.imshow("Linear Blend", dst);
        HighGui.waitKey(0);
        return dst;
    }

    private List<MatOfPoint> findContours(Mat src) {
        Mat cannyOutput = new Mat();
        Imgproc.Canny(src, cannyOutput, threshold, threshold * 2);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
            Imgproc.drawContours(drawing, contours, i, color, 2, Imgproc.LINE_8, hierarchy, 0, new Point());
        }

        return contours;
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new MorphologyRun().run(new String[]{"C:\\workspace\\documents\\IndusInd-PNB\\45.jpg"});
    }
}
