package com.tad3.iclass.view;

import com.tad3.iclass.dao.AlumnoDAO;
import com.tad3.iclass.dao.AsignaturaDAO;
import com.tad3.iclass.dao.LugarDAO;
import com.tad3.iclass.dao.ProfesorDAO;
import com.tad3.iclass.entidad.Alumno;
import com.tad3.iclass.entidad.Asignatura;
import com.tad3.iclass.entidad.Lugar;
import com.tad3.iclass.entidad.Profesor;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
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
 * @author francisco
 */
public class AlumnoView extends CustomComponent implements View {

    public static final String NAME = "alumno";
    AlumnoDAO alumno = new AlumnoDAO();

    TextField id_alumno = new TextField("DNI: ");
    ComboBox id_lugar_alumno = new ComboBox("ID lugar: ");
    TextField nombre_alumno = new TextField("Nombre: ");
    TextField apellidos_alumno = new TextField("Apellidos: ");
    TextField edad_alumno = new TextField("Edad: ");
    ComboBox curso_asig = new ComboBox("Curso: ");
    ComboBox asignatura = new ComboBox("Asignatura: ");
    TextField email_alumno = new TextField("Email: ");
    TextField password_alumno = new TextField("Contraseña: ");
    TextField repassword_alumno = new TextField("Repetir contraseña: ");

    Button modifyme = new Button("Actualizar");
    Button saveme = new Button("Guardar");
    Button deleteme = new Button("Borrar cuenta");
    Button cancel = new Button("Cancelar");
    Button buscar = new Button("Buscar Profesor");
    Button buscarTodo = new Button("BP todos lugares");

    HorizontalLayout botonesAlumno = new HorizontalLayout(saveme, deleteme, cancel);
    HorizontalLayout botonesBuscar = new HorizontalLayout(buscar, buscarTodo);
    VerticalLayout panelPrincipal = new VerticalLayout();
    VerticalLayout panelIzquierdo = new VerticalLayout();
    VerticalLayout panelDerecho = new VerticalLayout();
    Layout layaoutArriba = new HorizontalLayout();
    MenuBar menuBar = new MenuBar();

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

    public AlumnoView() {

        /*
         Menu Bar
         */
        layaoutArriba.setSizeFull();
        menuBar.setSizeFull();
        panelIzquierdo.setMargin(true);
        panelSubPrincipal.setSplitPosition(23.0f, Unit.PERCENTAGE);
        panelIzquierdo.setSpacing(true);
        panelSubPrincipal.setLocked(true);
        botonesAlumno.setSpacing(true);
        botonesBuscar.setSpacing(true);
        botonesAlumno.setMargin(true);
        botonesBuscar.setMargin(true);
        curso_asig.setInputPrompt("Curso");
        asignatura.setInputPrompt("Asignatura");
        Collection<String> cursos = new ArrayList<>();
        cursos.add("4º Primaria");
        cursos.add("5º Primaria");
        cursos.add("6º Primaria");
        cursos.add("1º ESO");
        cursos.add("2º ESO");
        cursos.add("3º ESO");
        cursos.add("4º ESO");
        cursos.add("1º Bachillerato");
        cursos.add("2º Bachillerato");
        curso_asig.addItems(cursos);
        /*##############################################################*/
        Collection<Lugar> lugares = new ArrayList<>();

        LugarDAO lugarDAO = new LugarDAO();

        Iterator<Lugar> it;
        try {
            it = lugarDAO.listaLugares().iterator();
            while (it.hasNext()) {
                lugares.add(it.next());
            }
            id_lugar_alumno.setInputPrompt("Ningún lugar seleccionado");

            id_lugar_alumno.setWidth(100.0f, Unit.PERCENTAGE);

            id_lugar_alumno.setFilteringMode(FilteringMode.CONTAINS);
            id_lugar_alumno.setImmediate(true);

            for (Lugar lug : lugares) {
                id_lugar_alumno.addItem(lug.getIdLugar());
                id_lugar_alumno.setItemCaption(lug.getIdLugar(), lug.toString());
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*##############################################################*/
        Collection<Asignatura> asig = new ArrayList<>();

        AsignaturaDAO asignaturaDAO = new AsignaturaDAO();

        Iterator<Asignatura> it1;
        try {
            it1 = asignaturaDAO.listaAsignaturas().iterator();
            while (it1.hasNext()) {
                asig.add(it1.next());
            }
            asignatura.setInputPrompt("Ningún lugar seleccionado");

            asignatura.setWidth(100.0f, Unit.PERCENTAGE);

            asignatura.setFilteringMode(FilteringMode.CONTAINS);
            asignatura.setImmediate(true);

            for (Asignatura asi : asig) {
                asignatura.addItem(asi.getIdAsignatura());
                asignatura.setItemCaption(asi.getIdAsignatura(), asi.toString());
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*###############################################################*/
        MenuBar.Command alumnoCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                panelIzquierdo.removeAllComponents();
                panelDerecho.removeAllComponents();

                try {
                    Alumno a = alumno.alumno((String) getSession().getAttribute("user"));
                    id_alumno.setValue(a.getIdAlumno());
                    id_lugar_alumno.setValue(a.getIdLugar());
                    nombre_alumno.setValue(a.getNombre());
                    apellidos_alumno.setValue(a.getApellidos());
                    edad_alumno.setValue(a.getEdad());
                    curso_asig.setValue(a.getCurso());
                    email_alumno.setValue(a.getEmail());
                    password_alumno.setValue(a.getPassword());
                    repassword_alumno.setValue(a.getPassword());

                } catch (UnknownHostException ex) {
                    Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                }

                id_alumno.setReadOnly(true);
                id_lugar_alumno.setReadOnly(true);
                nombre_alumno.setReadOnly(true);
                apellidos_alumno.setReadOnly(true);
                edad_alumno.setReadOnly(true);
                email_alumno.setReadOnly(true);
                curso_asig.setReadOnly(true);
                password_alumno.setReadOnly(true);

                modifyme.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {

                        id_alumno.setReadOnly(false);
                        id_lugar_alumno.setReadOnly(false);
                        nombre_alumno.setReadOnly(false);
                        apellidos_alumno.setReadOnly(false);
                        edad_alumno.setReadOnly(false);
                        email_alumno.setReadOnly(false);
                        curso_asig.setReadOnly(false);
                        password_alumno.setReadOnly(false);

                        try {
                            Alumno a = alumno.alumno((String) getSession().getAttribute("user"));
                            id_alumno.setValue(a.getIdAlumno());
                            id_lugar_alumno.setValue(a.getIdLugar());
                            nombre_alumno.setValue(a.getNombre());
                            apellidos_alumno.setValue(a.getApellidos());
                            edad_alumno.setValue(a.getEdad());
                            curso_asig.setValue(a.getCurso());
                            email_alumno.setValue(a.getEmail());
                            password_alumno.setValue(a.getPassword());
                            repassword_alumno.setValue(a.getPassword());

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        panelIzquierdo.addComponent(modifyme);
                        panelIzquierdo.addComponent(id_alumno);
                        panelIzquierdo.addComponent(id_lugar_alumno);
                        panelIzquierdo.addComponent(nombre_alumno);
                        panelIzquierdo.addComponent(apellidos_alumno);
                        panelIzquierdo.addComponent(edad_alumno);
                        panelIzquierdo.addComponent(curso_asig);
                        panelIzquierdo.addComponent(email_alumno);
                        panelIzquierdo.addComponent(password_alumno);
                        panelIzquierdo.addComponent(repassword_alumno);
                        panelIzquierdo.addComponent(botonesAlumno);

                    }

                });

                saveme.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        Alumno p = new Alumno();

                        p.setIdAlumno(id_alumno.getValue());
                        p.setIdLugar((String) id_lugar_alumno.getValue());
                        p.setNombre(nombre_alumno.getValue());
                        p.setApellidos(apellidos_alumno.getValue());
                        p.setEdad(edad_alumno.getValue());
                        p.setCurso((String) curso_asig.getValue());/*##############################*/

                        p.setEmail(email_alumno.getValue());
                        p.setPassword(password_alumno.getValue());
                        p.setPassword(repassword_alumno.getValue());
                        if (password_alumno.getValue().equals(repassword_alumno.getValue())) {
                            p.setPassword(password_alumno.getValue());
                            try {
                                panelIzquierdo.removeAllComponents();
                                Alumno a1 = alumno.alumno((String) getSession().getAttribute("user"));
                                alumno.modificar(a1, p);
                                panelDerecho.removeAllComponents();
                                Notification.show("Alumno modificado", "Se ha actualizado el "
                                        + "alumno en la base de datos", Notification.Type.TRAY_NOTIFICATION);

                            } catch (UnknownHostException ex) {
                                Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
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
                            Alumno a3 = alumno.alumno((String) getSession().getAttribute("user"));
                            alumno.borrar(a3.getEmail());
                            getUI().getSession().close();
                            getUI().getPage().setLocation(getLogoutPath());
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                cancel.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        panelIzquierdo.removeComponent(repassword_alumno);
                        panelIzquierdo.removeComponent(botonesAlumno);
                        try {
                            Alumno a = alumno.alumno((String) getSession().getAttribute("user"));
                            id_alumno.setValue(a.getIdAlumno());
                            id_lugar_alumno.setValue(a.getIdLugar());
                            nombre_alumno.setValue(a.getNombre());
                            apellidos_alumno.setValue(a.getApellidos());
                            edad_alumno.setValue(a.getEdad());
                            curso_asig.setValue(a.getCurso());
                            email_alumno.setValue(a.getEmail());
                            password_alumno.setValue(a.getPassword());
                            repassword_alumno.setValue(a.getPassword());

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        id_alumno.setReadOnly(true);
                        id_lugar_alumno.setReadOnly(true);
                        nombre_alumno.setReadOnly(true);
                        apellidos_alumno.setReadOnly(true);
                        edad_alumno.setReadOnly(true);
                        email_alumno.setReadOnly(true);
                        curso_asig.setReadOnly(true);
                        password_alumno.setReadOnly(true);
                    }
                });

                panelIzquierdo.addComponent(modifyme);
                panelIzquierdo.addComponent(id_alumno);
                panelIzquierdo.addComponent(id_lugar_alumno);
                panelIzquierdo.addComponent(nombre_alumno);
                panelIzquierdo.addComponent(apellidos_alumno);
                panelIzquierdo.addComponent(edad_alumno);
                panelIzquierdo.addComponent(curso_asig);
                panelIzquierdo.addComponent(email_alumno);
                panelIzquierdo.addComponent(password_alumno);
            }
        };

        MenuBar.Command profesorCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                id_alumno.setReadOnly(false);
                id_lugar_alumno.setReadOnly(false);
                nombre_alumno.setReadOnly(false);
                apellidos_alumno.setReadOnly(false);
                edad_alumno.setReadOnly(false);
                email_alumno.setReadOnly(false);
                curso_asig.setReadOnly(false);
                password_alumno.setReadOnly(false);
                panelIzquierdo.removeAllComponents();
                panelDerecho.removeAllComponents();
                curso_asig.setInputPrompt("Curso");
                asignatura.setInputPrompt("Asignatura");

                buscar.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        panelDerecho.removeAllComponents();
                        Table table = new Table();
                        //table.setWidth("1100");
                        table.setSizeFull();
                        table.setPageLength(table.size());
                        table.setImmediate(true);
                        try {
                            table.addContainerProperty("Nombre", String.class, null);
                            table.addContainerProperty("Apellidos", String.class, null);
                            table.addContainerProperty("Edad", String.class, null);
                            table.addContainerProperty("Movil", String.class, null);
                            table.addContainerProperty("Correo", String.class, null);
                            table.addContainerProperty("Direccion", String.class, null);
                            table.addContainerProperty("Horario", String.class, null);

                            ProfesorDAO alumnoDAO = new ProfesorDAO();
                            Alumno a1 = alumno.alumno((String) getSession().getAttribute("user"));
                            Iterator<Profesor> it3 = alumnoDAO.buscarProfAsig(a1.getIdLugar(), (String) asignatura.getValue(), "daIgual").iterator();
                            int i = 1;
                            if (alumnoDAO.buscarProfAsig(a1.getIdLugar(), (String) asignatura.getValue(), "").size() == 0) {
                                panelDerecho.addComponent(new Label("No hay datos"));
                            } else {
                                while (it3.hasNext()) {
                                    Profesor p = it3.next();
                                    Lugar l = new Lugar();
                                    LugarDAO lugarDAO = new LugarDAO();
                                    l = lugarDAO.lugar(p.getIdLugar());
                                    table.addItem(new Object[]{p.getNombre(), p.getApellidos(), p.getEdad(), p.getMovil(), p.getEmail(),l.getBarrio(), p.getHorario()}, i);
                                    i++;
                                }
                                panelDerecho.addComponent(table);
                            }

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

                buscarTodo.addClickListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        panelDerecho.removeAllComponents();
                        Table table = new Table();
                        //table.setWidth("1100");
                        table.setSizeFull();
                        table.setPageLength(table.size());
                        table.setImmediate(true);
                        try {
                            table.addContainerProperty("Nombre", String.class, null);
                            table.addContainerProperty("Apellidos", String.class, null);
                            table.addContainerProperty("Edad", String.class, null);
                            table.addContainerProperty("Movil", String.class, null);
                            table.addContainerProperty("Correo", String.class, null);
                            table.addContainerProperty("Direccion", String.class, null);
                            table.addContainerProperty("Horario", String.class, null);

                            ProfesorDAO alumnoDAO = new ProfesorDAO();
                            Alumno a1 = alumno.alumno((String) getSession().getAttribute("user"));
                            Iterator<Profesor> it3 = alumnoDAO.buscarProfAsig(a1.getIdLugar(), (String) asignatura.getValue(), "daIgual").iterator();
                            int i = 1;
                            if (alumnoDAO.buscarProfAsig(a1.getIdLugar(), (String) asignatura.getValue(), "daIgual").size() == 0) {
                                panelDerecho.addComponent(new Label("No hay datos"));
                            } else {
                                while (it3.hasNext()) {
                                    Profesor p = it3.next();
                                    Lugar l = new Lugar();
                                    LugarDAO lugarDAO = new LugarDAO();
                                    l = lugarDAO.lugar(p.getIdLugar());
                                    table.addItem(new Object[]{p.getNombre(), p.getApellidos(), p.getEdad(), p.getMovil(), p.getEmail(),l.getBarrio(), p.getHorario()}, i);
                                    i++;
                                }
                                panelDerecho.addComponent(table);
                            }

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                panelIzquierdo.addComponent(asignatura);
                botonesBuscar.setMargin(true);
                panelIzquierdo.addComponent(botonesBuscar);

            }
        };
        MenuBar.Command logoutCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getSession().close();
                getUI().getPage().setLocation(getLogoutPath());
            }
        };

        MenuBar.MenuItem alumnos = menuBar.addItem("Modificar Datos", alumnoCommand);
        MenuBar.MenuItem asignaturas = menuBar.addItem("Buscar Profesor", profesorCommand);
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
        text.setValue("Hello alumno " + username);
    }
}
