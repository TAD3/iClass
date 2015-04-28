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
import com.tad3.iclass.entidad.Profesor;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juanlu
 */
public class ProfesorDAO {

    MongoClient mongoClient;

    public MongoClient conexion() throws UnknownHostException {
        mongoClient = new MongoClient("localhost", 27017);
        return mongoClient;
    }

    public DBCollection collection(MongoClient conect) {
        DB database = conect.getDB("iclass");
        DBCollection coleccion = database.getCollection("profesor");
        return coleccion;
    }

    /*
     Traer lista de alumnos de mongo
     */
    public List<Profesor> listaProfesores() throws UnknownHostException {

        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        DBCursor cursor = coleccion.find();
        List<Profesor> lista = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                Profesor p = new Profesor();
                DBObject cur = cursor.next();
                BasicDBObject professorObj = (BasicDBObject) cur;
                p.setIdProfesor((professorObj.getString("idProfesor")));
                p.setIdLugar((professorObj.getString("idLugar")));
                p.setNombre((professorObj.getString("nombre")));
                p.setApellidos((professorObj.getString("apellidos")));
                p.setEdad((professorObj.getString("edad")));
                p.setEmail((professorObj.getString("email")));
                p.setMovil((professorObj.getString("movil")));
                p.setPassword((professorObj.getString("password")));
                p.setHorario((professorObj.getString("horario")));
                p.setDescripcion((professorObj.getString("descripcion")));
                p.setEvaluacion((professorObj.getString("evaluacion")));
                p.setNumVotos((professorObj.getString("numVotos")));
                p.setFoto((professorObj.getString("foto")));

                lista.add(p);
                //System.out.println(p.toString());
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
    public Profesor profesor(String correo) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", correo);
        DBObject user = coleccion.findOne(query);
        Profesor p = new Profesor();
        BasicDBObject professorObj = (BasicDBObject) user;
        p.setIdProfesor((professorObj.getString("idProfesor")));
        p.setIdLugar((professorObj.getString("idLugar")));
        p.setNombre((professorObj.getString("nombre")));
        p.setApellidos((professorObj.getString("apellidos")));
        p.setEdad((professorObj.getString("edad")));
        p.setEmail((professorObj.getString("email")));
        p.setMovil((professorObj.getString("movil")));
        p.setPassword((professorObj.getString("password")));
        p.setHorario((professorObj.getString("horario")));
        p.setDescripcion((professorObj.getString("descripcion")));
        p.setEvaluacion((professorObj.getString("evaluacion")));
        p.setNumVotos((professorObj.getString("numVotos")));
        p.setFoto((professorObj.getString("foto")));

        return p;
    }

    /*
     Comprobar si existe alumno
     */
    public boolean existe(String correo, String pass) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", correo).append("password", pass);
        DBObject user = coleccion.findOne(query);

        return user != null;
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
    public boolean crear(Profesor p) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject professorObj = new BasicDBObject();

        p.setIdProfesor((professorObj.getString("idProfesor")));
        p.setIdLugar((professorObj.getString("idLugar")));
        p.setNombre((professorObj.getString("nombre")));
        p.setApellidos((professorObj.getString("apellidos")));
        p.setEdad((professorObj.getString("edad")));
        p.setEmail((professorObj.getString("email")));
        p.setMovil((professorObj.getString("movil")));
        p.setPassword((professorObj.getString("password")));
        p.setHorario((professorObj.getString("horario")));
        p.setDescripcion((professorObj.getString("descripcion")));
        p.setEvaluacion((professorObj.getString("evaluacion")));
        p.setNumVotos((professorObj.getString("numVotos")));
        p.setFoto((professorObj.getString("foto")));
        coleccion.insert(professorObj);

        return true;
    }

    /*
     Actualizar
     */
    public void modificar(Profesor p1, Profesor p2) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);

        DBObject query = new BasicDBObject("email", p1.getEmail());
        DBObject doc2 = new BasicDBObject();
        
        doc2.put("idProfesor", p2.getIdProfesor());
        doc2.put("idLugar", p2.getIdLugar());
        doc2.put("nombre", p2.getNombre());
        doc2.put("apellidos", p2.getApellidos());
        doc2.put("edad", p2.getEdad());
        doc2.put("email", p2.getEmail());
        doc2.put("movil", p2.getMovil());
        doc2.put("password", p2.getPassword());
        doc2.put("horario", p2.getHorario());
        doc2.put("descripcion", p2.getDescripcion());
        doc2.put("evaluacion", p2.getEvaluacion());
        doc2.put("numVotos", p2.getNumVotos());
        doc2.put("foto", p2.getFoto());
        coleccion.update(query, doc2);
    }
    
     public boolean buscarProfesor(String email) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", email);
        DBObject profe = coleccion.findOne(query);
        System.out.println(profe);
        return profe != null;
    }
}
