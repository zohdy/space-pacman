package project.dat15v.kea.com.spacepacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by peterzohdy on 11/01/2017.
 */


public class Pacman
{
    private Bitmap bitmap;

    // The speed and location of Pacman
    private int x;
    private int y;
    private int speed;
    private boolean isMoving;
    private final int GRAVITY = -12;

    // Only Y axis is used for Pacman
    private int screenMax_Y;
    private int screenMin_Y;

    // Speed restrictions
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    private int life;

    private Rect hitBox;

    // Constructor
    public Pacman(Context context, int screenMax_Y)
    {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);

        // Max allowed value for Y - keep pacman inside screen
        this.screenMax_Y = screenMax_Y - bitmap.getHeight();
        screenMin_Y = 0;

        // Start position for pacman
        x = 50; // Little gab between left edge and pacman
        y = 0; // Start at the top

        // Init a hitbox around pacman
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

        life = 3;
    }

    public void update()
    {
        //If the user is pressing the screen the speed of Pacman is increased
        if(isMoving)
        {
            speed +=5;

            //If the user is releasing the screen the speed is slowed down
        } else {

            speed -= 10;
        }

        //Constrain top speed
        if(speed > MAX_SPEED)
        {
            speed = MAX_SPEED;
        }

        //Speed can't be stopped completely
        if(speed < MIN_SPEED)
        {
            speed = MIN_SPEED;
        }

        //Pacman must be inside screen resolution
        if(y < screenMin_Y)
        {
            y = screenMin_Y;
        }

        //Pacman must be inside screen resolution
        if(y > screenMax_Y)
        {
            y = screenMax_Y;
        }

        // ACTUALLY moving Pacman up or down
        y = y - (speed + GRAVITY);


        // // hitbox follows pacman movement
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();

    }

    public void stopMoving()
    {
        isMoving = false;
    }

    public void startMoving()
    {
        isMoving = true;
    }

    public void reduceLife()
    {
        life --;
    }

    public void addLife()
    {
        life ++;
    }

    // ================================  Getters ============================
    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Rect getHitBox()
    {
        return hitBox;
    }

    public int getLifes()
    {
        return life;
    }
}
