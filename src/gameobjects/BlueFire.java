package gameobjects;

import java.awt.Rectangle;
import java.awt.Graphics2D;

public class BlueFire extends Bullet {
    // private Animation forwardBulletAnim, backBulletAnim;

    public BlueFire(double posX, double posY, GameWorld gameWorld) {
        super(posX, posY, gameWorld, 35, 30, 1, 10);
        // forwardBulletAnim = CacheDataLoader.getInstance().getAnimation("Punch");
        // backBulletAnim = CacheDataLoader.getInstance().getAnimation("Punch");
        // backBulletAnim.flipAllImage();
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return getBoundForCollisionWithMap();
    }

    @Override
    public void draw(Graphics2D g2) {
        // drawBoundForCollisionWithMap(g2);
    }
    @Override
    public void Update() {
        super.Update();
    }
    @Override
    public void attack() {
        
    }

    @Override
    public void superAttack() {
        
    }
}
