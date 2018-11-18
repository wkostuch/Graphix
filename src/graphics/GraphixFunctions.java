package graphics;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JLabel;

public class GraphixFunctions extends JFrame {

	private JPanel contentPane;
	private JTextField xPos;
	private JTextField yPos;
	private JButton btnNewVertex;
	private JLabel lblXposition;
	private JLabel lblYposition;
	/**
	 * Launch the application.
	 */
	/**
	 * Main not needed, this JFrame will be called by a button in Graphix
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphixFunctions frame = new GraphixFunctions();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/

	/**
	 * Create the frame.
	 */
	public GraphixFunctions() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnDrawMwst = new JButton("Draw MWST");
		btnDrawMwst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {}
		});
		GridBagConstraints gbc_btnDrawMwst = new GridBagConstraints();
		gbc_btnDrawMwst.insets = new Insets(0, 0, 5, 0);
		gbc_btnDrawMwst.gridx = 1;
		gbc_btnDrawMwst.gridy = 0;
		contentPane.add(btnDrawMwst, gbc_btnDrawMwst);
		
		lblXposition = new JLabel("x");
		GridBagConstraints gbc_lblXposition = new GridBagConstraints();
		gbc_lblXposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblXposition.anchor = GridBagConstraints.EAST;
		gbc_lblXposition.gridx = 0;
		gbc_lblXposition.gridy = 1;
		contentPane.add(lblXposition, gbc_lblXposition);
		
		xPos = new JTextField();
		GridBagConstraints gbc_xPos = new GridBagConstraints();
		gbc_xPos.insets = new Insets(0, 0, 5, 0);
		gbc_xPos.fill = GridBagConstraints.HORIZONTAL;
		gbc_xPos.gridx = 1;
		gbc_xPos.gridy = 1;
		contentPane.add(xPos, gbc_xPos);
		xPos.setColumns(10);
		
		lblYposition = new JLabel("y");
		GridBagConstraints gbc_lblYposition = new GridBagConstraints();
		gbc_lblYposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblYposition.anchor = GridBagConstraints.EAST;
		gbc_lblYposition.gridx = 0;
		gbc_lblYposition.gridy = 2;
		contentPane.add(lblYposition, gbc_lblYposition);
		
		yPos = new JTextField();
		GridBagConstraints gbc_yPos = new GridBagConstraints();
		gbc_yPos.insets = new Insets(0, 0, 5, 0);
		gbc_yPos.fill = GridBagConstraints.HORIZONTAL;
		gbc_yPos.gridx = 1;
		gbc_yPos.gridy = 2;
		contentPane.add(yPos, gbc_yPos);
		yPos.setColumns(10);
		
		btnNewVertex = new JButton("Add Vertex");
		btnNewVertex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {}
		});
		GridBagConstraints gbc_btnNewVertex = new GridBagConstraints();
		gbc_btnNewVertex.gridx = 1;
		gbc_btnNewVertex.gridy = 3;
		contentPane.add(btnNewVertex, gbc_btnNewVertex);
	}
	
	/**
	 * Overloaded constructor that places and sizes the window based on the
	 * position and size of Graphix
	 */
	public GraphixFunctions(int width, int height) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnDrawMwst = new JButton("Draw MWST");
		btnDrawMwst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {}
		});
		GridBagConstraints gbc_btnDrawMwst = new GridBagConstraints();
		gbc_btnDrawMwst.insets = new Insets(0, 0, 5, 0);
		gbc_btnDrawMwst.gridx = 1;
		gbc_btnDrawMwst.gridy = 0;
		contentPane.add(btnDrawMwst, gbc_btnDrawMwst);
		
		lblXposition = new JLabel("x");
		GridBagConstraints gbc_lblXposition = new GridBagConstraints();
		gbc_lblXposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblXposition.anchor = GridBagConstraints.EAST;
		gbc_lblXposition.gridx = 0;
		gbc_lblXposition.gridy = 1;
		contentPane.add(lblXposition, gbc_lblXposition);
		
		xPos = new JTextField();
		GridBagConstraints gbc_xPos = new GridBagConstraints();
		gbc_xPos.insets = new Insets(0, 0, 5, 0);
		gbc_xPos.fill = GridBagConstraints.HORIZONTAL;
		gbc_xPos.gridx = 1;
		gbc_xPos.gridy = 1;
		contentPane.add(xPos, gbc_xPos);
		xPos.setColumns(10);
		
		lblYposition = new JLabel("y");
		GridBagConstraints gbc_lblYposition = new GridBagConstraints();
		gbc_lblYposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblYposition.anchor = GridBagConstraints.EAST;
		gbc_lblYposition.gridx = 0;
		gbc_lblYposition.gridy = 2;
		contentPane.add(lblYposition, gbc_lblYposition);
		
		yPos = new JTextField();
		GridBagConstraints gbc_yPos = new GridBagConstraints();
		gbc_yPos.insets = new Insets(0, 0, 5, 0);
		gbc_yPos.fill = GridBagConstraints.HORIZONTAL;
		gbc_yPos.gridx = 1;
		gbc_yPos.gridy = 2;
		contentPane.add(yPos, gbc_yPos);
		yPos.setColumns(10);
		
		btnNewVertex = new JButton("Add Vertex");
		btnNewVertex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {}
		});
		GridBagConstraints gbc_btnNewVertex = new GridBagConstraints();
		gbc_btnNewVertex.gridx = 1;
		gbc_btnNewVertex.gridy = 3;
		contentPane.add(btnNewVertex, gbc_btnNewVertex);
		
		this.setVisible(true);
	}

}
