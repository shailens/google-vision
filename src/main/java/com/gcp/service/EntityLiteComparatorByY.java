package com.gcp.service;

import com.gcp.model.EntityLite;

import java.util.Comparator;

public class EntityLiteComparatorByY implements Comparator<EntityLite> {

    @Override
    public int compare(EntityLite e1,EntityLite e2){
        if(e1.getVertexList().get(0).getY() == e2.getVertexList().get(0).getY())
            return 0;
        else if(e1.getVertexList().get(0).getY() > e2.getVertexList().get(0).getY())
            return 1;
        else
            return -1;
    }
}
