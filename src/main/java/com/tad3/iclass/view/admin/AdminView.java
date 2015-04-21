package com.tad3.iclass.view.admin;

import com.tad3.iclass.dao.AsignaturaDAO;
import com.tad3.iclass.entidad.Asignatura;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalSplitPanel;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laura
 */
public class AdminView extends CustomComponent implements View {

    public static final String NAME = "admin";

    Label text = new Label();
    Layout layout = new HorizontalLayout();
    Panel panel = new Panel("Bienvenido a la página de administración");

    //Asignatura
    BeanItemContainer<Asignatura> bAsignatura = new BeanItemContainer(Asignatura.class);
    Table tAsignaturas = new Table("Asignaturas", bAsignatura);
    ComboBox buscadorAsignaturas = new ComboBox("Asignatura: ");

    TextField id_asig = new TextField("ID: ");
    TextField nombre_asig = new TextField("Nombre: ");
    ComboBox curso_asig = new ComboBox("Curso: ");
    TextField descripcion_asig = new TextField("Descripción: ");

    FormLayout datosAsignatura = new FormLayout(id_asig, nombre_asig, curso_asig, descripcion_asig);

    Button nuevaAsignatura = new Button("Nueva asignatura");
    Button addAsignatura = new Button("Añadir asignatura");

    FormLayout asignaturaNueva = new FormLayout(id_asig, nombre_asig, curso_asig, descripcion_asig, addAsignatura);
    HorizontalLayout hlasignatura = new HorizontalLayout(buscadorAsignaturas, nuevaAsignatura);
    VerticalSplitPanel vspAsignatura = new VerticalSplitPanel(hlasignatura, tAsignaturas);
    HorizontalSplitPanel hspAsignatura = new HorizontalSplitPanel(vspAsignatura, datosAsignatura);

    private String getLogoutPath() {
        return getUI().getPage().getLocation().getPath();
    }

    public AdminView() {

        layout.setSizeFull();

        MenuBar menu = new MenuBar();
        menu.setSizeFull();

        layout.addComponent(menu);

        // Define a common menu command for all the menu items.
        MenuBar.Command mycommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                if (selectedItem.getText().equals("Asignaturas")) {
                    panel.setContent(hspAsignatura);
                }
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

        //Asignatura

        String[] columnHeadersAsig = {"Curso", "Descripción","ID", "Asignatura"};

        tAsignaturas.setColumnHeaders(columnHeadersAsig);
        tAsignaturas.setSizeFull();
        tAsignaturas.setPageLength(tAsignaturas.size());
        tAsignaturas.setSelectable(true);
        tAsignaturas.setImmediate(true);

        List<Asignatura> listaAsignaturas = new ArrayList<>();
        AsignaturaDAO asignatura = new AsignaturaDAO();
        try {
            listaAsignaturas = asignatura.listaAsignaturas();
        } catch (UnknownHostException ex) {
            Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
        }

        bAsignatura.addAll(listaAsignaturas);

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

        buscadorAsignaturas.setInputPrompt("Ninguna asignatura seleccionada");

        buscadorAsignaturas.setWidth(100.0f, Unit.PERCENTAGE);
        buscadorAsignaturas.setSizeFull();

        buscadorAsignaturas.setFilteringMode(FilteringMode.CONTAINS);
        buscadorAsignaturas.setImmediate(true);

        buscadorAsignaturas.setNullSelectionAllowed(false);
        
        for(Asignatura asig : listaAsignaturas){
            buscadorAsignaturas.addItem(asig.getIdAsignatura());
            buscadorAsignaturas.setItemCaption(asig.getIdAsignatura(), asig.toString());
        }
        
        //BUSCADOR: realizar accion

        hlasignatura.setMargin(true);
        hlasignatura.setSizeFull();
        hlasignatura.setComponentAlignment(buscadorAsignaturas, Alignment.MIDDLE_LEFT);
        hlasignatura.setComponentAlignment(nuevaAsignatura, Alignment.MIDDLE_RIGHT);

        id_asig.setRequired(true);
        id_asig.setInputPrompt("3 INICIALES + CURSO");
        nombre_asig.setRequired(true);
        nombre_asig.setInputPrompt("Nombre de la asignatura");
        curso_asig.setRequired(true);
        curso_asig.setInputPrompt("Curso de la asignatura");
        descripcion_asig.setRequired(false);
        descripcion_asig.setInputPrompt("Descripcion de la asignatura");

        asignaturaNueva.setComponentAlignment(id_asig, Alignment.MIDDLE_CENTER);
        asignaturaNueva.setComponentAlignment(nombre_asig, Alignment.MIDDLE_CENTER);
        asignaturaNueva.setComponentAlignment(curso_asig, Alignment.MIDDLE_CENTER);
        asignaturaNueva.setComponentAlignment(descripcion_asig, Alignment.MIDDLE_CENTER);
        asignaturaNueva.setCaption("Introduzca los datos de la nueva asignatura");
        asignaturaNueva.setMargin(true);
        asignaturaNueva.setSizeFull();

//        datosAsignatura.setComponentAlignment(id_asig, Alignment.MIDDLE_CENTER);
//        datosAsignatura.setComponentAlignment(nombre_asig, Alignment.MIDDLE_CENTER);
//        datosAsignatura.setComponentAlignment(curso_asig, Alignment.MIDDLE_CENTER);
//        datosAsignatura.setComponentAlignment(descripcion_asig, Alignment.MIDDLE_CENTER);
        datosAsignatura.setMargin(true);
        datosAsignatura.setReadOnly(true);

        vspAsignatura.setSplitPosition(17.5f, Unit.PERCENTAGE);
        vspAsignatura.setLocked(true);
        hspAsignatura.setSplitPosition(75.0f, Unit.PERCENTAGE);
        hspAsignatura.setLocked(true);

        //Datos Asignatura
        tAsignaturas.addItemClickListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                BeanItem<Asignatura> bi = bAsignatura.getItem(event.getItemId());
                Asignatura a = bi.getBean();
                id_asig.setValue(a.getIdAsignatura());
                nombre_asig.setValue(a.getNombre());
                curso_asig.setValue(a.getCurso());
                descripcion_asig.setValue(a.getDescripcion());
            }
        });

        nuevaAsignatura.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                id_asig.setValue("");
                nombre_asig.setValue("");
                curso_asig.select("");
                descripcion_asig.setValue("");
                hspAsignatura.setSecondComponent(asignaturaNueva);
            }
        });

        addAsignatura.addClickListener(new Button.ClickListener() {
            AsignaturaDAO asignatura = new AsignaturaDAO();

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Asignatura a = new Asignatura();

                a.setIdAsignatura(id_asig.getValue());
                a.setNombre(nombre_asig.getValue());
                a.setCurso((String) curso_asig.getValue());
                a.setDescripcion(descripcion_asig.getValue());
                try {
                    if (asignatura.crearAsignatura(a)) {
                        Notification.show("Asignatura creada", "Se ha añadido una nueva "
                                + "asignatura a la base de datos", Notification.Type.TRAY_NOTIFICATION);
                    } else {
                        Notification.show("ERROR", "No se ha podido añadir una nueva "
                                + "asignatura a la base de datos", Notification.Type.ERROR_MESSAGE);
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        panel.setWidth(String.valueOf(Page.getCurrent().getBrowserWindowWidth()) + "px");
        panel.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");
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
