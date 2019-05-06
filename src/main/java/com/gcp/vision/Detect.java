package com.gcp.vision;

import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Detect {

    /**
     * Detects entities, sentiment, and syntax in a document using the Vision API.
     *
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    public static void main(String[] args) throws Exception, IOException {
        argsHelper(args, System.out);
    }

    /**
     * Helper that handles the input passed to the program.
     *
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    public static void argsHelper(String[] args, PrintStream out) throws Exception, IOException {
        if (args.length < 1) {
            out.println("Usage:");
            out.printf(
                    "\tmvn exec:java -DDetect -Dexec.args=\"<command> <path-to-image>\"\n"
                            + "\tmvn exec:java -DDetect -Dexec.args=\"ocr <path-to-file> <path-to-destination>\""
                            + "\n"
                            + "Commands:\n"
                            + "\tfaces | labels | landmarks | logos | text | safe-search | properties"
                            + "| web | web-entities | web-entities-include-geo | crop | ocr \n"
                            + "| object-localization \n"
                            + "Path:\n\tA file path (ex: ./resources/wakeupcat.jpg) or a URI for a Cloud Storage "
                            + "resource (gs://...)\n"
                            + "Path to File:\n\tA path to the remote file on Cloud Storage (gs://...)\n"
                            + "Path to Destination\n\tA path to the remote destination on Cloud Storage for the"
                            + " file to be saved. (gs://BUCKET_NAME/PREFIX/)\n");
            return;
        }
        String command = args[0];
        String path = args.length > 1 ? args[1] : "";

        switch (command) {
            case "logos": {
                detectLogos(path, out);
                break;
            }
            case "text": {
                detectText(path, out);
                break;
            }
            case "properties": {
                detectProperties(path, out);
                break;
            }
            case "fulltext": {
                detectDocumentText(path, out);
                break;
            }
            case "object-localization": {
                detectLocalizedObjects(path, out);
                break;
            }
            default:
                System.out.println("No args found.");

        }
    }

    /**
     * Detects logos in the specified local image.
     *
     * @param filePath The path to the local file to perform logo detection on.
     * @param out      A {@link PrintStream} to write detected logos to.
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    // [START vision_logo_detection]
    public static void detectLogos(String filePath, PrintStream out) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.LOGO_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
                    out.println(annotation.getDescription());
                }
            }
        }
    }
    // [END vision_logo_detection]

    /**
     * Detects text in the specified image.
     *
     * @param filePath The path to the file to detect text in.
     * @param out      A {@link PrintStream} to write the detected text to.
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    // [START vision_text_detection]
    public static void detectText(String filePath, PrintStream out) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();



            System.out.println(responses);
            /*for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                 for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    out.printf("Text: %s\n", annotation.getDescription());
                    out.printf("Position : %s\n", annotation.getBoundingPoly());
                }
            }*/
        }
    }
    // [END vision_text_detection]

    /**
     * Detects image properties such as color frequency from the specified local image.
     *
     * @param filePath The path to the file to detect properties.
     * @param out      A {@link PrintStream} to write
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    // [START vision_image_property_detection]
    public static void detectProperties(String filePath, PrintStream out) throws Exception,
            IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.IMAGE_PROPERTIES).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
                for (ColorInfo color : colors.getColorsList()) {
                    out.printf(
                            "fraction: %f\nr: %f, g: %f, b: %f\n",
                            color.getPixelFraction(),
                            color.getColor().getRed(),
                            color.getColor().getGreen(),
                            color.getColor().getBlue());
                }
            }
        }
    }
    // [END vision_image_property_detection]



    /**
     * Performs document text detection on a local image file.
     *
     * @param filePath The path to the local file to detect document text on.
     * @param out      A {@link PrintStream} to write the results to.
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    // [START vision_fulltext_detection]
    public static void detectDocumentText(String filePath, PrintStream out) throws Exception,
            IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                TextAnnotation annotation = res.getFullTextAnnotation();
                for (Page page : annotation.getPagesList()) {
                    String pageText = "";
                    for (Block block : page.getBlocksList()) {
                        String blockText = "";
                        for (Paragraph para : block.getParagraphsList()) {
                            String paraText = "";
                            for (Word word : para.getWordsList()) {
                                String wordText = "";
                                for (Symbol symbol : word.getSymbolsList()) {
                                    wordText = wordText + symbol.getText();
                                    out.format("Symbol text: %s (confidence: %f)\n", symbol.getText(),
                                            symbol.getConfidence());
                                }
                                out.format("Word text: %s (confidence: %f)\n\n", wordText, word.getConfidence());
                                paraText = String.format("%s %s", paraText, wordText);
                            }
                            // Output Example using Paragraph:
                            out.println("\nParagraph: \n" + paraText);
                            out.format("Paragraph Confidence: %f\n", para.getConfidence());
                            blockText = blockText + paraText;
                        }
                        pageText = pageText + blockText;
                    }
                }
                out.println("\nComplete annotation:");
                out.println(annotation.getText());
            }
        }
    }
    // [END vision_fulltext_detection]

    /**
     * Detects localized objects in the specified local image.
     *
     * @param filePath The path to the file to perform localized object detection on.
     * @param out      A {@link PrintStream} to write detected objects to.
     * @throws Exception   on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    public static void detectLocalizedObjects(String filePath, PrintStream out)
            throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION))
                        .setImage(img)
                        .build();
        requests.add(request);

        // Perform the request
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            // Display the results
            for (AnnotateImageResponse res : responses) {
                for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
                    out.format("Object name: %s\n", entity.getName());
                    out.format("Confidence: %s\n", entity.getScore());
                    out.format("Normalized Vertices:\n");
                    entity
                            .getBoundingPoly()
                            .getNormalizedVerticesList()
                            .forEach(vertex -> out.format("- (%s, %s)\n", vertex.getX(), vertex.getY()));
                }
            }
        }
    }
    // [END vision_localize_objects]


}
