/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tad3.iclass.dao;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.tad3.iclass.entidad.Alumno;
import java.net.UnknownHostException;
import java.util.List;

/**
 *
 * @author francisco
 */
public class AlumnoDAO {

    MongoClient mongoClient;

    public MongoClient conexion() throws UnknownHostException {
        mongoClient = new MongoClient("localhost", 27017);

        return mongoClient;
    }

    public List listaAlumnos() throws UnknownHostException {

        MongoClient conect = conexion();

        DB database = conect.getDB("iclass");
        DBCollection coleccion = database.getCollection("iclass");

        /*
         DBCursor cursor = coleccion.find();
         try {
         while (cursor.hasNext()) {
         DBObject cur = cursor.next();
         lista.add((Alumno) cur);

         }
         } finally {
         cursor.close();
         }*/
        return coleccion.find().toArray();
    }

}
