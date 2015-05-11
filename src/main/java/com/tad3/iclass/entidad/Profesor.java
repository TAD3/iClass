/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tad3.iclass.entidad;

import java.util.ArrayList;

/**
 *
 * @author Juanlu
 */
public class Profesor {

    private String idProfesor;
    private String idLugar;
    private String nombre;
    private String apellidos;
    private String edad;
    private String email;
    private String movil;
    private String password;
    private String horario;
    private String descripcion;
    private ArrayList<Asignatura> asignaturas;

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + email;
    }

    public Profesor() {
    }
    /**
     * Constructor de la clase Profesor
     * @param idProfesor    identificador unico en mongodb
     * @param idLugar   lugar donde reside el profesor
     * @param nombre    nombre del profesor
     * @param apellidos apellidos del profesor
     * @param edad  edad del profesor
     * @param email correo electrónico del profesor. Servirá para iniciar sesión en el sistema
     * @param movil móvil del profesor
     * @param password  contraseña de acceso al sistema 
     * @param horario   horario del profesor para indicar su disponibilidad
     * @param descripcion   breve comentario sobre el profesor 
     * @param asignaturas   asignaturas que imparte
     */
    public Profesor(String idProfesor, String idLugar, String nombre, String apellidos, String edad, String email,
            String movil, String password, String horario, String descripcion, ArrayList asignaturas) {
        this.idProfesor = idProfesor;
        this.idLugar = idLugar;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.email = email;
        this.movil = movil;
        this.password = password;
        this.horario = horario;
        this.descripcion = descripcion;
        this.asignaturas = asignaturas;
    }

    /**
     * @return the idProfesor
     */
    public String getIdProfesor() {
        return idProfesor;
    }

    /**
     * @param idProfesor the idProfesor to set
     */
    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    /**
     * @return the idLugar
     */
    public String getIdLugar() {
        return idLugar;
    }

    /**
     * @param idLugar the idLugar to set
     */
    public void setIdLugar(String idLugar) {
        this.idLugar = idLugar;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the apellidos
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * @param apellidos the apellidos to set
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * @return the edad
     */
    public String getEdad() {
        return edad;
    }

    /**
     * @param edad the edad to set
     */
    public void setEdad(String edad) {
        this.edad = edad;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the movil
     */
    public String getMovil() {
        return movil;
    }

    /**
     * @param movil the movil to set
     */
    public void setMovil(String movil) {
        this.movil = movil;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the horario
     */
    public String getHorario() {
        return horario;
    }

    /**
     * @param horario the horario to set
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * 
     * @return lista de asignaturas
     */
    public ArrayList<Asignatura> getAsignaturas() {
        return asignaturas;
    }
    /**
     * 
     * @param asignaturas añade las asignaturas a su lista de asignaturas
     */
    public void setAsignaturas(ArrayList<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

}
