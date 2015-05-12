package com.tad3.iclass;

import com.tad3.iclass.dao.AlumnoDAO;
import com.tad3.iclass.dao.LugarDAO;
import com.tad3.iclass.dao.ProfesorDAO;
import com.tad3.iclass.entidad.Alumno;
import com.tad3.iclass.entidad.Lugar;
import com.tad3.iclass.entidad.Profesor;
import com.tad3.iclass.view.AlumnoView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;

/**
 * Clase que crea la vista del Login de la aplicación
 *
 * @author Laura
 * @author francisco
 */
@Theme("mytheme")
@Widgetset("com.tad3.iclass.MyAppWidgetset")
public class LoginView extends CustomComponent implements View {

    public static final String NAME = "login";

    private TextField user;
    private PasswordField password;
    private Button loginButton;

    @Override
    /**
     * Método que se centra en el campo email cuando se entra en el LoginView
     */
    public void enter(ViewChangeEvent event) {
        //Se centra en el campo de email cuando el usuario carga el LoginView
        user.focus();
    }

    /**
     * Método para crear la vista de LoginView
     */
    public LoginView() throws Exception {
        setSizeFull();

        // Crear el campo de entrada usuario
        user = new TextField("Usuario: ");

        user.setWidth("300px");
        user.setRequired(true);
        user.setInputPrompt("Tu correo electrónico (miguel@email.com)");
        user.addValidator(new EmailValidator("El nombre de usuario debe ser un correo electrónico"));
        user.setInvalidAllowed(false);

        // Crear el campo de entrada contraseña
        password = new PasswordField("Contraseña: ");
        password.setWidth("300px");
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        // Crear el botón de Login
        loginButton = new Button("Entrar");

        loginButton.addClickListener(new Button.ClickListener() {

            @Override
            /**
             * Método que se activa cuando se pulsa el boton Entrar para que 
             * un usuario pueda entrar en la aplicación
             *
             * @param event
             */
            public void buttonClick(ClickEvent event
            ) {
                // Validar el loginform 
                if (!user.isValid() || !password.isValid()) {
                    return;
                }

                String username = user.getValue();
                String pass = password.getValue();

                // Validar el correo y contraseña con la base de datos
                boolean isValid = false;
                boolean isAlumno = false;
                boolean isProfesor = false;
                boolean isAdmin = false;
                AlumnoDAO a = new AlumnoDAO();
                ProfesorDAO p = new ProfesorDAO();

                //Si el correo es admin@iclass.com es usuario Admin
                if (username.equals("admin@iclass.com") && pass.equals("passw0rd")) {
                    isValid = true;
                    isAdmin = true;
                } else {
                    //Sino puede ser profesor o alumno
                    try {
                        //Si el correo y la contraseña existen en la base de datos es alumno
                        if (a.existe(username, pass)) {
                            isValid = true;
                            isAlumno = true;
                        } //Si el correo y la contraseña existen en la base de datos es profesor
                        else if (p.existe(username, pass)) {
                            isValid = true;
                            isProfesor = true;
                        }
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (isValid) {

                    // Almacena el usuario actural en la sesión
                    getSession().setAttribute("user", username);

                    if (isAdmin) {
                        // Navega a la vista de AdminView
                        Notification.show("Usuario " + username + " logueado");
                        getUI().getNavigator().navigateTo(LoginUI.ADMINVIEW);
                    }

                    if (isProfesor) {
                        // Navega a la vista de ProfesorView
                        Notification.show("Usuario " + username + " logueado");
                        getUI().getNavigator().navigateTo(LoginUI.PROFESORVIEW);
                    }

                    if (isAlumno) {
                        // Navega a la vista de AlumnoView
                        Notification.show("Usuario " + username + " logueado");
                        getUI().getNavigator().navigateTo(LoginUI.ALUMNOVIEW);
                    }

                } else {

                    // Wrong password clear the password field and refocuses it
                    user.setValue("");
                    password.setValue(null);
                    user.focus();
                    
                    Notification.show("Usuario y/o contraseña incorrectas", "Introduzca un correo y "
                            + "una contraseña válidos", Notification.Type.WARNING_MESSAGE);
                    
                }
            }
        }
        );

        // Añadir todos los componentes al Layout
        VerticalLayout loginform = new VerticalLayout(user, password, loginButton);

        loginform.setCaption("Login");
        loginform.setSpacing(true);
        loginform.setMargin(true);
        loginform.setSizeUndefined();
        loginform.setComponentAlignment(user, Alignment.MIDDLE_CENTER);
        loginform.setComponentAlignment(password, Alignment.MIDDLE_CENTER);
        loginform.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
        loginform.setStyleName(Reindeer.LAYOUT_WHITE);

        
        Button crearAlu = new Button("Crear Alumno");
        Button crearPro = new Button("Crear Profesor");
        HorizontalLayout botonesCrear = new HorizontalLayout(crearAlu, crearPro);
        botonesCrear.setComponentAlignment(crearPro, Alignment.MIDDLE_RIGHT);
        botonesCrear.setComponentAlignment(crearAlu, Alignment.MIDDLE_LEFT);
        botonesCrear.setMargin(true);
        botonesCrear.setSpacing(true);

        VerticalLayout viewLayout = new VerticalLayout(loginform, botonesCrear);

        viewLayout.setSizeFull();
        viewLayout.setMargin(true);
        viewLayout.setComponentAlignment(loginform, Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(botonesCrear, Alignment.MIDDLE_CENTER);

        /*Formulario crear nuevo alumno*/
        VerticalLayout panelPrincipal = new VerticalLayout();
        final TextField id_alumno = new TextField("DNI: ");
        final ComboBox id_lugar_alumno = new ComboBox("ID lugar: ");
        final TextField nombre_alumno = new TextField("Nombre: ");
        final TextField apellidos_alumno = new TextField("Apellidos: ");
        final TextField edad_alumno = new TextField("Edad: ");
        final ComboBox curso_asig = new ComboBox("Curso: ");
        final TextField email_alumno = new TextField("Email: ");
        final TextField password_alumno = new TextField("Contraseña: ");
        final TextField repassword_alumno = new TextField("Repetir contraseña: ");

        email_alumno.setRequired(true);
        email_alumno.setInputPrompt("Tu correo electrónico (miguel@email.com)");
        email_alumno.addValidator(new EmailValidator("El nombre de usuario debe ser un correo electrónico"));
        email_alumno.setInvalidAllowed(false);

        /*Rellenamos el objeto id_lugar_alumno con los lugares que tenemos en la coleccion lugar*/
        Collection<Lugar> lugares = new ArrayList<>();

        LugarDAO lugarDAO = new LugarDAO();

        Iterator<Lugar> it;
        try {
            it = lugarDAO.listaLugares().iterator();
            while (it.hasNext()) {
                lugares.add(it.next());
            }
            id_lugar_alumno.setInputPrompt("Ningún lugar seleccionado");
            id_lugar_alumno.setFilteringMode(FilteringMode.CONTAINS);
            id_lugar_alumno.setImmediate(true);

            for (Lugar lug : lugares) {
                id_lugar_alumno.addItem(lug.getIdLugar());
                id_lugar_alumno.setItemCaption(lug.getIdLugar(), lug.toString());
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*Rellenamos el objeto curso_asig con los cursos*/
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

        /**
         * Pulsamos el boton Guardar del menu del formulario de crear un alumno
         * nuevo
         */
        Button guardar = new Button("Guardar", new Button.ClickListener() {

            /**
             * Metodo que se activa cuando pulsamos el boton Guardar, es el
             * utilizado cuando queremos crear un nuevo alumno en nuestra
             * aplicacion
             *
             * @param event
             */
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    boolean terminado = false;
                    AlumnoDAO alumnoDAO = new AlumnoDAO();
                    List<Alumno> listAlu = new ArrayList<>();
                    listAlu.addAll(alumnoDAO.listaAlumnos());
                    Iterator<Alumno> it = listAlu.iterator();

                    if (!email_alumno.equals("")) {
                        if (!id_alumno.equals("")) {
                            if (password_alumno.equals(repassword_alumno)) {
                                while ((it.hasNext() && terminado == false)) {
                                    Alumno a = it.next();
                                    if (a.getEmail().equals(email_alumno.getValue()) || a.getIdAlumno().equals(id_alumno)) {
                                        terminado = true;
                                        Notification.show("Campo email o identificador ya usado por otro usuario", Notification.Type.WARNING_MESSAGE);
                                    }
                                }
                            } else {
                                //Notification.show("No coincide los campos contraseña y repetir contraseña", Notification.Type.WARNING_MESSAGE);
                            }
                        } else {
                            Notification.show("Campo identificador vacio", Notification.Type.WARNING_MESSAGE);
                        }
                    } else {
                        Notification.show("Campo email vacio", Notification.Type.WARNING_MESSAGE);
                    }
                    if (terminado == false) {
                        Alumno a = new Alumno();
                        a.setIdAlumno(id_alumno.getValue());
                        a.setIdLugar((String) id_lugar_alumno.getValue());
                        a.setNombre(nombre_alumno.getValue());
                        a.setApellidos(apellidos_alumno.getValue());
                        a.setEdad(edad_alumno.getValue());
                        a.setCurso((String) curso_asig.getValue());
                        a.setEmail(email_alumno.getValue());
                        a.setPassword(password_alumno.getValue());
                        Notification.show("Alumno modificado", "Se ha actualizado el "
                                + "alumno en la base de datos", Notification.Type.TRAY_NOTIFICATION);
                        alumnoDAO.crear(a);
                        getUI().getPage().setLocation(getUI().getPage().getLocation().getPath());
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        /*Añadir los objetos del formulario de crear alumno en el panel principal*/
        panelPrincipal.addComponent(id_alumno);
        panelPrincipal.addComponent(id_lugar_alumno);
        panelPrincipal.addComponent(nombre_alumno);
        panelPrincipal.addComponent(apellidos_alumno);
        panelPrincipal.addComponent(edad_alumno);
        panelPrincipal.addComponent(curso_asig);
        panelPrincipal.addComponent(email_alumno);
        panelPrincipal.addComponent(password_alumno);
        panelPrincipal.addComponent(repassword_alumno);
        panelPrincipal.addComponent(guardar);

        /*Alinear los objetos del formulario en el centro*/
        panelPrincipal.setComponentAlignment(id_alumno, Alignment.MIDDLE_CENTER);
        panelPrincipal.setComponentAlignment(id_lugar_alumno, Alignment.MIDDLE_CENTER);
        panelPrincipal.setComponentAlignment(nombre_alumno, Alignment.MIDDLE_CENTER);
        panelPrincipal.setComponentAlignment(apellidos_alumno, Alignment.MIDDLE_CENTER);
        panelPrincipal.setComponentAlignment(edad_alumno, Alignment.MIDDLE_CENTER);
        panelPrincipal.setComponentAlignment(curso_asig, Alignment.MIDDLE_CENTER);
        panelPrincipal.setComponentAlignment(email_alumno, Alignment.MIDDLE_CENTER);
        panelPrincipal.setComponentAlignment(password_alumno, Alignment.MIDDLE_CENTER);
        panelPrincipal.setComponentAlignment(repassword_alumno, Alignment.MIDDLE_CENTER);
        panelPrincipal.setComponentAlignment(guardar, Alignment.MIDDLE_CENTER);

        /*Ventana emergente*/
        final Window window = new Window("Window");
        window.center();
        window.setContent(panelPrincipal);

        //boton crear alumno
        crearAlu.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(window);
            }
        });

        /*Formulario crear nuevo profesor*/
        VerticalLayout panelPrincipal2 = new VerticalLayout();
        final TextField id_profesor = new TextField("ID: ");
        final ComboBox id_lugar_profesor = new ComboBox("ID lugar: ");
        final TextField nombre_profesor = new TextField("Nombre: ");
        final TextField apellidos_profesor = new TextField("Apellidos: ");
        final TextField edad_profesor = new TextField("Edad: ");
        final TextField email_profesor = new TextField("Email: ");
        final TextField movil_profesor = new TextField("Móvil: ");
        final TextField password_profesor = new TextField("Contraseña: ");
        final TextField repassword_profesor = new TextField("Repetir contraseña: ");
        final TextField descripcion_profesor = new TextField("Descripción: ");
        final TextField horario_profesor = new TextField("Horario: ");
        final ArrayList asignaturas_profesor = new ArrayList();

        email_profesor.setRequired(true);
        email_profesor.setInputPrompt("Tu correo electrónico (miguel@email.com)");
        email_profesor.addValidator(new EmailValidator("El nombre de usuario debe ser un correo electrónico"));
        email_profesor.setInvalidAllowed(false);

        /*Añade los lugares al objeto id_lugar_profesor*/
        Collection<Lugar> lugares2 = new ArrayList<>();

        LugarDAO lugarDAO2 = new LugarDAO();

        Iterator<Lugar> it2;
        try {
            it2 = lugarDAO2.listaLugares().iterator();
            while (it2.hasNext()) {
                lugares2.add(it2.next());
            }
            id_lugar_profesor.setInputPrompt("Ningún lugar seleccionado");
            id_lugar_profesor.setFilteringMode(FilteringMode.CONTAINS);
            id_lugar_profesor.setImmediate(true);

            for (Lugar lug : lugares) {
                id_lugar_profesor.addItem(lug.getIdLugar());
                id_lugar_profesor.setItemCaption(lug.getIdLugar(), lug.toString());
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*Pulsamos el boton Guardar del menu del formulario de crear un profesor nuevo*/
        Button guardar2 = new Button("Guardar", new Button.ClickListener() {

            /**
             * Metodo que se activa cuando pulsamos el boton Guardar, es el
             * utilizado cuando queremos crear un nuevo profesor en nuestra
             * aplicacion
             *
             * @param event
             */
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    boolean terminado = false;
                    ProfesorDAO profesorDAO = new ProfesorDAO();
                    List<Profesor> listProf = new ArrayList<>();
                    listProf.addAll(profesorDAO.listaProfesores());
                    Iterator<Profesor> it = listProf.iterator();

                    if (!email_profesor.equals("")) {
                        if (!id_profesor.equals("")) {
                            if (password_profesor.equals(repassword_profesor)) {
                                while ((it.hasNext() && terminado == false)) {
                                    Profesor p = it.next();
                                    if (p.getEmail().equals(email_profesor.getValue()) || p.getIdProfesor().equals(id_profesor)) {
                                        terminado = true;
                                        Notification.show("Campo email o identificador ya usado por otro usuario", Notification.Type.WARNING_MESSAGE);
                                    }
                                }
                            } else {
                                //Notification.show("No coincide los campos contraseña y repetir contraseña", Notification.Type.WARNING_MESSAGE);
                            }
                        } else {
                            Notification.show("Campo identificador vacio", Notification.Type.WARNING_MESSAGE);
                        }
                    } else {
                        Notification.show("Campo email vacio", Notification.Type.WARNING_MESSAGE);
                    }
                    if (terminado == false) {
                        Profesor p = new Profesor();
                        p.setIdProfesor(id_profesor.getValue());
                        p.setIdLugar((String) id_lugar_profesor.getValue());
                        p.setNombre(nombre_profesor.getValue());
                        p.setApellidos(apellidos_profesor.getValue());
                        p.setEdad(edad_profesor.getValue());
                        p.setMovil(movil_profesor.getValue());
                        p.setHorario(horario_profesor.getValue());
                        p.setAsignaturas(asignaturas_profesor);
                        p.setDescripcion(descripcion_profesor.getValue());
                        p.setEmail(email_profesor.getValue());
                        p.setPassword(password_profesor.getValue());
                        Notification.show("Profesor modificado", "Se ha actualizado el "
                                + "profesor en la base de datos", Notification.Type.TRAY_NOTIFICATION);
                        profesorDAO.crear(p);
                        getUI().getPage().setLocation(getUI().getPage().getLocation().getPath());
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        /*Añade al panel todos los objetos para el formulario de crear un nuevo profesor*/
        panelPrincipal2.addComponent(id_profesor);
        panelPrincipal2.addComponent(id_lugar_profesor);
        panelPrincipal2.addComponent(nombre_profesor);
        panelPrincipal2.addComponent(apellidos_profesor);
        panelPrincipal2.addComponent(edad_profesor);
        panelPrincipal2.addComponent(movil_profesor);
        panelPrincipal2.addComponent(email_profesor);
        panelPrincipal2.addComponent(descripcion_profesor);
        panelPrincipal2.addComponent(password_profesor);
        panelPrincipal2.addComponent(repassword_profesor);
        panelPrincipal2.addComponent(horario_profesor);
        panelPrincipal2.addComponent(guardar2);

        /*Para centrar los objtos del formulario*/
        panelPrincipal2.setComponentAlignment(id_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(id_lugar_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(nombre_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(apellidos_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(edad_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(horario_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(descripcion_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(movil_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(email_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(password_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(repassword_profesor, Alignment.MIDDLE_CENTER);
        panelPrincipal2.setComponentAlignment(guardar2, Alignment.MIDDLE_CENTER);

        /*Ventana emergente*/
        final Window window2 = new Window("Window");
        window2.center();
        window2.setContent(panelPrincipal2);

        //boton crear profesor
        crearPro.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(window2);
            }
        });

        //viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

// Validator for validating the passwords
    private static final class PasswordValidator extends
            AbstractValidator<String> {

        public PasswordValidator() {
            super("La contraseña es incorrecta");
        }

        @Override
        protected boolean isValidValue(String value) {
            //
            // Password must be at least 8 characters long and contain at least
            // one number
            //
            if (value != null
                    && (value.length() < 8 || !value.matches(".*\\d.*"))) {
                return false;
            }
            return true;
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }
    }

    @WebServlet(urlPatterns = "/*", name = "LoginView", asyncSupported = true)
    @VaadinServletConfiguration(ui = LoginUI.class, productionMode = false)
    public static class Servlet extends VaadinServlet {
    }
}
