package com.tad3.iclass;

import com.tad3.iclass.dao.AlumnoDAO;
import com.tad3.iclass.dao.ProfesorDAO;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Laura
 */
@Theme("mytheme")
@Widgetset("com.tad3.iclass.MyAppWidgetset")
public class LoginView extends CustomComponent implements View {

    public static final String NAME = "login";

    private TextField user;

    private PasswordField password;

    private Button loginButton;

    @Override
    public void enter(ViewChangeEvent event) {
        // focus the username field when user arrives to the login view
        user.focus();
    }

    public LoginView() {
        setSizeFull();

//        final Styles styles = Page.getCurrent().getStyles();
//
//        // inject the new color as a style
//        styles.add(".reindeer.v-app {"
//                + "        background-image: url('../VAADIN/themes/mytheme/img/pizarra.jpg');"
//                + "        background-size: cover"
//                + "    }");

        // Create the user input field
        user = new TextField("Usuario: ");

        user.setWidth("300px");
        user.setRequired(true);
        user.setInputPrompt("Tu correo electrónico (miguel@email.com)");
        user.addValidator(new EmailValidator("El nombre de usuario debe ser un correo electrónico"));
        user.setInvalidAllowed(false);

        // Create the password input field
        password = new PasswordField("Contraseña: ");
        password.setWidth("300px");
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        // Create login button
        loginButton = new Button("Entrar");

        loginButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event
            ) {
                //
                // Validate the loginform using the navigator. By using validors for the
                // loginform we reduce the amount of queries we have to use to the database
                // for wrongly entered passwords
                //
                if (!user.isValid() || !password.isValid()) {
                    return;
                }

                String username = user.getValue();
                String pass = password.getValue();

                //
                // Validate username and password with database here. For examples sake
                // I use a dummy username and password.
                //
                boolean isValid = false;
                boolean isAlumno = false;
                boolean isProfesor = false;
                boolean isAdmin = false;
                AlumnoDAO a = new AlumnoDAO();
                ProfesorDAO p = new ProfesorDAO();

                //admin
                if (username.equals("admin@iclass.com") && pass.equals("passw0rd")) {
                    isValid = true;
                    isAdmin = true;
//                } else if (username.equals("profe@iclass.com") && pass.equals("passw0rd")) { //profesor
//                    isValid = true;
                } else {
                    try {
                        if (a.existe(username, pass)) { //alumno
                            isValid = true;
                            isAlumno = true;
                        } else if (p.existe(username, pass)) { //profesor
                            isValid = true;
                            isProfesor = true;
                        }
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (isValid) {

                    // Store the current user in the service session
                    getSession().setAttribute("user", username);

                    if (isAdmin) {
                        // Navigate to admin view
                        Notification.show("Usuario " + username + " logueado");
                        getUI().getNavigator().navigateTo(LoginUI.ADMINVIEW);
                    }

                    if (isProfesor) {
                        // Navigate to profe view
                        Notification.show("Usuario " + username + " logueado");
                        getUI().getNavigator().navigateTo(LoginUI.PROFESORVIEW);
                    }

                    if (isAlumno) {
                        // Navigate to alumno view
                        Notification.show("Usuario " + username + " logueado");
                        getUI().getNavigator().navigateTo(LoginUI.ALUMNOVIEW);
                    }

                } else {

                    // Wrong password clear the password field and refocuses it
                    user.setValue("");
                    password.setValue(null);
                    user.focus();

                }
            }
        }
        );

        // Add both to a panel
        VerticalLayout loginform = new VerticalLayout(user, password, loginButton);

        loginform.setCaption("Login");
        loginform.setSpacing(true);
        loginform.setMargin(true);
        loginform.setSizeUndefined();
        loginform.setComponentAlignment(user, Alignment.MIDDLE_CENTER);
        loginform.setComponentAlignment(password, Alignment.MIDDLE_CENTER);
        loginform.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
        loginform.setStyleName(Reindeer.LAYOUT_WHITE);

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(loginform);
        viewLayout.setSizeFull();
        viewLayout.setMargin(true);
        viewLayout.setComponentAlignment(loginform, Alignment.MIDDLE_CENTER);

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
