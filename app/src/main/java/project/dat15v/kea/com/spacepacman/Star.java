package project.dat15v.kea.com.spacepacman;

import java.util.Random;

/**
 * Created by peterzohdy on 11/01/2017.
 */

public class Star
{
    // For storing location and speed
    private int x;
    private int y;
    private int speed;

    // For storing Screen resolution
    private int screenMax_X;
    private int screenMax_Y;

    private Random random = new Random();

    // Constructor
    public Star(int screenMax_X, int screenMax_Y)
    {

        this.screenMax_X = screenMax_X;
        this.screenMax_Y = screenMax_Y;

        // Random speed between 0 and 10
        speed = random.nextInt(10);

        // Initialize with random x and y values within screensize
        x = random.nextInt(screenMax_X);
        y = random.nextInt(screenMax_Y);
    }

    public void update(int pacmanSpeed)
    {
        x -= pacmanSpeed;
        x -= speed;

        // If star moves out of screen
        if(x < 0)
        {
            x = screenMax_X; // set X position to the right edge of screen
            y = random.nextInt(screenMax_Y); // random Y position
            speed = random.nextInt(10); // Random speed between 0 and 10
        }
    }

    // For randomizing star sizes
    // setStrokeWidth() takes a float
    public float getStarWidth()
    {
        // nextFloat returns value beteween 0.0 and 1.0
        float starWidth = random.nextFloat() * 4;

        return starWidth;
    }

    // ================ GETTERS ====================== //
    public int getX()
    {
        return x;
    }


    public int getY()
    {
        return y;
    }
}
