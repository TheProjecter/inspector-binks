package be.planetsizebrain.inspectorbinks.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.border.EmptyBorder;

import jsyntaxpane.DefaultSyntaxKit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.sonatype.grrrowl.GrowlFactory;

import be.planetsizebrain.inspectorbinks.InspectorBinks;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.decompiler.ClazzSourceView;
import ru.andrew.jclazz.decompiler.ClazzSourceViewFactory;
import de.schlichtherle.truezip.file.TConfig;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import de.schlichtherle.truezip.file.TFileOutputStream;
import de.schlichtherle.truezip.fs.FsOutputOption;

public class FileListing extends JPanel implements DropTargetListener, DragGestureListener {
	
	private static final ImageIcon NORMAL = new ImageIcon(ApplicationToolbar.class.getResource("/airdrop-os-x-lion-icon-normal.png"));
	private static final ImageIcon LARGE = new ImageIcon(ApplicationToolbar.class.getResource("/airdrop-os-x-lion-icon.png"));
	
	private JLabel target;
	
	private TFile archive;
	private TFile currentRoot;
	private FileListingTable table;
	private JScrollPane scrollPane;
	
	private final JFrame parent;
	
	TransferHandler th = new TransferHandler(){

		@Override
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			return false;
		}

		@Override
		public boolean canImport(TransferSupport support) {
			return false;
		}
		
		@Override
		public int getSourceActions(JComponent c) {
			return COPY_OR_MOVE; 
		}

		@Override
		protected Transferable createTransferable(JComponent c) {
			final JTable t = (JTable) c;
			
			return new Transferable() {
				
				public boolean isDataFlavorSupported(DataFlavor flavor) {
					return DataFlavor.javaFileListFlavor.equals(flavor);
				}
				
				public DataFlavor[] getTransferDataFlavors() {
					return new DataFlavor[]{DataFlavor.javaFileListFlavor};
				}
				
				public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
					System.out.println("Prepare transfer: " + flavor.getHumanPresentableName());
					
					TFile[] listedFiles = currentRoot.listFiles();
					List<File> files = new ArrayList<File>(t.getSelectedRowCount());
					for (int index : t.getSelectedRows()) {
						int i = index - ((currentRoot == archive) ? 0 : 1);
						if (i > -1) {
							TFile file = listedFiles[i];
							System.out.println("CHOSEN: " + file.getName());
							
							if (file.isFile()) {
								String filename = file.getName();
								String tempDir = FileUtils.getTempDirectoryPath();
								File temp = new File(tempDir + "/" + FilenameUtils.getBaseName(filename) + "." + FilenameUtils.getExtension(filename));
								System.out.println("TEMP: " + temp.getAbsolutePath());
								temp.deleteOnExit();
								file.cp(temp);
								files.add(temp);
							} else {
								// TODO copy whole directory?
								System.out.println("Discard directory");
							}
						} else {
							System.out.println("Discard invalid index");
						}
					}
					return files;
				}
			};
		}

		@Override
		public void exportAsDrag(JComponent comp, InputEvent e, int action) {
			// TODO Auto-generated method stub
			super.exportAsDrag(comp, e, action);
		}
    	
    };
	
	public FileListing(final JFrame parent) {
		super(new BorderLayout());
		
		this.setDropTarget(new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true));
		
		target = new JLabel(NORMAL);
		this.add(target, BorderLayout.CENTER);
		
		this.parent = parent;
		
//		setLayout(new GridBagLayout());
//
//        GridBagConstraints c = new GridBagConstraints();
//        c.gridx = 0;
//        c.gridy = 0;
//        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 1;
//        c.weighty = 1;
        
        table = new FileListingTable(new FileListingTableModel());
        
        table.setDragEnabled(true);
        table.setTransferHandler(th);
        
//        DragSource ds = DragSource.getDefaultDragSource();
//        DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(table, DnDConstants.ACTION_COPY, this);
        
         scrollPane = new JScrollPane(table);
//        // TODO only do this on OSX specific
////        add(IAppWidgetFactory.makeIAppScrollPane(scrollPane), c);
//        add(scrollPane, c);
         scrollPane.setBorder(new EmptyBorder(0,0,0,0));
         scrollPane.setViewportView(table);
//        
        table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
		        if (e.getComponent().isEnabled() && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && table.getModel().getRowCount() > 0) {
		            Point p = e.getPoint();
		            int row = table.rowAtPoint(p); 
		            int realRow = table.convertRowIndexToModel(row);
		            
		            boolean refresh = true;
		            if (currentRoot != archive) {
		            	if (realRow == 0) {
		            		currentRoot = currentRoot.getParentFile();
		            	} else if (currentRoot.listFiles()[realRow - ((currentRoot == archive) ? 0 : 1)].isDirectory()) {
		            		currentRoot = currentRoot.listFiles()[realRow - ((currentRoot == archive) ? 0 : 1)];
		            	} else {
		            		// open viewer?
		            		refresh = false;
		            	}
		            } else {
		            	currentRoot = currentRoot.listFiles()[realRow - ((currentRoot == archive) ? 0 : 1)];
		            }
		            System.out.println("Current: " + currentRoot.getAbsolutePath());
		            
		            if (refresh) {
			            FileListingTableModel model = new FileListingTableModel(currentRoot, currentRoot == archive);
			    		table.setModel(model);
		            }
		        }
		    }
		});
        table.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_SPACE == e.getKeyChar()) {
					TFile selectedFile = currentRoot.listFiles()[table.getSelectedRow() - ((currentRoot == archive) ? 0 : 1)];
					final JDialog popup = new JDialog(parent, true);
					DefaultSyntaxKit.initKit();

			        final JEditorPane contents = new JEditorPane();
			        contents.setBackground(Color.WHITE);
			        JScrollPane scrPane = new JScrollPane(contents);
			        parent.getContentPane().add(scrPane, BorderLayout.CENTER);
			        parent.getContentPane().doLayout();
			        popup.getContentPane().setLayout(new BorderLayout());
			        
			        String mimeType = "text/plain";
			        Tika tika = new Tika();
					try {
						DefaultSyntaxKit.registerContentType("application/java-vm", "jsyntaxpane.syntaxkits.JavaSyntaxKit");
						mimeType = tika.detect(selectedFile);
						contents.setContentType(mimeType);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						contents.setContentType("text/plain");
					}
					
					contents.setEditable(false);
					
					if ("application/java-vm".equals(mimeType)) {
						try {
							Clazz clazz = new Clazz(selectedFile, true);
					        ClazzSourceView csv = ClazzSourceViewFactory.getClazzSourceView(clazz);
					        csv.setDecompileParameters(new HashMap());
					        
					        String java = csv.getSource();
					        
					        contents.setText(java);
					        
					        popup.getContentPane().add(scrPane, BorderLayout.CENTER);
						} catch (FileNotFoundException fnfe) {
							// TODO Auto-generated catch block
							fnfe.printStackTrace();
						} catch (ClazzException ce) {
							// TODO Auto-generated catch block
							ce.printStackTrace();
						} catch (IOException ioe) {
							// TODO Auto-generated catch block
							ioe.printStackTrace();
						} 
					} else {
						HtmlPanel panel = new HtmlPanel();
						UserAgentContext agentContext = new SimpleUserAgentContext();
						HtmlRendererContext context = new SimpleHtmlRendererContext(panel, agentContext);
						try {
							panel.setHtml(IOUtils.toString(new TFileInputStream(selectedFile)), selectedFile.toURI().toString(), context);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						popup.getContentPane().add(panel, BorderLayout.CENTER);
					}
					
//					popup.getContentPane().setLayout(new BorderLayout());
					
					popup.setSize(400, 400);
					popup.setVisible(true);
				}
        	}
		});
	}
	
	public void loadArchive(File archive) {
		this.archive = new TFile(archive);
		this.currentRoot = this.archive;
		
		FileListingTableModel model = new FileListingTableModel(this.archive, true);
		table.setModel(model);
		
		InspectorBinks.growl("Opened archive " + archive.getName());
	}
	
	public void addToArchive(List<File> files) {
		File file = files.get(0);
		// We are going to append "entry" to "archive.zip".
		TFile tf = new TFile(currentRoot.getAbsolutePath() + "/" + file.getName());
		
		// First, push a new current configuration on the inheritable thread local
		// stack.
		TConfig config = TConfig.push();
		try {
		    // Set FsOutputOption.GROW for appending-to rather than reassembling an
		    // archive file.
		    config.setOutputPreferences(config.getOutputPreferences().set(FsOutputOption.GROW));

		    // Now use the current configuration and append the entry to the archive
		    // file even if it's already present.
		    TFileOutputStream out = new TFileOutputStream(tf);
		    try {
		    	IOUtils.copy(new FileInputStream(file), out);
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
		        try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    // Pop the current configuration off the inheritable thread local stack.
		    config.close();
		}

	}
	
	public void close() {
		this.archive = this.currentRoot = null;
		table = new FileListingTable(new FileListingTableModel());
	}
	
	public boolean isOpen() {
		return this.archive != null;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		target.setIcon(LARGE);
		System.out.println("ENTER");
	}

	public void dragExit(DropTargetEvent dte) {
		target.setIcon(NORMAL);
		System.out.println("EXIT");
	}

	public void drop(DropTargetDropEvent dtde) {
		target.setIcon(NORMAL);
		System.out.println("DROP");
		
		
		
		if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			this.remove(target);
			
			dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			Transferable t = dtde.getTransferable();

            try {
                List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

                if (isOpen()){
	                addToArchive(files);
                } else {
                	if (files.size() == 1) {
	                	loadArchive(files.get(0));
	                } else {
	                	System.out.println("Only supports dropping 1 file");
	                }
                }
            } catch (UnsupportedFlavorException e) {
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            }

            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 1;
			this.add(scrollPane, c);
		} else {
			// unsupported drop
		}
		
	}

	public void dragOver(DropTargetDragEvent dtde) {}
	public void dropActionChanged(DropTargetDragEvent dtde) {}

	public void dragGestureRecognized(DragGestureEvent dge) {
		th.exportAsDrag(table, null, TransferHandler.COPY); 
//		try {
//			dge.startDrag(null, new Transferable() {
//				
//				@Override
//				public boolean isDataFlavorSupported(DataFlavor flavor) {
//					return DataFlavor.javaFileListFlavor.equals(flavor);
//				}
//				
//				@Override
//				public DataFlavor[] getTransferDataFlavors() {
//					return new DataFlavor[]{DataFlavor.javaFileListFlavor};
//				}
//				
//				@Override
//				public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
//					System.out.println("Drag Gesture Recognized: " + flavor.getHumanPresentableName());
//					
//					
//					TFile[] listedFiles = currentRoot.listFiles();
//					List<File> files = new ArrayList<File>(table.getSelectedRowCount());
//					for (int index : table.getSelectedRows()) {
//						int i = index - ((currentRoot == archive) ? 0 : 1);
//						if (i > 0) {
//							TFile file = listedFiles[i];
//							System.out.println("CHOSEN: " + file.getName());
//							
//							if (file.isFile()) {
//								String filename = file.getName();
//								String tempDir = FileUtils.getTempDirectoryPath();
//								File temp = new File(tempDir + "/" + FilenameUtils.getBaseName(filename) + "." + FilenameUtils.getExtension(filename));
//								System.out.println("TEMP: " + temp.getAbsolutePath());
//								temp.deleteOnExit();
//								file.cp(temp);
//								files.add(temp);
//							} else {
//								// TODO copy whole directory?
//								System.out.println("Discard directory");
//							}
//						} else {
//							System.out.println("Discard invalid index");
//						}
//					}
//	//				TFile selectedFile = currentRoot.listFiles()[table.getSelectedRow() - ((currentRoot == archive) ? 0 : 1)];
//	//				if (selectedFile.isFile()) {
//	//					String filename = selectedFile.getName();
//	//					String tempDir = FileUtils.getTempDirectoryPath();
//	//					File temp = new File(tempDir + "/" + FilenameUtils.getBaseName(filename) + "." + FilenameUtils.getExtension(filename));
//	//					System.out.println("TEMP: " + temp.getAbsolutePath());
//	//					temp.deleteOnExit();
//	//					selectedFile.cp(temp);
//	//					files.add(temp);
//	//				}
//					return files;
//				}
//			});
//		} catch (Exception e) {
//			System.out.println("DRAG PROBLEM");
//			e.printStackTrace();
//		}
	}
}