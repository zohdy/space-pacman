package project.dat15v.kea.com.spacepacman;

import android.graphics.Rect;

/**
 * Created by peterzohdy on 12/01/2017.
 */

public interface GameObject
{
    void update(int speed); // The gameobjects own update method

    int getX(); // The X position of the GameObject

    int getY(); // The Y position of the GameObject

    Rect getHitBox(); // The Hitbox of the GameObject for detecting collisions

    void setX(int x); // Set an X position
}
