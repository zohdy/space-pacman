package project.dat15v.kea.com.spacepacman;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity
{
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Point object has an X and Y value
        Point screenRes = new Point();

        // Load resolution into point object
        display.getSize(screenRes);

        // Initialize the GameView class and load in the screen resoulution size
        // 'This' refers to the 'Context' object.
        gameView = new GameView(this, screenRes.x, screenRes.y);
        setContentView(gameView);
    }

    // Whenever this is called the thread stops running in the background
    @Override
    protected void onPause()
    {
        super.onPause();
        gameView.pause();

    }

    // onResume is active whenever thread is actually running or resumed from a paused lifecycle
    @Override
    protected void onResume()
    {
        super.onResume();
        gameView.resume();
    }
}
