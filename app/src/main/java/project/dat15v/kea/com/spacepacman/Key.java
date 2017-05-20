package project.dat15v.kea.com.spacepacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by peterzohdy on 12/01/2017.
 */

public class Key implements GameObject
{
    private Bitmap bitmap;

    // Movement and speed
    private int x;
    private int y;
    private int speed = 1;

    // Detects key leaving screen
    private int screenMax_X;
    private int screenMax_Y;

    // Hit Box for collision detection
    private Rect hitBox;

    private Random random = new Random();

    public Key(Context context, int screenMax_X, int screenMax_Y)
    {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.key);

        // Screen size
        this.screenMax_X = screenMax_X;
        this.screenMax_Y = screenMax_Y;

        // Speed between 10 and 15
        speed = random.nextInt(6) + 10;

        x = screenMax_X; // start X pos at right edge
        y = random.nextInt(screenMax_Y) - bitmap.getHeight(); // start Y pos at random within screen bounds

        // hitbox around object
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(int pacmanSpeed)
    {
        // Move to the left relative to pacman speed
        x -= pacmanSpeed;
        x -= speed;

        // Re spawning
        if(x < -screenMax_X * 4) // Make object only appear rarely

        {
            speed = random.nextInt(10) + 10; // Random speed between 0 and 19
            x = screenMax_X;
            y = random.nextInt(screenMax_Y) - bitmap.getHeight();
        }

        // Hitbox follows position
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }


    // =========================== GETTERS AND SETTERS ==========================

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

    public void setX(int x)
    {
        this.x = x;
    }
}
