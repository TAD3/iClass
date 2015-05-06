package com.tad3.iclass.view;

import com.tad3.iclass.dao.AlumnoDAO;
import com.tad3.iclass.dao.LugarDAO;
import com.tad3.iclass.dao.ProfesorDAO;
import com.tad3.iclass.entidad.Alumno;
import com.tad3.iclass.entidad.Lugar;
import com.tad3.iclass.entidad.Profesor;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanlu
 */
public class ProfesorView_prueba extends CustomComponent implements View {

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
    TextField repassword_profesor = new TextField("Repetir contraseña: ");
    TextField descripcion_profesor = new TextField("Descripción: ");
    TextField horario_profesor = new TextField("Horario: ");


    Button modifyme = new Button("Actualizar");
    Button saveme = new Button("Guardar");
    Button deleteme = new Button("Borrar cuenta");
    Button cancel = new Button("Cancelar");

    HorizontalLayout botonesProfesor = new HorizontalLayout(saveme, deleteme, cancel);
    VerticalLayout panelPrincipal = new VerticalLayout();
    VerticalLayout panelIzquierdo = new VerticalLayout();
    VerticalLayout panelDerecho = new VerticalLayout();
    Layout layaoutArriba = new HorizontalLayout();
    MenuBar menuBar = new MenuBar();
    TextField curso = new TextField();
    TextField asignatura = new TextField();
    HorizontalSplitPanel panelSubPrincipal = new HorizontalSplitPanel();
    Panel panel = new Panel(panelSubPrincipal);
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

    public ProfesorView_prueba() {

        /*
         Menu Bar
         */
        layaoutArriba.setSizeFull();
        menuBar.setSizeFull();

        panelSubPrincipal.setSplitPosition(23.0f, Unit.PERCENTAGE);
        panelSubPrincipal.setLocked(true);

        Collection<Lugar> lugares = new ArrayList<>();

        LugarDAO lugarDAO = new LugarDAO();

        Iterator<Lugar> it;
        try {
            it = lugarDAO.listaLugares().iterator();
            while (it.hasNext()) {
                lugares.add(it.next());
            }
            id_lugar_profesor.setInputPrompt("Ningún lugar seleccionado");

            id_lugar_profesor.setWidth(100.0f, Unit.PERCENTAGE);

            id_lugar_profesor.setFilteringMode(FilteringMode.CONTAINS);
            id_lugar_profesor.setImmediate(true);

            for (Lugar lug : lugares) {
                id_lugar_profesor.addItem(lug.getIdLugar());
                id_lugar_profesor.setItemCaption(lug.getIdLugar(), lug.toString());
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
        }

        MenuBar.Command profesorCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                panelIzquierdo.removeAllComponents();
                panelDerecho.removeAllComponents();

                try {
                    Profesor a = profesor.profesor((String) getSession().getAttribute("user"));
                    id_profesor.setValue(a.getIdProfesor());
                    id_lugar_profesor.setValue(a.getIdLugar());
                    nombre_profesor.setValue(a.getNombre());
                    apellidos_profesor.setValue(a.getApellidos());
                    edad_profesor.setValue(a.getEdad());
                    email_profesor.setValue(a.getEmail());
                    movil_profesor.setValue(a.getMovil());
                    password_profesor.setValue(a.getPassword());
                    repassword_profesor.setValue(a.getPassword());
                    descripcion_profesor.setValue(a.getDescripcion());
                    horario_profesor.setValue(a.getHorario());


                } catch (UnknownHostException ex) {
                    Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                }

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


                modifyme.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {

                        id_profesor.setReadOnly(false);
                        id_lugar_profesor.setReadOnly(false);
                        nombre_profesor.setReadOnly(false);
                        apellidos_profesor.setReadOnly(false);
                        edad_profesor.setReadOnly(false);
                        email_profesor.setReadOnly(false);
                        movil_profesor.setReadOnly(false);
                        password_profesor.setReadOnly(false);
                        repassword_profesor.setReadOnly(false);
                        descripcion_profesor.setReadOnly(false);
                        horario_profesor.setReadOnly(false);


                        panelIzquierdo.addComponent(modifyme);
                        panelIzquierdo.addComponent(id_profesor);
                        panelIzquierdo.addComponent(id_lugar_profesor);
                        panelIzquierdo.addComponent(nombre_profesor);
                        panelIzquierdo.addComponent(apellidos_profesor);
                        panelIzquierdo.addComponent(edad_profesor);
                        panelIzquierdo.addComponent(email_profesor);
                        panelIzquierdo.addComponent(movil_profesor);
                        panelIzquierdo.addComponent(password_profesor);
                        panelIzquierdo.addComponent(repassword_profesor);
                        panelIzquierdo.addComponent(descripcion_profesor);
                        panelIzquierdo.addComponent(horario_profesor);
                        panelIzquierdo.addComponent(botonesProfesor);

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
                        p.setPassword(repassword_profesor.getValue());
                        p.setDescripcion(descripcion_profesor.getValue());
                        p.setHorario(horario_profesor.getValue());

                        if (password_profesor.getValue().equals(repassword_profesor.getValue())) {
                            p.setPassword(password_profesor.getValue());
                            try {
                                panelIzquierdo.removeAllComponents();
                                Profesor a1 = profesor.profesor((String) getSession().getAttribute("user"));
                                profesor.modificar(a1, p);
                                panelDerecho.removeAllComponents();
                                Notification.show("Profesor modificado", "Se ha actualizado el "
                                        + "profesor en la base de datos", Notification.Type.TRAY_NOTIFICATION);

                            } catch (UnknownHostException ex) {
                                Logger.getLogger(ProfesorView_prueba.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            Notification.show("No coincide los campos contraseña y repetir contraseña", Notification.Type.WARNING_MESSAGE);
                        }
                    }
                });

                deleteme.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        try {
                            Profesor a3 = profesor.profesor((String) getSession().getAttribute("user"));
                            profesor.borrar(a3.getEmail());
                            getUI().getSession().close();
                            getUI().getPage().setLocation(getLogoutPath());
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ProfesorView_prueba.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                cancel.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        panelIzquierdo.removeComponent(repassword_profesor);
                        panelIzquierdo.removeComponent(botonesProfesor);

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

                    }
                });

                panelIzquierdo.addComponent(modifyme);
                panelIzquierdo.addComponent(id_profesor);
                panelIzquierdo.addComponent(id_lugar_profesor);
                panelIzquierdo.addComponent(nombre_profesor);
                panelIzquierdo.addComponent(apellidos_profesor);
                panelIzquierdo.addComponent(edad_profesor);
                panelIzquierdo.addComponent(email_profesor);
                panelIzquierdo.addComponent(movil_profesor);
                panelIzquierdo.addComponent(password_profesor);
                panelIzquierdo.addComponent(descripcion_profesor);
                panelIzquierdo.addComponent(horario_profesor);

            }
        };

        MenuBar.Command logoutCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getSession().close();
                getUI().getPage().setLocation(getLogoutPath());
            }
        };

        MenuBar.MenuItem profesor = menuBar.addItem("Modificar Datos", profesorCommand);
        menuBar.addItem("Log out", logoutCommand);

        layaoutArriba.addComponent(menuBar);
        panelPrincipal.addComponent(layaoutArriba);
        panelPrincipal.addComponent(panelSubPrincipal);

        panelSubPrincipal.setFirstComponent(panelIzquierdo);
        panelSubPrincipal.setSecondComponent(panelDerecho);
        panelSubPrincipal.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");

        setCompositionRoot(new CssLayout(panelPrincipal));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello profesor " + username);
    }
}
