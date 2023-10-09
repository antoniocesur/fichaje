package com.example.fichaje;
import com.example.fichaje.Trabajador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
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

    RepositorioTrabajador repositorioTrabajador;
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

    }

    public void actualizarTabla(){
        ObservableList<Trabajador> listaTrabajadores=repositorioTrabajador.leerTodosFX();
        trabajadorTable.setItems(listaTrabajadores);
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

        repositorioTrabajador.update(t);
        actualizarTabla();
    }


}