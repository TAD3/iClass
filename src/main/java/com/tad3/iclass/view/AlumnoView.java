package com.tad3.iclass.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 *
 * @author Laura
 */
public class AlumnoView extends CustomComponent implements View {

    public static final String NAME = "alumno";

    Label text = new Label();

    private Button logoutButton() {
        Button button = new Button("Logout", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getUI().getSession().close();
                getUI().getPage().setLocation(getLogoutPath());
            }
        });
        return button;
    }

    private String getLogoutPath() {
        return getUI().getPage().getLocation().getPath();
    }

    public AlumnoView() {
        setCompositionRoot(new CssLayout(text, logoutButton()));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello alumno " + username);
    }
}
