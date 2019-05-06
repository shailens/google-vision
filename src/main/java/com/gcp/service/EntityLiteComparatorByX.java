package com.gcp.service;

import com.gcp.model.EntityLite;

import java.util.Comparator;

public class EntityLiteComparatorByX implements Comparator<EntityLite> {

    public int compare(EntityLite e1,EntityLite e2){
        if(e1.getVertexList().get(0).getX() == e2.getVertexList().get(0).getX())
            return 0;
        else if(e1.getVertexList().get(0).getX() > e2.getVertexList().get(0).getX())
            return 1;
        else
            return -1;
    }
}
