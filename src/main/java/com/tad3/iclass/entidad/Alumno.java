/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tad3.iclass.entidad;

/**
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
    private String foto;

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

    public Alumno(String idAlumno, String idLugar, String nombre, String apellidos, String edad, String curso, String email, String password, String foto) {
        this.idAlumno = idAlumno;
        this.idLugar = idLugar;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.curso = curso;
        this.email = email;
        this.password = password;
        this.foto = foto;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + email;
    }

    public Alumno() {

    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(String idLugar) {
        this.idLugar = idLugar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

}
