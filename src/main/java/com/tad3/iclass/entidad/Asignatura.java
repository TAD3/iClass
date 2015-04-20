
package com.tad3.iclass.entidad;

/**
 *
 * @author Laura
 */
public class Asignatura {
    private String idAsignatura;
    private String nombre;
    private String curso;
    private String descripcion;

    public Asignatura(String idAsignatura, String nombre, String curso, String descripcion) {
        this.idAsignatura = idAsignatura;
        this.nombre = nombre;
        this.curso = curso;
        this.descripcion = descripcion;
    }

    public Asignatura() {
        
    }

    public String getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(String idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
