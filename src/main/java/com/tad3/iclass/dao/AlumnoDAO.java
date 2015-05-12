package com.tad3.iclass.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.tad3.iclass.entidad.Alumno;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.cloudfoundry.runtime.env.CloudEnvironment;

/**
 *
 * Esta clase tiene las distintas query para traer los documentos de la
 * coleccion alumno de la base de datos iclass
 *
 * @author francisco
 */
public class AlumnoDAO {

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
    
    /**
     * Metodo para crear la conexion con mongodb
     *
     * @return mongoClient Devuelve la conexion a la base de datos
     * @throws UnknownHostException
     */
    public MongoClient conexion() throws UnknownHostException, Exception {
        mongoClient = getMongoClient();
        return mongoClient;
    }

    /**
     * Metodo para conectarnos a la base de datos iclass y la coleccion alumno
     *
     * @param conect Contine la conexion a mongodb
     * @return coleccion Devuelve la conexion a una base datos y la coleccion en
     * concreto
     */
    public DBCollection collection(MongoClient conect) {
        DB database = conect.getDB("db");
        DBCollection coleccion = database.getCollection("alumno");
        return coleccion;
    }

    /**
     * Metodo para extraer de la coleccion todos los alumnos
     *
     * @return lista Devuelve una lista con todos los alumno que se encuentra en
     * la coleccion alumno
     * @throws UnknownHostException
     */
    public List<Alumno> listaAlumnos() throws UnknownHostException, Exception {

        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        DBCursor cursor = coleccion.find();
        List<Alumno> lista = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                Alumno a = new Alumno();
                DBObject cur = cursor.next();
                BasicDBObject studentObj = (BasicDBObject) cur;
                a.setIdAlumno((studentObj.getString("_id")));
                a.setIdLugar((studentObj.getString("idLugar")));
                a.setNombre((studentObj.getString("nombre")));
                a.setApellidos((studentObj.getString("apellidos")));
                a.setEdad((studentObj.getString("edad")));
                a.setCurso((studentObj.getString("curso")));
                a.setEmail((studentObj.getString("email")));
                a.setPassword((studentObj.getString("password")));

                lista.add(a);
            }
        } finally {
            cursor.close();
            conect.close();
        }
        return lista;
    }

    /**
     * Metodo para extraer un alumno en concreto de la coleccion
     *
     * @param correo Variable que contiene el correo del alumno que vamos a
     * buscar
     * @return a Devuelve el alumno
     * @throws UnknownHostException
     */
    public Alumno alumno(String correo) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", correo);
        DBObject user = coleccion.findOne(query);
        Alumno a = new Alumno();
        BasicDBObject studentObj = (BasicDBObject) user;
        a.setIdAlumno((studentObj.getString("_id")));
        a.setIdLugar((studentObj.getString("idLugar")));
        a.setNombre((studentObj.getString("nombre")));
        a.setApellidos((studentObj.getString("apellidos")));
        a.setEdad((studentObj.getString("edad")));
        a.setCurso((studentObj.getString("curso")));
        a.setEmail((studentObj.getString("email")));
        a.setPassword((studentObj.getString("password")));

        conect.close();
        return a;
    }

    /**
     * Metodo para comprobar si existe un determinado alumno
     *
     * @param correo Variable que contiene el correo del alumno a comprobar que
     * existe
     * @param pass Variable que contiene la contraseña del alumno a comprobar
     * que existe
     * @return true si el correo y la contraseña son correctas y false si no son
     * correctas al menos una de las dos
     * @throws UnknownHostException
     */
    public boolean existe(String correo, String pass) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", correo).append("password", pass);
        DBObject user = coleccion.findOne(query);

        if (user == null) {
            conect.close();
            return false;
        } else {
            conect.close();
            return true;
        }
    }

    /**
     * Metodo para borrar un alumno de la coleccion
     *
     * @param correo Variable que contiene el correo del alumno que vamos a
     * borrar
     * @return true Se ha borrado correctamente
     * @throws UnknownHostException
     */
    public boolean borrar(String correo) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        coleccion.remove(new BasicDBObject("email", correo));

        return true;
    }

    /**
     * Metodo para crear un alumno nuevo en la coleccion
     *
     * @param a Variable que contiene los datos del alumno a crear
     * @return true Alumno creado correctamente en la coleccion
     * @throws UnknownHostException
     */
    public boolean crear(Alumno a) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject objeto = new BasicDBObject();

        objeto.put("_id", a.getIdAlumno());
        objeto.put("idLugar", a.getIdLugar());
        objeto.put("nombre", a.getNombre());
        objeto.put("apellidos", a.getApellidos());
        objeto.put("edad", a.getEdad());
        objeto.put("curso", a.getCurso());
        objeto.put("email", a.getEmail());
        objeto.put("password", a.getPassword());
        coleccion.insert(objeto);

        conect.close();
        return true;
    }

    /**
     * Metodo para modificar los datos de un alumno en la coleccion
     *
     * @param a1 Variable que contiene los datos antiguos del alumno a modificar
     * @param a2 Variable que contiene los datos nuevos del alumno a modificar
     * @return true Alumno modificado correctamente
     * @throws UnknownHostException
     */
    public boolean modificar(Alumno a1, Alumno a2) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);

        DBObject query = new BasicDBObject("email", a1.getEmail());
        DBObject doc2 = new BasicDBObject();
        doc2.put("_id", a2.getIdAlumno());
        doc2.put("idLugar", a2.getIdLugar());
        doc2.put("nombre", a2.getNombre());
        doc2.put("apellidos", a2.getApellidos());
        doc2.put("edad", a2.getEdad());
        doc2.put("curso", a2.getCurso());
        doc2.put("email", a2.getEmail());
        doc2.put("password", a2.getPassword());
        coleccion.update(query, doc2);

        conect.close();
        return true;
    }

    /**
     * Metodo para buscar un alumno en la coleccion
     *
     * @param email
     * @return alum Devuelve el alumno buscado
     * @throws UnknownHostException
     */
    public boolean buscar(String email) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("email", email);
        DBObject alum = coleccion.findOne(query);
        System.out.println(alum);

        conect.close();
        return alum != null;
    }

    /**
     * Metodo que devuelve el total de alumnos que hay por barrio
     *
     * @param idLugar Variable que contiene el identificador de un lugar
     * @return count Devuelve el total de alumno por un barrio
     * @throws UnknownHostException
     */
    public int alumnoPorBarrio(String idLugar) throws UnknownHostException, Exception {
        MongoClient conect = conexion();
        DBCollection coleccion = collection(conect);
        BasicDBObject query = new BasicDBObject("idLugar", idLugar);
        int count = coleccion.find(query).count();

        conect.close();
        return count;
    }
}
