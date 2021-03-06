package com.gcp;

import com.gcp.service.DetectTextFromImageService;
import com.gcp.utils.ImageUtilities;

public class RunApplication {

    public static void main(String[] args) throws Exception{
        String filePath = "C:\\workspace\\documents\\";
        String grayImagePath = ImageUtilities.convertImageToBinaryImage(filePath);
        new DetectTextFromImageService().detectText("fullText",grayImagePath);
    }
}
