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

    public DBCollection collection(MongoClient conect) {
        DB database = conect.getDB("iclass");
        DBCollection coleccion = database.getCollection("alumno");
        return coleccion;
    }

    /*
     Traer lista de alumnos de mongo
     */
    public List<Alumno> listaAlumnos() throws UnknownHostException {

        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        DBCursor cursor = coleccion.find();
        List<Alumno> lista = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                Alumno a = new Alumno();
                DBObject cur = cursor.next();
                BasicDBObject studentObj = (BasicDBObject) cur;
                a.setIdAlumno((studentObj.getString("idAlumno")));
                a.setIdLugar((studentObj.getString("idLugar")));
                a.setNombre((studentObj.getString("nombre")));
                a.setApellidos((studentObj.getString("apellidos")));
                a.setEdad((studentObj.getString("edad")));
                a.setCurso((studentObj.getString("curso")));
                a.setEmail((studentObj.getString("email")));
                a.setPassword((studentObj.getString("password")));
                a.setFoto((studentObj.getString("foto")));

                lista.add(a);
                //System.out.println(a.toString());
            }
        } finally {
            cursor.close();
            conect.close();
        }
        return lista;
    }

    /*
     Traer datos de un alumno en particular
     */
    public Alumno alumno(String correo) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", correo);
        DBObject user = coleccion.findOne(query);
        Alumno a = new Alumno();
        BasicDBObject studentObj = (BasicDBObject) user;
        a.setIdAlumno((studentObj.getString("idAlumno")));
        a.setIdLugar((studentObj.getString("idLugar")));
        a.setNombre((studentObj.getString("nombre")));
        a.setApellidos((studentObj.getString("apellidos")));
        a.setEdad((studentObj.getString("edad")));
        a.setCurso((studentObj.getString("curso")));
        a.setEmail((studentObj.getString("email")));
        a.setPassword((studentObj.getString("password")));
        a.setFoto((studentObj.getString("foto")));

        return a;
    }

    /*
     Comprobar si existe alumno
     */
    public boolean existe(String correo, String pass) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", correo).append("password", pass);
        DBObject user = coleccion.findOne(query);

        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    /*
     Borrar alumno
     */
    public void borrar(String correo) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        coleccion.remove(new BasicDBObject("email", correo));
    }

    /*
     Crear un alumno nuevo
     */
    public boolean crear(Alumno a) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject objeto = new BasicDBObject();
        
        
        objeto.put("idAlumno", a.getIdAlumno());
        objeto.put("idLugar", a.getIdLugar());
        objeto.put("nombre", a.getNombre());
        objeto.put("apellidos", a.getApellidos());
        objeto.put("edad", a.getEdad());
        objeto.put("curso", a.getCurso());
        objeto.put("email", a.getEmail());
        objeto.put("password", a.getPassword());
        objeto.put("foto", a.getFoto());
        coleccion.insert(objeto);
        
        return true;
    }

    /*
     Actualizar
     */
    public void modificar(Alumno a1, Alumno a2) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);

        DBObject query = new BasicDBObject("email", a1.getEmail());
        DBObject doc2 = new BasicDBObject();
        doc2.put("idAlumno", a2.getIdAlumno());
        doc2.put("idLugar", a2.getIdLugar());
        doc2.put("nombre", a2.getNombre());
        doc2.put("apellidos", a2.getApellidos());
        doc2.put("edad", a2.getEdad());
        doc2.put("curso", a2.getCurso());
        doc2.put("email", a2.getEmail());
        doc2.put("password", a2.getPassword());
        doc2.put("foto", a2.getFoto());
        coleccion.update(query, doc2);
    }
}
