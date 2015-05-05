package com.tad3.iclass.view;

import com.tad3.iclass.dao.ProfesorDAO;
import com.tad3.iclass.entidad.Profesor;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanlu
 */
public class NuevoProfesorView extends CustomComponent implements View {
    
    public static final String NAME = "profesor";
    ProfesorDAO profesor = new ProfesorDAO();
    
    VerticalLayout panelPrincipal = new VerticalLayout();
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
    
    public NuevoProfesorView() {
        final TextField idProfesor = new TextField();
        final TextField idLugar = new TextField();
        final TextField nombre = new TextField();
        final TextField apellidos = new TextField();
        final TextField edad = new TextField();
        final TextField email = new TextField();
        final TextField movil = new TextField();
        final PasswordField pass = new PasswordField();
        final PasswordField pass2 = new PasswordField();
        final Calendar horario = new Calendar();
        final TextField descripcion = new TextField();
        final TextField foto = new TextField();
        
        nombre.setRequired(true);
        apellidos.setRequired(true);
        edad.setRequired(true);
        email.setRequired(true);
        email.setInputPrompt("Tu correo electrÃ³nico (miguel@email.com)");
        email.addValidator(new EmailValidator("El nombre de usuario debe ser un correo electrÃ³nico"));
        email.setInvalidAllowed(false);
        movil.setRequired(true);
        descripcion.setRequired(true);
        
        Button guardar = new Button("Guardar", new Button.ClickListener() {
            
            @Override
            public void buttonClick(ClickEvent event) {
                if (pass.equals(pass2)) {
                    
                    Profesor p = new Profesor();
                    p.setIdProfesor(idProfesor.getValue());
                    p.setIdLugar(idLugar.getValue());
                    p.setNombre(nombre.getValue());
                    p.setApellidos(apellidos.getValue());
                    p.setEdad(edad.getValue());
                    p.setEmail(email.getValue());
                    p.setMovil(movil.getValue());
                    p.setPassword(pass.getValue());
                    p.setHorario(horario.toString());
                    try {
                        if (profesor.crear(p)) {
                            panelPrincipal.addComponent(new Label("Usuario creado"));
                        } else {
                            panelPrincipal.addComponent(new Label("Error"));
                        }
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(NuevoProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        });
        panelPrincipal.addComponent(idProfesor);
        panelPrincipal.addComponent(idLugar);
        panelPrincipal.addComponent(nombre);
        panelPrincipal.addComponent(apellidos);
        panelPrincipal.addComponent(edad);
        panelPrincipal.addComponent(email);
        panelPrincipal.addComponent(pass);
        panelPrincipal.addComponent(pass2);
        panelPrincipal.addComponent(horario);
        panelPrincipal.addComponent(descripcion);
        panelPrincipal.addComponent(foto);
        panelPrincipal.addComponent(guardar);
        
        setCompositionRoot(panelPrincipal);
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello profesor " + username);
    }
}
