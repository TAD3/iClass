package com.tad3.iclass.view;

import com.mongodb.BasicDBObject;
import com.tad3.iclass.dao.AsignaturaDAO;
import com.tad3.iclass.dao.LugarDAO;
import com.tad3.iclass.dao.ProfesorDAO;
import com.tad3.iclass.entidad.Asignatura;
import com.tad3.iclass.entidad.Lugar;
import com.tad3.iclass.entidad.Profesor;
import com.tad3.iclass.util.ExampleUtil;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.event.dd.acceptcriteria.ClientSideCriterion;
import com.vaadin.event.dd.acceptcriteria.SourceIs;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
import static com.vaadin.shared.ui.dd.VerticalDropLocation.BOTTOM;
import static com.vaadin.shared.ui.dd.VerticalDropLocation.MIDDLE;
import static com.vaadin.shared.ui.dd.VerticalDropLocation.TOP;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TargetItemAllowsChildren;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vista profesor
 *
 * @author
 */
public class ProfesorView extends CustomComponent implements View {

    public static final String NAME = "profesor";
    ProfesorDAO profesor = new ProfesorDAO();
    Tree tree;
    List<Asignatura> listaAsignaturas = new ArrayList<>();

    Table tableAsignaturas = new Table("Mis asignaturas");
    Table tableDragDrop;

    /*Todos los objetos usados para crear la interfaz de profesor*/
    TextField id_profesor = new TextField("ID: ");
    ComboBox id_lugar_profesor = new ComboBox("ID lugar: ");
    ComboBox asignatura = new ComboBox("ID lugar: ");
    TextField nombre_profesor = new TextField("Nombre: ");
    TextField apellidos_profesor = new TextField("Apellidos: ");
    TextField edad_profesor = new TextField("Edad: ");
    TextField email_profesor = new TextField("Email: ");
    TextField movil_profesor = new TextField("Móvil: ");
    TextField password_profesor = new TextField("Contraseña: ");
    TextField repassword_profesor = new TextField("Repetir contraseña: ");
    TextField descripcion_profesor = new TextField("Descripción: ");
    TextField horario_profesor = new TextField("Horario: ");
    ArrayList<Asignatura> asignaturas_profesor = new ArrayList<>();

    Button modifyme = new Button("Actualizar");
    Button saveme = new Button("Guardar");
    Button deleteme = new Button("Borrar cuenta");
    Button cancel = new Button("Cancelar");
    Button guardar = new Button("Guardar Asignaturas");
    Button manageCourses = new Button("Gestionar Asignaturas");

    HorizontalLayout botonesProfesor = new HorizontalLayout(saveme, deleteme, cancel);
    VerticalLayout panelPrincipal = new VerticalLayout();
    VerticalLayout panelIzquierdo = new VerticalLayout();
    HorizontalLayout panelDerecho = new HorizontalLayout();
    Layout layaoutArriba = new HorizontalLayout();
    MenuBar menuBar = new MenuBar();
    HorizontalSplitPanel panelSubPrincipal = new HorizontalSplitPanel();
    Panel panel = new Panel(panelSubPrincipal);
    Label text = new Label();

    /**
     * Boton para terminar la sesion
     */
    Button logoutButton = new Button("Logout", new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            getUI().getSession().close();
            getUI().getPage().setLocation(getLogoutPath());
        }
    });

    /**
     * Metodo para volver a la pagina de login
     *
     * @return getUI().getPage().getLocation().getPath() Devuelve la ruta de la
     * pagina login
     */
    private String getLogoutPath() {
        return getUI().getPage().getLocation().getPath();
    }

    public ProfesorView() throws Exception {

        /*Definiendo las caracterisiticas de los atributos creados anteriormente*/
        panelIzquierdo.setSpacing(true);
        panelIzquierdo.setMargin(true);
        panelDerecho.setSpacing(true);
        panelDerecho.setMargin(true);
        botonesProfesor.setSpacing(true);
        botonesProfesor.setMargin(true);

        tableAsignaturas.setSizeFull();
        tableAsignaturas.setPageLength(tableAsignaturas.size());
        tableAsignaturas.setSelectable(true);
        tableAsignaturas.setImmediate(true);

        layaoutArriba.setSizeFull();
        menuBar.setSizeFull();

        panelSubPrincipal.setSplitPosition(30.0f, Unit.PERCENTAGE);
        panelSubPrincipal.setLocked(true);

        /*Arbol y tabla para el drag and drop*/
        tree = new Tree("Selecciona la asignatura y arrastrala a la tabla");
        tree.setSelectable(false);
        tree.setSizeFull();
        tableDragDrop = new Table("Arrastra aqui asignaturas desde la lista");
        tableDragDrop.setPageLength(tableDragDrop.size());

        initializeTree(new SourceIs(tableDragDrop));
        initializeTable(new SourceIs(tree));

        /*Rellenando el atributo id_lugar_profesor con los lugares que tenemos en la coleccion de lugar*/
        Collection<Lugar> lugares = new ArrayList<>();
        LugarDAO lugarDAO = new LugarDAO();
        Iterator<Lugar> it;
        try {
            it = lugarDAO.listaLugares().iterator();
            while (it.hasNext()) {
                lugares.add(it.next());
            }
            id_lugar_profesor.setInputPrompt("Ningún lugar seleccionado");
            id_lugar_profesor.setWidth(100.0f, Unit.PERCENTAGE);
            id_lugar_profesor.setFilteringMode(FilteringMode.CONTAINS);
            id_lugar_profesor.setImmediate(true);

            for (Lugar lug : lugares) {
                id_lugar_profesor.addItem(lug.getIdLugar());
                id_lugar_profesor.setItemCaption(lug.getIdLugar(), lug.toString());
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*Menu Modificar Datos del profesor*/
        MenuBar.Command profesorCommand;
        profesorCommand = new MenuBar.Command() {

            /**
             * Metodo donde definimos las acciones que contendra el menu
             * Modificar Datos del profesor
             *
             * @param selectedItem
             */
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                /*Borramos todo lo que haya en los paneles*/
                panelIzquierdo.removeAllComponents();
                panelDerecho.removeAllComponents();

                /*Cargamos los datos del profesor*/
                try {
                    Profesor a = profesor.profesor((String) getSession().getAttribute("user"));
                    id_profesor.setValue(a.getIdProfesor());
                    id_lugar_profesor.setValue(a.getIdLugar());
                    nombre_profesor.setValue(a.getNombre());
                    apellidos_profesor.setValue(a.getApellidos());
                    edad_profesor.setValue(a.getEdad());
                    email_profesor.setValue(a.getEmail());
                    movil_profesor.setValue(a.getMovil());
                    password_profesor.setValue(a.getPassword());
                    repassword_profesor.setValue(a.getPassword());
                    descripcion_profesor.setValue(a.getDescripcion());
                    horario_profesor.setValue(a.getHorario());
                    llenarTablaAsig(a);

                } catch (UnknownHostException ex) {
                    Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                }
                /*Ponemos los objetos de la interfaz que no se puedan modificar*/
                id_profesor.setReadOnly(true);
                id_lugar_profesor.setReadOnly(true);
                nombre_profesor.setReadOnly(true);
                apellidos_profesor.setReadOnly(true);
                edad_profesor.setReadOnly(true);
                email_profesor.setReadOnly(true);
                movil_profesor.setReadOnly(true);
                password_profesor.setReadOnly(true);
                descripcion_profesor.setReadOnly(true);
                horario_profesor.setReadOnly(true);
                tableAsignaturas.setReadOnly(true);
                tableDragDrop.setReadOnly(true);

                /*Pulsamos en el boton Actualizar*/
                modifyme.addClickListener(new Button.ClickListener() {

                    /**
                     * Metodo que se activa cuando pulsamos el boton Actualizar,
                     * es el utilizado para que se activen los botones
                     * Modificar, Borrar y Cancelar
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {

                        id_profesor.setReadOnly(false);
                        id_lugar_profesor.setReadOnly(false);
                        nombre_profesor.setReadOnly(false);
                        apellidos_profesor.setReadOnly(false);
                        edad_profesor.setReadOnly(false);
                        email_profesor.setReadOnly(false);
                        movil_profesor.setReadOnly(false);
                        password_profesor.setReadOnly(false);
                        repassword_profesor.setReadOnly(false);
                        descripcion_profesor.setReadOnly(false);
                        horario_profesor.setReadOnly(false);
                        tableAsignaturas.setReadOnly(false);
                        tableDragDrop.setReadOnly(false);

                        /*Volvemos a traer los datos del profesor, lo hacemos por si la primera vez que hicimos esto y modificamos algo pero le dimos a cancelar que vuelva a cargar los datos correctamente*/
                        try {
                            Profesor a = profesor.profesor((String) getSession().getAttribute("user"));
                            id_profesor.setValue(a.getIdProfesor());
                            id_lugar_profesor.setValue(a.getIdLugar());
                            nombre_profesor.setValue(a.getNombre());
                            apellidos_profesor.setValue(a.getApellidos());
                            edad_profesor.setValue(a.getEdad());
                            email_profesor.setValue(a.getEmail());
                            movil_profesor.setValue(a.getMovil());
                            password_profesor.setValue(a.getPassword());
                            repassword_profesor.setValue(a.getPassword());
                            descripcion_profesor.setValue(a.getDescripcion());
                            horario_profesor.setValue(a.getHorario());

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        /*Añadimos todos los objetos del formulario al panel izquierdo*/
                        panelIzquierdo.addComponent(modifyme);
                        panelIzquierdo.addComponent(id_profesor);
                        panelIzquierdo.addComponent(id_lugar_profesor);
                        panelIzquierdo.addComponent(nombre_profesor);
                        panelIzquierdo.addComponent(apellidos_profesor);
                        panelIzquierdo.addComponent(edad_profesor);
                        panelIzquierdo.addComponent(email_profesor);
                        panelIzquierdo.addComponent(movil_profesor);
                        panelIzquierdo.addComponent(password_profesor);
                        panelIzquierdo.addComponent(repassword_profesor);
                        panelIzquierdo.addComponent(descripcion_profesor);
                        panelIzquierdo.addComponent(horario_profesor);
                        panelIzquierdo.addComponent(tableAsignaturas);
                        panelIzquierdo.addComponent(manageCourses);
                        panelIzquierdo.addComponent(botonesProfesor);

                    }

                });

                /*Pulsamos en el boton Guardar*/
                saveme.addClickListener(new Button.ClickListener() {

                    /**
                     * Metodo que se activa cuando pulsamos el boton Guardar, es
                     * el utilizado para modificar los datos del profesor
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        /*Guardamos en una variable de tipo profesor los datos que hemos recogidos del formulario*/
                        Profesor p = new Profesor();

                        p.setIdProfesor(id_profesor.getValue());
                        p.setIdLugar((String) id_lugar_profesor.getValue());
                        p.setNombre(nombre_profesor.getValue());
                        p.setApellidos(apellidos_profesor.getValue());
                        p.setEdad(edad_profesor.getValue());
                        p.setMovil(movil_profesor.getValue());
                        p.setHorario(horario_profesor.getValue());
                        p.setDescripcion(descripcion_profesor.getValue());
                        p.setEmail(email_profesor.getValue());
                        p.setPassword(password_profesor.getValue());
                        p.setAsignaturas((ArrayList) tableDragDrop.getData());

                        /*Comprobamos que los campo password y repassword sean iguales*/
                        if (password_profesor.getValue().equals(repassword_profesor.getValue())) {
                            p.setPassword(password_profesor.getValue());
                            try {
                                panelIzquierdo.removeAllComponents();
                                Profesor a1 = profesor.profesor((String) getSession().getAttribute("user"));
                                profesor.modificar(a1, p);
                                panelDerecho.removeAllComponents();
                                Notification.show("Profesor modificado", "Se ha actualizado el "
                                        + "profesor en la base de datos", Notification.Type.TRAY_NOTIFICATION);

                            } catch (UnknownHostException ex) {
                                Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            Notification.show("No coincide los campos contraseña y repetir contraseña", Notification.Type.WARNING_MESSAGE);
                        }
                    }
                });

                /*Pulsamos en el boton Borrar*/
                deleteme.addClickListener(new Button.ClickListener() {

                    /**
                     * Metodo que se activa cuando pulsamos el boton Borrar, es
                     * el utilizado para borrar la cuenta de un profesor
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        /*Borra el profesor, cierra la sesion y nos devuelve a la pagina de login*/
                        try {
                            Profesor a3 = profesor.profesor((String) getSession().getAttribute("user"));
                            profesor.borrar(a3.getEmail());
                            getUI().getSession().close();
                            getUI().getPage().setLocation(getLogoutPath());
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                /*Pulsamos el boton Gestionar Asignaturas*/
                manageCourses.addClickListener(new Button.ClickListener() {
                    /**
                     * Metodo que se activa cuando pulsamos el boton Gestionar
                     * Asignaturas, es el utilizado para seleccionar las
                     * asignaturas que un profesor impartirá
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        panelDerecho.addComponent(tree);
                        panelDerecho.addComponent(tableDragDrop);
                        panelDerecho.addComponent(guardar);
                        panelDerecho.setComponentAlignment(tree, Alignment.MIDDLE_CENTER);
                        panelDerecho.setComponentAlignment(tableDragDrop, Alignment.MIDDLE_CENTER);
                        panelDerecho.setComponentAlignment(guardar, Alignment.MIDDLE_CENTER);
                    }
                });

                /*Pulsamos el boton Guardar Asignaturas*/
                guardar.addClickListener(new Button.ClickListener() {
                    /**
                     * Metodo que se activa cuando pulsamos el boton Guardar
                     * Asignaturas, modficará las asignaturas que un profesor
                     * imparte
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        AsignaturaDAO a = new AsignaturaDAO();
                        Collection auxAsigs = tableDragDrop.getItemIds();
                        ArrayList<BasicDBObject> asigs = new ArrayList<>();
                        for (Object aux : auxAsigs) {
                            String split = aux.toString();
                            String[] values = split.split(" - ");
                            BasicDBObject as = null;
                            try {
                                as = a.buscarAsignaturaNombreYCurso(values[1], values[2]);
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            asigs.add(as);
                        }
                        try {
                            panelIzquierdo.removeAllComponents();
                            Profesor p = profesor.profesor((String) getSession().getAttribute("user"));
                            profesor.addAsignaturas(p.getEmail(), asigs);
                            panelDerecho.removeAllComponents();
                            llenarTablaAsig(p);

                            Notification.show("Profesor modificado", "Se ha actualizado el "
                                    + "profesor en la base de datos", Notification.Type.TRAY_NOTIFICATION);

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

                /*Pulsamos en el boton Cancelar*/
                cancel.addClickListener(new Button.ClickListener() {

                    /**
                     * Metodo que se activa cuando pulsamos el boton Cancelar,
                     * es el utilizado cuando queremos cancelar la accion de
                     * modificar o borrar un profesor
                     *
                     * @param event
                     */
                    @Override
                    public void buttonClick(ClickEvent event) {
                        /*Borramos todo lo que haya en los paneles*/
                        panelIzquierdo.removeComponent(repassword_profesor);
                        panelIzquierdo.removeComponent(botonesProfesor);

                        try {
                            Profesor a = profesor.profesor((String) getSession().getAttribute("user"));
                            id_profesor.setValue(a.getIdProfesor());
                            id_lugar_profesor.setValue(a.getIdLugar());
                            nombre_profesor.setValue(a.getNombre());
                            apellidos_profesor.setValue(a.getApellidos());
                            edad_profesor.setValue(a.getEdad());
                            email_profesor.setValue(a.getEmail());
                            movil_profesor.setValue(a.getMovil());
                            password_profesor.setValue(a.getPassword());
                            repassword_profesor.setValue(a.getPassword());
                            descripcion_profesor.setValue(a.getDescripcion());
                            horario_profesor.setValue(a.getHorario());

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AlumnoView.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        id_profesor.setReadOnly(true);
                        id_lugar_profesor.setReadOnly(true);
                        nombre_profesor.setReadOnly(true);
                        apellidos_profesor.setReadOnly(true);
                        edad_profesor.setReadOnly(true);
                        email_profesor.setReadOnly(true);
                        movil_profesor.setReadOnly(true);
                        password_profesor.setReadOnly(true);
                        descripcion_profesor.setReadOnly(true);
                        horario_profesor.setReadOnly(true);
                        tableAsignaturas.setReadOnly(true);
                        tableDragDrop.setReadOnly(true);

                    }
                });

                panelIzquierdo.addComponent(modifyme);
                panelIzquierdo.addComponent(id_profesor);
                panelIzquierdo.addComponent(id_lugar_profesor);
                panelIzquierdo.addComponent(nombre_profesor);
                panelIzquierdo.addComponent(apellidos_profesor);
                panelIzquierdo.addComponent(edad_profesor);
                panelIzquierdo.addComponent(email_profesor);
                panelIzquierdo.addComponent(movil_profesor);
                panelIzquierdo.addComponent(password_profesor);
                panelIzquierdo.addComponent(descripcion_profesor);
                panelIzquierdo.addComponent(horario_profesor);
                panelIzquierdo.addComponent(tableAsignaturas);
                panelIzquierdo.addComponent(manageCourses);

                panelIzquierdo.setComponentAlignment(tableAsignaturas, Alignment.MIDDLE_CENTER);
                panelIzquierdo.setComponentAlignment(manageCourses, Alignment.MIDDLE_CENTER);

            }
        };

        /*Menu Logout para cerrar la sesion*/
        MenuBar.Command logoutCommand = new MenuBar.Command() {
            
            /**
             * Metodo donde definimos las acciones que contendra el menu Logout
             *
             * @param selectedItem
             */
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getSession().close();
                getUI().getPage().setLocation(getLogoutPath());
            }
        };

        MenuBar.MenuItem profesor = menuBar.addItem("Modificar Datos", profesorCommand);
        menuBar.addItem("Log out", logoutCommand);

        layaoutArriba.addComponent(menuBar);
        panelPrincipal.addComponent(layaoutArriba);
        panelPrincipal.addComponent(panelSubPrincipal);

        panelSubPrincipal.setFirstComponent(panelIzquierdo);
        panelSubPrincipal.setSecondComponent(panelDerecho);
        panelSubPrincipal.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");

        setCompositionRoot(new CssLayout(panelPrincipal));
    }

    /**
     * Crea el árbol donde se mostrarán los cursos y las diferentes asignaturas
     * de cada uno para arrastrar a la tabla
     *
     * @param acceptCriterion
     */
    private void initializeTree(final ClientSideCriterion acceptCriterion) throws Exception {
        tree.setContainerDataSource(ExampleUtil.getAsignaturaContainer());
        tree.setItemCaptionPropertyId(ExampleUtil.as_PROPERTY_NAME);
        // Expand all nodes
        for (Iterator<?> it = tree.rootItemIds().iterator(); it.hasNext();) {
            tree.expandItemsRecursively(it.next());
        }
        tree.setDragMode(TreeDragMode.NODE);
        tree.setDropHandler(new DropHandler() {
            public void drop(DragAndDropEvent dropEvent) {
                // criteria verify that this is safe
                DataBoundTransferable t = (DataBoundTransferable) dropEvent.getTransferable();
                Container sourceContainer = t.getSourceContainer();
                Object sourceItemId = t.getItemId();
                Item sourceItem = sourceContainer.getItem(sourceItemId);
                String curso = sourceItem.getItemProperty("curso").toString();
                String nombre = sourceItem.getItemProperty("nombre").toString();

                AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent.getTargetDetails());
                Object targetItemId = dropData.getItemIdOver();

                // find curso in target: the target node itself or its parent
                if (targetItemId != null && nombre != null && curso != null) {
                    String treeCurso = getTreeNodeName(tree, targetItemId);
                    if (curso.equals(treeCurso)) {
                        // move item from tableDragDrop to curso'
                        Object newItemId = tree.addItem();
                        tree.getItem(newItemId)
                                .getItemProperty(ExampleUtil.as_PROPERTY_NAME)
                                .setValue(nombre);
                        tree.setParent(newItemId, targetItemId);
                        tree.setChildrenAllowed(newItemId, false);

                        sourceContainer.removeItem(sourceItemId);
                    }
                }
            }

            public AcceptCriterion getAcceptCriterion() {
                // Only allow dropping of data bound transferables within
                // folders.
                // In this example, checking for the correct curso in drop()
                // rather than in the criteria.
                return new And(acceptCriterion, TargetItemAllowsChildren.get(),
                        AcceptItem.ALL);
            }
        });
    }

    /**
     * Metodo que construye la tabla donde se arrastrarán las asignaturas
     *
     * @param acceptCriterion
     */
    private void initializeTable(final ClientSideCriterion acceptCriterion) {
        final BeanItemContainer<Asignatura> tableDragDropContainer = new BeanItemContainer<Asignatura>(Asignatura.class);
        // lista asignaturas del profesor
        tableDragDrop.setContainerDataSource(tableDragDropContainer);
        tableDragDrop.setVisibleColumns(new Object[]{"curso", "nombre"});

        // Handle drop in tableDragDrop: move hardware item or subtree to the tableDragDrop
        tableDragDrop.setDragMode(TableDragMode.ROW);
        tableDragDrop.setDropHandler(new DropHandler() {
            public void drop(DragAndDropEvent dropEvent) {
                // criteria verify that this is safe
                DataBoundTransferable t = (DataBoundTransferable) dropEvent
                        .getTransferable();
                if (!(t.getSourceContainer() instanceof Container.Hierarchical)) {
                    return;
                }
                Container.Hierarchical source = (Container.Hierarchical) t
                        .getSourceContainer();

                Object sourceItemId = t.getItemId();

                // find and convert the item(s) to move
                Object parentItemId = source.getParent(sourceItemId);
                // map from moved source item Id to the corresponding Asignatura
                LinkedHashMap<Object, Asignatura> asignaturaMap = new LinkedHashMap<Object, Asignatura>();
                if (parentItemId == null) {
                    // move the whole subtree
                    String curso = getTreeNodeName(source, sourceItemId);
                    Collection<?> children = source.getChildren(sourceItemId);
                    if (children != null) {
                        for (Object childId : children) {
                            String nombre = getTreeNodeName(source, childId);
                            Asignatura a = new Asignatura();
                            a.setCurso(curso);
                            a.setNombre(nombre);
                            asignaturaMap.put(childId, a);
                        }
                    }
                } else {
                    // move a single hardware item
                    String curso = getTreeNodeName(source, parentItemId);
                    String nombre = getTreeNodeName(source, sourceItemId);
                    Asignatura a = new Asignatura();
                    a.setCurso(curso);
                    a.setNombre(nombre);
                    asignaturaMap.put(sourceItemId, a);
                }

                // move item(s) to the correct location in the tableDragDrop
                AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent.getTargetDetails());
                Object targetItemId = dropData.getItemIdOver();

                for (Object sourceId : asignaturaMap.keySet()) {
                    Asignatura asignatura = asignaturaMap.get(sourceId);
                    if (targetItemId != null) {
                        switch (dropData.getDropLocation()) {
                            case BOTTOM:
                                tableDragDropContainer.addItemAfter(targetItemId, asignatura);
                                break;
                            case MIDDLE:
                            case TOP:
                                Object prevItemId = tableDragDropContainer
                                        .prevItemId(targetItemId);
                                tableDragDropContainer.addItemAfter(prevItemId, asignatura);
                                break;
                        }
                    } else {
                        tableDragDropContainer.addItem(asignatura);
                    }
                    source.removeItem(sourceId);
                }
            }

            public AcceptCriterion getAcceptCriterion() {
                return new And(acceptCriterion, AcceptItem.ALL);
            }
        });
    }

    /**
     * Devuelve el nombre del nodo padre del arbol
     */
    private static String getTreeNodeName(Container.Hierarchical source,
            Object sourceId) {
        return (String) source.getItem(sourceId)
                .getItemProperty(ExampleUtil.as_PROPERTY_NAME).getValue();
    }

    /**
     * Carga las asignaturas del profesor
     */
    public final void llenarTablaAsig(Profesor p) {
        tableAsignaturas.removeAllItems();
        listaAsignaturas = p.getAsignaturas();
        tableAsignaturas.addContainerProperty("Código - Asignatura - Curso", String.class, null);
        Iterator it = listaAsignaturas.iterator();
        Asignatura a;
        int i = 0;
        while (it.hasNext()) {
            a = (Asignatura) it.next();
            tableAsignaturas.addItem(new Object[]{a.toString()}, i);
            i++;
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello profesor " + username);
    }
}
