/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tad3.iclass.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.tad3.iclass.entidad.Alumno;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
//import org.json.*;

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

    public List<Alumno> listaAlumnos() throws UnknownHostException {

        MongoClient conect = conexion();
        List<Alumno> lista = new ArrayList<>();
        DB database = conect.getDB("iclass");
        DBCollection coleccion = database.getCollection("iclass");
        DBCursor cursor = coleccion.find();
        try {
            while (cursor.hasNext()) {
                Alumno a = new Alumno();
                DBObject cur = cursor.next();
                BasicDBObject studentObj = (BasicDBObject) cur;
                a.setIdAlumno(1);
                a.setIdLugar(1);
                a.setNombre((studentObj.getString("nombre")));
                a.setApellidos((studentObj.getString("apellidos")));
                a.setEdad(27);
                a.setCurso((studentObj.getString("curso")));
                a.setEmail((studentObj.getString("email")));
                a.setPassword((studentObj.getString("password")));
                a.setFoto((studentObj.getString("foto")));
                
                lista.add(a);
                System.out.println(a.toString());
            }
        } finally {
            cursor.close();
        }
        return lista;
    }

}
