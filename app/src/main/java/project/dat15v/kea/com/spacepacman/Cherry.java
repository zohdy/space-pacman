/**
 * Created by peterzohdy on 11/01/2017.
 */
package project.dat15v.kea.com.spacepacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.Random;


public class Cherry implements GameObject
{
    private Bitmap bitmap;

    // Location and speed of object
    private int x;
    private int y;
    private int speed;

    // For storing the Screen resolution
    private int screenMax_X;
    private int screenMax_Y;
    private int screenMin_X;

    // Hit Box for collision detection
    private Rect hitBox;

    private Random random = new Random();

    // Constructor
    public Cherry(Context context, int screenMax_X, int screenMax_Y)
    {

        // The Bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cherry);

        // For the screen resolution
        this.screenMax_X = screenMax_X;
        this.screenMax_Y = screenMax_Y;
        this.screenMin_X = 0;

        // Random speed between 10 and 15;
        speed = random.nextInt(6) + 10;

        // Init object with :
        // X = Max (right edge of screen)
        // Y = Random location within screen bounds
        x = screenMax_X;
        y = random.nextInt(screenMax_Y) - bitmap.getHeight();

        // Hitbox around object - (left, top, right, bottom)
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(int pacmanSpeed)
    {
        // Move to the left relative to the Pacman speed
        x -= pacmanSpeed;
        x -= speed;

        // Respawn when off screen
        if(x < screenMin_X - bitmap.getWidth())
        {
            random = new Random();
            speed = random.nextInt(6) + 10;
            x = screenMax_X;
            y = random.nextInt(screenMax_Y) - bitmap.getHeight();
        }

        // Move hitbox along with object
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }


    // ====================================== GETTERS AND SETTERS =========================== //
    public void setX(int x)
    {
        this.x = x;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getY()
    {
        return y;
    }

    public int getX()
    {
        return x;
    }


    public Rect getHitBox()
    {
        return hitBox;
    }
}


