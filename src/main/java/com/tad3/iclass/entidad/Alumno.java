/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tad3.iclass.entidad;

/**
 * Clase para Crear los objetos de tipo alumno
 *
 * @author francisco
 */
public class Alumno {

    private String idAlumno;
    private String idLugar;
    private String nombre;
    private String apellidos;
    private String edad;
    private String curso;
    private String email;
    private String password;

    /**
     * Constructor del metodo para crear un alumno con todos sus atributos
     *
     * @param idAlumno Identificacion del alumno
     * @param idLugar Identificacion del lugar al que pertenece el alumno
     * @param nombre Nombre del alumno
     * @param apellidos Apellidos del alumno
     * @param edad Edad del alumno
     * @param curso Curso al que pertenece el alumno
     * @param email Email del alumno
     * @param password Contraseña del alumno
     */
    public Alumno(String idAlumno, String idLugar, String nombre, String apellidos, String edad, String curso, String email, String password) {
        this.idAlumno = idAlumno;
        this.idLugar = idLugar;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.curso = curso;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor del metodo para crear un alumno vacio
     *
     */
    public Alumno() {

    }

    /**
     * Metodo que devuelve identificado del alumno
     * 
     * @return idAlumno Devuelve el identificador del alumno
     */
    public String getIdAlumno() {
        return idAlumno;
    }

    /**
     * Metodo para modificar el identificador del alumno
     * 
     * @param idAlumno Variable que contiene el nuevo identificador del alumno
     */
    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    /**
     * Metodo que devuelve el identificador del lugar del alumno
     * 
     * @return idLugar Devuelve el identificador del lugar del alumno 
     */
    public String getIdLugar() {
        return idLugar;
    }

    /**
     * Metodo para modificar el identificador del lugar del alumno
     * 
     * @param idLugar Variable que contiene el nuevo identificador del lugar del alumno
     */
    public void setIdLugar(String idLugar) {
        this.idLugar = idLugar;
    }

    /**
     * Metodo que devuelve el nombre del alumno
     * 
     * @return nombre Devuelve el nombre del alumno 
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Metodo para modificar el nombre del alumno
     * 
     * @param nombre Variable que contiene el nuevo nombre del alumno
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo que devuelve los apellidos del alumno
     * 
     * @return apellidos Devuelve los apellidos del alumno 
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Metodo para modificar los apellidos del alumno
     * 
     * @param apellidos Variable que contiene los nuevos apellidos del alumno
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Metodo que devuelve la edad del alumno
     * 
     * @return edad Devuelve la edad del alumno
     */
    public String getEdad() {
        return edad;
    }

    /**
     * Metodo para modificar la edad del alumno
     * 
     * @param edad Variable que contiene la nueva edad del alumno
     */
    public void setEdad(String edad) {
        this.edad = edad;
    }

    /**
     * Metodo que devuelve el curso del alumno
     * 
     * @return curso Devuelve el curso del alumno
     */
    public String getCurso() {
        return curso;
    }

    /**
     * Metodo para modificar el curso del alumno
     * 
     * @param curso Variable que contiene el nuevo curso del alumno
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     * Metodo que devuelve el email del alumno
     * 
     * @return email Devuelve el email del alumno
     */
    public String getEmail() {
        return email;
    }

    /**
     * Metodo para modificar el email del alumno
     * 
     * @param email Variable que contiene el nuevo email del alumno
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Metodo que devuelve la contraseña del alumno
     * 
     * @return password Devuelve la contraseña del alumno
     */
    public String getPassword() {
        return password;
    }

    /**
     * Metodo para modificar la contraseña del alumno
     * 
     * @param password Variable que contiene la nueva contraseña del alumno
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Metodo para definir como mostrar los datos del alumno
     * @return una cadena con las variable nombre, apellidos e email encadenados 
     */
    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + email;
    }

}
