package com.tad3.iclass.view;

import com.tad3.iclass.LoginView;
import com.tad3.iclass.dao.AlumnoDAO;
import com.tad3.iclass.entidad.Alumno;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francisco
 */
public class NuevoAlumnoView extends CustomComponent implements View {

    public static final String NAME = "alumno";
    AlumnoDAO alumno = new AlumnoDAO();

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

    public NuevoAlumnoView() {
        final TextField idLugar = new TextField();
        final TextField idAlumno = new TextField();
        final TextField nombre = new TextField();
        final TextField apellidos = new TextField();
        final TextField edad = new TextField();
        final TextField curso = new TextField();
        final TextField email = new TextField();
        final PasswordField pass = new PasswordField();
        final PasswordField pass2 = new PasswordField();
        final TextField foto = new TextField();
        
        email.setRequired(true);
        email.setInputPrompt("Tu correo electrónico (miguel@email.com)");
        email.addValidator(new EmailValidator("El nombre de usuario debe ser un correo electrónico"));
        email.setInvalidAllowed(false);

        

        Button guardar = new Button("Guardar", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(pass.equals(pass2)){
                    if(!email.equals("")){
                        if(!idLugar.equals("")){
                            Alumno a = new Alumno();
                            a.setIdAlumno(idAlumno.getValue());
                            a.setIdLugar(idLugar.getValue());
                            a.setNombre(nombre.getValue());
                            a.setApellidos(apellidos.getValue());
                            a.setEdad(edad.getValue());
                            a.setCurso(curso.getValue());
                            a.setEmail(email.getValue());
                            a.setPassword(pass.getValue());
                            try {
                                if(alumno.crear(a)){
                                    panelPrincipal.addComponent(new Label("Usuario creado"));
                                }else{
                                    panelPrincipal.addComponent(new Label("Error"));
                                }
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(NuevoAlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }
                    }else {
                        
                    }
                }else {
                    
                }
            }
        });
        panelPrincipal.addComponent(idAlumno);
        panelPrincipal.addComponent(idLugar);
        panelPrincipal.addComponent(nombre);
        panelPrincipal.addComponent(apellidos);
        panelPrincipal.addComponent(edad);
        panelPrincipal.addComponent(curso);
        panelPrincipal.addComponent(email);
        panelPrincipal.addComponent(pass);
        panelPrincipal.addComponent(pass2);
        panelPrincipal.addComponent(foto);
        
        setCompositionRoot(panelPrincipal);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello alumno " + username);
    }
}
