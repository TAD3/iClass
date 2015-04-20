
package com.tad3.iclass.entidad;

/**
 *
 * @author Laura
 */
public class Lugar {
    private String idLugar;
    private String codigoPostal;
    private String barrio;
    private String ciudad;

    public Lugar(String idLugar, String codigoPostal, String barrio, String ciudad) {
        this.idLugar = idLugar;
        this.codigoPostal = codigoPostal;
        this.barrio = barrio;
        this.ciudad = ciudad;
    }

    public Lugar() {
        
    }

    public String getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(String idLugar) {
        this.idLugar = idLugar;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    
}
