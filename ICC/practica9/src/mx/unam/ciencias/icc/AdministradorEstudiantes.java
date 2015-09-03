package mx.unam.ciencias.icc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * Clase para la interfaz gráfica de un administrador de
 * estudiantes.
 */
public class AdministradorEstudiantes {

    /* Componentes de la interfaz gráfica. */
    private JFrame ventana;
    private JTable tabla;
    private JLabel estado;
    private JButton botonGuardar;
    private JButton botonEliminar;
    private JMenuItem elemGuardar;
    private JMenuItem elemEliminar;
    private TableModelListener escuchaTabla;

    /* Archivo actual. */
    private File archivo;
    /* La base de datos/modelo. */
    private BaseDeDatosEstudiantes bdd;
    /* Bandera para saber si se han modificado los datos.*/
    private boolean modificado;

    /* Clase para dibujar enteros en la tabla. */
    private class DibujanteEnteros extends DefaultTableCellRenderer {

        /* Formateador de números. */
        private NumberFormat formato;

        /* Constructor que determina cuántos dígitos debe mostrar el
         * entero. */
        public DibujanteEnteros(int digitos) {
            super();
            formato = NumberFormat.getIntegerInstance();
            formato.setMinimumIntegerDigits(digitos);
            formato.setGroupingUsed(false);
            setHorizontalAlignment(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
        }

        /* Define el texto de la celda en la tabla. */
        public void setValue(Object value) {
            setText(formato.format((Integer)value));
        }
    }

    /* Clase para dibujar el promedio en la tabla. */
    private class DibujantePromedio extends DefaultTableCellRenderer {

        /* Formateador de números. */
        private NumberFormat formato;

        /* Constructor único. */
        public DibujantePromedio() {
            super();
            formato = NumberFormat.getNumberInstance();
            formato.setMinimumFractionDigits(2);
            setHorizontalAlignment(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
        }

        /* Define el texto de la celda en la tabla. */
        public void setValue(Object value) {
            setText(formato.format((Double)value));
        }
    }

    /* Crea un rotativo con los valores recibidos. */
    private JSpinner creaRotativo(double valor, double min,
                                  double max, double paso,
                                  String formato) {
        JSpinner spinner =
            new JSpinner(
                new SpinnerNumberModel(valor, min, max, paso));
        JSpinner.NumberEditor ne = new JSpinner.NumberEditor(spinner, formato);
        JTextField tf = ne.getTextField();
        tf.setColumns(17);
        spinner.setEditor(ne);
        return spinner;
    }

    /* Agrega una componente a un administrador de trazado. */
    private void agregaComponente(JPanel        p,
                                  Component     c,
                                  GridBagLayout gb,
                                  int           x,
                                  int           y) {
        GridBagConstraints ct = new GridBagConstraints();
        ct.anchor = GridBagConstraints.WEST;
        ct.gridx = x;
        ct.gridy = y;
        gb.setConstraints(c, ct);
        p.add(c);
    }

    /* Clase para un diálogo donde se pueda escribir la información
     * de un estudiante. */
    private class DialogoNuevoEstudiante extends JDialog {

        private JTextField nombre;
        private JSpinner cuenta;
        private JSpinner promedio;
        private JSpinner edad;
        private JButton aceptar;
        private JButton cancelar;
        private Estudiante estudiante;

        /* Construye un nuevo diálogo con la ventana que recibe como
         * padre. */
        public DialogoNuevoEstudiante(JFrame ventana) {
            super(ventana, "Agregar Estudiante", true);

            JPanel panel = new JPanel();
            GridBagLayout gb = new GridBagLayout();
            panel.setLayout(gb);
            panel.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 10, 0));
            JLabel etiqueta = new JLabel("Nombre: ");
            agregaComponente(panel, etiqueta, gb, 0, 0);
            nombre = new JTextField(20);
            agregaComponente(panel, nombre, gb, 1, 0);
            etiqueta = new JLabel("Matrícula: ");
            agregaComponente(panel, etiqueta, gb, 0, 1);
            cuenta = creaRotativo(500000, 500000, 999999999, 1, "000000000");
            agregaComponente(panel, cuenta, gb, 1, 1);
            etiqueta = new JLabel("Promedio: ");
            agregaComponente(panel, etiqueta, gb, 0, 2);
            promedio = creaRotativo(0, 0, 10, 0.01, "#0.00");
            agregaComponente(panel, promedio, gb, 1, 2);
            etiqueta = new JLabel("Edad: ");
            agregaComponente(panel, etiqueta, gb, 0, 3);
            edad = creaRotativo(15, 15, 99, 1, "##");
            agregaComponente(panel, edad, gb, 1, 3);

            JPanel hPanel = new JPanel();
            GridLayout gl = new GridLayout(1, 2);
            hPanel.setLayout(gl);
            hPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0));
            gl.setHgap(10);
            aceptar  = new JButton("Aceptar");
            hPanel.add(aceptar);
            cancelar = new JButton("Cancelar");
            hPanel.add(cancelar);

            JPanel vPanel = new JPanel();
            vPanel.setLayout(new BoxLayout(vPanel, BoxLayout.Y_AXIS));
            vPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
            vPanel.add(panel);
            vPanel.add(new JSeparator());
            vPanel.add(hPanel);

            getContentPane().add(vPanel);
            pack();

            nombre.setRequestFocusEnabled(true);
            nombre.requestFocus();
            setLocationRelativeTo(ventana);

            aceptar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        int c = (int)((Double)cuenta.getValue()).doubleValue();
                        double p = (Double)promedio.getValue();
                        int e = (int)((Double)edad.getValue()).doubleValue();
                        estudiante = new Estudiante(nombre.getText(), c, p, e);
                        setVisible(false);
                    }
                });

            cancelar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        estudiante = null;
                        setVisible(false);
                    }
                });

            compruebaNombre();
            nombre.getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        compruebaNombre();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        compruebaNombre();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        compruebaNombre();
                    }
                });
        }

        /* Comprueba que el campo de nombre sea válido. */
        public void compruebaNombre() {
            String nm = nombre.getText();
            if (nm.length() >= 3 && nm.indexOf(" ") != -1 &&
                !nm.startsWith(" ") && !nm.endsWith(" "))
                aceptar.setEnabled(true);
            else
                aceptar.setEnabled(false);
        }

        /* Regresa el nuevo estudiante. */
        public Estudiante getEstudiante() {
            return estudiante;
        }
    }
    
    private class DialogoAviso extends JDialog
    {
        
        private JButton aceptar;
        
        public DialogoAviso(JFrame ventana, String mr, String titulo, String si)
        {
            super(ventana, titulo, true);
            
            
            JPanel hPanel = new JPanel();
            
            GridLayout gl = new GridLayout(1, 2);
            hPanel.setLayout(gl);
            hPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            if (mr != null)
            {
                JLabel etiqueta= new JLabel(mr);
                hPanel.add(etiqueta);//
            }
            
            gl.setHgap(10);
            aceptar  = new JButton(si);
            hPanel.add(aceptar);
//            cancelar = new JButton("ff");
//            hPanel.add(cancelar);
            
            
            JPanel vPanel = new JPanel();
            vPanel.setLayout(new BoxLayout(vPanel, BoxLayout.Y_AXIS));
            vPanel.setBorder(
                             BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            
            //            vPanel.add(panel);
            //            vPanel.add(new JSeparator());
            
            vPanel.add(hPanel);
            
            getContentPane().add(vPanel);
            pack();
            
            //            nombre.setRequestFocusEnabled(true);
            //            nombre.requestFocus();
            setLocationRelativeTo(ventana);
            
            aceptar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
                    //                    int c = (int)((Double)cuenta.getValue()).doubleValue();
                    //                    double p = (Double)promedio.getValue();
                    //                    int e = (int)((Double)edad.getValue()).doubleValue();
                    //                    estudiante = new Estudiante(nombre.getText(), c, p, e);
                    //                    setVisible(false);
                    setVisible(false);
                }
            });
            
//            cancelar.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent evento) {
//
//                    setVisible(false);
//                }
//            });
            
            //            compruebaNombre();
            //            nombre.getDocument().addDocumentListener(new DocumentListener() {
            //                public void changedUpdate(DocumentEvent e) {
            //                    compruebaNombre();
            //                }
            //                public void insertUpdate(DocumentEvent e) {
            //                    compruebaNombre();
            //                }
            //                public void removeUpdate(DocumentEvent e) {
            //                    compruebaNombre();
            //                }
            //            });
            
            
            aceptar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
                    //                    int c = (int)((Double)cuenta.getValue()).doubleValue();
                    //                    double p = (Double)promedio.getValue();
                    //                    int e = (int)((Double)edad.getValue()).doubleValue();
                    //                    estudiante = new Estudiante(nombre.getText(), c, p, e);
                    //                    setVisible(false);
                    setVisible(false);
                }
            });
        }
    }
    private class DialogoDeDesicion extends JDialog {
        
//        private JTextField nombre;
//        private JSpinner cuenta;
//        private JSpinner promedio;
//        private JSpinner edad;
        
        private boolean respuesta;
        
        private JButton aceptar;
        private JButton cancelar;
//        private Estudiante estudiante;
        
        /* Construye un nuevo diálogo con la ventana que recibe como
         * padre. */
        
       
        public DialogoDeDesicion(JFrame ventana, String mr, String titulo, String si,String no) {
            super(ventana, titulo, true);
            
//            {
//            JPanel panel = new JPanel();
//            GridBagLayout gb = new GridBagLayout();
//            panel.setLayout(gb);
//            panel.setBorder(
//                            BorderFactory.createEmptyBorder(0, 0, 10, 0));
//            JLabel etiqueta;
//            
//            etiqueta= new JLabel("Nombre: ");
//            agregaComponente(panel, etiqueta, gb, 0, 0);
//            nombre = new JTextField(20);
//            agregaComponente(panel, nombre, gb, 1, 0);
//            
//            etiqueta = new JLabel("Matrícula: ");
//            agregaComponente(panel, etiqueta, gb, 0, 1);
//            cuenta = creaRotativo(500000, 500000, 999999999, 1, "000000000");
//            agregaComponente(panel, cuenta, gb, 1, 1);
//            
//            etiqueta = new JLabel("Promedio: ");
//            agregaComponente(panel, etiqueta, gb, 0, 2);
//            promedio = creaRotativo(0, 0, 10, 0.01, "#0.00");
//            agregaComponente(panel, promedio, gb, 1, 2);
//            
//            etiqueta = new JLabel("Edad: ");
//            agregaComponente(panel, etiqueta, gb, 0, 3);
//            edad = creaRotativo(15, 15, 99, 1, "##");
//            agregaComponente(panel, edad, gb, 1, 3);
//
//            }
            
            
            JPanel hPanel = new JPanel();
            
            GridLayout gl = new GridLayout(1, 2);
            hPanel.setLayout(gl);
            hPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            if (mr != null)
            {
            JLabel etiqueta= new JLabel(mr);
            hPanel.add(etiqueta);//
            }
            
            gl.setHgap(10);
            aceptar  = new JButton(si);
            hPanel.add(aceptar);
            cancelar = new JButton(no);
            hPanel.add(cancelar);
            
            
            JPanel vPanel = new JPanel();
            vPanel.setLayout(new BoxLayout(vPanel, BoxLayout.Y_AXIS));
            vPanel.setBorder(
                             BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            
//            vPanel.add(panel);
//            vPanel.add(new JSeparator());
            
            vPanel.add(hPanel);

            getContentPane().add(vPanel);
            pack();
            
//            nombre.setRequestFocusEnabled(true);
//            nombre.requestFocus();
            setLocationRelativeTo(ventana);
            
            aceptar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
//                    int c = (int)((Double)cuenta.getValue()).doubleValue();
//                    double p = (Double)promedio.getValue();
//                    int e = (int)((Double)edad.getValue()).doubleValue();
//                    estudiante = new Estudiante(nombre.getText(), c, p, e);
//                    setVisible(false);
                    respuesta = true;
                    setVisible(false);
                }
            });
            
            cancelar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
                    respuesta = false;
                    setVisible(false);
                }
            });
            
//            compruebaNombre();
//            nombre.getDocument().addDocumentListener(new DocumentListener() {
//                public void changedUpdate(DocumentEvent e) {
//                    compruebaNombre();
//                }
//                public void insertUpdate(DocumentEvent e) {
//                    compruebaNombre();
//                }
//                public void removeUpdate(DocumentEvent e) {
//                    compruebaNombre();
//                }
//            });
        }
        
        /* Comprueba que el campo de nombre sea válido. */
//        public void compruebaNombre() {
//            String nm = nombre.getText();
//            if (nm.length() >= 3 && nm.indexOf(" ") != -1 &&
//                !nm.startsWith(" ") && !nm.endsWith(" "))
//                aceptar.setEnabled(true);
//            else
//                aceptar.setEnabled(false);
//        }
        
        public boolean getRespuesta()
        {
            return respuesta;
        }
        /* Regresa el nuevo estudiante. */
//        public Estudiante getEstudiante() {
//            return estudiante;
//        }
    }

    /* Clase para mostrar un diálogo dónde escribir el nomre o
     * matrícula para buscar un estudiante. */
    private class DialogoBuscaEstudiantes extends JDialog {

        private JTextField nombre;
        private JSpinner cuenta;
        private JRadioButton botonNombre;
        private JRadioButton botonCuenta;
        private JButton buscar;
        private JButton cancelar;
        private Lista<Estudiante> resultados;

        private BaseDeDatosEstudiantes bdd;

        /* Construye un nuevo diálogo con la ventana que recibe como
         * padre, y con una base de datos. */
        public DialogoBuscaEstudiantes(JFrame ventana,
                                       BaseDeDatosEstudiantes b) {
            super(ventana, "Buscar Estudiantes", true);

            this.bdd = b;

            JPanel panel = new JPanel();
            GridBagLayout gb = new GridBagLayout();
            panel.setLayout(gb);
            panel.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 10, 0));

            botonNombre = new JRadioButton("Buscar por nombre:");
            botonNombre.setSelected(true);
            agregaComponente(panel, botonNombre, gb, 0, 0);
            nombre = new JTextField(20);
            agregaComponente(panel, nombre, gb, 1, 0);

            botonCuenta =
                new JRadioButton("Buscar por matrícula:");
            agregaComponente(panel, botonCuenta, gb, 0, 1);
            cuenta = creaRotativo(500000, 500000, 999999999, 1, "000000000");
            agregaComponente(panel, cuenta, gb, 1, 1);

            ButtonGroup grupo = new ButtonGroup();
            grupo.add(botonNombre);
            grupo.add(botonCuenta);

            JPanel hPanel = new JPanel();
            GridLayout gl = new GridLayout(1, 2);
            hPanel.setLayout(gl);
            hPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0));
            gl.setHgap(10);
            buscar = new JButton("Buscar");
            hPanel.add(buscar);
            cancelar = new JButton("Cancelar");
            hPanel.add(cancelar);

            JPanel vPanel = new JPanel();
            vPanel.setLayout(new BoxLayout(vPanel, BoxLayout.Y_AXIS));
            vPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
            vPanel.add(panel);
            vPanel.add(new JSeparator());
            vPanel.add(hPanel);

            getContentPane().add(vPanel);
            pack();

            nombre.setRequestFocusEnabled(true);
            nombre.requestFocus();
            setLocationRelativeTo(ventana);

            compruebaCampos();
            botonNombre.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        compruebaCampos();
                    }
                });

            botonCuenta.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        compruebaCampos();
                    }
                });

            buscar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        resultados = new Lista<Estudiante>();
                        String campo = "";
                        String cadena = "";
                        if (botonNombre.isSelected()) {
                            campo = "nombre";
                            cadena = nombre.getText();
                        } else {
                            campo = "cuenta";
                            cadena = nombre.getText();
                        }
                        resultados = bdd.buscaRegistros(campo, cadena);
                        setVisible(false);
                    }
                });

            cancelar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        resultados = new Lista<Estudiante>();
                        setVisible(false);
                    }
                });

            nombre.getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        compruebaCampos();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        compruebaCampos();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        compruebaCampos();
                    }
                });
        }

        /* Comprueba que los campos sean válidos, y actualiza el estado del
         * diálogo. */
        public void compruebaCampos() {
            if (botonNombre.isSelected()) {
                String nm = nombre.getText();
                if (nm.length() > 0)
                    buscar.setEnabled(true);
                else
                    buscar.setEnabled(false);
                nombre.setEnabled(true);
                cuenta.setEnabled(false);
            } else {
                buscar.setEnabled(true);
                nombre.setEnabled(false);
                cuenta.setEnabled(true);
            }
        }

        /* Regresa la lista de estudiantes encontrados. */
        public Lista<Estudiante> getResultados() {
            return resultados;
        }
    }

    /**
     * Constructor único.
     */
    public AdministradorEstudiantes() {
        escuchaTabla = new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getColumn() == TableModelEvent.ALL_COLUMNS)
                        return;
                    baseDeDatosModificada("Base de datos modificada.");
                }
            };

        bdd = new BaseDeDatosEstudiantes();
        bdd.addTableModelListener(escuchaTabla);

	ventana = new JFrame("Administrador de Estudiantes");
	ventana.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    quitaPrograma();
                }
            });
        
        ventana.setIconImage(getImageIcon("icons/ciencias.png").getImage());
        defineNombreDeAplicacion("Administrador de Estudiantes");

	JPanel panel = new JPanel(new BorderLayout());
	JMenuBar bm = creaBarraMenu();
	JToolBar bh = creaBarraHerramientas();
        activaEliminar(false);
        activaGuardar(false);
        tabla = new JTable();
        defineSeleccion();
        inicializaModeloTabla();
	JScrollPane rollo = new JScrollPane(tabla);
	rollo.setPreferredSize(new Dimension(600, 400));
	panel.add(rollo, BorderLayout.CENTER);
        ventana.setJMenuBar(bm);
        ventana.add(bh, BorderLayout.NORTH);
	ventana.add(panel);
        estado = new JLabel("Administrador de Estudiantes");
        ventana.add(estado, BorderLayout.SOUTH);
	ventana.pack();
	ventana.setVisible(true);
	tabla.requestFocus();
    }

    /* Define el nombre de la aplicacion. */
    private void defineNombreDeAplicacion(String nombre) {
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            java.lang.reflect.Field f = tk.getClass().getDeclaredField("awtAppClassName");
            f.setAccessible(true);
            f.set(tk, nombre);
        } catch (NoSuchFieldException nsfe) {
            /* Nada que hacer. */
        } catch (IllegalAccessException iae) {
            /* Lo mismo. */
        }
    }

    /* Obtiene un ImageIcon de los recursos de la aplicación. */
    private ImageIcon getImageIcon(String recurso) {
        ClassLoader cl = getClass().getClassLoader();
        URL url = cl.getResource(recurso);
        return new ImageIcon(url);
    }

    /* Crea la barra de herramientas. */
    private JToolBar creaBarraHerramientas() {
        JToolBar bh = new JToolBar("Barra de Herramientas");

        JButton nueva = new JButton(getImageIcon("icons/nuevo.png"));
        nueva.setToolTipText("Crea una base de datos nueva");
        nueva.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    nuevaBaseDeDatos();
		}
	    });
        botonGuardar = new JButton(getImageIcon("icons/guardar.png"));
        botonGuardar.setToolTipText("Guarda la base de " +
                                    "datos al disco duro");
        botonGuardar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    guardaBaseDeDatos();
		}
	    });
        JButton guardarComo = new JButton(getImageIcon("icons/guardar-como.png"));
        guardarComo.setToolTipText("Guarda la base de " +
                                   "datos al disco duro en un nuevo archivo");
        guardarComo.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    guardaBaseDeDatosComo();
		}
	    });
        JButton cargar = new JButton(getImageIcon("icons/cargar.png"));
        cargar.setToolTipText("Carga la base de datos " +
                              "del disco duro");
        cargar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    cargaBaseDeDatos();
		}
	    });
	JButton agregar = new JButton(getImageIcon("icons/agregar.png"));
        agregar.setToolTipText("Agrega un estudiante a " +
                               "la base de datos");
	agregar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    agregaEstudiante();
		}
	    });
	botonEliminar = new JButton(getImageIcon("icons/eliminar.png"));
	botonEliminar.setToolTipText("Elimina el o los " +
                                     "estudiantes seleccionados");
	botonEliminar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    eliminaEstudiantes();
		}
	    });
	JButton buscar = new JButton(getImageIcon("icons/buscar.png"));
        buscar.setToolTipText("Busca estudiantes en " +
                              "la base de datos");
	buscar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    buscaEstudiantes();
		}
	    });
        bh.add(nueva);
        bh.add(botonGuardar);
        bh.add(guardarComo);
        bh.add(cargar);
        bh.addSeparator();
	bh.add(agregar);
	bh.add(botonEliminar);
        bh.add(buscar);
	return bh;
    }

    /* Crea la barra de menú. */
    private JMenuBar creaBarraMenu() {
	JMenuBar bm = new JMenuBar();
	JMenu menu = new JMenu("Archivo");
	menu.setMnemonic(KeyEvent.VK_A);
	bm.add(menu);
        JMenuItem el = new JMenuItem("Nueva Base de Datos",
                                     KeyEvent.VK_N);
	el.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_N,
                                   ActionEvent.CTRL_MASK));
	el.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    nuevaBaseDeDatos();
		}
	    });
	menu.add(el);
        elemGuardar = new JMenuItem("Guardar Base de Datos",
                                    KeyEvent.VK_S);
	elemGuardar.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                   ActionEvent.CTRL_MASK));
        elemGuardar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    guardaBaseDeDatos();
		}
	    });
	menu.add(elemGuardar);
        el = new JMenuItem("Guardar Base de Datos como...",
                           KeyEvent.VK_T);
        el.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    guardaBaseDeDatosComo();
		}
	    });
	menu.add(el);
        el = new JMenuItem("Cargar Base de Datos",
                           KeyEvent.VK_O);
	el.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_O,
                                   ActionEvent.CTRL_MASK));
	el.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    cargaBaseDeDatos();
		}
	    });
	menu.add(el);
	menu.addSeparator();
	el = new JMenuItem("Quitar programa", KeyEvent.VK_Q);
	el.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                                   ActionEvent.CTRL_MASK));
	el.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    quitaPrograma();
		}
	    });
	menu.add(el);

        menu = new JMenu("Estudiante");
        menu.setMnemonic(KeyEvent.VK_E);
	bm.add(menu);
        el = new JMenuItem("Agregar Estudiante", KeyEvent.VK_E);
	el.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_E,
                                   ActionEvent.CTRL_MASK));
	el.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    agregaEstudiante();
		}
	    });
	menu.add(el);
        elemEliminar = new JMenuItem("Eliminar Estudiantes(s)",
                                     KeyEvent.VK_D);
	elemEliminar.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_D,
                                   ActionEvent.CTRL_MASK));
	elemEliminar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    eliminaEstudiantes();
		}
	    });
	menu.add(elemEliminar);
        el = new JMenuItem("Buscar Estudiantes", KeyEvent.VK_B);
	el.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_B,
                                   ActionEvent.CTRL_MASK));
	el.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    buscaEstudiantes();
		}
	    });
	menu.add(el);

	menu = new JMenu("Ayuda");
	menu.setMnemonic(KeyEvent.VK_Y);
	bm.add(menu);
	el = new JMenuItem("Acerca de...", KeyEvent.VK_D);
	el.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    acercaDe();
		}
	    });
	menu.add(el);

	return bm;
    }

    /* Define el comportamiento de la selección de renglones en la
     * tabla. */
    private void defineSeleccion() {
	tabla.setSelectionMode(
            ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	tabla.getSelectionModel().addListSelectionListener(
	    new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
		    if (e.getValueIsAdjusting())
			return;
		    if (tabla.getSelectedRows().length == 0)
                        activaEliminar(false);
                    else
                        activaEliminar(true);
		}
	    });
    }

    /* Inicializa el modelo de la tabla. */
    private void inicializaModeloTabla() {
        tabla.setModel(bdd);

        tabla.setAutoCreateRowSorter(true);
        // Ordena por nombre
	tabla.getRowSorter().toggleSortOrder(0);

        // Nombre
        TableColumn tc = tabla.getColumnModel().getColumn(0);
        DefaultTableCellRenderer cr =
            new DefaultTableCellRenderer();
        tc.setCellRenderer(cr);
        tc.setPreferredWidth(150);

        // Matrícula
        tc = tabla.getColumnModel().getColumn(1);
        cr = new DibujanteEnteros(9);
        tc.setCellRenderer(cr);
        tc.setPreferredWidth(50);

        // Promedio
        tc = tabla.getColumnModel().getColumn(2);
        cr = new DibujantePromedio();
        tc.setCellRenderer(cr);
        tc.setPreferredWidth(5);

        // Edad
        tc = tabla.getColumnModel().getColumn(3);
        cr = new DibujanteEnteros(0);
        tc.setCellRenderer(cr);
        tc.setPreferredWidth(5);
    }

    /* Activa las opciones de eliminar. */
    private void activaEliminar(boolean eliminar) {
	botonEliminar.setEnabled(eliminar);
	elemEliminar.setEnabled(eliminar);
    }

    /* Activa las opciones de guardar. */
    private void activaGuardar(boolean guardar) {
	botonGuardar.setEnabled(guardar);
	elemGuardar.setEnabled(guardar);
    }

    /* Crea una nueva base de datos. */
    private void nuevaBaseDeDatos() {
        if (modificado) {
            // Aquí va su código.
            
            DialogoDeDesicion d =
            new DialogoDeDesicion(ventana, "Desea guardar su base anterior?", "Nueva Base de Datos.", "Si", "No");
            d.setVisible(true);
            boolean r = d.getRespuesta();
            d.dispose();
            
            if(r)
                guardaBaseDeDatos();
            
            
        }
        bdd = new BaseDeDatosEstudiantes();
        bdd.addTableModelListener(escuchaTabla);
        inicializaModeloTabla();
        archivo = null;
        baseDeDatosSinModificar("Administrador de Estudiantes");
    }

    /* Guarda la base de datos. */
    private boolean guardaBaseDeDatos() {
        if (archivo == null) {
            if (!guardaBaseDeDatosComo()) {
                archivo = null;
                return false;
            }
            return true;
        }
        return guardaBDDenDisco();
    }

    /* Guarda nueva base de datos con un nombre nuevo. */
    private boolean guardaBaseDeDatosComo() {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filtro =
            new FileNameExtensionFilter("Bases de datos", "dat");
        fc.setFileFilter(filtro);
        int r = fc.showSaveDialog(ventana);
        if(r != JFileChooser.APPROVE_OPTION)
            return false;
        File nuevoArchivo = fc.getSelectedFile();
        if (nuevoArchivo.exists()) {
            // Aquí va su código.
            
            DialogoDeDesicion d =
            new DialogoDeDesicion(ventana, "Desea reeseciribr el archivo seleccionado?", "Guardar Base de Datos.", "Si", "No");
            d.setVisible(true);
            boolean rrr = d.getRespuesta();
            d.dispose();
            
            if(!rrr)
                return false;
           
        }
        archivo = nuevoArchivo;
        return guardaBDDenDisco();
    }

    /* Guarda la base de datos en el disco. */
    private boolean guardaBDDenDisco() {
        try {
            FileOutputStream fOut = new FileOutputStream(archivo);
            OutputStreamWriter wOut = new OutputStreamWriter(fOut);
            BufferedWriter out = new BufferedWriter(wOut);
            bdd.guarda(out);
            out.close();
        } catch (IOException ioe) {
            DialogoAviso d =
            new DialogoAviso(ventana, "Viejo, la mala suerte la traes hoy, se te borro todo hahaha", "Informacion", "Ok.");
            d.setVisible(true);
            d.dispose();
        }
        baseDeDatosSinModificar("Base de datos \"" +
                                archivo.getName()  +
                                "\" guardada.");
        return true;
    }

    /* Carga la base de datos. */
    private void cargaBaseDeDatos() {
        if (modificado) {
            // Aquí va su código.
            
            DialogoDeDesicion d =
            new DialogoDeDesicion(ventana, "Desea guardar su base anterior?", "Cargando Base de Datos.", "Si", "No");
            d.setVisible(true);
            boolean r = d.getRespuesta();
            d.dispose();
            
            if(r)
                guardaBaseDeDatos();
            
        }
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filtro =
            new FileNameExtensionFilter("Bases de datos", "dat","bdd");
        fc.setFileFilter(filtro);
        int r = fc.showOpenDialog(ventana);
        if (r != 0)
            return;
        File na = fc.getSelectedFile();
        try {
            FileInputStream fileIn = new FileInputStream(na);
            InputStreamReader isIn = new InputStreamReader(fileIn);
            BufferedReader in = new BufferedReader(isIn);
            BaseDeDatosEstudiantes nBD = new BaseDeDatosEstudiantes();
            nBD.carga(in);
            in.close();
            bdd = nBD;
        } catch (IOException ioe) {
            DialogoAviso d =
            new DialogoAviso(ventana, "Esto se fue a la *$%# y se borro todo HAHAHA", "Por cierto", "Ok.");
            d.setVisible(true);
            d.dispose();
        }
        bdd.addTableModelListener(escuchaTabla);
        inicializaModeloTabla();
        archivo = na;
        baseDeDatosSinModificar("Base de datos \"" +
                                archivo.getName()  +
                                "\" cargada.");
    }

    /* Quita el programa. */
    private void quitaPrograma() {
        if (true) {
            
            DialogoDeDesicion d =
            new DialogoDeDesicion(ventana, "Desea guardar su base de datos antes de salir?", "Quitar Programa.", "Si", "No");
            d.setVisible(true);
            boolean r = d.getRespuesta();
            d.dispose();
            
            if(r)
            guardaBaseDeDatos();
        
        }
        System.exit(0);
    }

    /* Agrega un estudiante a la base de datos. */
    private void agregaEstudiante() {
        DialogoNuevoEstudiante d =
            new DialogoNuevoEstudiante(ventana);
        d.setVisible(true);
        Estudiante e = d.getEstudiante();
        d.dispose();
        if (e == null)
            return;
        bdd.agregaRegistro(e);
        baseDeDatosModificada("Estudiante " + e.getNombre() +
                              " agregado.");
    }

    /* Elimina estudiantes de la base de datos. */
    private void eliminaEstudiantes() {
        int[] seleccion = tabla.getSelectedRows();
        String msj = "Se eliminarán " + seleccion.length
            + " estudiantes.";
        if (seleccion.length == 1)
            msj = "Se eliminará 1 estudiante.";
        
        DialogoDeDesicion d =
        new DialogoDeDesicion(ventana, "Va a eliminar cabrones, seguro?", "Borrando cabrones", "Si", "No");
        d.setVisible(true);
        boolean r = d.getRespuesta();
        d.dispose();
        
        if(!r)
            return;
          
        Integer[] indices = new Integer[seleccion.length];
	for (int i = 0; i < indices.length; i++)
	    indices[i] = tabla.convertRowIndexToModel(seleccion[i]);
        bdd.eliminaRegistros(indices);
        if (indices.length == 1)
            baseDeDatosModificada("1 estudiante eliminado.");
        else
            baseDeDatosModificada(indices.length +
                                  " estudiantes eliminados.");
    }

    /* Busca estudiantes en la base de datos. */
    private void buscaEstudiantes() {
        DialogoBuscaEstudiantes d =
            new DialogoBuscaEstudiantes(ventana, bdd);
        d.setVisible(true);
        Lista<Estudiante> l = d.getResultados();
        d.dispose();
        if (l.getLongitud() == 0)
            return;
        Integer[] indices = bdd.indicesDe(l);
        ListSelectionModel lsm = tabla.getSelectionModel();
        lsm.clearSelection();
        for (int i : indices) {
            int j = tabla.convertRowIndexToView(i);
            lsm.addSelectionInterval(j, j);
        }
    }

    /* Muestra un diálogo con información del programa. */
    private void acercaDe() {
        
        DialogoAviso d =
        new DialogoAviso(ventana, "Alfonso Casanova iOS Developer.", "Informacion", "Ok.");
        d.setVisible(true);
        d.dispose();
        
    }

    /* Define el estado como modificado. */
    private void baseDeDatosModificada(String mensaje) {
        if (archivo == null)
            ventana.setTitle("Administrador de Estudiantes*");
        else
            ventana.setTitle("Administrador de Estudiantes " +
                             archivo.getName() + "*");
        modificado = true;
        estado.setText(mensaje);
        activaGuardar(true);
    }

    /* Define el estado como no modificado. */
    private void baseDeDatosSinModificar(String mensaje) {
        if (archivo == null)
            ventana.setTitle("Administrador de Estudiantes");
        else
            ventana.setTitle("Administrador de Estudiantes: " +
                             archivo.getName());
        modificado = false;
        estado.setText(mensaje);
        activaGuardar(false);
    }
}
