package com.tad3.iclass.entidad;

/**
 * Clase para crear los objetos de tipo lugar
 *
 * @author Laura
 */
public class Lugar {

    private String idLugar;
    private String codigoPostal;
    private String barrio;
    private String ciudad;

    @Override
    /**
     * Método toString de lugar
     * 
     * @return codigoPostal + " - " + barrio + " - " + ciudad 
     */
    public String toString() {
        return codigoPostal + " - " + barrio + " - " + ciudad;
    }

    /**
     * Constructor del método para crear un lugar con todos sus atributos
     *
     * @param idLugar Identificación del lugar
     * @param codigoPostal Código postal del lugar
     * @param barrio Barrio del lugar
     * @param ciudad Ciudad del lugar
     */
    public Lugar(String idLugar, String codigoPostal, String barrio, String ciudad) {
        this.idLugar = idLugar;
        this.codigoPostal = codigoPostal;
        this.barrio = barrio;
        this.ciudad = ciudad;
    }

    /**
     * Constructor del método para crear un lugar vacío
     *
     */
    public Lugar() {

    }

    /**
     * Método que devuelve el identificador del lugar
     *
     * @return idLugar Devuelve el identificador del lugar
     */
    public String getIdLugar() {
        return idLugar;
    }

    /**
     * Método para modificar el identificador del lugar
     *
     * @param idLugar Variable que contiene el nuevo identificador del lugar
     */
    public void setIdLugar(String idLugar) {
        this.idLugar = idLugar;
    }

    /**
     * Método que devuelve el código postal del lugar
     *
     * @return codigoPostal Devuelve el código postal del lugar
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Método para modificar el código postal del lugar
     *
     * @param codigoPostal Variable que contiene el nuevo código postal del
     * lugar
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Método que devuelve el barrio del lugar
     *
     * @return barrio Devuelve el barrio del lugar
     */
    public String getBarrio() {
        return barrio;
    }

    /**
     * Método para modificar el barrio del lugar
     *
     * @param barrio Variable que contiene el nuevo barrio del lugar
     */
    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    /**
     * Método que devuelve la ciudad del lugar
     *
     * @return ciudad Devuelve la ciudad del lugar
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Método para modificar la ciudad del lugar
     *
     * @param ciudad Variable que contiene la nueva ciudad del lugar
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

}
