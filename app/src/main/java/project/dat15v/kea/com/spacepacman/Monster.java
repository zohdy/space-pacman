package project.dat15v.kea.com.spacepacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by peterzohdy on 11/01/2017.
 */

public class Monster implements GameObject
{
    private Bitmap bitmap;

    // For movement and speed
    private int x;
    private int y;
    private int speed;

    // For screen resolution
    private int screenMax_X;
    private int screenMin_X;
    private int screenMax_Y;

    // Hit Box for collision detection
    private Rect hitBox;

    Random random = new Random();
    Context context;

    // Constructor
    public Monster(Context context, int screenMax_X, int screenMax_Y)
    {
        this.context = context;

        // Switch random picture
        switchBitmap(context);

        // The screen size
        this.screenMax_X = screenMax_X;
        this.screenMax_Y = screenMax_Y;
        screenMin_X = 0;

        // Random speed between 10 and 15
        speed = random.nextInt(6) + 10;

        x = screenMax_X; // X Position at the right edge of the screen
        y = random.nextInt(screenMax_Y) - bitmap.getHeight(); // Random Y position within screen bounds


        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(int pacmanSpeed)
    {
        // Move to the left relative to pacman speed
        x -= pacmanSpeed;
        x -= speed;

        // Respawn when off screen
        if(x < screenMin_X - bitmap.getWidth())
        {
            switchBitmap(context); // change picture if re-spawn
            speed = random.nextInt(30) + 10; // Random speed between 10 and 39

            x = screenMax_X; // X Position at the right edge of the screen
            y = random.nextInt(screenMax_Y) - bitmap.getHeight(); // Random Y position within screen bounds
        }

        // Hitbox follows object
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }



    private void switchBitmap(Context context)
    {
        int randomBitmap = random.nextInt(5);

        switch (randomBitmap)
        {
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.monster_blue);
                break;

            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.monster_green);
                break;

            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.monster_orange);
                break;

            case 3:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.monster_pink);
                break;

            case 4:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.monster_red);
                break;
        }
    }


    // ============================== GETTERS ========================= //

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public Rect getHitBox()
    {
        return hitBox;
    }
}
