package com.suarez.guambana.controlasistencia;

public class Materia {
    public String nombreInstitucion;
    public String nombreMateria;
    public String nombreSalon;

    public Materia() {

    }

    public Materia(String nombreInstitucion, String nombreMateria, String nombreSalon) {
        this.nombreInstitucion = nombreInstitucion;
        this.nombreMateria = nombreMateria;
        this.nombreSalon = nombreSalon;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public String getNombreSalon() {
        return nombreSalon;
    }

    public void setNombreSalon(String nombreSalon) {
        this.nombreSalon = nombreSalon;
    }
}
