package de.lualzockt.SpigotPatcher;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SpigotPatcher extends JFrame implements ActionListener  {

	private static final long serialVersionUID = 6666866300059718714L;

	public static void main(String[] args) {
		new SpigotPatcher();
	}
	private File originalFile = null;
	private File patchFile = null;
	private File outputFile = null;
	private JButton originalButton = new JButton("...");
	private JButton patchButton = new JButton("...");
	private JButton outputButton = new JButton("...");

	public SpigotPatcher() {
		super("SpigotPatcher");
		this.setLayout(new GridLayout(4, 1));
		JPanel a = new JPanel(new FlowLayout());
		a.add(new JLabel("Original File:"));
		a.add(originalButton);
		JPanel b = new JPanel(new FlowLayout());
		b.add(new JLabel("Path File:"));
		b.add(patchButton);
		JPanel c = new JPanel(new FlowLayout());
		c.add(new JLabel("Output File:"));
		c.add(outputButton);

		outputButton.addActionListener(this);
		outputButton.setActionCommand("output");
		originalButton.addActionListener(this);
		originalButton.setActionCommand("original");
		patchButton.addActionListener(this);
		patchButton.setActionCommand("patch");
		this.add(a);
		this.add(b);
		this.add(c);

		JButton d = new JButton("Patch now!");
		d.addActionListener(this);
		d.setActionCommand("patchNow");
		this.add(d);
		this.setPreferredSize(new Dimension(500,500));
		this.pack();
		this.setVisible(true);
	}

	private void patch() throws IOException {
		if(originalFile == null || outputFile == null || patchFile == null) {
			JOptionPane.showMessageDialog(this, "Please select all files!");
			return;
		}
		if ( !originalFile.canRead() ){
					JOptionPane.showMessageDialog(this, "Specified original file " + originalFile + " does not exist or cannot be read!" );
			return;
		}
		if ( !patchFile.canRead() ){
			JOptionPane.showMessageDialog(this, "Specified patch file " + patchFile + " does not exist or cannot be read!!" );
			return;
		}
		if ( outputFile.exists() ){
			JOptionPane.showMessageDialog(this, "Specified output file " + outputFile + " exists, please remove it before running this program!" );
			return;
		}
		if ( !outputFile.createNewFile() ){
			System.out.println( "Could not create specified output file " + outputFile + " please ensure that it is in a valid directory which can be written to." );
			return;
		}
		new SpigotPatching(originalFile, outputFile, patchFile).init();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		FileDialog fd = null;
		switch(e.getActionCommand()) {
		case "output":
			fd = new FileDialog(this, "Select Output File");
			fd.setMode(FileDialog.SAVE);
			fd.setVisible(true);
			if(fd.getFile() != null) {
				File file = new File(fd.getDirectory(),fd.getFile());
				this.outputFile = file;
				this.outputButton.setText(this.outputFile.getName());
			}
			break;

		case "original":
			fd = new FileDialog(this, "Select Original File");
			fd.setMode(FileDialog.LOAD);
			fd.setFile("*.jar");
			fd.setVisible(true);
			if(fd.getFile() != null) {
				File file = new File(fd.getDirectory(),fd.getFile());
				if(file.exists()) {
					this.originalFile = file;
					this.originalButton.setText(this.originalFile.getName());
				}else {
					JOptionPane.showMessageDialog(this, "The file does not exist!");
				}
			}
			break;
		case "patch":
			fd = new FileDialog(this, "Select Patch File");
			fd.setMode(FileDialog.LOAD);
			fd.setFile("*.bps");
			fd.setVisible(true);
			if(fd.getFile() != null) {
				File file = new File(fd.getDirectory(),fd.getFile());
				if(file.exists()) {
					this.patchFile = file;
					this.patchButton.setText(this.originalFile.getName());
				}else {
					JOptionPane.showMessageDialog(this, "The file does not exist!");
				}
			}
			break;
		case "patchNow":
			try {
				this.patch();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, e1.toString());
			}
		}

	}

}
