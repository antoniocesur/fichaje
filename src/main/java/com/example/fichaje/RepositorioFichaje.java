package com.example.fichaje;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
                    " foreign key(id_trabajador) references trabajadores(id));";
            stmt.executeUpdate(CREATE_TABLE_SQL);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }


}
