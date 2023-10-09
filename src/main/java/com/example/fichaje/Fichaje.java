package com.example.fichaje;

import lombok.Data;

import java.sql.Time;
import java.sql.Date;

@Data
public class Fichaje {
    private int id;
    private int idTrabajador;
    private Date fechaEntrada;
    private Time horaEntrada;
    private Date fechaSalida;
    private Time horaSalida;
    private boolean salidaFijada; //Si ya se ha registrado una salida

}
