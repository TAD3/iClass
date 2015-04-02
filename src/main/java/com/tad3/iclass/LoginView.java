package com.tad3.iclass;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Laura
 */
@Theme("mytheme")
public class LoginView extends CustomComponent implements View{

    public static final String NAME = "login";

    private TextField user;

    private PasswordField password;

    private Button loginButton;

    @Override
    public void enter(ViewChangeEvent event) {
        // focus the username field when user arrives to the login view
        user.focus();
    }
    
    public LoginView()  {
        setSizeFull();

        // Create the user input field
        user = new TextField("User:");
        user.setWidth("300px");
        user.setRequired(true);
        user.setInputPrompt("Your username (eg. joe@email.com)");
        user.addValidator(new EmailValidator(
                "Username must be an email address"));
        user.setInvalidAllowed(false);

        // Create the password input field
        password = new PasswordField("Password:");
        password.setWidth("300px");
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        // Create login button
        loginButton = new Button("Login");
        loginButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                //
        // Validate the fields using the navigator. By using validors for the
        // fields we reduce the amount of queries we have to use to the database
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
        //admin
        if (username.equals("admin@iclass.com") && pass.equals("passw0rd")) {
            isValid = true;
        } else if (username.equals("profe@iclass.com") && pass.equals("passw0rd")) { //profesor
            isValid = true;
        } else if (username.equals("alumno@iclass.com") && pass.equals("passw0rd")) { //alumno
            isValid = true;
        }

        if (isValid) {

            // Store the current user in the service session
            getSession().setAttribute("user", username);

            if (username.equals("admin@iclass.com")) {
                // Navigate to admin view
                getUI().getNavigator().navigateTo(LoginUI.ADMINVIEW);
            }

            if (username.equals("profe@iclass.com")) {
                // Navigate to profe view
                getUI().getNavigator().navigateTo(LoginUI.PROFESORVIEW);
            }

            if (username.equals("alumno@iclass.com")) {
                // Navigate to alumno view
                getUI().getNavigator().navigateTo(LoginUI.ALUMNOVIEW);
            }

        } else {

            // Wrong password clear the password field and refocuses it
            user.setValue(null);
            password.setValue(null);
            user.focus();

        }
            }
        });

        // Add both to a panel
        VerticalLayout fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption("Please login to access the application. (admin/profe/alumno@iclass.com / passw0rd)");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, true));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

    // Validator for validating the passwords
    private static final class PasswordValidator extends
            AbstractValidator<String> {

        public PasswordValidator() {
            super("The password provided is not valid");
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