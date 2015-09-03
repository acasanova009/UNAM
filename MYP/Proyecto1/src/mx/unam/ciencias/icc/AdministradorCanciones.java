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
import java.net.Socket;
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
import javax.swing.text.Document;

/**
 * Clase para la interfaz gráfica de un administrador de
 * canciones.
 */
public class AdministradorCanciones {

    /* Opciones de la interfaz gráfica. */
    private static final int CONECTAR    = 0;
    private static final int DESCONECTAR = 1;
    private static final int AGREGAR     = 2;
    private static final int ELIMINAR    = 3;
    private static final int BUSCAR      = 4;
    private static final int OPCIONES    = 5;

    /* Componentes de la interfaz gráfica. */
    private JFrame ventana;
    private JTable tabla;
    private JLabel estado;
    private JButton[] botones;
    private JMenuItem[] elementos;

    /* La base de datos/modelo. */
    private BaseDeDatosCanciones bdd;

    /* El hilo para manejar la conexión. */
    private HiloCliente<Cancion> hilo;

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
     * de una cancion. */
    private class DialogoNuevaCancion extends JDialog
    {
        private JTextField nombre;
        private JTextField artist;
        private JSpinner duration;
        private JSpinner rating;
        private JSpinner plays;
        
        private JButton aceptar;
        private JButton cancelar;
        
        private Cancion cancion;
        
        public DialogoNuevaCancion(JFrame ventana)
        {
            super(ventana, "Nueva Cancion", true);
            JPanel panel = new JPanel();
            GridBagLayout gb = new GridBagLayout();
            panel.setLayout(gb);
            panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            
            JLabel etiqueta = new JLabel("Name: ");
            agregaComponente(panel, etiqueta, gb, 0, 0);
            nombre = new JTextField(20);
            agregaComponente(panel, nombre, gb, 1, 0);
            
            etiqueta = new JLabel("Artist: ");
            agregaComponente(panel, etiqueta, gb, 0, 1);
            artist = new JTextField(20);
            agregaComponente(panel, artist, gb, 1, 1);
            
            
            etiqueta = new JLabel("Duration: ");
            agregaComponente(panel, etiqueta, gb, 0, 2);
            duration = creaRotativo(180, 0, 1500, 1, "#0");
            agregaComponente(panel, duration, gb, 1, 2);
            
            
            etiqueta = new JLabel("Rating: ");
            agregaComponente(panel, etiqueta, gb, 0, 3);
            rating = creaRotativo(0, 0,5, 1, "#0");
            agregaComponente(panel, rating, gb, 1, 3);
            
            
            etiqueta = new JLabel("Plays: ");
            agregaComponente(panel, etiqueta, gb, 0, 4);
            plays = creaRotativo(0, 0, 1500, 1, "#0");
            agregaComponente(panel, plays, gb, 1, 4);
            
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
                    
                    int p = (int)((Double)plays.getValue()).doubleValue();
                    double d = (Double)duration.getValue();
                    int r = (int)((Double)rating.getValue()).doubleValue();
                    cancion = new Cancion(nombre.getText(),
                                                artist.getText(),
                                                d,
                                                new Rating(r),
                                                p);
                    setVisible(false);
                }
            });
            
            cancelar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evento) {
                    cancion = null;
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
        public void compruebaNombre() {
            Lista<Cancion> ccs = bdd.getRegistros();
            
            boolean canAddSong = true;
            
            for (Cancion c : ccs)
            {
                if(c.getName().equals(nombre.getText()))
                {
                    canAddSong = false;
                }
            }
            aceptar.setEnabled(canAddSong);
        }
        
        /* Regresa el nuevo cancion. */
        public Cancion getCancion() {
            return cancion;
        }
    }

    /* Clase para mostrar un diálogo dónde escribir el nomre o la
     * cuenta para buscar un cancion. */
    private class DialogoBuscaCanciones extends JDialog {

        private JTextField nombre;
        private JSpinner rating;
        
        private JRadioButton botonNombre;
        private JRadioButton botonRating;
        
        private JButton buscar;
        private JButton cancelar;
        private Lista<Cancion> resultados;

        private BaseDeDatosCanciones bdd;

        /* Construye un nuevo diálogo con la ventana que recibe como
         * padre, y con una base de datos. */
        public DialogoBuscaCanciones(JFrame ventana,
                                       BaseDeDatosCanciones b) {
            super(ventana, "Buscar Canciones", true);

            this.bdd = b;

            JPanel panel = new JPanel();
            GridBagLayout gb = new GridBagLayout();
            panel.setLayout(gb);
            panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            botonNombre = new JRadioButton("Buscar por nombre:");
            botonNombre.setSelected(true);
            agregaComponente(panel, botonNombre, gb, 0, 0);
            nombre = new JTextField(20);
            agregaComponente(panel, nombre, gb, 1, 0);

            botonRating =
                new JRadioButton("Search by rating");
            agregaComponente(panel, botonRating, gb, 0, 1);
            rating = creaRotativo(0, 0, 5, 1, "0");
            agregaComponente(panel, rating, gb, 1, 1);

            ButtonGroup grupo = new ButtonGroup();
            grupo.add(botonNombre);
            grupo.add(botonRating);

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

            botonRating.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        compruebaCampos();
                    }
                });

            buscar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        resultados = new Lista<Cancion>();
                        int columna = -1;
                        String cadena = "";
                        if (botonNombre.isSelected()) {
                            columna = BaseDeDatosCanciones.NAME;
                            cadena = nombre.getText();
                        } else {
                            columna = BaseDeDatosCanciones.RATING;
                            int mr = (int)((Double)rating.getValue()).doubleValue();
                            cadena = String.valueOf(mr);
                        }
                        resultados = bdd.buscaRegistros(columna, cadena);
                        setVisible(false);
                    }
                });

            cancelar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        resultados = new Lista<Cancion>();
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
                rating.setEnabled(false);
            } else {
                buscar.setEnabled(true);
                nombre.setEnabled(false);
                rating.setEnabled(true);
            }
        }

        /* Regresa la lista de cancciones encontrados. */
        public Lista<Cancion> getResultados() {
            return resultados;
        }
    }

    /* Clase para un diálogo de conexiones. */
    private class DialogoConecta extends JDialog {

        private JTextField entradaServidor;
        private JSpinner rotativoPuerto;
        private String servidor;
        private int puerto;
        private JButton conectar;
        private JButton cancelar;

        public DialogoConecta(JFrame ventana) {
            super(ventana, "Conectar a base de datos", true);

            JPanel panel = new JPanel();
            GridBagLayout gb = new GridBagLayout();
            panel.setLayout(gb);
            panel.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 10, 0));
            JLabel etiqueta = new JLabel("Servidor: ");
            agregaComponente(panel, etiqueta, gb, 0, 0);
            entradaServidor = new JTextField(20);
            agregaComponente(panel, entradaServidor, gb, 1, 0);
            etiqueta = new JLabel("Puerto: ");
            agregaComponente(panel, etiqueta, gb, 0, 1);
            rotativoPuerto = creaRotativo(1234, 1024, 65535, 1, "#");
            agregaComponente(panel, rotativoPuerto, gb, 1, 1);

            JPanel hPanel = new JPanel();
            GridLayout gl = new GridLayout(1, 2);
            hPanel.setLayout(gl);
            hPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0));
            gl.setHgap(10);
            conectar  = new JButton("Conectar");
            hPanel.add(conectar);
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

            entradaServidor.setText("localhost");
            entradaServidor.setRequestFocusEnabled(true);
            entradaServidor.requestFocus();
            setLocationRelativeTo(ventana);

            conectar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        servidor = entradaServidor.getText();
                        Object o = rotativoPuerto.getValue();
                        puerto = (int)((Double)o).doubleValue();
                        setVisible(false);
                    }
                });

            cancelar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evento) {
                        servidor = null;
                        puerto = -1;
                        setVisible(false);
                    }
                });

            compruebaServidor();
            Document doc = entradaServidor.getDocument();
            doc.addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        compruebaServidor();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        compruebaServidor();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        compruebaServidor();
                    }
                });
        }

        /* Comprueba que el campo de servidor sea válido. */
        public void compruebaServidor() {
            String sv = entradaServidor.getText();
            if (sv.length() > 0)
                conectar.setEnabled(true);
            else
                conectar.setEnabled(false);
        }

        /* Regresa el servidor. */
        public String getServidor() {
            return servidor;
        }

        /* Regresa el puerto. */
        public int getPuerto() {
            return puerto;
        }
    }

    /**
     * Constructor único.
     */
    public AdministradorCanciones() {
        bdd = new BaseDeDatosCanciones();
        bdd.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    String mensaje = "";
                    switch (e.getType()) {
                    case TableModelEvent.INSERT:
                        mensaje = "Cancion agregado.";
                        break;
                    case TableModelEvent.UPDATE:
                        mensaje = "Cancion modificado.";
                        break;
                    case TableModelEvent.DELETE:
                        mensaje = "Cancion eliminado.";
                        break;
                    default:
                        return;
                    }
                    estado.setText(mensaje);
                }
            });

	ventana = new JFrame("Music Administrator");
	ventana.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    quitaPrograma();
                }
            });
        ventana.setIconImage(getImageIcon("icons/ciencias.png").getImage());
        defineNombreDeAplicacion("Music Administrator");

	JPanel panel = new JPanel(new BorderLayout());
	JMenuBar bm = creaBarraMenu();
	JToolBar bh = creaBarraHerramientas();
        estadoOpcion(CONECTAR, true);
        estadoOpcion(DESCONECTAR, false);
        estadoOpcion(AGREGAR, false);
        estadoOpcion(ELIMINAR, false);
        estadoOpcion(BUSCAR, false);
        tabla = new JTable();
        defineSeleccion();
        inicializaModeloTabla();
	JScrollPane rollo = new JScrollPane(tabla);
	rollo.setPreferredSize(new Dimension(600, 400));
	panel.add(rollo, BorderLayout.CENTER);
        ventana.setJMenuBar(bm);
        ventana.add(bh, BorderLayout.NORTH);
	ventana.add(panel);
        estado = new JLabel("Music Administrator");
        ventana.add(estado, BorderLayout.SOUTH);
	ventana.pack();
	ventana.setVisible(true);
	tabla.requestFocus();
    }

    /* Define el nombre de la aplicacion. */
    private void defineNombreDeAplicacion(String nombre) {
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            java.lang.reflect.Field f;
            f = tk.getClass().getDeclaredField("awtAppClassName");
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

        botones = new JButton[OPCIONES];

        botones[CONECTAR] = new JButton(getImageIcon("icons/conectar.png"));
        botones[CONECTAR].setToolTipText(
            "Se conecta a una base de datos remota");
        botones[CONECTAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    conectaBaseDeDatos();
		}
	    });
        bh.add(botones[CONECTAR]);
        botones[DESCONECTAR] =
            new JButton(getImageIcon("icons/desconectar.png"));
        botones[DESCONECTAR].setToolTipText(
            "Desconecta la base de datos remota");
        botones[DESCONECTAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    desconectaBaseDeDatos();
		}
	    });
        bh.add(botones[DESCONECTAR]);
        bh.addSeparator();
	botones[AGREGAR] = new JButton(getImageIcon("icons/agregar.png"));
        botones[AGREGAR].setToolTipText("Agrega un cancion a " +
                               "la base de datos");
	botones[AGREGAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    agregaCancion();
		}
	    });
	bh.add(botones[AGREGAR]);
	botones[ELIMINAR] = new JButton(getImageIcon("icons/eliminar.png"));
	botones[ELIMINAR].setToolTipText("Elimina el o los " +
                                     "canciones seleccionados");
	botones[ELIMINAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    eliminaCanciones();
		}
	    });
	bh.add(botones[ELIMINAR]);
	botones[BUSCAR] = new JButton(getImageIcon("icons/buscar.png"));
        botones[BUSCAR].setToolTipText("Busca canciones en " +
                              "la base de datos");
	botones[BUSCAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    buscaCanciones();
		}
	    });
        bh.add(botones[BUSCAR]);
	return bh;
    }

    /* Crea la barra de menú. */
    private JMenuBar creaBarraMenu() {
	JMenuBar bm = new JMenuBar();
	JMenu menu = new JMenu("Administrador");
	menu.setMnemonic(KeyEvent.VK_R);
	bm.add(menu);

        elementos = new JMenuItem[OPCIONES];

        elementos[CONECTAR] = new JMenuItem("Conectar a Base de Datos",
                                            KeyEvent.VK_C);
	elementos[CONECTAR].setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_C,
                                   ActionEvent.CTRL_MASK));
	elementos[CONECTAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    conectaBaseDeDatos();
		}
	    });
	menu.add(elementos[CONECTAR]);
        elementos[DESCONECTAR] = new JMenuItem("Desconectar Base de Datos",
                                               KeyEvent.VK_D);
	elementos[DESCONECTAR].setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_D,
                                   ActionEvent.CTRL_MASK));
        elementos[DESCONECTAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    desconectaBaseDeDatos();
		}
	    });
	menu.add(elementos[DESCONECTAR]);
	menu.addSeparator();
	JMenuItem quitar = new JMenuItem("Quitar programa", KeyEvent.VK_Q);
	quitar.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                                   ActionEvent.CTRL_MASK));
	quitar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    quitaPrograma();
		}
	    });
	menu.add(quitar);

        menu = new JMenu("Cancion");
        menu.setMnemonic(KeyEvent.VK_E);
	bm.add(menu);
        elementos[AGREGAR] =
            new JMenuItem("Agregar Cancion", KeyEvent.VK_E);
	elementos[AGREGAR].setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_E,
                                   ActionEvent.CTRL_MASK));
	elementos[AGREGAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    agregaCancion();
		}
	    });
	menu.add(elementos[AGREGAR]);
        elementos[ELIMINAR] = new JMenuItem("Eliminar Cancione(s)",
                                     KeyEvent.VK_D);
	elementos[ELIMINAR].setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_D,
                                   ActionEvent.CTRL_MASK));
	elementos[ELIMINAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    eliminaCanciones();
		}
	    });
	menu.add(elementos[ELIMINAR]);
        elementos[BUSCAR] = new JMenuItem("Buscar cancion", KeyEvent.VK_B);
	elementos[BUSCAR].setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_B,
                                   ActionEvent.CTRL_MASK));
	elementos[BUSCAR].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    buscaCanciones();
		}
	    });
	menu.add(elementos[BUSCAR]);

	menu = new JMenu("Ayuda");
	menu.setMnemonic(KeyEvent.VK_Y);
	bm.add(menu);
	JMenuItem acercaDe = new JMenuItem("Acerca de...", KeyEvent.VK_D);
	acercaDe.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    acercaDe();
		}
	    });
	menu.add(acercaDe);

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
                        estadoOpcion(ELIMINAR, false);
                    else
                        estadoOpcion(ELIMINAR, true);
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
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        tc.setCellRenderer(cr);
        tc.setPreferredWidth(150);

        // Artist
        tc = tabla.getColumnModel().getColumn(1);
        cr = new DefaultTableCellRenderer();
        tc.setCellRenderer(cr);
        tc.setPreferredWidth(150);
        
        
        // Duration
        tc = tabla.getColumnModel().getColumn(2);
        cr = new DefaultTableCellRenderer();
        tc.setCellRenderer(cr);
        tc.setPreferredWidth(5);
        
        // Rating
        tc = tabla.getColumnModel().getColumn(3);
        cr = new DefaultTableCellRenderer();
        tc.setCellRenderer(cr);
        tc.setPreferredWidth(50);

        // Edad
        tc = tabla.getColumnModel().getColumn(4);
        cr = new DefaultTableCellRenderer();
        tc.setCellRenderer(cr);
        tc.setPreferredWidth(5);
    }

    /* Activa opciones. */
    private void estadoOpcion(int opcion, boolean estado) {
        botones[opcion].setEnabled(estado);
        elementos[opcion].setEnabled(estado);
    }

    /* Se conecta a una base de datos remota. */
    private void conectaBaseDeDatos() {
        DialogoConecta d = new DialogoConecta(ventana);
        d.setVisible(true);
        String servidor = d.getServidor();
        int puerto = d.getPuerto();
        d.dispose();
        if (servidor == null || puerto == -1)
            return;
        try {
            Socket enchufe = new Socket(servidor, puerto);
            hilo = new HiloCliente<Cancion>(bdd, enchufe);
            hilo.start();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(
                ventana, "Hubo un error al tratar " +
                "de conectarse al servidor\n" + servidor +
                " en el puerto " +  puerto + ".",
                "Error al conectar",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        estadoOpcion(CONECTAR, false);
        estadoOpcion(DESCONECTAR, true);
        estadoOpcion(AGREGAR, true);
        estadoOpcion(ELIMINAR, true);
        estadoOpcion(BUSCAR, true);
        estado.setText("Conexión establecida a " + servidor + ":" + puerto);
    }

    /* Desconecta la base de datos remota. */
    private void desconectaBaseDeDatos() {
        hilo.cierraTodo();
        estadoOpcion(CONECTAR, true);
        estadoOpcion(DESCONECTAR, false);
        estadoOpcion(AGREGAR, false);
        estadoOpcion(ELIMINAR, false);
        estadoOpcion(BUSCAR, false);
        estado.setText("Conexión terminada.");
    }

    /* Quita el programa. */
    private void quitaPrograma() {
        desconectaBaseDeDatos();
        System.exit(0);
    }

    /* Agrega un cancion a la base de datos. */
    private void agregaCancion() {
        DialogoNuevaCancion d =
            new DialogoNuevaCancion(ventana);
        d.setVisible(true);
        Cancion c = d.getCancion();
        d.dispose();
        if (c == null)
            return;
        bdd.agregaRegistro(c);
    }

    /* Elimina cancion de la base de datos. */
    private void eliminaCanciones() {
        int[] seleccion = tabla.getSelectedRows();
        String msj = "Se eliminarán " + seleccion.length
            + " canciones.";
        if (seleccion.length == 1)
            msj = "Se eliminará 1na cancion.";
	int r = JOptionPane.showConfirmDialog(
            ventana,
            msj + "\n¿Está seguro de hacerlo?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);
        if (r != 0)
            return;
        Integer[] indices = new Integer[seleccion.length];
	for (int i = 0; i < indices.length; i++)
	    indices[i] = tabla.convertRowIndexToModel(seleccion[i]);
        bdd.eliminaRegistros(indices);
    }

    /* Busca cancion en la base de datos. */
    private void buscaCanciones() {
        DialogoBuscaCanciones d =
            new DialogoBuscaCanciones(ventana, bdd);
        d.setVisible(true);
        Lista<Cancion> l = d.getResultados();
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
	JOptionPane.showMessageDialog(
            ventana,
            "Administrador de Canciones, v1.0\n" +
            "© 2014 Canek Peláez, y Alfonso Casanova",
            "Acerca de...",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
