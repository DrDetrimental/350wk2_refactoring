package maze;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class Frame {

	private JFrame frame;
	private static int mazeHeight;
	private static int mazeWidth;
	private static int[][] maze;
	private static JLabel[][] spaces;
	private static boolean loop = true;
	private static char[] openDirections;
	private static int[] playerLocation;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		readMazeFile();
		getMazeInfo();
		Player player = new Player();
		createGameWindow();
		drawMaze();
		spaces[Maze.getEndCoords()[0]][Maze.getEndCoords()[1]].setText("F");
		
		// Game loop
		while (loop) {
			
			// Get the current location of the player; defaults to starting coordinates
			playerLocation = player.getPosition();
			// Check the available movement options
			openDirections = player.getMovementOptions();
			
			// Movement. Checks if the player is sitting on the end space, and if so, wins the game; if not, will
			// mark the space the player is in as "closed" and move into an available space in the order N, E, S, W
			for (int i = 0; i < openDirections.length; i++) {
				if (openDirections[i] != 'x') {
					
					// Mark the square the player is currently in as closed
					maze[playerLocation[0]][playerLocation[1]] = 0;
					spaces[playerLocation[0]][playerLocation[1]].setText("x");
					
					if (openDirections[i] == 'n') {
						System.out.println("Moving north");
						player.moveNorth();
						break;
					}
					if (openDirections[i] == 'e') {
						System.out.println("Moving east");
						player.moveEast();
						break;
					}
					if (openDirections[i] == 's') {
						System.out.println("Moving south");
						player.moveSouth();
						break;
					}
					if (openDirections[i] == 'w') {
						System.out.println("Moving west");
						player.moveWest();
						break;
					}
					
				} else {
					System.out.println("Player win!");
					loop = false;
				}
			}
			// Sets the player's new position as "O" to show where they are on the grid
			spaces[playerLocation[0]][playerLocation[1]].setText("O");
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void drawMaze() {
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				JLabel lblNewLabel;
				if (maze[y][x] == 0) {
					lblNewLabel = new JLabel("X");
				} else {
					lblNewLabel = new JLabel("");
				}
				spaces[y][x] = lblNewLabel;
			}
		}
	}

	private static void createGameWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame window = new Frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void getMazeInfo() {
		mazeHeight = Maze.getHeight();
		mazeWidth = Maze.getWidth();
		maze = Maze.getMaze();
		spaces = new JLabel[mazeHeight][mazeWidth];
	}

	private static void readMazeFile() {
		try {
			Maze.readFile("maze.txt");
		} catch (Exception e) {
			System.out.println("Something went wrong reading the file, is the filename right?");
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public Frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 650, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(10, 10, 0, 0)); // the maze height/width variables throw exceptions when going to the design tab; change them to arbitrary numbers to solve it
		
		System.out.println("Initializing window");
		
		// Creates the spaces[][] array, a frontend "mirror" of the backend maze[][] array. The text/titles of the JLabels in this array can be changed to update what is shown to the user.
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				frame.getContentPane().add(spaces[y][x]);
			}
		}
	}
}
