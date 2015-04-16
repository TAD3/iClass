package com.tad3.iclass.view;

import com.tad3.iclass.dao.AlumnoDAO;
import com.tad3.iclass.entidad.Alumno;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laura
 */
public class AlumnoView extends CustomComponent implements View {

    public static final String NAME = "alumno";

    Label text = new Label();
    Label nombre = new Label("nombre");
    
    Button modificar = new Button("Modificar", new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            AlumnoDAO alumno = new AlumnoDAO();
            VerticalLayout panel = new VerticalLayout();
            Label l = new Label();
            Table table = new Table();
            table.addContainerProperty("idAlumno", int.class, null);
            table.addContainerProperty("idLugar", int.class, null);
            table.addContainerProperty("nombre", String.class, null);
            table.addContainerProperty("apellidos", String.class, null);
            table.addContainerProperty("edad", int.class, null);
            table.addContainerProperty("curso", String.class, null);
            table.addContainerProperty("email", String.class, null);
            table.addContainerProperty("password", String.class, null);
            table.addContainerProperty("foto", String.class, null);
            try {
                List<Alumno> lista = alumno.listaAlumnos();
                Iterator it = lista.iterator();
                while(it.hasNext()){
                    
                    l.setValue(it.next().toString());
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            panel.addComponent(table);
            panel.addComponent(atras);
            panel.addComponent(l);
            setCompositionRoot(panel);

        }
    });

    Button atras = new Button("Atras", new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            VerticalLayout panel = new VerticalLayout();
            panel.addComponent(text);
            panel.addComponent(logoutButton);
            panel.addComponent(modificar);
            setCompositionRoot(panel);

        }
    });

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
        setCompositionRoot(new CssLayout(text, logoutButton, modificar));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello alumno " + username);
    }
}
