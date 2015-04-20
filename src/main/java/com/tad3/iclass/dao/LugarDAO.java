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
import com.tad3.iclass.entidad.Lugar;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Laura
 */
public class LugarDAO {

    MongoClient mongoClient;

    public MongoClient conexion() throws UnknownHostException {
        mongoClient = new MongoClient("localhost", 27017);
        return mongoClient;
    }

    public DBCollection collection(MongoClient conect) {
        DB database = conect.getDB("iclass");
        DBCollection coleccion = database.getCollection("lugar");
        return coleccion;
    }

    /*
     Traer lista de lugares de mongo
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
                l.setIdLugar((studentObj.getString("idLugar")));
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

    /*
     Traer datos de un lugar en particular
     */
    public Lugar lugar(String idLugar) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("idLugar", idLugar);
        DBObject user = coleccion.findOne(query);
        Lugar l = new Lugar();
        BasicDBObject studentObj = (BasicDBObject) user;
        l.setIdLugar((studentObj.getString("idLugar")));
        l.setCodigoPostal((studentObj.getString("codigoPostal")));
        l.setBarrio((studentObj.getString("barrio")));
        l.setCiudad((studentObj.getString("ciudad")));

        return l;
    }

    /*
     Borrar lugar
     */
    public void borrarLugar(String idLugar) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        coleccion.remove(new BasicDBObject("idLugar", idLugar));
    }

    /*
     Crear un lugar nuevo
     */
    public boolean crearLugar(Lugar l) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject objeto = new BasicDBObject();

        objeto.put("idLugar", l.getIdLugar());
        objeto.put("codigoPostal", l.getCodigoPostal());
        objeto.put("barrio", l.getBarrio());
        objeto.put("ciudad", l.getCiudad());
        coleccion.insert(objeto);

        return true;
    }

    /*
     Actualizar
     */
    public void modificarLugar(Lugar l1, Lugar l2) throws UnknownHostException {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);

        DBObject query = new BasicDBObject("idLugar", l1.getIdLugar());
        DBObject doc2 = new BasicDBObject();
        doc2.put("idLugar", l2.getIdLugar());
        doc2.put("codigoPostal", l2.getCodigoPostal());
        doc2.put("barrio", l2.getBarrio());
        doc2.put("ciudad", l2.getCiudad());
        coleccion.update(query, doc2);
    }
}
