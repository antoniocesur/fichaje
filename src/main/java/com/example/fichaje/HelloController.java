package com.example.fichaje;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField apellidosTextField;

    @FXML
    private Button btnInsertar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<Trabajador, String> colApellidos;

    @FXML
    private TableColumn<Trabajador, String> colDepartamento;

    @FXML
    private TableColumn<Trabajador, String> colDni;

    @FXML
    private TableColumn<Trabajador, Integer> colId;

    @FXML
    private TableColumn<Trabajador, String> colNombre;
    @FXML
    private TextField idTextField;

    @FXML
    private TextField dniTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TableView<Trabajador> trabajadorTable;
    @FXML
    private TableView<Trabajador> trabajadorTable1;
    @FXML
    private TableColumn<Trabajador, String> colApellidos1;

    @FXML
    private TableColumn<Trabajador, String> colDepartamento1;

    @FXML
    private TableColumn<Trabajador, String> colDni1;

    @FXML
    private TableColumn<Trabajador, Integer> colId1;

    @FXML
    private TableColumn<Trabajador, String> colNombre1;
    @FXML
    private ComboBox<String> comboDepartamento;
    @FXML
    private Label tiempo;
    @FXML
    private Tab tab2;
    AnimationTimer timer;

    RepositorioTrabajador repositorioTrabajador;
    RepositorioFichaje repositorioFichaje;
    Conexion conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conexion=new Conexion();
        repositorioTrabajador=new RepositorioTrabajador(conexion.conexion);

        ObservableList<Trabajador> listaTrabajadores=repositorioTrabajador.leerTodosFX();

        colId.setCellValueFactory(new PropertyValueFactory<Trabajador, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("apellidos"));
        colDni.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("dni"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("departamento"));

        ObservableList<String> listaDepartamentos = FXCollections.observableArrayList("Administración", "Producción", "Limpieza");
        comboDepartamento.setItems(listaDepartamentos);
        comboDepartamento.getSelectionModel().selectFirst();

        trabajadorTable.setItems(listaTrabajadores);

        trabajadorTable.setOnMouseClicked(e -> {
            Trabajador t =trabajadorTable.getSelectionModel().getSelectedItem();
            idTextField.setText(String.valueOf(t.getId()));
            nombreTextField.setText(t.getNombre());
            apellidosTextField.setText(t.getApellidos());
            dniTextField.setText(t.getDni());
            //departamentoTextField.setText(t.getDepartamento());
            comboDepartamento.getSelectionModel().select(t.getDepartamento());
        });

        //Ahora preparo la tabla de los trabajadores de la segunda pestaña
        colId1.setCellValueFactory(new PropertyValueFactory<Trabajador, Integer>("id"));
        colNombre1.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("nombre"));
        colApellidos1.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("apellidos"));
        colDni1.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("dni"));
        colDepartamento1.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("departamento"));

        trabajadorTable1.setItems(listaTrabajadores);

        //Ahora creo el repositorio para añadir fichajes usando la conexión anterior a la BBDD
        repositorioFichaje=new RepositorioFichaje(conexion.conexion);

    }

    public void actualizarTabla(){
        ObservableList<Trabajador> listaTrabajadores=repositorioTrabajador.leerTodosFX();
        trabajadorTable.setItems(listaTrabajadores);
        trabajadorTable1.setItems(listaTrabajadores);
    }
    public void pulsarInsertar(){
        Trabajador t=new Trabajador();
        t.setNombre(nombreTextField.getText());
        t.setApellidos(apellidosTextField.getText());
        t.setDni(dniTextField.getText());
        //t.setDepartamento(departamentoTextField.getText());
        t.setDepartamento(comboDepartamento.getSelectionModel().getSelectedItem().toString());

        repositorioTrabajador.inserta(t);
        actualizarTabla();
    }

    public void pulsarModificar(){
        Trabajador t=new Trabajador();
        t.setId(Integer.parseInt(idTextField.getText()));
        t.setNombre(nombreTextField.getText());
        t.setApellidos(apellidosTextField.getText());
        t.setDni(dniTextField.getText());
        //t.setDepartamento(departamentoTextField.getText());

        repositorioTrabajador.modificar(t);
        actualizarTabla();
    }

    public void ponReloj(){
        //Pongo un reloj en una etiqueta cuando se pulse la segunda pestaña (tab2) por primera vez
        if(timer==null) {
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    tiempo.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            };
            timer.start();
        }
    }

    public void pulsaFichaje(){
        //Cogemos el id del trabajador seleccionado en la tabla
        Trabajador t =trabajadorTable1.getSelectionModel().getSelectedItem();
        int idTrabajadorActual=t.getId();

        //Consultamos si hay alguna entrada sin salida de ese trabajador. Si no hay fichajes o todos tienen salida, devuelve null
        Fichaje fichaje=repositorioFichaje.fichajeSinSalida(idTrabajadorActual);

        //Si fichaje es null, tengo que hacer un nuevo fichaje con los datos de entrada
        if(fichaje==null){
            fichaje=new Fichaje();
            fichaje.setIdTrabajador(idTrabajadorActual);
            fichaje.setFechaEntrada(java.sql.Date.valueOf(LocalDate.now()));
            fichaje.setHoraEntrada(java.sql.Time.valueOf(LocalTime.now()));
            fichaje.setSalidaFijada(false);
            repositorioFichaje.inserta(fichaje);
        }else{
            //Si nos devuelven un fichaje, completamos los datos de la fecha y hora de salida
            fichaje.setFechaSalida(java.sql.Date.valueOf(LocalDate.now()));
            fichaje.setHoraSalida(java.sql.Time.valueOf(LocalTime.now()));
            fichaje.setSalidaFijada(true);
            repositorioFichaje.modifica(fichaje);
        }
    }

}
