package com.tad3.iclass;

import com.tad3.iclass.view.AlumnoView;
import com.tad3.iclass.view.ProfesorView_prueba;
import com.tad3.iclass.view.admin.AdminView;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.Navigator.ComponentContainerViewDisplay;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * Esta clase es la UI del LoginView que extiende la UI para poder cambiar de
 * vista según de qué tipo sea el usuario que se identifique en el LoginView
 *
 * @author Laura
 */
@Widgetset("com.tad3.iclass.MyAppWidgetset")
public class LoginUI extends UI {

    public Navigator navigator;

    //Nombres de las diferentes vistas
    public static final String LOGINVIEW = "login";
    public static final String ADMINVIEW = "admin";
    public static final String ALUMNOVIEW = "alumno";
    public static final String PROFESORVIEW = "profesor";

    @Override
    /**
     * Método para añadir las diferentes vistas según los tipos de usuarios
     *
     * @param request
     */
    protected void init(VaadinRequest request) {

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        ComponentContainerViewDisplay viewDisplay = new ComponentContainerViewDisplay(layout);

        //Obtener la UI actual y añadir las vistas de los usuarios
        navigator = new Navigator(UI.getCurrent(), viewDisplay);
        navigator.addView("", new LoginView());
        navigator.addView(ADMINVIEW, new AdminView());
        navigator.addView(ALUMNOVIEW, new AlumnoView());
        navigator.addView(PROFESORVIEW, new ProfesorView_prueba());

        getNavigator().addViewChangeListener(new ViewChangeListener() {

            @Override
            /**
             * Método para comprobar si un usuario se ha logueado
             *
             * @param request
             */
            public boolean beforeViewChange(ViewChangeEvent event) {

                // Comprobar si un usuario se ha logueado
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginView;

                if (!isLoggedIn && !isLoginView) {
                    // Redirigir al LoginView siempre si un usuario no se ha logueado todavía
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    //Si alguien intenta acceder al LoginView mientras ya está logueado se cancel
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }
        });
    }
}
