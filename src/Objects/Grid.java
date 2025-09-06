package Objects;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Grid extends Drawable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/* BLOCK CODES ==
	 * 0 = NOTHING
	 * 1 = SOLID
	 * 2 = PLAYER
	 * 3 = GOAL
	 * 4 = SEMISOLID TRANSPARENT
	 * 5 = SEMISOLID SOLID
	 * 6 = SPOT
	 * 7 = SPOT WITH PLAYER
	 * 
	 */
	
	int size;
	int[][] grid;
	String name = "";
	transient int offsetX;
	transient int offsetY;
	transient int cameraMovingX;
	transient int cameraMovingY;
	transient boolean alreadyHasImages = false;
	int playerX = -1;
	int playerY = -1;
	transient boolean playing = false;
	int startX = -1;
	int startY = -1;
	
	transient Image solid;
	transient Image player;
	transient Image goal;
	transient Image semisolid;
	transient Image spot;
	
	transient float deathState = 0;
	transient float winState = 0;
	transient boolean won = false;
	
	public Grid(int size, boolean playing) {
		this.size = size;
		grid = new int[size][size];
		this.playing = playing;
		
		try {
			solid = ImageIO.read(new File("media/images/blocks/solid.png"));
			player = ImageIO.read(new File("media/images/blocks/player.png"));
			goal = ImageIO.read(new File("media/images/blocks/goal.png"));
			semisolid = ImageIO.read(new File("media/images/blocks/semisolid.png"));
			spot = ImageIO.read(new File("media/images/blocks/spot.png"));
			alreadyHasImages = true;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	@Override
	public void draw(Graphics g) {
		if (!alreadyHasImages) {
			try {
				solid = ImageIO.read(new File("media/images/blocks/solid.png"));
				player = ImageIO.read(new File("media/images/blocks/player.png"));
				goal = ImageIO.read(new File("media/images/blocks/goal.png"));
				semisolid = ImageIO.read(new File("media/images/blocks/semisolid.png"));
				spot = ImageIO.read(new File("media/images/blocks/spot.png"));
				alreadyHasImages = true;
				if (playing) {
					if (playerX > 5 && playerX <= size - 5) {
						offsetX = playerX * 80 + 40 - 400;
					} else if (playerX <= 5) {
						offsetX = 0;
					} else if (playerX > size - 5) {
						offsetX = size*80 - 800;
					}
					if (playerY > 5 && playerY <= size - 5) {
						offsetY = playerY * 80 + 40 - 400;
					} else if (playerY <= 5) {
						offsetY = 0;
					} else if (playerY > size - 5) {
						offsetY = size*80 - 800;
					}
					adjustCameraMoving();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		//MOVING CAMERA TOWARDS PLAYER
		if (playing) {
			if (playerX + cameraMovingX > 5 && playerX + cameraMovingX < size - 6) {
				offsetX += ((playerX*80 + cameraMovingX*80 - 360) - offsetX)/5;
			} else if (playerX + cameraMovingX <= 5) {
				offsetX += (0 - offsetX)/5;
			} else if (playerX + cameraMovingX >= size - 6) {
				offsetX += ((size*80 - 800) - offsetX)/5;
			}
			if (playerY + cameraMovingY > 5 && playerY + cameraMovingY < size - 6) {
				offsetY += ((playerY*80 + cameraMovingY*80 - 360) - offsetY)/5;
			} else if (playerY + cameraMovingY <= 5) {
				offsetY += (0 - offsetY)/5;
			} else if (playerY + cameraMovingY >= size - 6) {
				offsetY += ((size*80 - 800) - offsetY)/5;
			}
		}
		//DRAWING BLOCKS
		for (int drawX = offsetX / 80; drawX < offsetX / 80 + 11; drawX++) {
			for (int drawY = offsetY / 80; drawY < offsetY / 80 + 11; drawY++) {
				if (drawX >= size || drawY >= size) {
					continue;
				}
				if (grid[drawX][drawY] == 0) {
					continue;
				} else if (grid[drawX][drawY] == 1) {
					g.drawImage(solid, 560 + drawX * 80 - offsetX, 140 + drawY * 80 - offsetY, 560 + drawX * 80 - offsetX + 80, 140 + drawY * 80 - offsetY + 80, 0, 0, 8, 8, null);
				} else if (grid[drawX][drawY] == 2) {
					g.drawImage(player, 560 + drawX * 80 - offsetX, 140 + drawY * 80 - offsetY, 560 + drawX * 80 - offsetX + 80, 140 + drawY * 80 - offsetY + 80, 0, 0, 1, 1, null);
				} else if (grid[drawX][drawY] == 3) {
					g.drawImage(goal, 560 + drawX * 80 - offsetX, 140 + drawY * 80 - offsetY, 560 + drawX * 80 - offsetX + 80, 140 + drawY * 80 - offsetY + 80, 0, 0, 1, 1, null);
				} else if (grid[drawX][drawY] == 4) {
					g.drawImage(semisolid, 560 + drawX * 80 - offsetX, 140 + drawY * 80 - offsetY, 560 + drawX * 80 - offsetX + 80, 140 + drawY * 80 - offsetY + 80, 8, 0, 16, 8, null);
				} else if (grid[drawX][drawY] == 5) {
					g.drawImage(semisolid, 560 + drawX * 80 - offsetX, 140 + drawY * 80 - offsetY, 560 + drawX * 80 - offsetX + 80, 140 + drawY * 80 - offsetY + 80, 0, 0, 8, 8, null);
				} else if (grid[drawX][drawY] == 6) {
					g.drawImage(spot, 560 + drawX * 80 - offsetX, 140 + drawY * 80 - offsetY, 560 + drawX * 80 - offsetX + 80, 140 + drawY * 80 - offsetY + 80, 0, 0, 8, 8, null);
				}
				else if (grid[drawX][drawY] == 7) {
					g.drawImage(spot, 560 + drawX * 80 - offsetX, 140 + drawY * 80 - offsetY, 560 + drawX * 80 - offsetX + 80, 140 + drawY * 80 - offsetY + 80, 8, 0, 16, 8, null);
				}
			}
		}
		//DRAWING DEATH RED SCREEN
		if (deathState > 0) {			
			Graphics2D g2 = (Graphics2D) g;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, deathState));
			g2.drawImage(player, 560, 140, 1360, 940, 0, 0, 1, 1, null);
			deathState -= 0.01f;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
		//DRAWING WIN GREEN SCREEN
		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, winState));
		g2.drawImage(goal, 560, 140, 1360, 940, 0, 0, 1, 1, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		if (winState < 0.5f && won) {
			winState += 0.01f;
		}
	}
	public void setOffset(int x, int y) {
		offsetX = x;
		offsetY = y;
		return;
	}
	public void set(int x, int y, int block) {
		if (block == 2) {
			if (playerX != -1) {
				grid[playerX][playerY] = 0;
			}
			playerX = x;
			startX = x;
			playerY = y;
			startY = y;
		}
		if (x == playerX && y == playerY && block != 2) {
			playerX = -1;
			playerY = -1;
		}
		grid[x][y] = block;
		return;
	}
	public void setPlaying (boolean playing) {
		this.playing = playing;
		return;
	}
	public void moveRight() {
		if (playing) {
			for (int i = playerX; i < size; i++) {
				if (grid[i][playerY] == 0) {
					continue;
				} else if (grid[i][playerY] == 1) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerX = i - 1;
					if (grid[playerX][playerY] == 6) {
						grid[playerX][playerY] = 7;
					} else {
						grid[playerX][playerY] = 2;
					}
					adjustCameraMoving();
					return;
				} else if (grid[i][playerY] == 3) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playing = false;
					playerX = -1;
					playerY = -1;
					won = true;
					adjustCameraMoving();
					return;
				} else if (grid[i][playerY] == 4) {
					grid[i][playerY] = 5;
					if (i + 1 < size) {
						if (grid[i + 1][playerY] == 1 || grid[i + 1][playerY] == 5) {
							die();
							adjustCameraMoving();
							return;
						}
					}
					continue;
				} else if (grid[i][playerY] == 5) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerX = i - 1;
					if (grid[playerX][playerY] == 6) {
						grid[playerX][playerY] = 7;
					} else {
						grid[playerX][playerY] = 2;
					}
					adjustCameraMoving();
					return;
				} else if (grid[i][playerY] == 6) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerX = i;
					grid[playerX][playerY] = 7;
					adjustCameraMoving();
					return;
				}
			}
			die();
			adjustCameraMoving();
		}
	}
	public void moveDown() {
		if (playing) {
			for (int i = playerY; i < size; i++) {
				if (grid[playerX][i] == 0) {
					continue;
				} else if (grid[playerX][i] == 1) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerY = i - 1;
					if (grid[playerX][playerY] == 6) {
						grid[playerX][playerY] = 7;
					} else {
						grid[playerX][playerY] = 2;
					}
					adjustCameraMoving();
					return;
				} else if (grid[playerX][i] == 3) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playing = false;
					playerX = -1;
					playerY = -1;
					won = true;
					adjustCameraMoving();
					return;
				} else if (grid[playerX][i] == 4) {
					grid[playerX][i] = 5;
					if (i + 1 < size) {
						if (grid[playerX][i + 1] == 1 || grid[playerX][i + 1] == 5) {
							die();
							adjustCameraMoving();
							return;
						}
					}
					continue;
				} else if (grid[playerX][i] == 5) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerY = i - 1;
					if (grid[playerX][playerY] == 6) {
						grid[playerX][playerY] = 7;
					} else {
						grid[playerX][playerY] = 2;
					}
					adjustCameraMoving();
					return;
				} else if (grid[playerX][i] == 6) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerY = i;
					grid[playerX][playerY] = 7;
					adjustCameraMoving();
					return;
				}
			}
			die();
			adjustCameraMoving();
		}
	}
	public void moveLeft() {
		if (playing) {
			for (int i = playerX; i >= 0; i--) {
				if (grid[i][playerY] == 0) {
					continue;
				} else if (grid[i][playerY] == 1) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerX = i + 1;
					if (grid[playerX][playerY] == 6) {
						grid[playerX][playerY] = 7;
					} else {
						grid[playerX][playerY] = 2;
					}
					adjustCameraMoving();
					return;
				} else if (grid[i][playerY] == 3) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playing = false;
					playerX = -1;
					playerY = -1;
					won = true;
					adjustCameraMoving();
					return;
				} else if (grid[i][playerY] == 4) {
					grid[i][playerY] = 5;
					if (i - 1 >= 0) {
						if (grid[i - 1][playerY] == 1 || grid[i - 1][playerY] == 5) {
							die();
							adjustCameraMoving();
							return;
						}
					}
					continue;
				} else if (grid[i][playerY] == 5) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerX = i + 1;
					if (grid[playerX][playerY] == 6) {
						grid[playerX][playerY] = 7;
					} else {
						grid[playerX][playerY] = 2;
					}
					adjustCameraMoving();
					return;
				} else if (grid[i][playerY] == 6) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerX = i;
					grid[playerX][playerY] = 7;
					adjustCameraMoving();
					return;
				}
			}
			die();
			adjustCameraMoving();
		}
	}
	public void moveUp() {
		if (playing) {
			for (int i = playerY; i >= 0; i--) {
				if (grid[playerX][i] == 0) {
					continue;
				} else if (grid[playerX][i] == 1) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerY = i + 1;
					if (grid[playerX][playerY] == 6) {
						grid[playerX][playerY] = 7;
					} else {
						grid[playerX][playerY] = 2;
					}
					adjustCameraMoving();
					return;
				} else if (grid[playerX][i] == 3) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playing = false;
					playerX = -1;
					playerY = -1;
					won = true;
					adjustCameraMoving();
					return;
				} else if (grid[playerX][i] == 4) {
					grid[playerX][i] = 5;
					if (i - 1 >= 0) {
						if (grid[playerX][i - 1] == 1 || grid[playerX][i - 1] == 5) {
							die();
							adjustCameraMoving();
							return;
						}
					}
					continue;
				} else if (grid[playerX][i] == 5) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerY = i + 1;
					if (grid[playerX][playerY] == 6) {
						grid[playerX][playerY] = 7;
					} else {
						grid[playerX][playerY] = 2;
					}
					adjustCameraMoving();
					return;
				} else if (grid[playerX][i] == 6) {
					if (grid[playerX][playerY] == 7) {
						grid[playerX][playerY] = 6;
					} else {
						grid[playerX][playerY] = 0;
					}
					playerY = i;
					grid[playerX][playerY] = 7;
					adjustCameraMoving();
					return;
				}
			}
			die();
			adjustCameraMoving();
		}
	}
	public void die() {
		//THIS METHOD KILLS THE PLAYER
		if (grid[playerX][playerY] == 7) {
			grid[playerX][playerY] = 6;
		} else {
			grid[playerX][playerY] = 0;
		}
		playerX = startX;
		playerY = startY;
		if (grid[playerX][playerY] == 6) {
			grid[playerX][playerY] = 7;
		} else {
			grid[playerX][playerY] = 2;
		}
		deathState = 0.5f;
	}
	public void addCameraMovingX(int x) {
		if (x == 1) {
			if (cameraMovingX + playerX < size - 6) {
				cameraMovingX += x;							
			}
		} else if (x == -1) {
			if (cameraMovingX + playerX > 5) {
				cameraMovingX += x;										
			}
		}
	}
	public void addCameraMovingY (int y) {
		if (y == 1) {
			if (cameraMovingY + playerY < size - 6) {
				cameraMovingY += y;							
			}
		} else if (y == -1) {
			if (cameraMovingY + playerY > 5) {
				cameraMovingY += y;										
			}
		}
	}
	public int getSize() {
		return size;
	}
	public String getName() {
		return name;
	}
	public void setName(String a) {
		name = a;
	}
	public void adjustCameraMoving() {
		if (playerX <= 5) {
			cameraMovingX = 5 - playerX;
		} else if (playerX > size - 5) {
			cameraMovingX = 5 - (size - playerX);
		} else {
			cameraMovingX = 0;
		}
		if (playerY <= 5) {
			cameraMovingY = 5 - playerY;
		} else if (playerY > size - 5) {
			cameraMovingY = 5 - (size - playerY);
		} else {
			cameraMovingY = 0;
		}
	}
}
