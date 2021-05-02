package com.upc.appcentroidiomas;

public class Curso {
    private String id;
    private String curso;
    private String seccion;
    private String horario;

    //get set


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
    @Override
    public String toString() {
        return curso;
    }
}
