
package com.tad3.iclass.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.tad3.iclass.entidad.Asignatura;
import com.tad3.iclass.util.CustomComparator;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Laura
 */
public class AsignaturaDAO {

    MongoClient mongoClient;

    public MongoClient conexion() throws UnknownHostException {
        mongoClient = new MongoClient("localhost", 27017);
        return mongoClient;
    }

    public DBCollection collection(MongoClient conect) {
        DB database = conect.getDB("iclass");
        DBCollection coleccion = database.getCollection("asignatura");
        return coleccion;
    }

    /*
     Traer lista de asignaturas de mongo
     */
    public List<Asignatura> listaAsignaturas() throws UnknownHostException {

        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        DBCursor cursor = coleccion.find();
        List<Asignatura> lista = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                Asignatura a = new Asignatura();
                DBObject cur = cursor.next();
                BasicDBObject studentObj = (BasicDBObject) cur;
                a.setIdAsignatura((studentObj.getString("_id")));
                a.setNombre((studentObj.getString("nombre")));
                a.setCurso((studentObj.getString("curso")));
                a.setDescripcion((studentObj.getString("descripcion")));

                lista.add(a);
            }
        } finally {
            cursor.close();
            conect.close();
        }
        CustomComparator comparador = new CustomComparator();
        Collections.sort(lista, comparador);        
        return lista;
    }

    /*
     Traer datos de un asignatura en particular
     */
    public Asignatura asignatura(String idAsignatura) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("_id", idAsignatura);
        DBObject asig = coleccion.findOne(query);
        Asignatura a = new Asignatura();
        BasicDBObject studentObj = (BasicDBObject) asig;
        a.setIdAsignatura((studentObj.getString("_id")));
        a.setNombre((studentObj.getString("nombre")));
        a.setCurso((studentObj.getString("curso")));
        a.setDescripcion((studentObj.getString("descripcion")));

        conect.close();
        return a;
    }

    /*
     Borrar asignatura
     */
    public boolean borrarAsignatura(String idAsignatura) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        coleccion.remove(new BasicDBObject("_id", idAsignatura));
        
        conect.close();
        return true;
    }

    /*
     Crear un asignatura
     */
    public boolean crearAsignatura(Asignatura a) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject objeto = new BasicDBObject();

        objeto.put("_id", a.getIdAsignatura());
        objeto.put("nombre", a.getNombre());
        objeto.put("curso", a.getCurso());
        objeto.put("descripcion", a.getDescripcion());
        coleccion.insert(objeto);

        conect.close();
        return true;
    }

    /*
     Actualizar
     */
    public boolean modificarAsignatura(Asignatura a1, Asignatura a2) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);

        DBObject query = new BasicDBObject("_id", a1.getIdAsignatura());
        DBObject doc2 = new BasicDBObject();
        doc2.put("_id", a2.getIdAsignatura());
        doc2.put("nombre", a2.getNombre());
        doc2.put("curso", a2.getCurso());
        doc2.put("descripcion", a2.getDescripcion());
        coleccion.update(query, doc2);
        
        conect.close();
        return true;
    }

    public boolean buscarAsignatura(String idAsignatura) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("_id", idAsignatura);
        DBObject asig = coleccion.findOne(query);
        System.out.println(asig);
        
        conect.close();
        return asig != null;
    }
}
