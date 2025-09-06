package Objects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import startup.Camera;

public class Menu extends Drawable implements MouseListener, KeyListener{
	
	/*
	 * MENUSTATE THING:
	 * 
	 * 0 = WON'T WORK / WON'T SHOW
	 * 1 = MAIN MENU STATE
	 * 2 = MAP CREATOR STATE
	 * 3 = IN MAP CREATOR STATE
	 * 4 = SAVING MAP
	 * 5 = PRESSED PLAY
	 * 6 = PLAYING
	 * 
	 */
	private int menustate = 1;
	int pointerX;
	int pointerY;
	int size = 10;
	Grid currentGrid;
	Background currentbg;
	boolean scrolling = false;
	int scrollingCenterX;
	int scrollingCenterY;
	int lastLevelPage = 0;
	int selectedBlock = 0;
	int gridOffsetX = 0;
	int gridOffsetY = 0;
	String currentName = "";
	char[] numberlist = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	ObjectInputStream in;
	ObjectOutputStream out;
	List<String> levelNames = new ArrayList<String>();
	int levelPage = 0;
	int nameIndex;
	int levelsToDraw;
	
	Image exit;
	Image play;
	Image mapeditor;
	Image options;
	Image back;
	Image plusminus;
	Image numbers;
	Image go;
	Image grid;
	Image save;
	Image erasemenu;
	Image solidmenu;
	Image playermenu;
	Image goalmenu;
	Image textfield;
	Image load;
	Image updown;
	Image semisolidmenu;
	Image spotmenu;
	
	
	public Menu() {
		Camera.addToDraw(this, 4);
		try {
			exit = ImageIO.read(new File("media/images/menu/exit.png"));
			play = ImageIO.read(new File("media/images/menu/play.png"));
			mapeditor = ImageIO.read(new File("media/images/menu/mapeditor.png"));
			options = ImageIO.read(new File("media/images/menu/options.png"));
			back = ImageIO.read(new File("media/images/menu/back.png"));
			plusminus = ImageIO.read(new File("media/images/menu/plusminus.png"));
			numbers = ImageIO.read(new File("media/images/menu/numbers.png"));
			go = ImageIO.read(new File("media/images/menu/go.png"));
			grid = ImageIO.read(new File("media/images/misc/grid.png"));
			save = ImageIO.read(new File("media/images/menu/save.png"));
			erasemenu = ImageIO.read(new File("media/images/menu/erasemenu.png"));
			solidmenu = ImageIO.read(new File("media/images/menu/solidmenu.png"));
			playermenu = ImageIO.read(new File("media/images/menu/playermenu.png"));
			semisolidmenu = ImageIO.read(new File("media/images/menu/semisolidmenu.png"));
			goalmenu = ImageIO.read(new File("media/images/menu/goalmenu.png"));
			textfield = ImageIO.read(new File("media/images/menu/textfield.png"));
			load = ImageIO.read(new File("media/images/menu/load.png"));
			updown = ImageIO.read(new File("media/images/menu/updown.png"));
			spotmenu = ImageIO.read(new File("media/images/menu/spotmenu.png"));
		} catch (IOException e) {
			System.exit(1);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if (menustate == 4) {
			if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' ||
			c == '6' || c == '7' || c == '8' || c == '9') {
				if (currentName.toCharArray().length < 10) {
					currentName = currentName + Character.toString(e.getKeyChar());
				}
			}
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (menustate == 4 && currentName.length() > 0 && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			currentName = currentName.substring(0, currentName.length() - 1);
		} else if (menustate == 6) {
			// PLAYER MOVING
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				currentGrid.moveRight();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				currentGrid.moveDown();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				currentGrid.moveLeft();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				currentGrid.moveUp();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
			//CAMERA MOVING
				currentGrid.addCameraMovingY(-1);
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_A) {
				currentGrid.addCameraMovingX(-1);
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				currentGrid.addCameraMovingY(1);
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				currentGrid.addCameraMovingX(1);
				return;
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		if (menustate == 1) {
			//MAIN MENU
			if (e.getButton() == 1 && e.getX() >= 1850 && e.getX() <= 1900 && e.getY() >= 20 && e.getY() <= 70) {
				//PRESSED EXIT
				System.exit(0);
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 335 && e.getY() <= 435) {
				//PRESSED MAP EDITOR
				levelNames = Arrays.asList(new File("levels/custom").list());
				if (levelNames == null) {
					return;
				}
				menustate = 2;
				lastLevelPage = levelNames.size()/8;
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 135 && e.getY() <= 235) {
				//PRESSED PLAY
				levelNames = Arrays.asList(new File("levels/custom").list());
				if (levelNames == null) {
					return;
				}
				menustate = 5;
				lastLevelPage = levelNames.size()/8;
			}
		} else if (menustate == 2) {					
			//MAP CREATOR STATE
			if (e.getButton() == 1 && e.getX() >= 1850 && e.getX() <= 1900 && e.getY() >= 20 && e.getY() <= 70) {
				System.exit(0);																									
				//PRESSED EXIT
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 135 && e.getY() <= 235) {
				menustate = 1;																									
				//PRESSED BACK
				size = 10;
			} else if (e.getButton() == 1 && e.getX() >= 80 && e.getX() <= 130 && e.getY() >= 535 && e.getY() <= 585) {			
				//BIG MINUS
				if (size >= 20) {
					size = size - 10;
				} else {
					size = 10;
				}
			} else if (e.getButton() == 1 && e.getX() >= 180 && e.getX() <= 230 && e.getY() >= 535 && e.getY() <= 585) {		
				// MINUS
				if (size > 10) {
					size--;
				}
			} else if (e.getButton() == 1 && e.getX() >= 430 && e.getX() <= 480 && e.getY() >= 535 && e.getY() <= 585) {		
				//BIS PLUS
				if (size <= 90) {
					size = size + 10;
				} else {
					size = 100;
				}
			} else if (e.getButton() == 1 && e.getX() >= 330 && e.getX() <= 380 && e.getY() >= 535 && e.getY() <= 585) {		
				//PLUS
				if (size < 100) {
					size++;
				}
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 685 && e.getY() <= 785) {		
				//GO
				menustate = 3;
				currentGrid = new Grid(size, false);
				Camera.addToDraw(currentGrid, 1);
				currentbg = new Background("bgeditor");
				Camera.addToDraw(currentbg, 0);
			} else if (e.getButton() == 1 && e.getX() >= 1615 && e.getX() <= 1665 && e.getY() >= 980 && e.getY() <= 1030 &&
			levelPage < levelNames.size() / 8) {
				//PRESSED DOWN
				levelPage++;
			} else if (levelPage > 0 && e.getButton() == 1 && e.getX() >= 1615 && e.getX() <= 1665 && e.getY() >= 50 && e.getY() <= 100) {
				//PRESSED UP
				levelPage--;
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 140 && e.getY() < 240 && levelsToDraw >= 1) {
				/*
				 * START CHECKING FOR LEVEL PRESSED FOR EDITING
				 */
				// PRESSED LEVEL IN POSITION 0
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(false);
					size = currentGrid.getSize();
					menustate = 3;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 240 && e.getY() < 340 && levelsToDraw >= 2) {
				//PRESSED LEVEL IN POSITION 1
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 1)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(false);
					size = currentGrid.getSize();
					menustate = 3;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 340 && e.getY() < 440 && levelsToDraw >= 3) {
				//PRESSED LEVEL IN POSITION 2
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 2)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(false);
					size = currentGrid.getSize();
					menustate = 3;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 440 && e.getY() < 540 && levelsToDraw >= 4) {
				//PRESSED LEVEL IN POSITION 3
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 3)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(false);
					size = currentGrid.getSize();
					menustate = 3;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 540 && e.getY() < 640 && levelsToDraw >= 5) {
				//PRESSED LEVEL IN POSITION 4
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 4)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(false);
					size = currentGrid.getSize();
					menustate = 3;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 640 && e.getY() < 740 && levelsToDraw >= 6) {
				//PRESSED LEVEL IN POSITION 5
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 5)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(false);
					size = currentGrid.getSize();
					menustate = 3;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 740 && e.getY() < 840 && levelsToDraw >= 7) {
				//PRESSED LEVEL IN POSITION 6
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 6)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(false);
					size = currentGrid.getSize();
					menustate = 3;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 840 && e.getY() < 940 && levelsToDraw >= 8) {
				//PRESSED LEVEL IN POSITION 7 (LAST ONE)
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 7)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(false);
					size = currentGrid.getSize();
					menustate = 3;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			}
		} else if (menustate == 3) {
			/*
			 * CREATING MAP STATE
			 */
			if (e.getButton() == 1 && e.getX() >= 1850 && e.getX() <= 1900 && e.getY() >= 20 && e.getY() <= 70 && !scrolling) {
				System.exit(0);																									//PRESSED EXIT
			} else if (e.getButton() == 3 && e.getX() >= 560 && e.getX() <= 1360 && e.getY() >= 140 && e.getY() <= 940) {
				scrolling = true;
				scrollingCenterX = e.getX();
				scrollingCenterY = e.getY();
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 135 && e.getY() <= 235 && !scrolling) {
				//PRESSED BACK
				menustate = 2;
				Camera.removeToDraw(1);
				currentGrid = null;
				scrollingCenterX = 0;
				scrollingCenterY = 0;
				size = 10;
				Camera.removeToDraw(0);
				currentbg = null;
				gridOffsetX = 0;
				gridOffsetY = 0;
				selectedBlock = 0;
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 335 && e.getY() <= 435 && !scrolling) {
				//PRESSED SAVE
				currentName = currentGrid.getName();
				menustate = 4;
			} else if (e.getButton() == 1 && e.getX() > 560 && e.getX() < 1360 && e.getY() > 140 && e.getY() < 940) {
				currentGrid.set((e.getX() - 560 + gridOffsetX) / 80, (e.getY() - 140 + gridOffsetY) / 80, selectedBlock);
				/*
				 * BLOCK IN MENU PRESSING
				 */
			} else if (e.getButton() == 1 && !scrolling && e.getX() >= 1390 && e.getX() <= 1490 && e.getY() >= 140 && e.getY() <= 290) {
				//PRESSED ERASEMENU
				selectedBlock = 0;
			} else if (e.getButton() == 1 && !scrolling && e.getX() >= 1590 && e.getX() <= 1690 && e.getY() >= 140 && e.getY() <= 290) {
				//PRESSED SOLIDMENU
				selectedBlock = 1;
			} else if (e.getButton() == 1 && !scrolling && e.getX() >= 1790 && e.getX() <= 1890 && e.getY() >= 140 && e.getY() <= 290) {
				//PRESSED PLAYERMENU
				selectedBlock = 2;
			} else if (e.getButton() == 1 && !scrolling && e.getX() >= 1390 && e.getX() <= 1490 && e.getY() >= 340 && e.getY() <= 490) {
				//PRESSED GOALMENU
				selectedBlock = 3;
			} else if (e.getButton() == 1 && !scrolling && e.getX() >= 1590 && e.getX() <= 1690 && e.getY() >= 340 && e.getY() <= 490) {
				//PRESSED SEMISOLID MENU
				selectedBlock = 4;
			} else if (e.getButton() == 1 && !scrolling && e.getX() >= 1790 && e.getX() <= 1890 && e.getY() >= 340 && e.getY() <= 490) {
				//PRESSED SPOTMENU
				selectedBlock = 6;
			}
		} else if (menustate == 4) {
			/*
			 * SAVING MAP
			 */
			if (e.getButton() == 1 && e.getX() >= 1850 && e.getX() <= 1900 && e.getY() >= 20 && e.getY() <= 70) {
				//PRESSED EXIT
				System.exit(0);
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 450 && e.getY() <= 550) {
				//PRESSED BACK
				currentName = currentGrid.getName();
				menustate = 3;
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 250 && e.getY() <= 350) {
				//PRESSED SAVE (ACTUALLY SAVING NOW)
				if (currentName.length() == 0) {
					return;
				}
				try {
					currentGrid.setName(currentName);
					out = new ObjectOutputStream(new FileOutputStream("levels/custom/" + currentName + ".txt"));
					out.writeObject(currentGrid);
					out.flush();
					out.close();
					menustate = 3;
				} catch (Exception e1) {
					e1.printStackTrace();
					System.exit(1);
				}
			}
		} else if (menustate == 5) {
			/*
			 * IN PLAY MENU
			 */
			if (e.getButton() == 1 && e.getX() >= 1850 && e.getX() <= 1900 && e.getY() >= 20 && e.getY() <= 70) {
				//PRESSED EXIT
				System.exit(0);
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 135 && e.getY() <= 235) {
				//PRESSED BACK
				menustate = 1;
				levelNames = null;
				levelPage = 0;
			} else if (e.getButton() == 1 && e.getX() >= 1615 && e.getX() <= 1665 && e.getY() >= 980 && e.getY() <= 1030 &&
			levelPage < levelNames.size() / 8) {
				//PRESSED DOWN
				levelPage++;
			} else if (levelPage > 0 && e.getButton() == 1 && e.getX() >= 1615 && e.getX() <= 1665 && e.getY() >= 50 && e.getY() <= 100) {
				//PRESSED UP
				levelPage--;
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 140 && e.getY() < 240 && levelsToDraw >= 1) {
				/*
				 * START CHECKING FOR LEVEL PRESSED
				 */
				// PRESSED LEVEL IN POSITION 0
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(true);
					menustate = 6;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 240 && e.getY() < 340 && levelsToDraw >= 2) {
				//PRESSED LEVEL IN POSITION 1
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 1)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(true);
					menustate = 6;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 340 && e.getY() < 440 && levelsToDraw >= 3) {
				//PRESSED LEVEL IN POSITION 2
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 2)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(true);
					menustate = 6;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 440 && e.getY() < 540 && levelsToDraw >= 4) {
				//PRESSED LEVEL IN POSITION 3
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 3)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(true);
					menustate = 6;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 540 && e.getY() < 640 && levelsToDraw >= 5) {
				//PRESSED LEVEL IN POSITION 4
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 4)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(true);
					menustate = 6;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 640 && e.getY() < 740 && levelsToDraw >= 6) {
				//PRESSED LEVEL IN POSITION 5
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 5)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(true);
					menustate = 6;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 740 && e.getY() < 840 && levelsToDraw >= 7) {
				//PRESSED LEVEL IN POSITION 6
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 6)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(true);
					menustate = 6;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			} else if (e.getButton() == 1 && e.getX() >= 1390 && e.getX() <= 1890 && e.getY() > 840 && e.getY() < 940 && levelsToDraw >= 8) {
				//PRESSED LEVEL IN POSITION 7 (LAST ONE)
				try {
					in = new ObjectInputStream( new FileInputStream("levels/custom/" + levelNames.get(8 * levelPage + 7)));
					currentGrid = (Grid)in.readObject();
					currentGrid.setPlaying(true);
					menustate = 6;
					Camera.addToDraw(currentGrid, 1);
					currentbg = new Background("bgeditor");
					Camera.addToDraw(currentbg, 0);
				} catch (Exception e1) {
					e1.printStackTrace(); 
					System.exit(2);
				}
			}
		} else if (menustate == 6) {
			/*
			 * PLAYING
			 */
			if (e.getButton() == 1 && e.getX() >= 1850 && e.getX() <= 1900 && e.getY() >= 20 && e.getY() <= 70) {
				//PRESSED EXIT
				System.exit(0);
			} else if (e.getButton() == 1 && e.getX() >= 100 && e.getX() <= 460 && e.getY() >= 135 && e.getY() <= 235) {
				//PRESSED BACK
				Camera.removeToDraw(1);
				Camera.removeToDraw(0);
				menustate = 5;
				currentbg = null;
				currentGrid = null;
				levelPage = 0;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (menustate == 3) {
			if (e.getButton() == 3 && scrolling) {
				scrolling = false;
			}
		}
	}

	

	@Override
	public void draw(Graphics g) {
		pointerX = (int)MouseInfo.getPointerInfo().getLocation().getX();
		pointerY = (int)MouseInfo.getPointerInfo().getLocation().getY();
		if (menustate == 0) {
			return;
		} else if (menustate == 1) {															//MAIN MENU
			
			if (pointerX >= 1850 && pointerX <= 1900 && pointerY >= 20 && pointerY <= 70) {
				g.drawImage(exit, 1850, 20, 1900, 70, 50, 0, 100, 50, null);
			} else {
				g.drawImage(exit, 1850, 20, 1900, 70, 0, 0, 50, 50, null);
			}
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 135 && pointerY <= 235) {
				g.drawImage(play, 100, 135, 460, 235, 0, 100, 360, 200, null);
			} else {
				g.drawImage(play, 100, 135, 460, 235, 0, 0, 360, 100, null);
			}
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 335 && pointerY <= 435) {
				g.drawImage(mapeditor, 100, 335, 460, 435, 0, 100, 360, 200, null);
			} else {
				g.drawImage(mapeditor, 100, 335, 460, 435, 0, 0, 360, 100, null);
			}
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 535 && pointerY <= 635) {
				g.drawImage(options, 100, 535, 460, 635, 0, 100, 360, 200, null);
			} else {
				g.drawImage(options, 100, 535, 460, 635, 0, 0, 360, 100, null);
			}
		} else if (menustate == 2) {															//MAP EDITOR CHOOSING SETTINGS MENU
			
			if (pointerX >= 1850 && pointerX <= 1900 && pointerY >= 20 && pointerY <= 70) {
				g.drawImage(exit, 1850, 20, 1900, 70, 50, 0, 100, 50, null);
			} else {
				g.drawImage(exit, 1850, 20, 1900, 70, 0, 0, 50, 50, null);
			}
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 135 && pointerY <= 235) {
				g.drawImage(back, 100, 135, 460, 235, 0, 100, 360, 200, null);
			} else {
				g.drawImage(back, 100, 135, 460, 235, 0, 0, 360, 100, null);
			}
			if (pointerX >= 80 && pointerX <= 130 && pointerY >= 535 && pointerY <= 585) {		//BIG MINUS
				g.drawImage(plusminus, 80, 535, 130, 585, 150, 50, 200, 100, null);				//
			} else {																			//
				g.drawImage(plusminus, 80, 535, 130, 585, 150, 0, 200, 50, null);				//
			}
			if (pointerX >= 180 && pointerX <= 230 && pointerY >= 535 && pointerY <= 585) {		//MINUS
				g.drawImage(plusminus, 180, 535, 230, 585, 100, 50, 150, 100, null);			//
			} else {																			//
				g.drawImage(plusminus, 180, 535, 230, 585, 100, 0, 150, 50, null);				//
			}
			if (pointerX >= 430 && pointerX <= 480 && pointerY >= 535 && pointerY <= 585) {		//BIG PLUS
				g.drawImage(plusminus, 430, 535, 480, 585, 50, 50, 100, 100, null);				//
			} else {																			//
				g.drawImage(plusminus, 430, 535, 480, 585, 50, 0, 100, 50, null);				//
			}
			if (pointerX >= 330 && pointerX <= 380 && pointerY >= 535 && pointerY <= 585) {		//PLUS
				g.drawImage(plusminus, 330, 535, 380, 585, 0, 50, 50, 100, null);				//
			} else {																			//
				g.drawImage(plusminus, 330, 535, 380, 585, 0, 0, 50, 50, null);					//
			}
			//DRAWING NUMBERS
			g.drawImage(numbers, 205, 335, 255, 385, size/100*50, 0, size/100*50+50, 50, null);
			g.drawImage(numbers, 255, 335, 305, 385, size%100/10*50, 0, size%100/10*50+50, 50, null);
			g.drawImage(numbers, 305, 335, 355, 385, size%10*50, 0, size%10*50+50, 50, null);
			//DRAWING GO
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 685 && pointerY <= 785) {
				g.drawImage(go, 100, 685, 460, 785, 0, 100, 360, 200, null);
			} else {
				g.drawImage(go, 100, 685, 460, 785, 0, 0, 360, 100, null);
			}
			//DRAWING LEVELS AND THEIR SELECTION BOXES IN MAP EDITOR MODE
			try {
				levelsToDraw = 0;
				if (levelPage == lastLevelPage) {
					levelsToDraw = levelNames.size()%8;
				} else {
					levelsToDraw = 8;
				}
				for (nameIndex = levelPage*8; nameIndex < levelsToDraw + 8*levelPage; nameIndex++) {
					if (nameIndex >= levelNames.size()) {
						continue;
					}
					String name = levelNames.get(nameIndex);
					for (int i = 0; i < name.toCharArray().length - 4; i++) {
						g.drawImage(numbers, 1390 + 50*i, 165 + 100*nameIndex - 800*levelPage, 1440 + 50*i, 215 + 100*nameIndex - 800*levelPage, 
						Integer.parseInt(Character.toString(name.charAt(i))) * 50, 0, 
						Integer.parseInt(Character.toString(name.charAt(i))) * 50 + 50, 50, null);
					}
					if (pointerX >= 1390 && pointerX <= 1890 && pointerY > 140 + 100*nameIndex - 800*levelPage && pointerY < 240 + 100*nameIndex - 800*levelPage) {
						g.drawImage(textfield, 1390, 140 + 100*nameIndex - 800*levelPage, 1890, 240 + 100*nameIndex - 800*levelPage, 0, 0, 500, 100, null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(2);
			}
			//DRAWING UP BUTTOM
			if (levelPage > 0 && pointerX >= 1615 && pointerX <= 1665 && pointerY >= 50 && pointerY <= 100) {
				g.drawImage(updown, 1615, 50, 1665, 100, 0, 0, 50, 50, null);
			} else {
				g.drawImage(updown, 1615, 50, 1665, 100, 50, 0, 100, 50, null);
			}
			//DRAWING DOWN BUTTOM
			if (levelNames != null) {
				if (levelPage < levelNames.size() / 8 && pointerX >= 1615 && pointerX <= 1665 && pointerY >= 980 && pointerY <= 1030) {
					g.drawImage(updown, 1615, 980, 1665, 1030, 0, 50, 50, 100, null);
				} else {
					g.drawImage(updown, 1615, 980, 1665, 1030, 50, 50, 100, 100, null);
				}
			} else {
				g.drawImage(updown, 1615, 980, 1665, 1030, 50, 50, 100, 100, null);
			}
		} else if (menustate == 3) {			
			/*
			 *  IN MAP EDITOR
			 */
			if (pointerX >= 1850 && pointerX <= 1900 && pointerY >= 20 && pointerY <= 70) {
				g.drawImage(exit, 1850, 20, 1900, 70, 50, 0, 100, 50, null);
			} else {
				g.drawImage(exit, 1850, 20, 1900, 70, 0, 0, 50, 50, null);
			}
			if (scrolling) {																	// ADJUSTING GRID SCROLL
				gridOffsetX += scrollingCenterX - pointerX;										//
				scrollingCenterX = pointerX;													//
				gridOffsetY += scrollingCenterY - pointerY;										//
				scrollingCenterY = pointerY;													//
			}																					//
			if (gridOffsetX < 0) {																//
				gridOffsetX = 0;																//
			} else if (gridOffsetX + 800 > size * 80) {											//
				gridOffsetX = size * 80 - 800;													//
			}																					//
			if (gridOffsetY < 0) {																//
				gridOffsetY = 0;																//
			} else if (gridOffsetY + 800 > size * 80) {											//
				gridOffsetY = size * 80 - 800;													//
			}
			currentGrid.setOffset(gridOffsetX, gridOffsetY);
			g.drawImage(grid, 560, 140, 1360, 940, 80 + gridOffsetX % 80, 80 + gridOffsetY % 80, 880 + gridOffsetX % 80, 880 + gridOffsetY % 80, null);
			// DRAWING BACK BUTTOM
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 135 && pointerY <= 235 && !scrolling) {
				g.drawImage(back, 100, 135, 460, 235, 0, 100, 360, 200, null);
			} else {
				g.drawImage(back, 100, 135, 460, 235, 0, 0, 360, 100, null);
			}
			//DRAWING SAVE BUTTOM
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 335 && pointerY <= 435 && !scrolling) {
				g.drawImage(save, 100, 335, 460, 435, 0, 100, 360, 200, null);
			} else {
				g.drawImage(save, 100, 335, 460, 435, 0, 0, 360, 100, null);
			}
			/*
			 * MENUS
			 * 
			 * 0 = ERASE
			 * 1 = SOLID
			 * 2 = PLAYER
			 * 3 = GOAL
			 * 4 = SEMISOLID
			 * 5 = SOMISOLID SOLID (not placeable)
			 * 6 = SPOT
			 * 7 = SPOT WITH PLAYER
			 * 
			 */
			//DRAWING ERASEMENU BUTTOM
			if (selectedBlock == 0) {
				g.drawImage(erasemenu, 1390, 140, 1490, 290, 0, 0, 100, 150, null);
			} else {
				g.drawImage(erasemenu, 1390, 140, 1490, 290, 100, 0, 200, 150, null);
			}
			//DRAWING SOLIDMENU BUTTOM
			if (selectedBlock == 1) {
				g.drawImage(solidmenu, 1590, 140, 1690, 290, 0, 0, 100, 150, null);
			} else {
				g.drawImage(solidmenu, 1590, 140, 1690, 290, 100, 0, 200, 150, null);
			}
			//DRAWING PLAYERMENU BUTTOM
			if (selectedBlock == 2) {
				g.drawImage(playermenu, 1790, 140, 1890, 290, 0, 0, 100, 150, null);
			} else {
				g.drawImage(playermenu, 1790, 140, 1890, 290, 100, 0, 200, 150, null);
			}
			//DRAWING GOALMENU BUTTOM
			if (selectedBlock == 3) {
				g.drawImage(goalmenu, 1390, 340, 1490, 490, 0, 0, 100, 150, null);
			} else {
				g.drawImage(goalmenu, 1390, 340, 1490, 490, 100, 0, 200, 150, null);
			}
			//DRAWING SEMISOLIDMENU BUTTOM
			if (selectedBlock == 4) {
				g.drawImage(semisolidmenu, 1590, 340, 1690, 490, 0, 0, 100, 150, null);
			} else {
				g.drawImage(semisolidmenu, 1590, 340, 1690, 490, 100, 0, 200, 150, null);
			}
			//DRAWING SPOTMENU BUTTOM
			if (selectedBlock == 6) {
				g.drawImage(spotmenu, 1790, 340, 1890, 490, 0, 0, 100, 150, null);
			} else {
				g.drawImage(spotmenu, 1790, 340, 1890, 490, 100, 0, 200, 150, null);
			}
		} else if (menustate == 4) {
			/*
			 * SAVING MAP STATE
			 */
			//DRAWING EXIT
			if (pointerX >= 1850 && pointerX <= 1900 && pointerY >= 20 && pointerY <= 70) {
				g.drawImage(exit, 1850, 20, 1900, 70, 50, 0, 100, 50, null);
			} else {
				g.drawImage(exit, 1850, 20, 1900, 70, 0, 0, 50, 50, null);
			}
			//DRAWING TEXT FIELD
			g.drawImage(textfield, 30, 140, 530, 240, 0, 0, 500, 100, null);
			//DRAWING CURRENT NAME
			//NUMBERS ONLY
			for (int i = 0; i < currentName.toCharArray().length; i++) {
				g.drawImage(numbers, 30 + 50*i, 165, 80 + 50*i, 215, 
				Integer.parseInt(Character.toString(currentName.charAt(i))) * 50, 0, 
				Integer.parseInt(Character.toString(currentName.charAt(i))) * 50 + 50, 50, null);
			}
			//DRAWING SAVE BUTTOM
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 250 && pointerY <= 350) {
				g.drawImage(save, 100, 250, 460, 350, 0, 100, 360, 200, null);
			} else {
				g.drawImage(save, 100, 250, 460, 350, 0, 0, 360, 100, null);
			}
			//DRAWING BACK BUTTOM
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 450 && pointerY <= 550) {
				g.drawImage(back, 100, 450, 460, 550, 0, 100, 360, 200, null);
			} else {
				g.drawImage(back, 100, 450, 460, 550, 0, 0, 360, 100, null);
			}
		} else if (menustate == 5) {
			/*
			 * PRESSED PLAY MENU
			 */
			//DRAWING EXIT
			if (pointerX >= 1850 && pointerX <= 1900 && pointerY >= 20 && pointerY <= 70) {
				g.drawImage(exit, 1850, 20, 1900, 70, 50, 0, 100, 50, null);
			} else {
				g.drawImage(exit, 1850, 20, 1900, 70, 0, 0, 50, 50, null);
			}
			// DRAWING BACK BUTTOM
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 135 && pointerY <= 235) {
				g.drawImage(back, 100, 135, 460, 235, 0, 100, 360, 200, null);
			} else {
				g.drawImage(back, 100, 135, 460, 235, 0, 0, 360, 100, null);
			}
			//DRAWING UP BUTTOM
			if (levelPage > 0 && pointerX >= 1615 && pointerX <= 1665 && pointerY >= 50 && pointerY <= 100) {
				g.drawImage(updown, 1615, 50, 1665, 100, 0, 0, 50, 50, null);
			} else {
				g.drawImage(updown, 1615, 50, 1665, 100, 50, 0, 100, 50, null);
			}
			//DRAWING DOWN BUTTOM
			if (levelNames != null) {
				if (levelPage < levelNames.size() / 8 && pointerX >= 1615 && pointerX <= 1665 && pointerY >= 980 && pointerY <= 1030) {
					g.drawImage(updown, 1615, 980, 1665, 1030, 0, 50, 50, 100, null);
				} else {
					g.drawImage(updown, 1615, 980, 1665, 1030, 50, 50, 100, 100, null);
				}
			} else {
				g.drawImage(updown, 1615, 980, 1665, 1030, 50, 50, 100, 100, null);
			}
			//DRAWING LEVELS AND THEIR SELECTION BOXES
			try {
				levelsToDraw = 0;
				if (levelPage == lastLevelPage) {
					levelsToDraw = levelNames.size()%8;
				} else {
					levelsToDraw = 8;
				}
				for (nameIndex = levelPage*8; nameIndex < levelsToDraw + 8*levelPage; nameIndex++) {
					if (nameIndex >= levelNames.size()) {
						continue;
					}
					String name = levelNames.get(nameIndex);
					for (int i = 0; i < name.toCharArray().length - 4; i++) {
						g.drawImage(numbers, 1390 + 50*i, 165 + 100*nameIndex - 800*levelPage, 1440 + 50*i, 215 + 100*nameIndex - 800*levelPage, 
						Integer.parseInt(Character.toString(name.charAt(i))) * 50, 0, 
						Integer.parseInt(Character.toString(name.charAt(i))) * 50 + 50, 50, null);
					}
					if (pointerX >= 1390 && pointerX <= 1890 && pointerY > 140 + 100*nameIndex - 800*levelPage && pointerY < 240 + 100*nameIndex - 800*levelPage) {
						g.drawImage(textfield, 1390, 140 + 100*nameIndex - 800*levelPage, 1890, 240 + 100*nameIndex - 800*levelPage, 0, 0, 500, 100, null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(2);
			}
		} else if (menustate == 6) {
			/*
			 * PLAYING
			 */
			//DRAWING EXIT
			if (pointerX >= 1850 && pointerX <= 1900 && pointerY >= 20 && pointerY <= 70) {
				g.drawImage(exit, 1850, 20, 1900, 70, 50, 0, 100, 50, null);
			} else {
				g.drawImage(exit, 1850, 20, 1900, 70, 0, 0, 50, 50, null);
			}
			// DRAWING BACK BUTTOM
			if (pointerX >= 100 && pointerX <= 460 && pointerY >= 135 && pointerY <= 235) {
				g.drawImage(back, 100, 135, 460, 235, 0, 100, 360, 200, null);
			} else {
				g.drawImage(back, 100, 135, 460, 235, 0, 0, 360, 100, null);
			}
		}
	}
}
