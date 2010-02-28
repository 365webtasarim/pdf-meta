package com.btr.pdfmeta;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.btr.pdfmeta.FileDnDSupport.FileDropListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/*****************************************************************************
 * GUI Editor
 *
 * @author Bernd Rosstauscher (pdfmeta@rosstauscher.de) Copyright 2010
 ****************************************************************************/

public class GuiEditor extends JFrame {
	
	private HashMap<String, String> metadata = new HashMap<String, String>();

	private JTextField authorField;
	private JTextField titleField;
	private JTextField subjectField;
	private JTextField keywordsField;
	private JCheckBox backupCheckbox;
	private JButton saveButton;
	private JList fileList;

	private String inFileName;

	/*************************************************************************
	 * Constructor
	 * @param args command line arguments.
	 ************************************************************************/
	
	public GuiEditor(final String[] args) {
		super();
		setTitle(Messages.getString("GuiEditor.window_title")); //$NON-NLS-1$
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		URL url = getClass().getClassLoader().getResource("com/btr/pdfmeta/logo.png"); //$NON-NLS-1$
		setIconImage(new ImageIcon(url).getImage());
		
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			// Use default L&F instead
//		}
		
		init();
		
		if (args.length == 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					showFileChooser();
				}
			});
		} else {
			fillFileList(args);
		}
	}

	/*************************************************************************
	 * Initialize the GUI
	 * @param files passed in as command line arguments.
	 ************************************************************************/
	
	private void init() {
		setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(5, 5, 5,5);
		
		gc.gridx = 0; gc.gridy = 0; gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		centerPanel.add(new JLabel(Messages.getString("GuiEditor.author")), gc); //$NON-NLS-1$
		gc.gridx++; gc.anchor = GridBagConstraints.BASELINE_LEADING; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0; gc.gridwidth = 2;
		centerPanel.add(this.authorField = new JTextField(metadata.get("Author"), 20), gc); //$NON-NLS-1$
	
		gc.gridy++; gc.gridx = 0; gc.anchor = GridBagConstraints.BASELINE_TRAILING; gc.fill = GridBagConstraints.NONE; gc.weightx = 0.0; gc.gridwidth = 1;
		centerPanel.add(new JLabel(Messages.getString("GuiEditor.title")), gc); //$NON-NLS-1$
		gc.gridx++; gc.anchor = GridBagConstraints.BASELINE_LEADING; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0; gc.gridwidth = 2;
		centerPanel.add(this.titleField = new JTextField(metadata.get("Title"), 20), gc); //$NON-NLS-1$

		gc.gridy++; gc.gridx = 0; gc.anchor = GridBagConstraints.BASELINE_TRAILING; gc.fill = GridBagConstraints.NONE; gc.weightx = 0.0; gc.gridwidth = 1;
		centerPanel.add(new JLabel(Messages.getString("GuiEditor.subject")), gc); //$NON-NLS-1$
		gc.gridx++; gc.anchor = GridBagConstraints.BASELINE_LEADING; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0; gc.gridwidth = 2;
		centerPanel.add(this.subjectField = new JTextField(metadata.get("Subject"), 20), gc); //$NON-NLS-1$

		gc.gridy++; gc.gridx = 0; gc.anchor = GridBagConstraints.BASELINE_TRAILING; gc.fill = GridBagConstraints.NONE; gc.weightx = 0.0; gc.gridwidth = 1;
		centerPanel.add(new JLabel(Messages.getString("GuiEditor.keywords")), gc); //$NON-NLS-1$
		
		gc.gridx++; gc.anchor = GridBagConstraints.BASELINE_LEADING; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0; gc.gridwidth = 2;
		centerPanel.add(this.keywordsField = new JTextField(metadata.get("Keywords"), 20), gc); //$NON-NLS-1$

		gc.gridy++; gc.gridx = 1; gc.gridwidth = 1;
		centerPanel.add(backupCheckbox = new JCheckBox(Messages.getString("GuiEditor.enableBackup"), true), gc);  //$NON-NLS-1$
		
		gc.gridx++; gc.gridwidth = GridBagConstraints.REMAINDER; gc.fill = GridBagConstraints.NONE; gc.weightx = 0.0; 
		gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		this.saveButton = new JButton(Messages.getString("GuiEditor.save")); //$NON-NLS-1$
		saveButton.setMnemonic('S');
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveMetaData();
			}
		});
		centerPanel.add(saveButton, gc); 
		add(centerPanel, BorderLayout.CENTER);
		
		this.fileList = new JList(new DefaultListModel());
		this.fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.fileList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					loadPdfFile();
				}
			}
		});
		FileDnDSupport dnd = new FileDnDSupport();
		dnd.addDropTarget(this.fileList);
		dnd.addFileFilter(new PdfFileFilter());
		dnd.addDropListener(new FileDropListener() {
			public void filesDropped(List<File> fileList) {
				addFilesToList(fileList);
			}
		});
		
		JPanel westPanel = new JPanel(new BorderLayout());
		westPanel.add(new JScrollPane(this.fileList), BorderLayout.CENTER);
		westPanel.setPreferredSize(new Dimension(200, 100));
		JButton loadButton = new JButton(Messages.getString("GuiEditor.openButton")); //$NON-NLS-1$
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileChooser();
			}
		});
		westPanel.add(loadButton, BorderLayout.SOUTH);
		add(westPanel, BorderLayout.WEST);
		
		pack();
		setLocationRelativeTo(null);
	}

	/*************************************************************************
	 * Files the files list from the given list of file names.
	 * @param files to fill into the left side list.
	 ************************************************************************/
	
	private void fillFileList(Object[] files) {
		DefaultListModel m = new DefaultListModel();
		for (Object f : files) {
			String name = f.toString();
			int index = name.lastIndexOf(File.separatorChar);
			if (index > -1) {
				name = name.substring(index+1);
			}
			index = name.lastIndexOf('.');
			if (index > -1) {
				name = name.substring(0, index);
			}
			m.addElement(new ListEntry(f.toString()));
		}
		
		this.fileList.setModel(m);	
		if (m.size() > 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					fileList.setSelectedIndex(0);
				}
			}); 
		}
	}
	
	/*************************************************************************
	 * Load the selected PDF info into the GUI and open the reader.
	 ************************************************************************/
	
	@SuppressWarnings("unchecked")
	private void loadPdfFile() {
		this.inFileName = ""; //$NON-NLS-1$
		
		boolean fileSelected = fileList.getSelectedValue() != null;
		enableEditor(fileSelected);
		if (fileSelected == false) {
			return;
 		} else {
 			inFileName = ((ListEntry) fileList.getSelectedValue()).getFilePath();
 		}

		try {
			PdfReader reader = new PdfReader(new FileInputStream(inFileName));
			this.metadata = reader.getInfo();
			reader.close();
			
			updateFields();
		} catch (IOException e) {
			enableEditor(false);
			JOptionPane.showMessageDialog(this, e.getMessage(), 
					Messages.getString("GuiEditor.error1"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			e.printStackTrace();
		}
		
	}

	/*************************************************************************
	 * Enables or disables the editor fields.
	 * @param enable 
	 ************************************************************************/
	
	private void enableEditor(boolean enable) {
		this.authorField.setEnabled(enable);
		this.titleField.setEnabled(enable);
		this.subjectField.setEnabled(enable);
		this.keywordsField.setEnabled(enable);
		this.saveButton.setEnabled(enable);
		if (enable == false) {
			this.authorField.setText(""); //$NON-NLS-1$
			this.titleField.setText(""); //$NON-NLS-1$
			this.subjectField.setText(""); //$NON-NLS-1$
			this.keywordsField.setText(""); //$NON-NLS-1$
		}
	}

	/*************************************************************************
	 * Shows a file chooser to select a input PDF file.
	 ************************************************************************/
	
	private void showFileChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.addChoosableFileFilter(new PdfFileFilter());
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			fillFileList(files);
		}
	}

	/*************************************************************************
	 * Update the fields with the meta data read from the file.
	 ************************************************************************/
	
	private void updateFields() {
		this.authorField.setText(this.metadata.get("Author")); //$NON-NLS-1$
		this.titleField.setText(this.metadata.get("Title")); //$NON-NLS-1$
		this.subjectField.setText(this.metadata.get("Subject")); //$NON-NLS-1$
		this.keywordsField.setText(this.metadata.get("Keywords")); //$NON-NLS-1$
	}
	
	/*************************************************************************
	 * Saves the meta data and closes the editor. 
	 ************************************************************************/
	
	private void saveMetaData() {
		try {
			this.metadata.put("Author",  this.authorField.getText()); //$NON-NLS-1$
			this.metadata.put("Title", this.titleField.getText()); //$NON-NLS-1$
			this.metadata.put("Subject", this.subjectField.getText()); //$NON-NLS-1$
			this.metadata.put("Keywords", this.keywordsField.getText()); //$NON-NLS-1$
			
			File f = new File(inFileName);
			String backupFileName = f.getAbsolutePath()+".bak"; //$NON-NLS-1$
			File backupFile = new File(backupFileName);
			if (f.renameTo(backupFile) == false) {
				JOptionPane.showMessageDialog(this, Messages.getString("GuiEditor.error2")+backupFile,  //$NON-NLS-1$
						Messages.getString("GuiEditor.error3"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				return;
			}
			
			FileInputStream fin = new FileInputStream(backupFile);
			PdfReader reader = new PdfReader(fin);
			
			FileOutputStream fout = new FileOutputStream(inFileName);
			PdfStamper stamper = new PdfStamper(reader,	fout);
			stamper.setMoreInfo(metadata);
			
			// Add new XML based metadata too.
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			XmpWriter xmp = new XmpWriter(baos, metadata);
//			xmp.close();
//			stamper.setXmpMetadata(baos.toByteArray());

			reader.close();
			fin.close();
			stamper.close();
			fout.close();
			
			// Delete temporary file if no backup is requested
			if (backupCheckbox.isSelected() == false) {
				backupFile.delete();
			}
			
		} catch (DocumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), 
					Messages.getString("GuiEditor.error3"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), 
					Messages.getString("GuiEditor.error3"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			e.printStackTrace();
		}
	}

	/*************************************************************************
	 * Adds the given files to the GUI list.
	 * @param fileList
	 ************************************************************************/
	
	private void addFilesToList(List<File> fileList) {
		DefaultListModel m = (DefaultListModel) GuiEditor.this.fileList.getModel();
		for (File file : fileList) {
			ListEntry e = new ListEntry(file.getPath());
			if (m.contains(e) == false) {
				m.addElement(e);
			}
		} 
	}


}
