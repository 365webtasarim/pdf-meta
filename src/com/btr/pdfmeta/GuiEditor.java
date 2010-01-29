package com.btr.pdfmeta;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

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

	private String inFileName;

	/*************************************************************************
	 * Constructor
	 * @param args command line arguments.
	 ************************************************************************/
	
	public GuiEditor(final String[] args) {
		super();
		setTitle(Messages.getString("GuiEditor.window_title")); //$NON-NLS-1$
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			// Use default L&F instead
//		}
		
		init();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loadPdfFile(args);
			}
		});
		
	}

	/*************************************************************************
	 * Initialize the GUI
	 ************************************************************************/
	
	private void init() {
		setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(5, 5, 5,5);
		
		gc.gridx = 0; gc.gridy = 0; gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		centerPanel.add(new JLabel(Messages.getString("GuiEditor.author")), gc); //$NON-NLS-1$
		gc.gridx++; gc.anchor = GridBagConstraints.BASELINE_LEADING;
		centerPanel.add(this.authorField = new JTextField(metadata.get("Author"), 20), gc); //$NON-NLS-1$
	
		gc.gridy++; gc.gridx = 0; gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		centerPanel.add(new JLabel(Messages.getString("GuiEditor.title")), gc); //$NON-NLS-1$
		gc.gridx++; gc.anchor = GridBagConstraints.BASELINE_LEADING;
		centerPanel.add(this.titleField = new JTextField(metadata.get("Title"), 20), gc); //$NON-NLS-1$

		gc.gridy++; gc.gridx = 0; gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		centerPanel.add(new JLabel(Messages.getString("GuiEditor.subject")), gc); //$NON-NLS-1$
		gc.gridx++; gc.anchor = GridBagConstraints.BASELINE_LEADING;
		centerPanel.add(this.subjectField = new JTextField(metadata.get("Subject"), 20), gc); //$NON-NLS-1$

		gc.gridy++; gc.gridx = 0; gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		centerPanel.add(new JLabel(Messages.getString("GuiEditor.keywords")), gc); //$NON-NLS-1$
		
		gc.gridx++; gc.anchor = GridBagConstraints.BASELINE_LEADING;
		centerPanel.add(this.keywordsField = new JTextField(metadata.get("Keywords"), 20), gc); //$NON-NLS-1$

		gc.gridy++; gc.gridx = 0; gc.gridwidth = GridBagConstraints.REMAINDER; 
		gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		JButton saveButton = new JButton(Messages.getString("GuiEditor.save")); //$NON-NLS-1$
		saveButton.setMnemonic('S');
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveMetaData();
			}
		});
		centerPanel.add(saveButton, gc); 
		
		add(centerPanel, BorderLayout.CENTER);
		
		pack();
		setLocationRelativeTo(null);
	}
	
	/*************************************************************************
	 * Load the PDF info into the GUI and open the reader.
	 * @param args command line arguments.
	 ************************************************************************/
	@SuppressWarnings("unchecked")
	private void loadPdfFile(String[] args) {
		this.inFileName = ""; //$NON-NLS-1$
		if (args.length < 2) {
			inFileName = showFileChooser();
			if (inFileName == null) {
				dispose();
				return;
			}
 		} else {
 			inFileName = args[1];
 		}

		try {
			PdfReader reader = new PdfReader(new FileInputStream(inFileName));
			this.metadata = reader.getInfo();
			reader.close();
			
			updateFields();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), 
					Messages.getString("GuiEditor.error1"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			e.printStackTrace();
		}
		
	}

	/*************************************************************************
	 * Shows a file chooser to select a input PDF file.
	 ************************************************************************/
	
	private String showFileChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return Messages.getString("GuiEditor.filter"); //$NON-NLS-1$
			}
			@Override
			public boolean accept(File f) {
				return f.isDirectory()
					|| f.getName().toLowerCase().endsWith(".pdf") ; //$NON-NLS-1$
			}
		});
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getPath();
		}
		return null;
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
			String backupFile = f.getAbsolutePath()+".bak"; //$NON-NLS-1$
			if (f.renameTo(new File(backupFile)) == false) {
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
			
			dispose();
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


}
