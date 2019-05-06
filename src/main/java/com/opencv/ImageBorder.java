package com.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;

import java.util.Random;

public class ImageBorder {

    public Mat addBorderToImage(Mat src) {
        // Declare the variables
        Mat  dst = new Mat();
        int top, bottom, left, right;
        int borderType = Core.BORDER_CONSTANT;
        String window_name = "copyMakeBorder Demo";
        Random rng;

        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default ../data/lena.jpg] \n");
            System.exit(-1);
        }
        // Brief how-to for this program
        System.out.println("\n" +
                "\t copyMakeBorder Demo: \n" +
                "\t -------------------- \n" +
                " ** Press 'c' to set the border to a random constant value \n" +
                " ** Press 'r' to set the border to be replicated \n" +
                " ** Press 'ESC' to exit the program \n");
        HighGui.namedWindow( window_name, HighGui.WINDOW_AUTOSIZE );
        // Initialize arguments for the filter
        top = (int) (0.05*src.rows()); bottom = top;
        left = (int) (0.05*src.cols()); right = left;
        while( true ) {
            rng = new Random();
            Scalar value = new Scalar( rng.nextInt(256),
                    rng.nextInt(256), rng.nextInt(256) );
            Core.copyMakeBorder( src, dst, top, bottom, left, right, borderType, value);
            HighGui.imshow( window_name, dst );
            char c = (char) HighGui.waitKey(500);
            c = Character.toLowerCase(c);
            if( c == 27 )
            { break; }
            else if( c == 'c' )
            { borderType = Core.BORDER_CONSTANT;}
            else if( c == 'r' )
            { borderType = Core.BORDER_REPLICATE;}
        }
      return src;
    }
}
