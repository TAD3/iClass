package com.tad3.iclass.view;

import com.tad3.iclass.dao.ProfesorDAO;
import com.tad3.iclass.entidad.Profesor;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanlu
 */
public class ProfesorView extends CustomComponent implements View {

    public static final String NAME = "profesor";
    ProfesorDAO profesor = new ProfesorDAO();

    //Profesor
    TextField id_profesor = new TextField("ID: ");
    ComboBox id_lugar_profesor = new ComboBox("ID lugar: ");
    TextField nombre_profesor = new TextField("Nombre: ");
    TextField apellidos_profesor = new TextField("Apellidos: ");
    TextField edad_profesor = new TextField("Edad: ");
    TextField email_profesor = new TextField("Email: ");
    TextField movil_profesor = new TextField("Móvil: ");
    TextField password_profesor = new TextField("Contraseña: ");
    TextField descripcion_profesor = new TextField("Descripción: ");
    TextField horario_profesor = new TextField("Horario: ");
    TextField evaluacion_profesor = new TextField("Evaluación: ");
    TextField votos_profesor = new TextField("Nº votos: ");
    TextField foto_profesor = new TextField("Foto: ");

    Button modifyme = new Button("Actualizar");
    Button saveme = new Button("Guardar");
    Button deleteme = new Button("Borrar cuenta");

    HorizontalLayout botonesProfesor = new HorizontalLayout(saveme, deleteme);
    FormLayout datosProfesor = new FormLayout(modifyme, id_profesor, id_lugar_profesor,
            nombre_profesor, apellidos_profesor, edad_profesor, email_profesor, movil_profesor,
            descripcion_profesor, horario_profesor, evaluacion_profesor, votos_profesor,
            password_profesor, foto_profesor, botonesProfesor);

    VerticalLayout panelDerecho = new VerticalLayout();
    Layout layaoutArriba = new HorizontalLayout();
    MenuBar menuBar = new MenuBar();
    TextField curso = new TextField();
    TextField asignatura = new TextField();
    VerticalLayout panelPrincipal = new VerticalLayout();
    HorizontalSplitPanel panelSubPrincipal = new HorizontalSplitPanel(datosProfesor, panelDerecho);
    Panel panel = new Panel(panelSubPrincipal);

    Profesor me = new Profesor();

    Label text = new Label();

    Button logoutButton = new Button("Logout", new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            getUI().getSession().close();
            getUI().getPage().setLocation(getLogoutPath());
        }
    });

    private String getLogoutPath() {
        return getUI().getPage().getLocation().getPath();
    }

    public ProfesorView() {

        /*
         Menu Bar
         */
        layaoutArriba.setSizeFull();
        menuBar.setSizeFull();

        misDatos();

        panelSubPrincipal.setSplitPosition(23.0f, Unit.PERCENTAGE);
        panelSubPrincipal.setLocked(true);

        datosProfesor.setComponentAlignment(modifyme, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(id_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(id_lugar_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(nombre_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(apellidos_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(edad_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(email_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(movil_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(password_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(descripcion_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(horario_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(evaluacion_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(votos_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(foto_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(botonesProfesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setMargin(true);

        id_profesor.setRequired(true);
        id_lugar_profesor.setRequired(true);
        nombre_profesor.setRequired(true);
        apellidos_profesor.setRequired(true);
        edad_profesor.setRequired(true);
        email_profesor.setRequired(true);
        movil_profesor.setRequired(true);
        password_profesor.setRequired(true);
        descripcion_profesor.setRequired(false);
        horario_profesor.setRequired(true);

        readOnly();

        modifyme.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                id_profesor.setReadOnly(true);
                id_lugar_profesor.setReadOnly(false);
                nombre_profesor.setReadOnly(false);
                apellidos_profesor.setReadOnly(false);
                edad_profesor.setReadOnly(false);
                email_profesor.setReadOnly(true);
                movil_profesor.setReadOnly(false);
                password_profesor.setReadOnly(false);
                descripcion_profesor.setReadOnly(false);
                horario_profesor.setReadOnly(false);
                evaluacion_profesor.setReadOnly(true);
                votos_profesor.setReadOnly(true);
                foto_profesor.setReadOnly(false);
            }
        });

        saveme.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Profesor p = new Profesor();
                p.setIdProfesor(id_profesor.getValue());
                p.setIdLugar((String) id_lugar_profesor.getValue());
                p.setNombre(nombre_profesor.getValue());
                p.setApellidos(apellidos_profesor.getValue());
                p.setEdad(edad_profesor.getValue());
                p.setEmail(email_profesor.getValue());
                p.setMovil(movil_profesor.getValue());
                p.setPassword(password_profesor.getValue());
                p.setDescripcion(descripcion_profesor.getValue());
                p.setHorario(horario_profesor.getValue());
                p.setEvaluacion(evaluacion_profesor.getValue());
                p.setNumVotos(votos_profesor.getValue());
                p.setFoto(foto_profesor.getValue());
                boolean encontrado = false;
                try {
                    encontrado = profesor.buscarProfesor(me.getEmail());
                    if (encontrado == true) {
                        profesor.modificar(me, p);
                        me = p;
                        readOnly();
                        Notification.show("Profesor modificado", "Se ha actualizado el "
                                + "profesor en la base de datos", Notification.Type.TRAY_NOTIFICATION);
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        deleteme.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Profesor p = new Profesor();
                boolean encontrado = false;
                String email = email_profesor.getValue();
                try {
                    encontrado = profesor.buscarProfesor(email);
                    if (encontrado == true) {
                        profesor.borrar(email);
                        getUI().getSession().close();
                        getUI().getPage().setLocation(getLogoutPath());
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        MenuBar.Command profesorCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {

            }
        };

        MenuBar.Command logoutCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getSession().close();
                getUI().getPage().setLocation(getLogoutPath());
            }
        };
        menuBar.addItem("Profesor", profesorCommand);
        menuBar.addItem("Log out", logoutCommand);

        layaoutArriba.addComponent(menuBar);

        panel.setWidth(String.valueOf(Page.getCurrent().getBrowserWindowWidth()) + "px");
        panel.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");
        setCompositionRoot(new CssLayout(layaoutArriba, panel));
    }

    public final void readOnly() {
        id_profesor.setReadOnly(true);
        id_lugar_profesor.setReadOnly(true);
        nombre_profesor.setReadOnly(true);
        apellidos_profesor.setReadOnly(true);
        edad_profesor.setReadOnly(true);
        email_profesor.setReadOnly(true);
        movil_profesor.setReadOnly(true);
        password_profesor.setReadOnly(true);
        descripcion_profesor.setReadOnly(true);
        horario_profesor.setReadOnly(true);
        evaluacion_profesor.setReadOnly(true);
        votos_profesor.setReadOnly(true);
        foto_profesor.setReadOnly(true);
    }

    public void misDatos() {
        try {
            String username = "profesor@gmail.com";//String.valueOf(getSession().getAttribute("user"));
            me = profesor.profesor(username); 
            id_profesor.setValue(me.getIdProfesor());
            id_lugar_profesor.setValue(me.getIdLugar());
            nombre_profesor.setValue(me.getNombre());
            apellidos_profesor.setValue(me.getApellidos());
            edad_profesor.setValue(me.getEdad());
            email_profesor.setValue(me.getEmail());
            movil_profesor.setValue(me.getMovil());
            password_profesor.setValue(me.getPassword());
            descripcion_profesor.setValue(me.getDescripcion());
            horario_profesor.setValue(me.getHorario());
            evaluacion_profesor.setValue(me.getEvaluacion());
            votos_profesor.setValue(me.getNumVotos());
            foto_profesor.setValue(me.getFoto());
        } catch (UnknownHostException ex) {
            Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
        String username = String.valueOf(getSession().getAttribute("user"));
    }
}
