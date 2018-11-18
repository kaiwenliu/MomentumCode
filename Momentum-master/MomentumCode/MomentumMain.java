
//Made by Kevin Chu for SMHacksII
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MomentumMain extends JPanel {
	private String fileName;
	private String code;
	private JFileChooser chooser;
	private FileNameExtensionFilter filter;
	private JFrame frmMomentum;
	private JTextArea consoleArea;
	private JTextArea editor;
	private JScrollPane scroll;
	private JScrollPane scroll2;
	private String[] codeList;
	private ArrayList<Text> text;
	private ArrayList<Letter> letters;
	private ArrayList<Cond> conds;
	private ArrayList<Number> numbers;

	public ArrayList<Text> getText() {
		return text;
	}

	public ArrayList<Letter> getLetters() {
		return letters;
	}

	public ArrayList<Cond> getConds() {
		return conds;
	}

	public ArrayList<Number> getNumbers() {
		return numbers;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawLine(300, 0, 300, 1000);
	}

	@SuppressWarnings("resource")
	public MomentumMain() {
		numbers = new ArrayList<Number>();
		text = new ArrayList<Text>();
		conds = new ArrayList<Cond>();
		letters = new ArrayList<Letter>();

		frmMomentum = new JFrame();
		frmMomentum.setTitle("Momentum");
		frmMomentum.setBounds(100, 100, 1100, 700);
		frmMomentum.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMomentum.getContentPane().setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmMomentum.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel console = new JPanel();
		tabbedPane.addTab("Console", null, console, null);
		console.setLayout(null);

		JLabel consoleLabel = new JLabel("Console");
		consoleLabel.setBounds(690, 0, 100, 50);
		console.add(consoleLabel);

		consoleArea = new JTextArea(100, 100);
		consoleArea.setBounds(400, 45, 589, 316);
		consoleArea.setBackground(Color.WHITE);
		consoleArea.setEditable(false);
		scroll = new JScrollPane(consoleArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		console.add(scroll);
		console.add(consoleArea);

		editor = new JTextArea(100, 100);
		editor.setBounds(21, 100, 316, 400);
		editor.setBackground(Color.WHITE);
		scroll2 = new JScrollPane(editor);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		console.add(scroll2);
		console.add(editor);

		JLabel openFiles = new JLabel("Select a .mc or .txt file");
		openFiles.setBounds(21, 10, 137, 25);
		console.add(openFiles);
		
		JButton runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset();
				codeList = code.split(":\\)");

				for (int i = 0; i < codeList.length; i++) {
					String currentStatement = codeList[i].replace("\r", "");
					currentStatement = currentStatement.replace("\n", "");

					String[] currentKeywords = currentStatement.split(" ");
					String first = currentKeywords[0];

					if (first.equals("Container")) {
						String second = currentKeywords[1];
						if (newVariable(currentKeywords[2])) {
							if (second.equals("number")) {
								if (!currentKeywords[4].contains(".")) {
									currentKeywords[4] = currentKeywords[4] + ".0";
								}
								numbers.add(new Number(currentKeywords[2], Double.parseDouble(currentKeywords[4])));
							} else if (second.equals("text")) {
								text.add(new Text(currentKeywords[2], currentKeywords[4].substring(1, currentKeywords[4].length()-1)));
//								System.out.println(currentKeywords[2].substring(1));
							} else if (second.equals("cond")) {
								if (currentKeywords[4].equals("true")) {
									conds.add(new Cond(currentKeywords[2], true));
								} else {
									conds.add(new Cond(currentKeywords[2], false));

								}

							}
							if (second.equals("letter")) {
								letters.add(new Letter(currentKeywords[2], currentKeywords[4].charAt(0)));

							}

						}
					} else if (first.equals("Print")) {
						boolean isQuote = false;
						for (int j = 1; j < currentKeywords.length; j++) {
							if (isQuote) {
								if (currentKeywords[j].charAt(currentKeywords[j].length() - 1) == '"') {
									isQuote = !isQuote;
									consoleArea.append("\n" + currentKeywords[j].substring(0, currentKeywords[j].length() - 1));
								} else {
									consoleArea.append("\n" + currentKeywords[j]);
								}
							} else {
								if (currentKeywords[j].charAt(0) == '"') {
									isQuote = !isQuote;
									consoleArea.append("\n" + currentKeywords[j].substring(1, currentKeywords[j].length() - 1));
								} else {
									printVariable(currentKeywords[j]);
								}
							}
						}
					} else if (first.equals("Change")) {
						String dataType = "";
						for (int b = 0; b < numbers.size(); b++) {
							if (numbers.get(b).getName().trim().equals(currentKeywords[1]))
								dataType = "number";
						}
						for (int y = 0; y < conds.size(); y++) {
							if (conds.get(y).getName().trim().equals(currentKeywords[1]))
								dataType = "cond";
						}
						for (int x = 0; x < text.size(); x++) {
							if (text.get(x).getName().trim().equals(currentKeywords[1]))
								dataType = "text";
						}
						for (int e = 0; e < letters.size(); e++) {
							if (letters.get(e).getName().trim().equals(currentKeywords[1]))
								dataType = "letter";
						}

						new ChangeParser(numbers,text,conds,letters).change(currentStatement,dataType);
					} else if (first.equals("If")) {
						boolean b = new ConditionParser(numbers).getCondition(currentKeywords[1] + " "  + currentKeywords[2]+ " " + currentKeywords[3]);
						
						if (b) {
							if (currentKeywords[4].equals("Change")) {
								String dataType = "";
								for (int c = 0; c < numbers.size(); c++) {
									if (numbers.get(c).getName().trim().equals(currentKeywords[5]))
										dataType = "number";
								}
								for (int y = 0; y < conds.size(); y++) {
									if (conds.get(y).getName().trim().equals(currentKeywords[5]))
										dataType = "cond";
								}
								for (int x = 0; x < text.size(); x++) {
									if (text.get(x).getName().trim().equals(currentKeywords[5]))
										dataType = "text";
								}
								for (int e = 0; e < letters.size(); e++) {
									if (letters.get(e).getName().trim().equals(currentKeywords[5]))
										dataType = "letter";
								}
								new ChangeParser(numbers,text,conds,letters).change(currentStatement,dataType);
							} else if (currentKeywords[4].equals("Print")) {
								boolean isQuote = false;
								for (int j = 5; j < currentKeywords.length; j++) {
									if (isQuote) {
										if (currentKeywords[j].charAt(currentKeywords[j].length() - 1) == '"') {
											isQuote = !isQuote;
											consoleArea.append("\n" + currentKeywords[j].substring(0, currentKeywords[j].length() - 1));
										} else {
											consoleArea.append("\n" + currentKeywords[j]);
										}
									} else {
										if (currentKeywords[j].charAt(0) == '"') {
											isQuote = !isQuote;
											consoleArea.append("\n" + currentKeywords[j].substring(1, currentKeywords[j].length() - 1));
										} else {
											printVariable(currentKeywords[j]);
										}
									}
								}
							} else if (currentKeywords[4].equals("Container")) {
								String second = currentKeywords[5];
								if (newVariable(currentKeywords[6])) {
									if (second.equals("number")) {
										if (!currentKeywords[8].contains(".")) {
											currentKeywords[8] = currentKeywords[8] + ".0";
										}
										numbers.add(new Number(currentKeywords[6], Double.parseDouble(currentKeywords[8])));
									} else if (second.equals("text")) {
										text.add(new Text(currentKeywords[6], currentKeywords[8].substring(1, currentKeywords[8].length()-1)));
//										System.out.println(currentKeywords[2].substring(1));
									} else if (second.equals("cond")) {
										if (currentKeywords[8].equals("true")) {
											conds.add(new Cond(currentKeywords[2], true));
										} else {
											conds.add(new Cond(currentKeywords[2], false));

										}

									}
									if (second.equals("letter")) {
										letters.add(new Letter(currentKeywords[6], currentKeywords[8].charAt(0)));

									}
								}
							}
						} 
					}
				}
			}
		});
		JButton fileButton = new JButton("Open");
		fileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooser = new JFileChooser();
				filter = new FileNameExtensionFilter(".mc or .txt files", "txt", "mc");
				chooser.setFileFilter(filter);
				int returnedValue = chooser.showOpenDialog(console);
				if (returnedValue == JFileChooser.APPROVE_OPTION) {
					fileName = chooser.getSelectedFile().getPath();
				}
				try {
					code = new Scanner(new File(fileName)).useDelimiter("\\A").next();
					editor.append(code);
					code = code.replace("\n", "");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				
			}
		});
		fileButton.setBounds(21, 45, 117, 25);
		console.add(fileButton);
		runButton.setBounds(200, 45, 117, 25);
		console.add(runButton);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MomentumMain window = new MomentumMain();
					window.frmMomentum.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private boolean newVariable(String name) {
		boolean newVar = true;
		for (int i = 0; i < conds.size(); i++) {
			if (name.equals(conds.get(i).getName())) {
				newVar = false;
			}
		}
		for (int i = 0; i < text.size(); i++) {
			if (name.equals(text.get(i).getName())) {
				newVar = false;
			}

		}
		for (int i = 0; i < numbers.size(); i++) {
			if (name.equals(numbers.get(i).getName())) {
				newVar = false;
			}
		}
		for (int i = 0; i < letters.size(); i++) {
			if (name.equals(letters.get(i).getName())) {
				newVar = false;
			}
		}

		if (!newVar) {
			consoleArea.append("\n" + "The variable name " + name + " is already being used");
		}
		return newVar;
	}

	private void printVariable(String name) {
		boolean printed = false;
		for (int i = 0; i < conds.size(); i++) {
			if (name.equals(conds.get(i).getName())) {
				consoleArea.append("\n" + conds.get(i).getCond() + "");
				printed = true;
			}
		}
		if (!printed) {
			for (int i = 0; i < text.size(); i++) {
				if (name.equals(text.get(i).getName())) {
					consoleArea.append("\n" + text.get(i).getText() + "");
					printed = true;
				}
			}
		}
		if (!printed) {
			for (int i = 0; i < numbers.size(); i++) {
				if (name.equals(numbers.get(i).getName())) {
					consoleArea.append("\n" + numbers.get(i).getValue() + "");

					printed = true;
				}
			}
		}
		if (!printed) {
			for (int i = 0; i < letters.size(); i++) {
				if (name.equals(letters.get(i).getName())) {
					consoleArea.append("\n" + letters.get(i).getLetter() + "");

					printed = true;
				}
			}
		}
	}
	private void reset() {
		consoleArea.setText(null);
		text.clear();
		numbers.clear();
		conds.clear();
		letters.clear();
	}
}
