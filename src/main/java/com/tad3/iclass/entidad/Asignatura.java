package com.tad3.iclass.entidad;

/**
 * Clase para crear los objetos de tipo asignatura
 *
 * @author Laura
 */
public class Asignatura {

    private String idAsignatura;
    private String nombre;
    private String curso;
    private String descripcion;

    @Override
    /**
     * Método toString de asignatura
     *
     * @return idAsignatura + " - " + nombre + " - " + curso
     */
    public String toString() {
        return idAsignatura + " - " + nombre + " - " + curso;
    }

    /**
     * Constructor del método para crear una asignatura con todos sus atributos
     *
     * @param idAsignatura Identificación de la asignatura
     * @param nombre Nombre de la asignatura
     * @param curso Curso de la asignatura
     * @param descripcion Descripción de la asignatura
     */
    public Asignatura(String idAsignatura, String nombre, String curso, String descripcion) {
        this.idAsignatura = idAsignatura;
        this.nombre = nombre;
        this.curso = curso;
        this.descripcion = descripcion;
    }

    /**
     * Constructor del método para crear una asignatura vacía
     *
     */
    public Asignatura() {

    }

    /**
     * Método que devuelve el identificador de la asignatura
     *
     * @return idAsignatura Devuelve el identificador de la asignatura
     */
    public String getIdAsignatura() {
        return idAsignatura;
    }

    /**
     * Método para modificar el identificador de la asignatura
     *
     * @param idAsignatura Variable que contiene el nuevo identificador de la
     * asignatura
     */
    public void setIdAsignatura(String idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    /**
     * Método que devuelve el nombre de la asignatura
     *
     * @return nombre Devuelve el nombre de la asignatura
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para modificar el nombre de la asignatura
     *
     * @param nombre Variable que contiene el nuevo nombre de la asignatura
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método que devuelve el curso de la asignatura
     *
     * @return curso Devuelve el curso de la asignatura
     */
    public String getCurso() {
        return curso;
    }

    /**
     * Método para modificar el curso de la asignatura
     *
     * @param curso Variable que contiene el nuevo curso de la asignatura
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     * Método que devuelve la descripción de la asignatura
     *
     * @return descripcion Devuelve la descripción de la asignatura
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método para modificar la descripción de la asignatura
     *
     * @param descripcion Variable que contiene la nueva descripción de la asignatura
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
