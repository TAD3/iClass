/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tad3.iclass.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.tad3.iclass.entidad.Asignatura;
import com.tad3.iclass.entidad.Profesor;
import com.tad3.iclass.util.CustomComparator;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
                p.setIdProfesor((professorObj.getString("_id")));
                p.setIdLugar((professorObj.getString("idLugar")));
                p.setNombre((professorObj.getString("nombre")));
                p.setApellidos((professorObj.getString("apellidos")));
                p.setEdad((professorObj.getString("edad")));
                p.setEmail((professorObj.getString("email")));
                p.setMovil((professorObj.getString("movil")));
                p.setPassword((professorObj.getString("password")));
                p.setHorario((professorObj.getString("horario")));
                p.setDescripcion((professorObj.getString("descripcion")));
                p.setAsignaturas((ArrayList) (professorObj.get("asignaturas")));
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
        p.setIdProfesor((professorObj.getString("_id")));
        p.setIdLugar((professorObj.getString("idLugar")));
        p.setNombre((professorObj.getString("nombre")));
        p.setApellidos((professorObj.getString("apellidos")));
        p.setEdad((professorObj.getString("edad")));
        p.setEmail((professorObj.getString("email")));
        p.setMovil((professorObj.getString("movil")));
        p.setPassword((professorObj.getString("password")));
        p.setHorario((professorObj.getString("horario")));
        p.setDescripcion((professorObj.getString("descripcion")));
        p.setAsignaturas((ArrayList) professorObj.get("asignaturas"));

        conect.close();
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

        conect.close();
        return user != null;
    }
    /*
     Traer lista de asignaturas por profesor de mongo
     */

    public List<Asignatura> listaAsignaturasPorProfesor(String idProfesor) throws UnknownHostException {

        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("_id", idProfesor);
        DBObject profe = coleccion.findOne(query);
        List<Asignatura> lista = new ArrayList<>();
        lista = (ArrayList) profe.get("asignaturas");
        conect.close();
        CustomComparator comparador = new CustomComparator();
        Collections.sort(lista, comparador);
        
        conect.close();
        return lista;
    }

    /*
     Borrar alumno
     */
    public boolean borrar(String correo) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        coleccion.remove(new BasicDBObject("email", correo));

        conect.close();
        return true;
    }

    /*
     Crear un alumno nuevo
     */
    public boolean crear(Profesor p) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject objeto = new BasicDBObject();

        objeto.put("_id", p.getIdProfesor());
        objeto.put("idLugar", p.getIdLugar());
        objeto.put("nombre", p.getNombre());
        objeto.put("apellidos", p.getApellidos());
        objeto.put("edad", p.getEdad());
        objeto.put("movil", p.getMovil());
        objeto.put("email", p.getEmail());
        objeto.put("horario", p.getHorario());
        objeto.put("descripcion", p.getDescripcion());
        objeto.put("password", p.getPassword());
        objeto.put("asignaturas", p.getAsignaturas());
        coleccion.insert(objeto);

        conect.close();
        return true;
    }

    /*
     Actualizar
     */
    public boolean modificar(Profesor p1, Profesor p2) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);

        DBObject query = new BasicDBObject("email", p1.getEmail());
        DBObject doc2 = new BasicDBObject();
        doc2.put("_id", p2.getIdProfesor());
        doc2.put("idLugar", p2.getIdLugar());
        doc2.put("nombre", p2.getNombre());
        doc2.put("apellidos", p2.getApellidos());
        doc2.put("edad", p2.getEdad());
        doc2.put("email", p2.getEmail());
        doc2.put("movil", p2.getMovil());
        doc2.put("password", p2.getPassword());
        doc2.put("horario", p2.getHorario());
        doc2.put("descripcion", p2.getDescripcion());
        doc2.put("asignaturas", p2.getAsignaturas());
        coleccion.update(query, doc2);

        conect.close();
        return true;
    }

    public boolean buscarProfesor(String email) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", email);
        DBObject profe = coleccion.findOne(query);
        System.out.println(profe);
        
        conect.close();
        return profe != null;
    }

    public List<Profesor> buscarProfAsig(String idLugar, String asignatura, String sitio) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);

        List<Profesor> lista = new ArrayList<>();
        if (sitio.equals("daIgual")) {

            BasicDBObject query = new BasicDBObject("asignaturas._id", asignatura);
            DBCursor cursor = coleccion.find(query);
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

                lista.add(p);
            }
            cursor.close();
        } else {
            BasicDBObject query = new BasicDBObject("idLugar", idLugar).append("asignaturas._id", asignatura);
            DBCursor cursor = coleccion.find(query);
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

                lista.add(p);

            }
            cursor.close();
        }
        
        conect.close();
        return lista;
    }

    public int profesorPorBarrio(String idLugar) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("idLugar", idLugar);
        int count = coleccion.find(query).count();
        
        conect.close();
        return count;
    }
}
