package qrrc.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import qrrc.controller.BtnListener;

public class Window{

	private JFrame frame;
	
	private BtnListener al;
	
	public static final String STR_LOAD_FILE = "Load File";
	public static final String STR_SAVE_IMAGE = "Save Image";
	public static final String STR_LOAD_IMAGE = "Load Image";

	/**
	 * Create the application.
	 */
	public Window(BtnListener al) {
		this.al = al;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[10px][89px,grow][][][][][][][][][][][]", "[79px][][][][][][grow]"));
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, "cell 12 0,alignx left,aligny top");
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton btnLoadFile = new JButton(STR_LOAD_FILE);
		GridBagConstraints gbc_btnLoadFile = new GridBagConstraints();
		gbc_btnLoadFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoadFile.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadFile.gridx = 0;
		gbc_btnLoadFile.gridy = 0;
		btnLoadFile.addActionListener(al);
		panel_1.add(btnLoadFile, gbc_btnLoadFile);
		
		JButton btnSaveImage = new JButton(STR_SAVE_IMAGE);
		GridBagConstraints gbc_btnSaveImage = new GridBagConstraints();
		gbc_btnSaveImage.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSaveImage.insets = new Insets(0, 0, 5, 0);
		gbc_btnSaveImage.gridx = 0;
		gbc_btnSaveImage.gridy = 1;
		btnSaveImage.addActionListener(al);
		panel_1.add(btnSaveImage, gbc_btnSaveImage);
		
		JButton btnLoadImage = new JButton(STR_LOAD_IMAGE);
		GridBagConstraints gbc_btnLoadImage = new GridBagConstraints();
		gbc_btnLoadImage.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoadImage.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadImage.gridx = 0;
		gbc_btnLoadImage.gridy = 2;
		btnLoadImage.addActionListener(al);
		panel_1.add(btnLoadImage, gbc_btnLoadImage);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, "cell 0 0 12 7,grow");
	}
	
	public JFrame getFrame() {
		return frame;
	}
	

}
