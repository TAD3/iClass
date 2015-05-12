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
import com.vaadin.ui.Alignment;
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
import org.vaadin.haijian.ExcelExporter;
import org.vaadin.haijian.PdfExporter;

/**
 *
 * @author francisco
 */
public class AlumnoView extends CustomComponent implements View {
    
    public static final String NAME = "alumno";
    AlumnoDAO alumno = new AlumnoDAO();

    /*Todos los objetos usados para crear la interfaz de alumno*/
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
    HorizontalLayout botonesExp = new HorizontalLayout();
    VerticalLayout panelPrincipal = new VerticalLayout();
    VerticalLayout panelIzquierdo = new VerticalLayout();
    VerticalLayout panelDerecho = new VerticalLayout();
    Layout layaoutArriba = new HorizontalLayout();
    MenuBar menuBar = new MenuBar();
    
    HorizontalSplitPanel panelSubPrincipal = new HorizontalSplitPanel();
    Panel panel = new Panel(panelSubPrincipal);
    Label text = new Label();

    /**
     * Boton para terminar la sesion
     */
    Button logoutButton = new Button("Logout", new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            getUI().getSession().close();
            getUI().getPage().setLocation(getLogoutPath());
        }
    });

    /**
     * Metodo para volver a la pagina de login
     *
     * @return getUI().getPage().getLocation().getPath() Devuelve la ruta de la
     * pagina login
     */
    private String getLogoutPath() {
        return getUI().getPage().getLocation().getPath();
    }

    /**
     * Metodo para crear la vista de alumno
     */
    public AlumnoView() {

        /*Definiendo las caracterisiticas de los atributos creados anteriormente*/
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

        /*Rellenando el atributo curso_asig con las asignaturas que tenemos*/
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

        /*Rellenando el atributo id_lugar_alumno con los lugares que tenemos en la coleccion de lugar*/
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

        /*Rellenando el atributo asignatura con las asignaturas que tenemos en la coleccion de asignatura*/
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

        /*Menu Modificar Datos del alumno*/
        MenuBar.Command alumnoCommand = new MenuBar.Command() {

            /**
             * Metodo donde definimos las acciones que contendra el menu
             * Modificar Datos del alumno
             *
             * @param selectedItem
             */
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                /*Borramos todo lo que haya en los paneles*/
                panelIzquierdo.removeAllComponents();
                panelDerecho.removeAllComponents();

                /*Cargamos los datos del alumno*/
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
                /*Ponemos los objetos de la interfaz que no se puedan modificar*/
                id_alumno.setReadOnly(true);
                id_lugar_alumno.setReadOnly(true);
                nombre_alumno.setReadOnly(true);
                apellidos_alumno.setReadOnly(true);
                edad_alumno.setReadOnly(true);
                email_alumno.setReadOnly(true);
                curso_asig.setReadOnly(true);
                password_alumno.setReadOnly(true);

                /*Pulsamos en el boton Actualizar*/
                modifyme.addClickListener(new Button.ClickListener() {

                    /**
                     * Metodo que se activa cuando pulsamos el boton Actualizar,
                     * es el utilizado para que se activen los botones
                     * Modificar, Borrar y Cancelar
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        /*Ponemos los objetos de la interfaz que se puedan modificar*/
                        id_alumno.setReadOnly(false);
                        id_lugar_alumno.setReadOnly(false);
                        nombre_alumno.setReadOnly(false);
                        apellidos_alumno.setReadOnly(false);
                        edad_alumno.setReadOnly(false);
                        email_alumno.setReadOnly(false);
                        curso_asig.setReadOnly(false);
                        password_alumno.setReadOnly(false);

                        /*Volvemos a traer los datos del alumno, lo hacemos por si la primera vez que hicimos esto y modificamos algo pero le dimos a cancelar que vuelva a cargar los datos correctamente*/
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

                        /*Añadimos todos los objetos del formulario al panel izquierdo*/
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

                /*Pulsamos en el boton Guardar*/
                saveme.addClickListener(new Button.ClickListener() {

                    /**
                     * Metodo que se activa cuando pulsamos el boton Guardar, es
                     * el utilizado para modificar los datos del alumno
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        /*Guardamos en una variable de tipo alumno los datos que hemos recogidos del formulario*/
                        Alumno p = new Alumno();
                        
                        p.setIdAlumno(id_alumno.getValue());
                        p.setIdLugar((String) id_lugar_alumno.getValue());
                        p.setNombre(nombre_alumno.getValue());
                        p.setApellidos(apellidos_alumno.getValue());
                        p.setEdad(edad_alumno.getValue());
                        p.setCurso((String) curso_asig.getValue());
                        p.setEmail(email_alumno.getValue());
                        p.setPassword(password_alumno.getValue());
                        p.setPassword(repassword_alumno.getValue());

                        /*Comprobamos que los campo password y repassword sean iguales*/
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

                /*Pulsamos en el boton Borrar*/
                deleteme.addClickListener(new Button.ClickListener() {

                    /**
                     * Metodo que se activa cuando pulsamos el boton Borrar, es
                     * el utilizado para borrar la cuenta de un alumno
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        /*Borra el alumno, cierra la sesion y nos devuelve a la pagina de login*/
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

                /*Pulsamos el boton Cancelar*/
                cancel.addClickListener(new Button.ClickListener() {
                    /**
                     * Metodo que se activa cuando pulsamos el boton Cancelar,
                     * es el utilizado cuando queremos cancelar la accion de
                     * modificar o borrar un alumno
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        /*Borramos todo lo que haya en los paneles*/
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

        /*Menu Buscar Profesor dependiendo el lugar y la asignatura*/
        MenuBar.Command profesorCommand = new MenuBar.Command() {

            /**
             * Metodo donde definimos las acciones que contendra el menu para
             * buscar un profesor
             *
             * @param selectedItem
             */
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                /*Borramos todo lo que haya en los paneles*/
                panelIzquierdo.removeAllComponents();
                panelDerecho.removeAllComponents();

                /*Pulsamos el boton Buscar Profesor*/
                buscar.addClickListener(new Button.ClickListener() {

                    /**
                     * Metodo que se activa cuando pulsamos el boton Buscar
                     * Profesor, es el utilizado cuando queremos buscar un
                     * profasor que esta en nuestro mismo barrio
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        /*Borramos todo lo que haya en el panel derecho*/
                        panelDerecho.removeAllComponents();

                        /*Creamos la tabla que mostrará los profesores encontrados*/
                        Table table = new Table();
                        table.setSizeFull();
                        table.setPageLength(table.size());
                        table.setImmediate(true);
                        try {
                            table.addContainerProperty("Nombre", String.class, null);
                            table.addContainerProperty("Apellidos", String.class, null);
                            table.addContainerProperty("Edad", String.class, null);
                            table.addContainerProperty("Móvil", String.class, null);
                            table.addContainerProperty("Correo", String.class, null);
                            table.addContainerProperty("Dirección", String.class, null);
                            table.addContainerProperty("Horario", String.class, null);

                            /*Buscamos los profesores*/
                            ProfesorDAO alumnoDAO = new ProfesorDAO();
                            Alumno a1 = alumno.alumno((String) getSession().getAttribute("user"));
                            Iterator<Profesor> it3 = alumnoDAO.buscarProfAsig(a1.getIdLugar(), (String) asignatura.getValue(), "si").iterator();
                            int i = 1;
                            if (alumnoDAO.buscarProfAsig(a1.getIdLugar(), (String) asignatura.getValue(), "").isEmpty()) {
                                panelDerecho.addComponent(new Label("No hay datos"));
                            } else {
                                while (it3.hasNext()) {
                                    Profesor p = it3.next();
                                    Lugar l = new Lugar();
                                    LugarDAO lugarDAO = new LugarDAO();
                                    l = lugarDAO.lugar(p.getIdLugar());
                                    table.addItem(new Object[]{p.getNombre(), p.getApellidos(), p.getEdad(), p.getMovil(), p.getEmail(), l.getBarrio(), p.getHorario()}, i);
                                    i++;
                                }
                                panelDerecho.addComponent(table);
                                botonesExp.removeAllComponents();
                                ExcelExporter excelExporter = new ExcelExporter(table);
                                excelExporter.setCaption("Exportar a Excel");
                                
                                PdfExporter pdfExporter = new PdfExporter(table);
                                pdfExporter.setCaption("Export a PDF");
                                pdfExporter.setWithBorder(false);
                                
                                botonesExp.addComponents(excelExporter, pdfExporter);
                                botonesExp.setComponentAlignment(excelExporter, Alignment.MIDDLE_LEFT);
                                botonesExp.setComponentAlignment(pdfExporter, Alignment.MIDDLE_RIGHT);
                                panelIzquierdo.addComponent(botonesExp);
                                panelIzquierdo.setComponentAlignment(botonesExp, Alignment.MIDDLE_CENTER);
                                
                            }
                            
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                });

                /*Pulsamos el boton BP todos lugares*/
                buscarTodo.addClickListener(new Button.ClickListener() {

                    /**
                     * Metodo que se activa cuando pulsamos el boton BP todos
                     * lugares, es el utilizado cuando queremos buscar un
                     * profasor sin importar el lugar
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        /*Borramos todo lo que haya en el panel derecho*/
                        panelDerecho.removeAllComponents();

                        /*Creamos la tabla que mostrará los profesores encontrados*/
                        Table table = new Table();
                        table.setSizeFull();
                        table.setPageLength(table.size());
                        table.setImmediate(true);
                        try {
                            table.addContainerProperty("Nombre", String.class, null);
                            table.addContainerProperty("Apellidos", String.class, null);
                            table.addContainerProperty("Edad", String.class, null);
                            table.addContainerProperty("Móvil", String.class, null);
                            table.addContainerProperty("Correo", String.class, null);
                            table.addContainerProperty("Dirección", String.class, null);
                            table.addContainerProperty("Horario", String.class, null);

                            /*Buscamos los profesores*/
                            ProfesorDAO alumnoDAO = new ProfesorDAO();
                            Alumno a1 = alumno.alumno((String) getSession().getAttribute("user"));
                            Iterator<Profesor> it3 = alumnoDAO.buscarProfAsig(a1.getIdLugar(), (String) asignatura.getValue(), "daIgual").iterator();
                            int i = 1;
                            String x = (String) asignatura.getValue();
                            if (alumnoDAO.buscarProfAsig(a1.getIdLugar(), (String) asignatura.getValue(), "daIgual").isEmpty()) {
                                panelDerecho.addComponent(new Label("No hay datos"));
                            } else {
                                while (it3.hasNext()) {
                                    Profesor p = it3.next();
                                    Lugar l = new Lugar();
                                    LugarDAO lugarDAO = new LugarDAO();
                                    l = lugarDAO.lugar(p.getIdLugar());
                                    table.addItem(new Object[]{p.getNombre(), p.getApellidos(), p.getEdad(), p.getMovil(), p.getEmail(), l.getBarrio(), p.getHorario()}, i);
                                    i++;
                                }
                                panelDerecho.addComponent(table);
                                botonesExp.removeAllComponents();
                                ExcelExporter excelExporter = new ExcelExporter(table);
                                excelExporter.setCaption("Exportar a Excel");
                                
                                PdfExporter pdfExporter = new PdfExporter(table);
                                pdfExporter.setCaption("Export a PDF");
                                pdfExporter.setWithBorder(false);
                                
                                botonesExp.addComponents(excelExporter, pdfExporter);
                                botonesExp.setComponentAlignment(excelExporter, Alignment.MIDDLE_LEFT);
                                botonesExp.setComponentAlignment(pdfExporter, Alignment.MIDDLE_RIGHT);
                                panelIzquierdo.addComponent(botonesExp);
                                panelIzquierdo.setComponentAlignment(botonesExp, Alignment.MIDDLE_CENTER);
                            }
                            
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                botonesBuscar.setMargin(true);
                panelIzquierdo.addComponent(asignatura);
                panelIzquierdo.addComponent(botonesBuscar);
                
            }
        };

        /*Menu Logout para cerrar la sesion*/
        MenuBar.Command logoutCommand = new MenuBar.Command() {

            /**
             * Metodo donde definimos las acciones que contendra el menu Logout
             *
             * @param selectedItem
             */
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {

                /*Cierra la sesion y vuelve a la pagina de login*/
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

    /**
     * Metodo que nos muestra el nombre del alumno logueado al entrar
     *
     * @param event
     */
    @Override
    public void enter(ViewChangeEvent event) {
        
        String username = String.valueOf(getSession().getAttribute("user"));
        text.setValue("Hello alumno " + username);
    }
}
