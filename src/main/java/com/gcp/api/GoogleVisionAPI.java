package com.gcp.api;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shailendra
 * This class is used to get information about a given image file.
 */
public class GoogleVisionAPI {

    public static final Logger LOGGER = Logger.getLogger(GoogleVisionAPI.class);


    /**
     * @param command
     * @param filePath
     * @return {@code EntityAnnotation}
     */
    public List<EntityAnnotation> getEntityAnnotation(String command, String filePath) throws Exception {
        List<EntityAnnotation> entityAnnotationList = null;
        if (command == null || filePath == null) {
            LOGGER.error("FilePath or/and Command is missing.Provide Filepath and command to process.");
            return null;
        }

        switch (command) {
            case "text": {
                entityAnnotationList = detectText(filePath);
            }
            case "fullText": {
                entityAnnotationList = detectDocumentText(filePath);
            }
            default: {
                LOGGER.error("Command did not match.Provide a valid command.");
            }
        }

        return entityAnnotationList;
    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     * @throws IOException
     */

    public List<EntityAnnotation> detectText(String filePath) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);
        List<AnnotateImageResponse> responses = null;
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            responses = response.getResponsesList();
        }

        return responses.get(0).getTextAnnotationsList();

    }

    public static List<EntityAnnotation> detectDocumentText(String filePath) throws Exception {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        List<AnnotateImageResponse> responses = null;
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            responses = response.getResponsesList();
        }

        return responses.get(0).getTextAnnotationsList();
    }

}
