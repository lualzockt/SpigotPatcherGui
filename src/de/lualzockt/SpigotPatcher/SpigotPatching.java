package de.lualzockt.SpigotPatcher;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.md_5.jbeat.Patcher;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public class SpigotPatching extends JDialog implements Runnable{

	
	
	public SpigotPatching(File originalFile, File outputFile, File patchFile) {
		super();
		this.originalFile = originalFile;
		this.outputFile = outputFile;
		this.patchFile = patchFile;
	}


	private static final long serialVersionUID = 2720953188461704220L;
	
	private File originalFile, outputFile,patchFile;
	private JLabel inputMD5 = new JLabel("Original MD5: ");
	private JLabel patchMD5 = new JLabel("Patch MD5: ");
	private JLabel status = new JLabel("...");
	private void status(String s) {
		status.setText("Status: " + s);
	}
			
	public void init() {
		
		this.add(inputMD5);
		this.add(patchMD5);
		this.add(status);
		this.setLayout(new GridLayout(4,1));
		this.setPreferredSize(new Dimension(400, 400));
		this.pack();
		this.setVisible(true);
		this.status("Getting original md5 checksum...");
		new Thread(this).start();
		
	}


	@Override
	public void run() {
		try {
			HashCode hash = Files.hash(originalFile,Hashing.md5());
			/*if(!hash.equals("f2edc09c45b1f80237602dc0d1b05969")) {
				JOptionPane.showMessageDialog(this, "Spigot Build #1649 is required!");
				return;
			}*/
			this.inputMD5.setText("Original MD5: " + hash);
			this.status("Getting patch md5 checksum...");
			this.patchMD5.setText("Patch MD5: " + Files.hash(patchFile, Hashing.md5()));
			this.status("Patching...");
			new Patcher(patchFile, originalFile, outputFile).patch();
			this.status("Finished!");
		} catch (FileNotFoundException e) {
			e.printStackTrace(); // This won't happen, we have checked this before
		} catch (IOException e) {
			this.status.setText("An error occured!");
			JOptionPane.showMessageDialog(this, e.toString());
			e.printStackTrace();
		}
		
	}
}
