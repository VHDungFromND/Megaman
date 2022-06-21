package gameobjects;

import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import effect.CacheDataLoader;

public class Gaara extends Human {

    public static final int RUNSPEED = 3;
    
    public boolean startBulletTime=false;
    public long lastSuperAttackTime;

    private AudioClip Punch1;
    
    public Gaara(float x, float y, GameWorld gameWorld) {
        super(x, y, gameWorld, 70, 110, 0.2f, 10, 100);
        
        Punch1 = CacheDataLoader.getInstance().getSound("bluefireshooting");
        // hurtingSound = CacheDataLoader.getInstance().getSound("narutohurt");
        
        setTeamType(ENEMY_TEAM);

        setTimeNotBeHurt(500*1000000);

        idlePunchForwardAnim = CacheDataLoader.getInstance().getAnimation("Combo");
        idlePunchBackAnim = CacheDataLoader.getInstance().getAnimation("Combo");
        idlePunchBackAnim.flipAllImage();

        idleSuperAttackForwardAnim = CacheDataLoader.getInstance().getAnimation("SandBurialRepeat");
        idleSuperAttackBackAnim = CacheDataLoader.getInstance().getAnimation("SandBurialRepeat");
        // idlePunchForwardAnim.setIsRepeat(false);
        // idleSuperAttackBackAnim.setIsRepeat(false);
        idleSuperAttackBackAnim.flipAllImage();
        
        runForwardAnim = CacheDataLoader.getInstance().getAnimation("Running");
        runBackAnim = CacheDataLoader.getInstance().getAnimation("Running");
        runBackAnim.flipAllImage();   

        dickForwardAnim = CacheDataLoader.getInstance().getAnimation("Dicking");
        dickBackAnim = CacheDataLoader.getInstance().getAnimation("Dicking");
        dickBackAnim.flipAllImage();

        walkForwardAnim = CacheDataLoader.getInstance().getAnimation("Walking");
        walkBackAnim = CacheDataLoader.getInstance().getAnimation("Walking");
        walkBackAnim.flipAllImage();  
        
        idleForwardAnim = CacheDataLoader.getInstance().getAnimation("Idle");
        idleBackAnim = CacheDataLoader.getInstance().getAnimation("Idle");
        idleBackAnim.flipAllImage();
        
        
        flyForwardAnim = CacheDataLoader.getInstance().getAnimation("Jumping");
        flyForwardAnim.setIsRepeat(false);
        flyBackAnim = CacheDataLoader.getInstance().getAnimation("Jumping");
        flyBackAnim.setIsRepeat(false);
        flyBackAnim.flipAllImage();
        
        landingForwardAnim = CacheDataLoader.getInstance().getAnimation("G_Landing");
        landingBackAnim = CacheDataLoader.getInstance().getAnimation("G_Landing");
        landingBackAnim.flipAllImage();
        
        // climWallBack = CacheDataLoader.getInstance().getAnimation("clim_wall");
        // climWallForward = CacheDataLoader.getInstance().getAnimation("clim_wall");
        // climWallForward.flipAllImage();
        
        beHurtForwardAnim = CacheDataLoader.getInstance().getAnimation("Hurt");
        beHurtBackAnim = CacheDataLoader.getInstance().getAnimation("Hurt");
        beHurtBackAnim.flipAllImage();

        deathForwardAnim = CacheDataLoader.getInstance().getAnimation("KnockedBack");
        deathBackAnim = CacheDataLoader.getInstance().getAnimation("KnockedBack");
        deathBackAnim.flipAllImage();
        
        // idlePunchForwardAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
        // idlePunchBackAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
        // idlePunchBackAnim.flipAllImage();
      
        // runPunchForwarAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
        // runPunchBackAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
        // runPunchBackAnim.flipAllImage();
        
        // flyPunchForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
        // flyPunchBackAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
        // flyPunchBackAnim.flipAllImage();
        
    }

    @Override
    public void Update() {
        super.Update();
        if(getState()==DEATH && (deathBackAnim.getCurrentFrame()>=2 || deathForwardAnim.getCurrentFrame()>=2)) {
            setSpeedX(0);
        }
        if(deathBackAnim.getCurrentFrame()==3 || deathForwardAnim.getCurrentFrame()==3) {
            GameWorld.isGameOver=true;
        } 
        if(getState()==DEATH) {
            if(deathBackAnim.getCurrentFrame()<2 && deathForwardAnim.getCurrentFrame()<2 && getPosX()>getGameWorld().naruto.getPosX() ) setSpeedX(3);
            if(deathBackAnim.getCurrentFrame()<2 && deathForwardAnim.getCurrentFrame()<2 && getPosX()<getGameWorld().naruto.getPosX() ) setSpeedX(-3); 
        }

        if(getBlood()<=0 && !timeCheck) {
            timeDeath=System.currentTimeMillis();
            timeCheck=true;
        }
        if(isRunning && (Math.abs(getPosX()-getPosNow())>400 || isCollision) ) {
            isRunning=false;
            setSpeedX(0);
            isCollision=false;
        }
        if(!isSuperAttack) {
            idleSuperAttackBackAnim.reset();
            idleSuperAttackBackAnim.reset();
        }


        if(System.currentTimeMillis() -lastRunningTime > 5*1000) {
            permitToRun=true;
        }

        if(isPunch){
            if(System.nanoTime() - lastPunchTime > 500*1000000){
                isPunch = false;
                idlePunchBackAnim.reset();
                idlePunchBackAnim.reset();
            }
        }


        
        if(getIsLanding()){
            landingBackAnim.Update(System.nanoTime());
            if(landingBackAnim.isLastFrame()) {
                setIsLanding(false);
                landingBackAnim.reset();
                runForwardAnim.reset();
                runBackAnim.reset();
                walkForwardAnim.reset();
                walkBackAnim.reset();
            }
        }
        
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();
        
        if(getIsDicking()){
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() + 20;
            rect.width = 44;
            rect.height = 60;
        }else{
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 40;
            rect.width = 54;
            rect.height = 90;
        }
        
        return rect;
    }

    @Override
    public void draw(Graphics2D g2) {
        
        switch(getState()){
        
            case ALIVE:
            case NOTBEHURT:
                if(getState() == NOTBEHURT && (System.nanoTime()/10000000)%2!=1)
                {
                    
                }else{
                    
                    if(getIsLanding()){

                        if(getDirection() == RIGHT_DIR){
                            landingForwardAnim.setCurrentFrame(landingBackAnim.getCurrentFrame());
                            landingForwardAnim.draw((int) (getPosX()), 
                                    (int) getPosY()+ (getBoundForCollisionWithMap().height/2 - landingForwardAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }else{
                            landingBackAnim.draw((int) (getPosX()), 
                                    (int) getPosY() + (getBoundForCollisionWithMap().height/2 - landingBackAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }

                    }else if(getIsJumping()){

                        if(getDirection() == RIGHT_DIR){
                            flyForwardAnim.Update(System.nanoTime());
                            
                            flyForwardAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                        }else{
                            flyBackAnim.Update(System.nanoTime());
                            
                            flyBackAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                        }

                    }else if(getIsDicking()){

                        if(getDirection() == RIGHT_DIR){
                            dickForwardAnim.Update(System.nanoTime());
                            dickForwardAnim.draw((int) (getPosX()), 
                                    (int) getPosY() + (getBoundForCollisionWithMap().height/2 - dickForwardAnim.getCurrentImage().getHeight()/2)+10,
                                    g2);
                        }else{
                            dickBackAnim.Update(System.nanoTime());
                            dickBackAnim.draw((int) (getPosX()), 
                                    (int) getPosY() + (getBoundForCollisionWithMap().height/2 - dickBackAnim.getCurrentImage().getHeight()/2)+10,
                                    g2);
                        }

                    }else{
                        if(getSpeedX() > 0 && !isRunning){
                            walkForwardAnim.Update(System.nanoTime());
                    
                            walkForwardAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                            if(walkForwardAnim.getCurrentFrame() == 1) walkForwardAnim.setIgnoreFrame(0);
                        }else if(getSpeedX() < 0 && !isRunning){
                            walkBackAnim.Update(System.nanoTime());
                            
                            walkBackAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                            if(walkBackAnim.getCurrentFrame() == 1) walkBackAnim.setIgnoreFrame(0);
                        }else if(getSpeedX()==0){
                            if(getDirection() == RIGHT_DIR){
                                if(isPunch){
                                    idlePunchForwardAnim.Update(System.nanoTime());
                                    idlePunchForwardAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                                }else if(isSuperAttack) {
                                    idleSuperAttackForwardAnim.Update(System.nanoTime());
                                    idleSuperAttackForwardAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                                    if( idleSuperAttackForwardAnim.getCurrentFrame()==1) isSuperAttack=false;
                                } else {
                                    idleForwardAnim.Update(System.nanoTime());
                                    idleForwardAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                                }
                
                                
                            }else{
                                
                                if(isPunch){
                                    idlePunchBackAnim.Update(System.nanoTime());
                                    idlePunchBackAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                                }else if(isSuperAttack) {
                                    idleSuperAttackBackAnim.Update(System.nanoTime());
                                    idleSuperAttackBackAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                                    if( idleSuperAttackBackAnim.getCurrentFrame()==1) isSuperAttack=false;
                                } else {
                                    idleBackAnim.Update(System.nanoTime());
                                    idleBackAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                                }
                                
                            }
                        }        
                        if(getSpeedX()>0 && isRunning) {
                            runForwardAnim.Update(System.nanoTime());
                            runForwardAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                            if(runForwardAnim.getCurrentFrame() == 1) runForwardAnim.setIgnoreFrame(0);
                        }
                        if(getSpeedX()<0 && isRunning) {
                            runBackAnim.Update(System.nanoTime());
                            
                            runBackAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                            if(runBackAnim.getCurrentFrame() == 1) runBackAnim.setIgnoreFrame(0);
                        }
                    }
                }
                
                break;
            
            case BEHURT:
                if(getDirection() == RIGHT_DIR){
                    beHurtForwardAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                }else{
                    beHurtBackAnim.setCurrentFrame(beHurtForwardAnim.getCurrentFrame());
                    beHurtBackAnim.draw((int) (getPosX()), (int) getPosY(), g2);
                }
                break;
             
            case FEY:
                
                break;
            case DEATH:
                if(deathBackAnim.getCurrentFrame()==2 || deathForwardAnim.getCurrentFrame()==2) {
                    deathBackAnim.setIgnoreFrame(0);
                    deathBackAnim.setIgnoreFrame(1);
                    deathForwardAnim.setIgnoreFrame(0);
                    deathForwardAnim.setIgnoreFrame(1);
                }
                if(getPosX()>getGameWorld().naruto.getPosX()) {
                    deathForwardAnim.Update(System.nanoTime());
                    deathForwardAnim.draw((int)getPosX(),(int) getPosY(), g2);
                    
                }
                else {
                    deathBackAnim.Update(System.nanoTime());
                    deathBackAnim.draw((int)getPosX(),(int) getPosY(), g2);
                }
                break;
        }
        
        // drawBoundForCollisionWithMap(g2);
        // drawBoundForCollisionWithEnemy(g2);
    }

    @Override
    public void attack() {
        if(!isPunch  && !getIsDicking() && !isRunning && getMana()>=10) {
            setMana(getMana()-10);
            // Punch1.play();
            Bullet bullet = new BlueFire(getPosX(), getPosY(), this.getGameWorld());
            if(getDirection()==LEFT_DIR) {
                bullet.setPosX(getPosX()-getWidth() +35);
                bullet.setPosY(getPosY()-30);
            }
            else {
                bullet.setPosX(getPosX() +getWidth() - 35);
                bullet.setPosY(getPosY()-30);
            }
            bullet.setTeamType(getTeamType());
            getGameWorld().bulletManager.addObject(bullet);

            lastPunchTime = System.nanoTime();
            isPunch = true;
        }

    
    }

    @Override
    public void superAttack() {
        if(!isSuperAttack  && !getIsDicking() && getMana()>=50) {
            lastSuperAttackTime=System.currentTimeMillis();
            setMana(getMana()-50);
            isSuperAttack = true;
            setPosNow((int)getPosX());
            Bullet bullet = new SuperAttack2(getPosX(), getPosY(), this.getGameWorld());
            bullet.setTeamType(getTeamType());
            getGameWorld().bulletManager.addObject(bullet);
        }
    }

    @Override
    public void hurtingCallBack(){
        
        // hurtingSound.play();
    }

}
