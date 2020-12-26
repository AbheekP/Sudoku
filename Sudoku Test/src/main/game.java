package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class game {
	
	JFrame frame;
	JPanel panel;
	int width = 390;
	int height = 440;
	int componentOffset = 37;
	
	int easyCount = 21;
	int mediumCount = 31;
	int hardCount = 41;
	int difficultyCount = easyCount;
	String difficulty = "easy";
	
	File highscores;
	
	int incorrectCount;
	int selectedNum;
	numberButton[] inputButtons;
	Cell[][] board;
	
	Random generator = new Random();
	
	Thread updateTime;
	Thread updateIncorrect;
	
	int minutes;
	int seconds;
	
	public game() {
		
		seconds = 0;
		minutes = 0;
		incorrectCount = 0;
		selectedNum = 0;
		inputButtons = new numberButton[9];
		board = new Cell[9][9];
		
		try {
			
			highscores = new File("highscores.txt");
			highscores.createNewFile();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		/* Creates start frame and the buttons/labels associated with it */
		
		JFrame startFrame = new JFrame();
		startFrame.setPreferredSize(new Dimension(width / 2, height / 2));
		startFrame.setResizable(false);
		startFrame.setLocationRelativeTo(null);
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel startScreen = new JPanel();
		startScreen.setPreferredSize(startFrame.getPreferredSize());
		startScreen.setLayout(null);
		
		startFrame.getContentPane().add(startScreen);
		startFrame.pack();
		
		JLabel title = new JLabel("Sudoku");
		title.setFont(new Font(null, Font.BOLD, 20));
		title.setSize(getCorrectSize(title));
		title.setLocation((startScreen.getWidth() / 2) - (title.getWidth() / 2), 25);
		title.setVisible(true);
		
		JButton startButton = new JButton("Start Game");
		startButton.setSize(title.getWidth() + 30, title.getHeight());
		startButton.setLocation(title.getX() + (title.getWidth() / 2) - (startButton.getWidth() / 2), title.getY() + 30);
		startButton.setVisible(true);
		
		startButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				startFrame.setVisible(false);
				startGame();
				
			}
			
		});
		
		JButton settings = new JButton("Settings");
		settings.setSize(startButton.getSize());
		settings.setLocation(startButton.getX(), startButton.getY() + settings.getHeight() + 5);
		settings.setVisible(true);
		
		settings.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				/* Creates settings frame and all of the buttons/labels associated with it */
				
				startFrame.setVisible(false);
				
				JFrame settingsFrame = new JFrame("Sudoku Settings");
				JPanel settingsPanel = new JPanel();
				
				settingsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				settingsFrame.setPreferredSize(new Dimension(startScreen.getWidth() + 150, startScreen.getHeight() + 50));
				settingsFrame.setLocationRelativeTo(null);
				settingsFrame.setResizable(false);
				settingsFrame.addWindowListener(new WindowAdapter() {
					
					public void windowClosing(WindowEvent e) {
						
						startFrame.setVisible(true);
						settingsFrame.dispose();
						
					}
					
				});
				
				settingsPanel.setSize(settingsFrame.getPreferredSize());
				settingsPanel.setLayout(null);
				settingsPanel.setVisible(true);
				
				settingsFrame.getContentPane().add(settingsPanel);
				settingsFrame.pack();
				
				JLabel currentDifficulty = new JLabel("Currently, " + (81 - difficultyCount) + "/81 numbers are visible (" + difficulty + ")");
				currentDifficulty.setSize(getCorrectSize(currentDifficulty));
				currentDifficulty.setLocation((settingsPanel.getWidth() / 2) - (currentDifficulty.getWidth() / 2), settingsPanel.getHeight() - currentDifficulty.getHeight() - 5);
				currentDifficulty.setVisible(true);
				
				JLabel difficultyExplanation = new JLabel("A harder difficulty means less numbers will visible");
				difficultyExplanation.setSize(getCorrectSize(difficultyExplanation));
				difficultyExplanation.setLocation((settingsPanel.getWidth() / 2) - (difficultyExplanation.getWidth() / 2) + 5, currentDifficulty.getY() - difficultyExplanation.getHeight() - 3);
				difficultyExplanation.setVisible(true);
				
				JButton easy = new JButton("Easy (" + (81 - easyCount) + ")");
				easy.setSize(settings.getWidth() + 5, settings.getHeight());
				easy.setLocation((settingsPanel.getWidth() / 2) - (easy.getWidth() / 2), startButton.getY() - 20);
				easy.setVisible(true);
				easy.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						difficultyCount = easyCount;
						difficulty = "easy";
						currentDifficulty.setText("Currently, " + (81 - difficultyCount) + "/81 numbers are visible (" + difficulty + ")");
						currentDifficulty.setSize(getCorrectSize(currentDifficulty));
						currentDifficulty.setLocation((settingsPanel.getWidth() / 2) - (currentDifficulty.getWidth() / 2), settingsPanel.getHeight() - currentDifficulty.getHeight() - 5);
						
					}
					
				});
				
				JButton medium = new JButton("Medium (" + (81 - mediumCount) + ")");
				medium.setSize(easy.getSize());
				medium.setLocation(easy.getX(), easy.getY() + medium.getHeight() + 5);
				medium.setVisible(true);
				medium.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						difficultyCount = mediumCount;
						difficulty = "medium";
						currentDifficulty.setText("Currently, " + (81 - difficultyCount) + "/81 numbers are visible (" + difficulty + ")");
						currentDifficulty.setSize(getCorrectSize(currentDifficulty));
						currentDifficulty.setLocation((settingsPanel.getWidth() / 2) - (currentDifficulty.getWidth() / 2), settingsPanel.getHeight() - currentDifficulty.getHeight() - 5);
						
					}
					
				});
				
				JButton hard = new JButton("Hard (" + (81 - hardCount) + ")");
				hard.setSize(medium.getSize());
				hard.setLocation(medium.getX(), medium.getY() + medium.getHeight() + 5);
				hard.setVisible(true);
				hard.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						difficultyCount = hardCount;
						difficulty = "hard";
						currentDifficulty.setText("Currently, " + (81 - difficultyCount) + "/81 numbers are visible (" + difficulty + ")");
						currentDifficulty.setSize(getCorrectSize(currentDifficulty));
						currentDifficulty.setLocation((settingsPanel.getWidth() / 2) - (currentDifficulty.getWidth() / 2), settingsPanel.getHeight() - currentDifficulty.getHeight() - 5);
						
					}
					
				});
				
				JLabel difficulty = new JLabel("Difficulty");
				difficulty.setSize(getCorrectSize(difficulty));
				difficulty.setLocation(hard.getX() + (hard.getWidth() / 2) - (difficulty.getWidth() / 2) + 5, 15);
				difficulty.setVisible(true);
				
				settingsPanel.add(easy);
				settingsPanel.add(medium);
				settingsPanel.add(hard);
				settingsPanel.add(difficulty);
				settingsPanel.add(difficultyExplanation);
				settingsPanel.add(currentDifficulty);
			
				settingsFrame.setVisible(true);
				
			}
			
		});
		
		JButton highscore = new JButton("High Scores");
		highscore.setSize(startButton.getSize());
		highscore.setLocation(startButton.getX(), settings.getY() + highscore.getHeight() + 5);
		highscore.setVisible(true);
		
		highscore.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				/* Creates difficulty frame and all of the labels associated with it */
				
				startFrame.setVisible(false);
				
				JFrame highscoreFrame = new JFrame();
				JPanel highscorePanel = new JPanel();
				
				highscoreFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				highscoreFrame.setResizable(false);
				highscoreFrame.setPreferredSize(new Dimension(450, 365));
				highscoreFrame.setLocationRelativeTo(null);
				highscoreFrame.addWindowListener(new WindowAdapter() {
					
					public void windowClosing(WindowEvent e) {
						
						startFrame.setVisible(true);
						highscoreFrame.dispose();
						
					}
					
				});

				highscorePanel.setPreferredSize(highscoreFrame.getPreferredSize());
				highscorePanel.setLayout(null);
				highscorePanel.setVisible(true);
				
				highscoreFrame.getContentPane().add(highscorePanel);
				highscoreFrame.pack();
				
				ArrayList<Score> highscoreList = new ArrayList<Score>();
				Score[] sortedHighScoreList = new Score[10];
				
				try {
					
					Scanner highscoreReader = new Scanner(highscores);
					
					while(highscoreReader.hasNextLine()) {
						
						try {
							
							String nextLine = highscoreReader.nextLine();
							
							String incorrect = nextLine.substring(0, nextLine.indexOf(" "));
							nextLine = nextLine.substring(nextLine.indexOf(" ") + 1);
							
							String minutes = nextLine.substring(0, nextLine.indexOf(" "));
							nextLine = nextLine.substring(nextLine.indexOf(" ") + 1);
							
							String seconds = nextLine.substring(0, nextLine.indexOf(" "));
							nextLine = nextLine.substring(nextLine.indexOf(" ") + 1);
							
							String time = nextLine.substring(0, nextLine.indexOf(" "));
							nextLine = nextLine.substring(nextLine.indexOf(" ") + 1);
							
							String date = nextLine.substring(0, nextLine.indexOf(" "));
							nextLine = nextLine.substring(nextLine.indexOf(" ") + 1);
							
							String difficulty = nextLine;
							
							highscoreList.add(new Score(incorrect, minutes, seconds, time, date, difficulty));
							
						} catch (NoSuchElementException e2) {
							
							System.out.println("no element");
							
						}
						
					}
					
					highscoreReader.close();
					
				} catch (FileNotFoundException e1) {
					
					e1.printStackTrace();
					
				}
				
				JLabel highscoreIncorrectLabel = new JLabel("Incorrect");
				highscoreIncorrectLabel.setSize(getCorrectSize(highscoreIncorrectLabel));
				highscoreIncorrectLabel.setLocation(50, 5);
				
				JLabel highscoreDateLabel = new JLabel("Date");
				highscoreDateLabel.setSize(getCorrectSize(highscoreDateLabel));
				highscoreDateLabel.setLocation(highscoreFrame.getWidth() - highscoreDateLabel.getWidth() - 70, 5);
				
				JLabel highscoreTimeLabel = new JLabel("Time Taken");
				highscoreTimeLabel.setSize(getCorrectSize(highscoreTimeLabel));
				highscoreTimeLabel.setLocation(200, 5);
				
				highscorePanel.add(highscoreDateLabel);
				highscorePanel.add(highscoreIncorrectLabel);
				highscorePanel.add(highscoreTimeLabel);
				
				/* Ranks the highscores based on lowest penalty Score */
				
				for(int i = 0; i < sortedHighScoreList.length; i++) {
					
					int lowestScoreIndex = -1;
					int lowestScore = Integer.MAX_VALUE;
					
					for(int j = 0; j < highscoreList.size(); j++) {
						
						if(highscoreList.get(j).getPenaltyScore() < lowestScore) {
							
							lowestScore = highscoreList.get(j).getPenaltyScore();
							lowestScoreIndex = j;
							
						}
						
					}
					
					Score bestScore = null;
					
					if(lowestScoreIndex != -1) {
						
						bestScore = highscoreList.remove(lowestScoreIndex);
						
					} 
					
					JLabel numberRanking = new JLabel((i + 1) + ".");
					numberRanking.setSize(getCorrectSize(numberRanking));
					numberRanking.setLocation(15, highscoreIncorrectLabel.getY() + highscoreIncorrectLabel.getHeight() + 10 + (i * 30));
					numberRanking.setVisible(true);
					
					highscorePanel.add(numberRanking);
					
					if(bestScore != null) {
						
						JLabel highscoreIncorrectCount = new JLabel();
						
						if(bestScore.getDifficulty().equals("easy")) {
							
							highscoreIncorrectCount.setText(bestScore.getIncorrect() + " (E)");
							
						} else if (bestScore.getDifficulty().equals("medium")) {
							
							highscoreIncorrectCount.setText(bestScore.getIncorrect() + " (M)");
							
						} else if (bestScore.getDifficulty().equals("hard")) {
							
							highscoreIncorrectCount.setText(bestScore.getIncorrect() + " (H)");
							
						} else {
							
							highscoreIncorrectCount.setText(bestScore.getIncorrect() + " (X)");
							
						}
						
						highscoreIncorrectCount.setSize(getCorrectSize(highscoreIncorrectCount));
						highscoreIncorrectCount.setLocation(highscoreIncorrectLabel.getX() + (highscoreIncorrectLabel.getWidth() / 2) - (highscoreIncorrectCount.getWidth() / 2), highscoreIncorrectLabel.getY() + highscoreIncorrectLabel.getHeight() + 10 + (i * 30));
						highscoreIncorrectCount.setVisible(true);
						
						JLabel highscoreTimeCount = new JLabel();
						
						if(bestScore.getSeconds() < 10) {
							
							highscoreTimeCount.setText(bestScore.getMinutes() + ":0" + bestScore.getSeconds());
							
						} else {
							
							highscoreTimeCount.setText(bestScore.getMinutes() + ":" + bestScore.getSeconds());
							
						}
						
						highscoreTimeCount.setSize(getCorrectSize(highscoreTimeCount));
						highscoreTimeCount.setLocation(2 + highscoreTimeLabel.getX() + (highscoreTimeLabel.getWidth() / 2) - (highscoreTimeCount.getWidth() / 2), highscoreTimeLabel.getY() + highscoreTimeLabel.getHeight() + 10 + (i * 30));
						highscoreTimeCount.setVisible(true);
						
						JLabel highscoreDateCount = new JLabel();
						
						int hour = Integer.parseInt(bestScore.getTime().substring(0, 2));
						int minute = Integer.parseInt(bestScore.getTime().substring(3, 5));
						if(hour > 12) {
							
							if(minute < 10) {
								
								highscoreDateCount.setText((hour - 12) + ":0" + minute + " pm  " + bestScore.getDate());
								
							} else {
								
								highscoreDateCount.setText((hour - 12) + ":" + minute + " pm  " + bestScore.getDate());
								
							}
							
							
						} else if (hour == 12) {
							
							if(minute < 10) {
								
								highscoreDateCount.setText(hour + ":0" + minute + " pm  " + bestScore.getDate());

							} else {
								
								highscoreDateCount.setText(bestScore.getTime() + " pm  " + bestScore.getDate());
								
							}
							
							
						} else {
							
							if(minute < 10) {
								
								highscoreDateCount.setText(hour + ":0" + minute + " am  " + bestScore.getDate());
								
							} else {
								
								highscoreDateCount.setText(bestScore.getTime() + " am  " + bestScore.getDate());
								
							}
							
						}
						
						highscoreDateCount.setSize(getCorrectSize(highscoreDateCount));
						highscoreDateCount.setLocation(5 + highscoreDateLabel.getX() + (highscoreDateLabel.getWidth() / 2) - (highscoreDateCount.getWidth() / 2), highscoreDateLabel.getY() + highscoreDateLabel.getHeight() + 10 + (i * 30));
						highscoreDateCount.setVisible(true);
						
						highscorePanel.add(highscoreIncorrectCount);
						highscorePanel.add(highscoreTimeCount);
						highscorePanel.add(highscoreDateCount);
						
					}
					
				}
				
				highscoreFrame.setVisible(true);
				
			}
			
		});
		
		startScreen.add(title);
		startScreen.add(startButton);
		startScreen.add(settings);
		startScreen.add(highscore);
		startScreen.setVisible(true);
		
		startFrame.setVisible(true);

	}
	
	public void startGame() {
		
		frame = new JFrame();
		panel = new JPanel();
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		panel.setLayout(null);
		panel.setPreferredSize(frame.getPreferredSize());
		panel.setVisible(true);
		
		frame.getContentPane().add(panel);
		frame.pack();
		
		setBoard();
		addNumberCell();
		removeNumbers();
		checkInitialNumFinish();
		addInfo();
		
		frame.setVisible(true);	
		
	}
	
	public void setBoard() {
		
		/* Draws the outline of the sudoku board and splits it into 9 sections of 3x3 cells */
		
		boardComp[] comps = new boardComp[2];
		
		/* Vertical section splitters */
		for(int i = 0; i < 2; i++) {
			
			comps[i] = new boardComp("vertical");
			comps[i].setLocation(100 * (i + 1) + componentOffset, componentOffset - 10);
			panel.add(comps[i]);
			
		}
		
		/* Horizontal section splitters */
		for(int i = 0; i < 2; i++) {
			
			comps[i] = new boardComp("horizontal");
			comps[i].setLocation(0 + componentOffset, 100 * (i + 1) + componentOffset - 10);
			panel.add(comps[i]);
			
		}
		
		/* Outline */
		for(int i = 0; i < 2; i++) {
			
			comps[i] = new boardComp("horizontal");
			comps[i].setLocation(0 + componentOffset, 299 * i + componentOffset - 10);
			panel.add(comps[i]);
			
		}
		
		for(int i = 0; i < 2; i++) {
			
			comps[i] = new boardComp("vertical");
			comps[i].setLocation(300 * i - (i * 2) + componentOffset, 0 + componentOffset - 10);
			panel.add(comps[i]);
			
		}
		
	}
	
	public void addNumberCell() {
		
		/* Adds the cells to the board */
		for(int i = 0; i < board.length; i++) {
			
			for(int j = 0; j < board.length; j++) {
				
				board[i][j] = new Cell(0);
				board[i][j].setLocation(2 + j * 33 + componentOffset, 2 + i * 33 + componentOffset - 10);
				panel.add(board[i][j]);
				board[i][j].setEnabled(false);
				
				int k = i;
				int l = j;
				
				board[i][j].addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						board[k][l].setInputting(true);
						board[k][l].setInputtingNum(selectedNum);
						
						if(selectedNum == board[k][l].getNumber()) {
							
							board[k][l].setIncorrect(false);
							board[k][l].setSolved(true);
							board[k][l].setCorrect(true);
							checkNumFinish();
							if(checkWin()) {
								
								gameWon();
								
							}
							
						} else {
							
							incorrectCount++;
							board[k][l].setIncorrect(true);
							
						}
						
					}
					
				});
				
			}
			
		}
		
		/* 
		 * Generates a valid sudoku board and assigns each cell their respective number. 
		 * 
		 * If the program becomes stuck at one cell because it cannot input a number there, then it
		 * will reset that whole row to 0's and attempt to redo that row. 
		 * 
		 * If the program has to redo the same row more than 5 times, then it will generate a completely new board. 
		 * 
		 */
		
		boolean failed = false;
		int failedAmount = 0;
		for(int i = 0; i < board.length; i++) {
			
			ArrayList<Integer> availableNums = new ArrayList<Integer>();
			
			for(int j = 0; j < 9; j++) {
				
				availableNums.add(j + 1);
				
			}
						
			for(int j = 0; j < board.length; j++) {
				
				int pos = 0;
				int num = 0;
				boolean okay = false;
				boolean stop = false;
				int falseCount = 0;
				while(!okay) {
					
					pos = generator.nextInt(availableNums.size());
					num = availableNums.get(pos);
					
					okay = checkDuplicate(num, i, j);
					if(!okay) {
						
						falseCount++;
						
					}
					
					if(falseCount > 10) {
												
						failedAmount++;
						
						if(failedAmount > 5) {
											
							failedAmount = 0;
							stop = true;
							failed = true;
							i = board.length;
							break;
							
						}
						
						falseCount = 0;
						num = 0;
						for(int k = 0; k < board.length; k++) {
							
							board[i][k].setNumber(0);
							
						}
						i--;
						stop = true;
						break;
						
					}
					
				}
				
				if(stop) {
					
					break;
					
				}

				board[i][j].setNumber(num);
				availableNums.remove(pos);

			}
			
		}
		
		if(failed) {
			
			for(int i = 0; i < board.length; i++) {
				
				for(int j = 0; j < board.length; j++) {
					
					board[i][j].setEnabled(false);
					board[i][j].setVisible(false);
					panel.remove(board[i][j]);
					board[i][j] = null;
					
				}
				
			}
			
			addNumberCell();
			
		} else {
			
			/* Creates user input buttons from 1 - 9. */
			
			for(int i = 0; i < 9; i++) {
				
				inputButtons[i] = new numberButton(i + 1);
				inputButtons[i].setLocation(12 + (40 * i), 300 + componentOffset);
				panel.add(inputButtons[i]);
				
				int k = i;

				inputButtons[i].addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						
						for(int j = 0; j < inputButtons.length; j++) {
							
							inputButtons[j].setSelected(false);
							
						}
						
						inputButtons[k].setSelected(true);
						selectedNum = inputButtons[k].getNumber();
						
						for(int i = 0; i < board.length; i++) {
							
							for(int j = 0; j < board.length; j++) {
								
								board[i][j].setCorrect(false);
								
							}
							
						}
						
						for(int i = 0; i < board.length; i++) {
							
							for(int j = 0; j < board.length; j++) {
								
								if(board[i][j].getNumber() == selectedNum && board[i][j].isSolved()) {
									
									board[i][j].setCorrect(true);
									
								}
								
							}
							
						}
						
					}
					
				});
				
			}
			
			/* Creates a button that resets all incorrect cells to normal */
			
			JButton resetWrong = new JButton("Reset Wrong Answers");
			resetWrong.setSize((panel.getWidth() / 2) - 7, 25);
			resetWrong.setLocation((panel.getWidth() / 2) + 3, inputButtons[0].getY() + inputButtons[0].getHeight() + 5);
			panel.add(resetWrong);
			
			resetWrong.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					for(int i = 0; i < board.length; i++) {
						
						for(int j = 0; j < board.length; j++) {
							
							if(board[i][j].isIncorrect()) {
								
								board[i][j].setIncorrect(false);
								board[i][j].setInputting(false);
								board[i][j].repaint();
								
							}
							
						}
						
					}
					
				}
				
			});
			
			/* Creates a button that returns the user back to the main menu */
			
			JButton menuReturn = new JButton("Return to Menu");
			menuReturn.setSize(resetWrong.getSize());
			menuReturn.setLocation(5, resetWrong.getY());
			panel.add(menuReturn);
			
			menuReturn.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					updateTime.stop();
					updateIncorrect.stop();		
					
					frame.dispose();
					new game();
					
				}
				
			});
			
		}
				
	}
	
	/* Called when the user has solved the board */
	public void gameWon() {

		/* 
		 * Creates an entry in the "highscores" text file that lists certain values of the 
		 * played game (amount of incorrect guesses, time taken to complete, and the time and date of the entry) 
		 * 
		 */
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");
		LocalDateTime now = LocalDateTime.now();
		
		try {
			
			FileWriter highscoreWriter = new FileWriter(highscores, true);
			
			highscoreWriter.write(incorrectCount + " " + minutes + " " + seconds + " " + dateFormatter.format(now) + " " + difficulty + "\n");
			highscoreWriter.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		/*
		 * Lists the time taken and amount of incorrect guesses. 
		 * Also provides the user with the choice of whether to go back to the main menu or to exit 
		 * 
		 */
		
		JDialog wonFrame = new JDialog();
		JPanel wonPanel = new JPanel();
		
		wonFrame.setPreferredSize(new Dimension(250, 150));
		wonFrame.setResizable(false);
		wonFrame.setLocationRelativeTo(null);
		wonFrame.setTitle("You won!");
		
		wonPanel.setPreferredSize(wonFrame.getPreferredSize());
		wonPanel.setLayout(null);
		wonPanel.setVisible(true);
		wonFrame.getContentPane().add(wonPanel);
		wonFrame.pack();
		
		JLabel incorrect = new JLabel();
		
		if(incorrectCount == 1) {
			
			incorrect.setText(incorrectCount + " incorrect guess");
			
		} else {
			
			incorrect.setText(incorrectCount + " incorrect guesses");
			
		}
		
		incorrect.setSize(getCorrectSize(incorrect));
		incorrect.setLocation((wonPanel.getWidth() / 2) - (incorrect.getWidth() / 2), 20);
		incorrect.setVisible(true);
		
		JLabel time = new JLabel();
		
		if(seconds < 10) {
			
			time.setText(minutes + ":0" + seconds + " to complete");
			
		} else {
			
			time.setText(minutes + ":" + seconds + " to complete");
			
		}
		
		JButton menu = new JButton("Main Menu");
		menu.setSize((wonPanel.getWidth() / 2) - 7, 30);
		menu.setLocation(4, wonPanel.getHeight() - menu.getHeight() - 4);
		menu.setVisible(true);
		
		menu.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				updateTime.stop();
				updateIncorrect.stop();	
				
				frame.dispose();
				wonFrame.dispose();
				new game();
				
			}
			
		});
		
		JButton exit = new JButton("Exit");
		exit.setSize(menu.getSize());
		exit.setLocation(menu.getX() + exit.getWidth() + 6, menu.getY());
		exit.setVisible(true);
		
		exit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
				
			}
			
		});
		
		time.setSize(getCorrectSize(time));
		time.setLocation(incorrect.getX() + (incorrect.getWidth() / 2) - (time.getWidth() / 2), incorrect.getY() + incorrect.getHeight() + 5);
		time.setVisible(true);
		
		wonPanel.add(incorrect);
		wonPanel.add(time);
		wonPanel.add(menu);
		wonPanel.add(exit);
		
		wonFrame.setVisible(true);
		
	}
	
	/* Called every time a number is generated for the sudoku board */
	public boolean checkDuplicate(int number, int i, int j) {
		
		/* 
		 * Checks whether a number (provided in the parameter) that is about to be inputted into the sudoku board is 
		 * valid or not. If the same number appears in the same row, column, or 3x3 square, then the method will return false, otherwise true. 
		 */
		
		/* Checks column */
		for(int k = 0; k < board.length; k++) {
			
			if(board[i][k].getNumber() == number) {
				
				return false;
				
			}
			
		}
		
		/* Checks row */
		for(int k = 0; k < board.length; k++) {
			
			if(board[k][j].getNumber() == number) {
				
				return false;
				
			}
			
		}
		
		/* Checks 3x3 square */
		int sqrt = (int) Math.sqrt(board.length);
		int boxRowStart = i - (i % sqrt);
		int boxColStart = j - (j % sqrt);
		
		for(int k = boxRowStart; k < boxRowStart + sqrt; k++) {
			
			for(int l = boxColStart; l < boxColStart + sqrt; l++) {
				
				if(board[k][l].getNumber() == number) {
					
					return false;
					
				}
				
			}
			
		}
		
		return true;
		
	}
	
	public void removeNumbers() {
		
		/* Hides the number of various cells on the sudoku board */
		
		int x = 0;
		int y = 0;
		
		for(int i = 0; i < difficultyCount; i++) {
			
			do {
				
				x = generator.nextInt(9);
				y = generator.nextInt(9);
				
			} while (!board[x][y].isSolved());
			
			board[x][y].setSolved(false);
			
		}
		
	}
	
	public boolean checkWin() {
		
		/* Checks if the board is solved */
		
		for(int i = 0; i < board.length; i++) {
			
			for(int j = 0; j < board.length; j++) {
				
				if(board[i][j].isSolved() == false) {
					
					return false;
					
				}
				
			}
			
		}
		
		return true;
		
	}
	
	public void addInfo() {
		
		/* Adds the timer and the incorrect counter on the sudoku board's HUD */
		
		JLabel time = new JLabel();
		JLabel incorrect = new JLabel();
		
		time.setVisible(true);
		incorrect.setVisible(true);
		panel.add(time);
		panel.add(incorrect);
		
		updateTime = new Thread() {
			
			public void run() {
				
				boolean running = true;
				while(running) {
					
					if(seconds < 10) {
						
						time.setText("Time: " + minutes + ":0" + seconds);
						
					} else {
						
						time.setText("Time: " + minutes + ":" + seconds);
						
					}
					
					time.setSize(getCorrectSize(time));
					time.setLocation((width / 11) - (((int) getCorrectSize(time).getWidth()) / 2) + 5, 5);
					
					try {
						
						Thread.sleep(1000);
						
					} catch (InterruptedException e) {
						
						e.printStackTrace();
						
					}
					
					seconds++;
					if(seconds > 59) {
						
						minutes++;
						seconds = 0;
						
					}
					
				}
				
			}
			
		};
		
		updateIncorrect = new Thread() {
			
			public void run() {
				
				boolean running = true;
				while(running) {
					
					incorrect.setText("Incorrect: " + incorrectCount);
					incorrect.setSize(getCorrectSize(incorrect));
					incorrect.setLocation((int) (335 - (getCorrectSize(incorrect).getWidth() / 2)), 5);
					
					try {
						
						Thread.sleep(100);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
						
					}
					
				}
				
			}
			
		};
		
		updateTime.start();
		updateIncorrect.start();
		
	}
	
	public void checkNumFinish() {
		
		/* 
		 * Checks if the current number being inputted is finished, meaning that all of the cells with this number
		 * have already been solved. If this is the case, then the number input button is hidden and disabled. 
		 * 
		 */
		
		int count = 0;
		
		for(int i = 0; i < board.length; i++) {
			
			for(int j = 0; j < board.length; j++) {
				
				if(board[i][j].getNumber() == selectedNum && board[i][j].isSolved()) {
					
					count++;
					
				}
				
			}
			
		}
		
		if(count == 9) {
			
			for(int i = 0; i < inputButtons.length; i++) {
				
				if(selectedNum == inputButtons[i].getNumber()) {
					
					inputButtons[i].setEnabled(false);
					inputButtons[i].setVisible(false);
					
				}
			
			}
			
		}
		
	}
	
	public void checkInitialNumFinish() {
		
		/* 
		 * In rare cases, before the sudoku board is even presented to the user, a number may already be finished. 
		 * This checks if this is the case and hides the number input buttons accordingly. 
		 * 
		 */
		
		for(int i = 1; i < 10; i++) {
			
			int count = 0;
			
			for(int j = 0; j < board.length; j++) {
				
				for(int k = 0; k < board.length; k++) {
					
					if(board[j][k].getNumber() == i && board[j][k].isSolved()) {
						
						count++;
						
					}
					
				}
				
			}
			
			if(count == 9) {
				
				inputButtons[i - 1].setEnabled(false);
				inputButtons[i - 1].setVisible(false);
				
			}
			
		}
		
	}
	
	public Dimension getCorrectSize(JLabel label) {
		
		/* Returns the minimum size a JLabel needs in order for all of its text to be readable */
		
		AffineTransform at = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(at, true, true);
		
		int width = (int) (label.getFont().getStringBounds(label.getText(), frc).getWidth()) + 5;
		int height = (int) (label.getFont().getStringBounds(label.getText(), frc).getHeight());
		
		return new Dimension(width, height);
		
	}
	
	public static void main(String[] args) {
		
		game game = new game();		
		
	}
	
}
