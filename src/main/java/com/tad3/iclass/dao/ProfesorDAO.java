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
import com.mongodb.MongoClientURI;
import com.tad3.iclass.entidad.Asignatura;
import com.tad3.iclass.entidad.Profesor;
import com.tad3.iclass.util.CustomComparator;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.cloudfoundry.runtime.env.CloudEnvironment;

/**
 * Clase DAO para las CRUD del profesor
 *
 * @author Juanlu
 */
public class ProfesorDAO {

    MongoClient mongoClient;
    
    public MongoClient getMongoClient() throws Exception {
        CloudEnvironment environment = new CloudEnvironment();
        if (environment.getServiceDataByLabels("mongodb").size() == 0) {
        // To connect to mongodb server
            mongoClient = new MongoClient("localhost", 27017);
        } else {
            Map credential = (Map) ((Map) environment.getServiceDataByLabels("mongodb").get(0)).get("credentials");
            String connURL = (String) credential.get("url");
            mongoClient = new MongoClient(new MongoClientURI(connURL));
        }
        return mongoClient;
    }

    public MongoClient conexion() throws UnknownHostException, Exception {
        mongoClient = getMongoClient();
        return mongoClient;
    }

    public DBCollection collection(MongoClient conect) {
        DB database = conect.getDB("db");
        DBCollection coleccion = database.getCollection("profesor");
        return coleccion;
    }

    /**
     * Devuelve una lista con todos los profesores del sistema
     *
     * @return Lista con los profesores
     * @throws UnknownHostException
     */
    public List<Profesor> listaProfesores() throws UnknownHostException, Exception {
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

    /**
     * Devuelve un profesor si el correo existe en la bd
     *
     * @param correo devuelve el profesor cuyo correo es el pasado como
     * parámetro
     * @return un objeto tipo Profesor con sus datos
     * @throws UnknownHostException
     */
    public Profesor profesor(String correo) throws UnknownHostException, Exception {
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
        BasicDBList listaAsig = (BasicDBList) professorObj.get("asignaturas");

        ArrayList<Asignatura> asignaturas = new ArrayList();
        for (Iterator<Object> it = listaAsig.iterator(); it.hasNext();) {
            DBObject a = (DBObject) it.next();
            Asignatura asig = new Asignatura();
            asig.setCurso((String) a.get("curso"));
            asig.setDescripcion((String) a.get("descripcion"));
            asig.setNombre((String) a.get("nombre"));
            asig.setIdAsignatura((String) a.get("_id"));
            asignaturas.add(asig);
        }
        p.setAsignaturas(asignaturas);

        conect.close();
        return p;
    }

    /**
     * Comprueba si existe el profesor, para el login
     *
     * @param correo es el nombre de usuario del profesor, que lo identifica
     * @param pass contraseña del profesor para acceder a la plataforma
     * @return devuelve verdadero si la clave coincide con la del profesor cuyo
     * email es el pasado como parámetro correo
     * @throws UnknownHostException
     */
    public boolean existe(String correo, String pass) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", correo).append("password", pass);
        DBObject user = coleccion.findOne(query);

        conect.close();
        return user != null;
    }

    /**
     * Devuelve la lista de asignaturas de un profesor indicado
     *
     * @param idProfesor id único del profesor en la bd
     * @return una lista con sus asignaturas
     * @throws UnknownHostException
     */
    public List<Asignatura> listaAsignaturasPorProfesor(String idProfesor) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("_id", idProfesor);
        DBObject profe = coleccion.findOne(query);
        List<Asignatura> lista;
        lista = (ArrayList) profe.get("asignaturas");
        conect.close();
        CustomComparator comparador = new CustomComparator();
        Collections.sort(lista, comparador);

        conect.close();
        return lista;
    }

    /**
     * Borrar profesor del sistema
     *
     * @param correo nombre de usuario del profesor a borrar
     * @return verdadero para indicar que ha sido borrado
     * @throws UnknownHostException
     */
    public boolean borrar(String correo) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        coleccion.remove(new BasicDBObject("email", correo));

        conect.close();
        return true;
    }

    /**
     * Crear profesor en el sistema
     *
     * @param p objeto Profesor que va a ser creado en BD
     * @return devuelve verdadero si el profesor ha sido creado correctamente
     * @throws UnknownHostException
     */
    public boolean crear(Profesor p) throws UnknownHostException, Exception {
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

    /**
     * Actualizar datos del profesor
     *
     * @param p1 profesor que será actualizado
     * @param p2 datos nuevos del profesor
     * @return verdadero si se ha actualizado correctamente
     * @throws UnknownHostException
     */
    public boolean modificar(Profesor p1, Profesor p2) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);

        DBObject query = new BasicDBObject("email", p1.getEmail());
        DBObject updateQuery = new BasicDBObject("$set", 
                new BasicDBObject("_id", p2.getIdProfesor()).append("idLugar", p2.getIdLugar())
                        .append("nombre", p2.getNombre()).append("apellidos", p2.getApellidos())
                        .append("edad", p2.getEdad()).append("email", p2.getEmail())
                        .append("movil", p2.getMovil()).append("password", p2.getPassword())
                        .append("horario", p2.getHorario()).append("descripcion", p2.getDescripcion()));
        coleccion.update(query, updateQuery);

        conect.close();
        return true;
    }

    public boolean addAsignaturas(String email, ArrayList asigs) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        
        DBObject findQuery = new BasicDBObject("email", email);
        DBObject updateQuery = new BasicDBObject("$set", new BasicDBObject("asignaturas", asigs));
        DBObject profe = coleccion.findOne(findQuery);
        coleccion.update(profe, updateQuery);

        conect.close();
        return true;
    }

    /**
     * Buscar un profesor en el sistema
     *
     * @param email nombre de usuario del profesor, el correo electrónico, por
     * cual será buscado en el sistema
     * @return verdadero si ha sido encontrado. Falso en caso contrario
     * @throws UnknownHostException
     */
    public boolean buscarProfesor(String email) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", email);
        DBObject profe = coleccion.findOne(query);

        conect.close();
        return profe != null;
    }

    /**
     * Buscar un profesor por asignatura/lugar
     *
     * @param idLugar lugar del profesor encontrado
     * @param asignatura asignatura que el alumno quiere mejorar
     * @param sitio elegido por el alumno para buscar un profesor
     * @return
     * @throws UnknownHostException
     */
    public List<Profesor> buscarProfAsig(String idLugar, String asignatura, String sitio) throws UnknownHostException, Exception {
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

    /**
     * Buscar profesor por barrio
     *
     * @param idLugar se filtrará la búsqueda por el barrio seleccionado
     * @return número de profesores encontrados
     * @throws UnknownHostException
     */
    public int profesorPorBarrio(String idLugar) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("idLugar", idLugar);
        int count = coleccion.find(query).count();

        conect.close();
        return count;
    }
}
