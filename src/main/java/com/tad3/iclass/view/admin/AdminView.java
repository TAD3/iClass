package com.tad3.iclass.view.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;

/**
 *
 * @author Laura
 */
public class AdminView extends CustomComponent implements View {

    public static final String NAME = "admin";

    Label text = new Label();
    Layout layout = new HorizontalLayout();
    Panel panel = new Panel("Bienvenido a la página de administración");

    private String getLogoutPath() {
        return getUI().getPage().getLocation().getPath();
    }

    public AdminView() {

        layout.setSizeFull();

        MenuBar menu = new MenuBar();
        menu.setSizeFull();

        layout.addComponent(menu);

// A feedback component
        final Label selection = new Label("");
        //layout.addComponent(selection);

// Define a common menu command for all the menu items.
        MenuBar.Command mycommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                selection.setValue("Ir a "
                        + selectedItem.getText()
                        + " from menu.");
            }
        };
        MenuBar.Command logoutCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                getUI().getSession().close();
                getUI().getPage().setLocation(getLogoutPath());
            }
        };

        MenuItem profesores = menu.addItem("Profesores", null, mycommand);
        MenuItem alumnos = menu.addItem("Alumnos", null, mycommand);
        MenuItem asignaturas = menu.addItem("Asignaturas", null, mycommand);
        MenuItem lugares = menu.addItem("Lugares", null, mycommand);
        lugares.addSeparator();
        menu.addItem("Log out", logoutCommand);

        panel.setWidth(String.valueOf(Page.getCurrent().getBrowserWindowWidth()) + "px");
        panel.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight()*0.9) + "px");
        panel.setContent(new Label("Este es el panel de administración de la "
                + "aplicación iClass. Aquí podrá gestionar las entidades Profesor,"
                + " Alumno, Asignatura y Lugar."));
        setCompositionRoot(new CssLayout(layout, panel));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello admin " + username);
    }
}
