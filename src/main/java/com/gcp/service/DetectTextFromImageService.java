package com.gcp.service;

import com.gcp.api.GoogleVisionAPI;
import com.gcp.model.BoundingMaxMin;
import com.gcp.model.EntityLite;
import com.gcp.model.VertexLite;
import com.gcp.utils.Utilities;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Vertex;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Shailendra
 */
public class DetectTextFromImageService {

    private static final Logger LOGGER = Logger.getLogger(DetectTextFromImageService.class);

    public void detectText(String command,String filePath) throws Exception{
        LOGGER.info("Call Google Vision API....");
        List<EntityAnnotation> list = new GoogleVisionAPI().getEntityAnnotation(command,filePath);
        LOGGER.info("Parse Annotation Entity to EntityLite.");
        List<EntityLite> entityLites =  parseTextFromGCPResponse(list);
        LOGGER.info("Sort entities by x-coordinate");
       // Collections.sort(entityLites, new EntityLiteComparatorByX());
        /*LOGGER.info("Sort entities by x-coordinate");
        Collections.sort(entityLites, new EntityLiteComparatorByY());*/
        LOGGER.info("Write JSON to file.");
        Utilities.convertAndWriteJsonToFile(entityLites,new File(Utilities.FILE_PATH + System.currentTimeMillis() + ".txt") );
        List<String> descriptions = new ArrayList<>();
        for(EntityLite eL:entityLites){
            descriptions.add(eL.getDescription());
        }

        Utilities.writeTextToFile(Utilities.FILE_PATH,descriptions);
        //structureData(entityLites);

    }

    public void structureData(List<EntityLite> entityLites){
        EntityLite el = entityLites.get(0);
        BoundingMaxMin bmm = new BoundingMaxMin(el.getVertexList().get(0).getX(),el.getVertexList().get(1).getX(),
                el.getVertexList().get(0).getY(),el.getVertexList().get(3).getY());

        for(int i=1;i<entityLites.size();i++){
            EntityLite entityLite = entityLites.get(i);
            int[] vertices = {entityLite.getVertexList().get(0).getX(),
                    entityLite.getVertexList().get(1).getX(),
                    entityLite.getVertexList().get(0).getY(),
                    entityLite.getVertexList().get(3).getY()};

            System.out.print(el.getDescription());

           /* if(vertices[1] + 5 > bmm.getMaxX()){
                System.out.println("\n");
            }*/
        }


    }

    /**
     * This method creates {@link EntityLite} objects
     * @param entityAnnotationList
     * @return
     * @throws Exception
     */
    public List<EntityLite> parseTextFromGCPResponse(List<EntityAnnotation> entityAnnotationList) throws Exception{
            if(entityAnnotationList==null || entityAnnotationList.size()==0)return  null;
            List<EntityLite> entities = new ArrayList<>();
            for(EntityAnnotation annotate:entityAnnotationList){
                EntityLite lite = new EntityLite();
                lite.setDescription(annotate.getDescription());
                lite.setConfidence(annotate.getScore());
                List<VertexLite> vertices = new ArrayList<>();
                for(Vertex v:annotate.getBoundingPoly().getVerticesList()){
                    VertexLite vL = new VertexLite(v.getX(),v.getY());
                    vertices.add(vL);
                }
                lite.setVertexList(vertices);
                entities.add(lite);
            }
           // Utilities.convertAndWriteJsonToFile(entities,new File(FILE_PATH+System.currentTimeMillis() + ".txt"));
            return entities;
    }


}
