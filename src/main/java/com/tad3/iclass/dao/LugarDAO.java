package com.tad3.iclass.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.tad3.iclass.entidad.Lugar;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Esta clase tiene las distintas queries para obtener los documentos de la
 * colección lugar de la base de datos iclass
 *
 * @author Laura
 */
public class LugarDAO {

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
     * Método para conectarnos a la base de datos iclass y la colección lugar
     *
     * @param conect Contiene la conexión a mongodb
     * @return coleccion Devuelve la conexión a una base datos y la colección en
     * concreto
     */
    public DBCollection collection(MongoClient conect) {
        DB database = conect.getDB("iclass");
        DBCollection coleccion = database.getCollection("lugar");
        return coleccion;
    }

    /**
     * Método para extraer de la colección todos los lugares
     *
     * @return lista Devuelve una lista con todas los lugares que se encuentra
     * en la colección lugar
     * @throws UnknownHostException
     */
    public List<Lugar> listaLugares() throws UnknownHostException {

        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        DBCursor barrior = coleccion.find();
        List<Lugar> lista = new ArrayList<>();
        try {
            while (barrior.hasNext()) {
                Lugar l = new Lugar();
                DBObject cur = barrior.next();
                BasicDBObject studentObj = (BasicDBObject) cur;
                l.setIdLugar((studentObj.getString("_id")));
                l.setCodigoPostal((studentObj.getString("codigoPostal")));
                l.setBarrio((studentObj.getString("barrio")));
                l.setCiudad((studentObj.getString("ciudad")));

                lista.add(l);
            }
        } finally {
            barrior.close();
            conect.close();
        }
        return lista;
    }

    /**
     * Método para extraer un lugar en concreto de la colección
     *
     * @param idLugar Variable que contiene el identificador del lugar que se va
     * a buscar
     * @return a Devuelve el lugar
     * @throws UnknownHostException
     */
    public Lugar lugar(String idLugar) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("_id", idLugar);
        DBObject user = coleccion.findOne(query);
        Lugar l = new Lugar();
        BasicDBObject studentObj = (BasicDBObject) user;
        l.setIdLugar((studentObj.getString("idLugar")));
        l.setCodigoPostal((studentObj.getString("codigoPostal")));
        l.setBarrio((studentObj.getString("barrio")));
        l.setCiudad((studentObj.getString("ciudad")));

        conect.close();
        return l;
    }

    /**
     * Método para borrar un lugar de la colección
     *
     * @param idLugar Variable que contiene el identificador del lugar que se va
     * a borrar
     * @return true Se ha borrado correctamente
     * @throws UnknownHostException
     */
    public boolean borrarLugar(String idLugar) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        coleccion.remove(new BasicDBObject("_id", idLugar));

        conect.close();
        return true;
    }

    /**
     * Método para crear un lugar nuevo en la colección
     *
     * @param l Variable que contiene los datos del lugar a crear
     * @return true Lugar creado correctamente en la colección
     * @throws UnknownHostException
     */
    public boolean crearLugar(Lugar l) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject objeto = new BasicDBObject();

        objeto.put("_id", l.getIdLugar());
        objeto.put("codigoPostal", l.getCodigoPostal());
        objeto.put("barrio", l.getBarrio());
        objeto.put("ciudad", l.getCiudad());
        coleccion.insert(objeto);

        conect.close();
        return true;
    }

    /**
     * Método para modificar los datos de un lugar en la colección
     *
     * @param l1 Variable que contiene los datos antiguos del lugar a
     * modificar
     * @param l2 Variable que contiene los datos nuevos del lugar a
     * modificar
     * @return true Lugar modificado correctamente
     * @throws UnknownHostException
     */
    public boolean modificarLugar(Lugar l1, Lugar l2) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);

        DBObject query = new BasicDBObject("_id", l1.getIdLugar());
        DBObject doc2 = new BasicDBObject();
        doc2.put("_id", l2.getIdLugar());
        doc2.put("codigoPostal", l2.getCodigoPostal());
        doc2.put("barrio", l2.getBarrio());
        doc2.put("ciudad", l2.getCiudad());
        coleccion.update(query, doc2);

        conect.close();
        return true;
    }

    /**
     * Método para buscar una asignatura en la coleccion
     *
     * @param idLugar Variable que contiene el identificador del 
     * lugar que se va a borrar
     * @return lugar Devuelve el lugar buscado
     * @throws UnknownHostException
     */
    public boolean buscarLugar(String idLugar) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("_id", idLugar);
        DBObject lugar = coleccion.findOne(query);

        conect.close();
        return lugar != null;
    }
}
