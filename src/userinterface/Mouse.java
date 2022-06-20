package userinterface;
import java.awt.event.*;

import gameobjects.GameWorld;  
public class Mouse implements MouseListener{
    GameWorld gameWorld;
    Mouse(GameWorld gameWorld){            
        this.gameWorld=gameWorld;
    }  
    public void mouseClicked(MouseEvent e) {  
        if(gameWorld.state==GameWorld.TUTORIAL) {
            if(e.getX()>510 && e.getX()<634 && e.getY()>465 && e.getY()<544) GameWorld.Level=1;
            if(e.getX()>614 && e.getX()<740 && e.getY()>465 && e.getY()<544) GameWorld.Level=2;
        }
        
    }  
    public void mouseEntered(MouseEvent e) {  
          
    }  
    public void mouseExited(MouseEvent e) {  
         
    }  
    public void mousePressed(MouseEvent e) {  
         
    }  
    public void mouseReleased(MouseEvent e) {  
          
    }
} 
