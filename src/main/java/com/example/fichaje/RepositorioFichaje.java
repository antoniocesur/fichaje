package com.example.fichaje;

import java.sql.*;
import java.util.ArrayList;

public class RepositorioFichaje {
    Connection conexion;
    public RepositorioFichaje(Connection miConexion){
        this.conexion=miConexion;
        createTable();
    }

    public void createTable(){
        Statement stmt=null;
        try {
            stmt = conexion.createStatement();
            String CREATE_TABLE_SQL="CREATE TABLE IF NOT EXISTS fichajes (" +
                    "id               INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
                    "id_trabajador    INTEGER," +
                    "fecha_entrada    DATE," +
                    "hora_entrada     TIME," +
                    "fecha_salida     DATE," +
                    "hora_salida      TIME," +
                    "salida_fijada     BOOLEAN," +
                    " foreign key(id_trabajador) references trabajadores(id));";
            stmt.executeUpdate(CREATE_TABLE_SQL);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void inserta(Fichaje a){
        PreparedStatement sentencia = null;
        String sentenciaSql = "INSERT INTO fichajes (id_trabajador, fecha_entrada, hora_entrada, fecha_salida, hora_salida, salida_fijada) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, a.getIdTrabajador());
            sentencia.setDate(2, a.getFechaEntrada());
            sentencia.setTime(3, a.getHoraEntrada());
            sentencia.setDate(4, a.getFechaSalida());
            sentencia.setTime(5, a.getHoraSalida());
            sentencia.setBoolean(6, a.isSalidaFijada());
            sentencia.executeUpdate();

            /*ResultSet rs = sentencia.getGeneratedKeys();
            a.setId(rs.getInt(1));*/

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void modifica(Fichaje a){
        PreparedStatement sentencia = null;
        String sentenciaSql = "UPDATE fichajes SET id_trabajador=?, fecha_entrada=?, hora_entrada=?, fecha_salida=?, hora_salida=?, salida_fijada=? WHERE id=?";
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, a.getIdTrabajador());
            sentencia.setDate(2, a.getFechaEntrada());
            sentencia.setTime(3, a.getHoraEntrada());
            sentencia.setDate(4, a.getFechaSalida());
            sentencia.setTime(5, a.getHoraSalida());
            sentencia.setBoolean(6, a.isSalidaFijada());
            sentencia.setInt(7, a.getId());
            sentencia.executeUpdate();

            /*ResultSet rs = sentencia.getGeneratedKeys();
            a.setId(rs.getInt(1));*/

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public Fichaje fichajeSinSalida(int idTrabajador){
        Fichaje f=null;
        try {
            PreparedStatement ps=conexion.prepareStatement("SELECT * FROM fichajes WHERE id_trabajador=? AND NOT salida_fijada");
            ps.setInt(1, idTrabajador);
            ResultSet rs=ps.executeQuery();

            //Si no ha encontrado ningún fichaje de ese trabajador con entrada y sin salida, devuelvo null
            //Si ha encontrado algún fichaje, lo devuelvo
            if(rs.next()!=false){
                f=new Fichaje();
                f.setId(rs.getInt("id"));
                f.setIdTrabajador(idTrabajador);
                f.setFechaEntrada(rs.getDate("fecha_entrada"));
                f.setHoraEntrada(rs.getTime("hora_entrada"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return f;
    }


}
