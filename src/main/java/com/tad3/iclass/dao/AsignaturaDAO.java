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
 * Esta clase tiene las distintas queries para obtener los documentos de la
 * colección asignatura de la base de datos iclass
 *
 * @author Laura
 */
public class AsignaturaDAO {

    MongoClient mongoClient;

    /**
     * Método para crear la conexión con mongodb
     *
     * @return mongoClient Devuelve la conexión a la base de datos
     * @throws UnknownHostException
     */
    public MongoClient conexion() throws UnknownHostException {
        mongoClient = new MongoClient("localhost", 27017);
        return mongoClient;
    }

    /**
     * Método para conectarnos a la base de datos iclass y la colección
     * asignatura
     *
     * @param conect Contiene la conexión a mongodb
     * @return coleccion Devuelve la conexión a una base datos y la colección en
     * concreto
     */
    public DBCollection collection(MongoClient conect) {
        DB database = conect.getDB("iclass");
        DBCollection coleccion = database.getCollection("asignatura");
        return coleccion;
    }

    /**
     * Método para extraer de la colección todos las asignaturas
     *
     * @return lista Devuelve una lista con todas las asignaturas que se
     * encuentra en la colección asigantura
     * @throws UnknownHostException
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

    /**
     * Método para extraer una asignatura en concreto de la colección
     *
     * @param idAsignatura Variable que contiene el identificador de la
     * asignatura que se va a buscar
     * @return a Devuelve la asignatura
     * @throws UnknownHostException
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

    /**
     * Método para borrar una asignatura de la colección
     *
     * @param idAsignatura Variable que contiene el identificador de la
     * asignatura que se va a borrar
     * @return true Se ha borrado correctamente
     * @throws UnknownHostException
     */
    public boolean borrarAsignatura(String idAsignatura) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        coleccion.remove(new BasicDBObject("_id", idAsignatura));

        conect.close();
        return true;
    }

    /**
     * Método para crear una asignatura nueva en la colección
     *
     * @param a Variable que contiene los datos de la asignatura a crear
     * @return true Asignatura creada correctamente en la colección
     * @throws UnknownHostException
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

    /**
     * Método para modificar los datos de una asignatura en la colección
     *
     * @param a1 Variable que contiene los datos antiguos de la asignatura a
     * modificar
     * @param a2 Variable que contiene los datos nuevos de la asignatura a
     * modificar
     * @return true Asignatura modificada correctamente
     * @throws UnknownHostException
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

    /**
     * Método para buscar una asignatura en la coleccion
     *
     * @param idAsignatura Variable que contiene el identificador de la
     * asignatura que se va a borrar
     * @return asig Devuelve la asignatura buscada
     * @throws UnknownHostException
     */
    public boolean buscarAsignatura(String idAsignatura) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("_id", idAsignatura);
        DBObject asig = coleccion.findOne(query);
        System.out.println(asig);

        conect.close();
        return asig != null;
    }

    public BasicDBObject buscarAsignaturaNombreYCurso(String nombre, String curso) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("nombre", nombre).append("curso", curso);
        DBObject asig = coleccion.findOne(query);
        System.out.println(asig);
        BasicDBObject a = new BasicDBObject();
        a.put("_id", asig.get("_id"));
        a.put("nombre", asig.get("nombre"));
        a.put("curso", asig.get("curso"));
        a.put("descripcion", asig.get("descripcion"));
        conect.close();
        System.out.println("buscarDAO: " + a);
        return a;
    }
}
