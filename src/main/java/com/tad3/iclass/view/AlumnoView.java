package com.tad3.iclass.view;

import com.tad3.iclass.dao.AlumnoDAO;
import com.tad3.iclass.entidad.Alumno;
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
public class AlumnoView extends CustomComponent implements View {

    public static final String NAME = "alumno";
    AlumnoDAO alumno = new AlumnoDAO();

    VerticalLayout panelPrincipal = new VerticalLayout();
    HorizontalSplitPanel panelSubPrincipal = new HorizontalSplitPanel();
    VerticalLayout panelIzquierdo = new VerticalLayout();
    VerticalLayout panelDerecho = new VerticalLayout();
    Layout layaoutArriba = new HorizontalLayout();
    MenuBar menuBar = new MenuBar();
    TextField curso = new TextField();
    TextField asignatura = new TextField();

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

    public AlumnoView() {

        /*
         Menu Bar
         */
        menuBar.setSizeFull();

        MenuBar.Command alumnoCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                panelIzquierdo.removeAllComponents();

                final Table table = new Table();
                table.setPageLength(table.size());
                table.setSelectable(true);
                table.setImmediate(true);
                table.addContainerProperty("idAlumno", String.class, null);
                table.addContainerProperty("idLugar", String.class, null);
                table.addContainerProperty("nombre", String.class, null);
                table.addContainerProperty("apellidos", String.class, null);
                table.addContainerProperty("edad", String.class, null);
                table.addContainerProperty("curso", String.class, null);
                table.addContainerProperty("email", String.class, null);
                table.addContainerProperty("password", String.class, null);

                try {
                    Alumno a = alumno.alumno((String) getSession().getAttribute("user"));
                    table.addItem(new Object[]{a.getIdAlumno(), a.getIdLugar(), a.getNombre(), a.getApellidos(),
                        a.getEdad(), a.getCurso(), a.getEmail(), a.getPassword()}, 1);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                }

                table.addItemClickListener(new ItemClickEvent.ItemClickListener() {

                    @Override
                    public void itemClick(ItemClickEvent event) {
                        panelDerecho.removeAllComponents();
                        final TextField idAlu = new TextField("IdAlumno");
                        final TextField idLug = new TextField("IdLugar");
                        final TextField nomb = new TextField("Nombre");
                        final TextField apel = new TextField("Apellidos");
                        final TextField eda = new TextField("Edad");
                        final TextField curs = new TextField("Curso");
                        final TextField corr = new TextField("Email");
                        final TextField cont = new TextField("Password");
                        final TextField cont2 = new TextField("rePassword");
                        final TextField fot = new TextField("Foto");
                        try {

                            Alumno a = alumno.alumno((String) getSession().getAttribute("user"));
                            idAlu.setValue(a.getIdAlumno());
                            idLug.setValue(a.getIdLugar());
                            nomb.setValue(a.getNombre());
                            apel.setValue(a.getApellidos());
                            eda.setValue(a.getEdad());
                            curs.setValue(a.getCurso());
                            corr.setValue(a.getEmail());
                            cont.setValue(a.getPassword());
                            cont2.setValue(a.getPassword());

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        Button modificar = new Button("Guardar", new Button.ClickListener() {
                            @Override
                            public void buttonClick(ClickEvent event) {
                                Alumno a2 = new Alumno();
                                a2.setIdAlumno(idAlu.getValue());
                                a2.setIdLugar(idLug.getValue());
                                a2.setNombre(nomb.getValue());
                                a2.setApellidos(apel.getValue());
                                a2.setEdad(eda.getValue());
                                a2.setCurso(curs.getValue());
                                a2.setEmail(corr.getValue());
                                if (cont.getValue().equals(cont2.getValue())) {
                                    a2.setPassword(cont.getValue());
                                    try {
                                        Alumno a1 = alumno.alumno((String) getSession().getAttribute("user"));
                                        alumno.modificar(a1, a2);
                                        panelDerecho.removeAllComponents();
                                        panelIzquierdo.removeAllComponents();
                                        panelIzquierdo.addComponent(new Label("Actualizado"));
                                    } catch (UnknownHostException ex) {
                                        Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    cont2.setValue("no son iguales");
                                }

                            }
                        });

                        Button borrar = new Button("Borrar", new Button.ClickListener() {

                            @Override
                            public void buttonClick(ClickEvent event) {
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
                        panelDerecho.addComponent(idAlu);
                        panelDerecho.addComponent(idLug);
                        panelDerecho.addComponent(nomb);
                        panelDerecho.addComponent(apel);
                        panelDerecho.addComponent(eda);
                        panelDerecho.addComponent(curs);
                        panelDerecho.addComponent(corr);
                        panelDerecho.addComponent(cont);
                        panelDerecho.addComponent(cont2);
                        panelDerecho.addComponent(fot);
                        panelDerecho.addComponent(modificar);
                        panelDerecho.addComponent(borrar);
                    }
                });

                panelIzquierdo.addComponent(table);

            }
        };
        MenuBar.Command profesorCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                panelIzquierdo.removeAllComponents();
                panelDerecho.removeAllComponents();
                curso.setInputPrompt("Curso");
                asignatura.setInputPrompt("Asignatura");
                Button buscar = new Button("Buscar Profesor", new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {

                    }
                });
                panelIzquierdo.addComponent(curso);
                panelIzquierdo.addComponent(asignatura);
                panelIzquierdo.addComponent(buscar);
            }
        };
        MenuBar.Command logoutCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
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
        //panelPrincipal.setWidth(String.valueOf(Page.getCurrent().getBrowserWindowWidth()) + "px");
        //panelPrincipal.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");
        panelSubPrincipal.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");

        setCompositionRoot(new CssLayout(panelPrincipal));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello alumno " + username);
    }
}
