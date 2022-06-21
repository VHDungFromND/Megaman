package gameobjects;

import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import effect.CacheDataLoader;
import userinterface.GameFrame;
import effect.FrameImage;
import java.awt.Color;
import java.awt.Font;

public class GameWorld {
	
    private BufferedImage bufferedImage;
    private long blinkTime=0;
    Color color = new Color(224,60,60,255);
    public ParticularObjectManager particularObjectManager;
    public BulletManager bulletManager;

    public Naruto naruto;
    public Gaara gaara;
    
    public PhysicalMap physicalMap;
    public static boolean isGameOver=false;
    public static final int finalBossX = 3600;
    
    // public static final int INIT_GAME = 0;
    public static final int TUTORIAL = 1;
    public static final int GAMEPLAY = 2;
    public static final int GAMEOVER = 3;
    public static final int GAMEWIN = 4;
    public static final int PAUSEGAME = 0;
    public static final int GAMELOOP = 5;
    public static int Level=0;
    public static final int INTROGAME = 0;
    public static final int MEETFINALBOSS = 1;
    public static boolean isInitEnemy=false;
    
    public int openIntroGameY = 0;
    public int state = PAUSEGAME;
    public int previousState = state;
    public int tutorialState = INTROGAME;
    public String easyButtonState;
    public String hardButtonState;
    public int storyTutorial = 0;
    public String[] texts1 = new String[4];

    public String textTutorial;
    public int currentSize = 1;
    
    private boolean finalbossTrigger = true;
    ParticularObject boss;
    
    FrameImage avatar = CacheDataLoader.getInstance().getFrameImage("Avatar");
    
    
    private int numberOfLife = 0;
    
    public AudioClip bgMusic;
    
    public GameWorld(){
        
        texts1[0] = "We are heros, and our mission is protecting our Home\nEarth....";
        texts1[1] = "There was a Monster from University on Earth in 10 years\n"
                + "and we lived in the scare in that 10 years....";
        texts1[2] = "Choose level pleasee";
        texts1[3] = "      LET'S GO!.....";
        textTutorial = texts1[0];

        
        bufferedImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        naruto = new Naruto(400, 400, this);
        gaara = new Gaara(800, 400, this);
        physicalMap = new PhysicalMap(0, 0, this);
        bulletManager = new BulletManager(this);
        
        particularObjectManager = new ParticularObjectManager(this);
        particularObjectManager.addObject(naruto);
        particularObjectManager.addObject(gaara);
        
        bgMusic = CacheDataLoader.getInstance().getSound("bgmusic");
        
    }
    
    private void initEnemies(){

        ParticularObject darkraise = new DarkRaise(656, 210, this);
        darkraise.setTeamType(ParticularObject.BOT_TEAM);
        particularObjectManager.addObject(darkraise);
        
    }

    public void switchState(int state){
        previousState = this.state;
        this.state = state;
    }
    
    private void TutorialUpdate(){
        switch(tutorialState){
            case INTROGAME:
                
                if(storyTutorial == 0){
                    if(openIntroGameY < 450) {
                        openIntroGameY+=4;
                    }else storyTutorial ++;
                    
                }else{
                
                    if(currentSize < textTutorial.length()) currentSize++;
                }
                break;
        }
    }
    
    private void drawString(Graphics2D g2, String text, int x, int y){
        for(String str : text.split("\n"))
            g2.drawString(str, x, y+=g2.getFontMetrics().getHeight());
    }
    
    private void TutorialRender(Graphics2D g2){
        switch(tutorialState){
            case INTROGAME:
                int yMid = GameFrame.SCREEN_HEIGHT/2 - 15;
                int y1 = yMid - GameFrame.SCREEN_HEIGHT/2 - openIntroGameY/2;
                int y2 = yMid + openIntroGameY/2;

                g2.setColor(Color.BLACK);
                g2.fillRect(0, y1, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT/2);
                g2.fillRect(0, y2, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT/2);
                
                if(storyTutorial >= 1){
                    g2.drawImage(avatar.getImage(), 700, 379, null);
                    g2.setColor(Color.white);
                    g2.fillRect(380, 450, 350, 80);
                    g2.setColor(Color.BLACK);
                    String text = textTutorial.substring(0, currentSize - 1);
                    drawString(g2, text, 390, 475);
                }

                if(storyTutorial==3) {
                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage(easyButtonState).getImage(), 510, 465, null);
                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage(hardButtonState).getImage(), 620, 465, null);
                }
                
                break;
        }
    }
    
    public void Update(){
        if(System.currentTimeMillis()-blinkTime>500 && System.currentTimeMillis()-blinkTime<1000) {
            color = new Color(0,0,0,255);
        } else if(System.currentTimeMillis()-blinkTime<500) {
            color = new Color(224,60,60,255);
        } else {
            blinkTime=System.currentTimeMillis();
        }
        if(Level!=1) {
            easyButtonState="EasyDisabled";
        } else easyButtonState="Easy";
        if(Level!=2) {
            hardButtonState="HardDisabled";
        } else hardButtonState="Hard";
        switch(state){
            // case INIT_GAME:
                
            //     break;

            case PAUSEGAME:
                bufferedImage.getGraphics().drawImage(CacheDataLoader.getInstance().getFrameImage("Intro").getImage(), 0,0, null);
                break;
            case TUTORIAL:
                TutorialUpdate();
                
                break;
            case GAMEPLAY:
                if(!isInitEnemy && Level==2) {
                    isInitEnemy=true;
                    initEnemies();
                }
                particularObjectManager.UpdateObject();
                bulletManager.UpdateObject();
        
                physicalMap.Update();
                
                
                
                if(naruto.getState() == ParticularObject.DEATH || gaara.getState() ==ParticularObject.DEATH){
                    numberOfLife --;
                    if(numberOfLife >= 0){
                        naruto.setBlood(100);
                        naruto.setPosY(naruto.getPosY() - 50);
                        naruto.setState(ParticularObject.NOTBEHURT);
                        particularObjectManager.addObject(naruto);
                    }else{
                        if(isGameOver && (System.currentTimeMillis()-((naruto.timeDeath>gaara.timeDeath)?naruto.timeDeath:gaara.timeDeath)>5000)) {
                            switchState(GAMEOVER);
                            bgMusic.stop();
                        }
                        
                    }
                }
                if(!finalbossTrigger && boss.getState() == ParticularObject.DEATH)
                    switchState(GAMEWIN);
                
                break;
            case GAMEOVER:
                // switchState(PAUSEGAME);
                break;
            case GAMEWIN:
                
                break;
            case GAMELOOP:
                break;
        }
        

    }

    public void Render(){

        Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
        g2.setColor(color);
        if(g2!=null){
            
            switch(state){
                // case INIT_GAME:
                //     g2.setColor(Color.BLACK);
                //     g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
                //     g2.setColor(Color.WHITE);
                //     g2.drawString("PRESS ENTER TO CONTINUE", 400, 300);
                //     break;
                case PAUSEGAME:
                    // g2.setColor(Color.BLACK);
                    // g2.fillRect(0 , 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
                    // g2.setColor(Color.WHITE);
                    // // g2.drawImage(CacheDataLoader.getInstance().getFrameImage("Intro").getImage(), 0,0, null);
                    g2.setFont(new Font("SANS_SERIF", Font.BOLD, 20));
                    g2.drawString("PRESS ENTER TO CONTINUE", 520, 350);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
                    break;
                case TUTORIAL:
                    // backgroundMap.draw(g2);
                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage("Map").getImage(), 0,0, null);
                    // physicalMap.draw(g2);
                    if(tutorialState == MEETFINALBOSS){
                        particularObjectManager.draw(g2);
                    }
                    TutorialRender(g2);
                    
                    break;
                case GAMEWIN:
                case GAMEPLAY:
                    // backgroundMap.draw(g2);
                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage("Map").getImage(), 0,0, null);
                    // physicalMap.draw(g2);
                    particularObjectManager.draw(g2);  
                    bulletManager.draw(g2);
                    
                    g2.setColor(Color.GRAY);
                    g2.fillRect(19, 59, 306, 22);
                    g2.setColor(Color.red);
                    g2.fillRect(20, 60, (int)(naruto.getBlood()*3), 20);

                    g2.setColor(Color.GRAY);
                    g2.fillRect(954, 59, 306, 22);
                    g2.setColor(Color.red);
                    g2.fillRect(955, 60, (int)(gaara.getBlood()*3), 20);

                    g2.setColor(Color.GRAY);
                    g2.fillRect(19, 89, 306, 22);
                    g2.setColor(Color.blue);
                    g2.fillRect(20, 90, (int)(naruto.getMana()*3), 20);

                    g2.setColor(Color.GRAY);
                    g2.fillRect(954, 89, 306, 22);
                    g2.setColor(Color.blue);
                    g2.fillRect(955, 90, (int)(gaara.getMana()*3), 20);
                    
                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage(easyButtonState).getImage(), 585, 20, null);
                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage(hardButtonState).getImage(), 585, 77, null);
                    g2.setColor(Color.BLACK);
                    drawString(g2, "PRESS T TO CHANGE THE LEVEL", 545, 132);

                    g2.setColor(Color.white);
                    drawString(g2,"HP: "+Integer.toString(naruto.getBlood()), 25, 58);
                    drawString(g2, "MP: "+Integer.toString(naruto.getMana()), 25, 88);
                    drawString(g2,"HP: "+Integer.toString(gaara.getBlood()), 960, 58);
                    drawString(g2, "MP: "+Integer.toString(gaara.getMana()), 960, 88);

                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage("Logo1").getImage(), 340, 60, null);
                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage("Logo2").getImage(), 750, 60, null);
                    
                    
                    if(state == GAMEWIN){
                        // g2.drawImage(CacheDataLoader.getInstance().getFrameImage("gamewin").getImage(), 300, 300, null);
                    }
                    
                    break;
                case GAMEOVER:
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
                    g2.setColor(Color.WHITE);
                    g2.drawString("GAME OVER!", 450, 300);
                    break;

            }
            

        }

    }

    public BufferedImage getBufferedImage(){
        return bufferedImage;
    }
}