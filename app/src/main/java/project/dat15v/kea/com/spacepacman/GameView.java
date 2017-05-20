package project.dat15v.kea.com.spacepacman;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;


/**
 * Created by peterzohdy on 11/01/2017.
 */

// SurfaceView is a view designed for drawing
public class GameView extends SurfaceView implements Runnable
{

    private Context context;

    // Thread management
    private volatile boolean playing; // Volatile so that both threads can see the current state of that thread. If another datatype synchronized is used
    private Thread gameThread = null;
    private boolean gameEnded;

    // Screen Size
    private int screenMax_X;
    private int screenMax_Y;

    // Game objects
    private Pacman pacman;
    private ArrayList<Monster> monsterList;
    private int numOfMonsters;
    private ArrayList<Cherry> cherryList;
    private int numOfCherries;
    private Key key;

    // Background Stars
    private ArrayList<Star> starList;
    private int numOfStars;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    // For score
    private int distanceRemaining;
    private int score;
    private int hiScore;

    // Sound
    private MediaPlayer soundDies;
    private MediaPlayer soundEatingCherry;
    private MediaPlayer soundExtraLife;
    private MediaPlayer soundFinished;
    private MediaPlayer soundGameStart;
    private MediaPlayer soundHitMonster;

    // Persistence
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // Levels
    private boolean levelSwitched;
    private int level;


    // Context parameter is a reference to the current state of the application in the Android system.
    public GameView(Context context, int screenMax_X, int screenMax_Y)
    {
        super(context);
        this.context = context;

        // Screen Size
        this.screenMax_X = screenMax_X;
        this.screenMax_Y = screenMax_Y;

        // Sounds init
        soundDies = MediaPlayer.create(context, R.raw.sound_dies);
        soundEatingCherry = MediaPlayer.create(context, R.raw.sound_eating_cherry);
        soundExtraLife = MediaPlayer.create(context, R.raw.sound_extra_life);
        soundFinished = MediaPlayer.create(context, R.raw.sound_finished);
        soundGameStart = MediaPlayer.create(context, R.raw.sound_start);
        soundHitMonster = MediaPlayer.create(context, R.raw.sound_hit_monster);

        // Initialize drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        // Initialize shared preferences for storing hi-score
        preferences = context.getSharedPreferences("HiScores", context.MODE_PRIVATE);
        editor = preferences.edit();
        hiScore = preferences.getInt("hiScore", 0);

        startGame();
    }

    private void startGame()
    {
        // Initialize Pacman
        pacman = new Pacman(context, screenMax_Y);

        // Init Monsters
        monsterList = new ArrayList<>();
        numOfMonsters = 3;
        for(int i = 0; i < numOfMonsters; i++)
        {
            monsterList.add(new Monster(context, screenMax_X, screenMax_Y));
        }

        // Init Cherries
        cherryList = new ArrayList<>();
        numOfCherries = 3;
        for(int i = 0; i < numOfCherries; i++)
        {
            cherryList.add(new Cherry(context, screenMax_X, screenMax_Y));
        }

        // Init Stars
        starList = new ArrayList<>();
        numOfStars = 100;
        for(int i = 0; i < numOfStars; i++)
        {
            starList.add(new Star(screenMax_X, screenMax_Y));
        }

        // Init Key
        key = new Key(context, screenMax_X, screenMax_Y);

        // Reset time and distance
        distanceRemaining = 500;
        score = 0;

        soundGameStart.start();

        level = 1;



        gameEnded = false;
    }

    @Override
    public void run()
    {

        // Thread only runs if 'playing' is true
        while (playing)
        {
            // Get the user inputs and update the game data
            update();
            // Draw it to the screen
            draw();
            // control the FPS
            control();

        }
    }

    private void update()
    {


        if(levelSwitched) // levelSwitched is changed in changeLevel() method
        {
            try
            {
                Thread.sleep(2000); // sleep for 2 sec. to notify user with some text
                levelSwitched = false; // levelSwitched variable is used in drawing()

            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        // Change level when distance is reached
        if(distanceRemaining < 0)
        {
            level++;
            changeLevel();
            soundFinished.start();
        }

        // If game is not ended, decrease distance to next level
        if(!gameEnded)
        {
            distanceRemaining--;
        }


        // ====== SPRITE UPDATES ========= //

        pacman.update();


        for(Monster monster : monsterList)
        {
            monster.update(pacman.getSpeed());
        }

        for(Cherry cherry : cherryList)
        {
            cherry.update(pacman.getSpeed());
        }

        for(Star star : starList)
        {
            star.update(pacman.getSpeed());
        }

        key.update(pacman.getSpeed());

        // ============================= //


        detectCollision();

    }

    private void changeLevel()
    {

        switch (level)
        {
            case 2:
                monsterList.add(new Monster(context, screenMax_X, screenMax_Y)); // adds new monster
                distanceRemaining = 1000; // setting new distance to reach next level
                score += 500; // adding to score
                levelSwitched = true; // levelSwitched variable is used in drawing
                break;

            case 3:
                monsterList.add(new Monster(context, screenMax_X, screenMax_Y));
                distanceRemaining = 2000;
                score += 1000;
                levelSwitched = true;
                break;

            case 4:
                monsterList.add(new Monster(context, screenMax_X, screenMax_Y));
                cherryList.add(new Cherry(context, screenMax_X, screenMax_Y));
                distanceRemaining = 3000;
                score += 2500;
                levelSwitched = true;
                break;

            case 5:
                monsterList.add(new Monster(context, screenMax_X, screenMax_Y));
                cherryList.add(new Cherry(context, screenMax_X, screenMax_Y));
                distanceRemaining = 4000;
                score += 5000;
                levelSwitched = true;
                break;

            default:

                gameEnded = true;
        }
    }

    private void detectCollision()
    {
        boolean cherryCollision = false;

        //========== Monster Collision ===========//

        for(Monster monster : monsterList) // Checking each monster for collisions
        {
            if(Rect.intersects(pacman.getHitBox(), monster.getHitBox()))
            {
                monster.setX(-200); // push monster out of screen
                soundHitMonster.start();
                pacman.reduceLife();
            }
        }

        if(pacman.getLifes() < 0) // If out of lifes
        {
            soundDies.start();
            setHiScore();
            gameEnded = true;
        }


        //========== Cherry Collision ===========//

        for(Cherry cherry : cherryList) // Checking each monster for collisions
        {
            if(Rect.intersects(pacman.getHitBox(), cherry.getHitBox()))
            {
                cherry.setX(-200); // push cherry out of screen
                soundEatingCherry.start();

                switch (level) // score for eating cherries is depending on level
                {
                    case 1:
                        score+= 100;
                        break;
                    case 2:
                        score+= 200;
                        break;
                    case 3:
                        score+= 300;
                        break;
                    case 4:
                        score+= 400;
                        break;
                    case 5:
                        score+= 500;
                }
            }
        }


        //========== Key Collision ===========//

        if(Rect.intersects(pacman.getHitBox(), key.getHitBox()))
        {
            key.setX(-200); // push key out of screen
            soundExtraLife.start();
            pacman.addLife();
        }
    }

    private void draw()
    {
        // If surface available to draw on
        if(surfaceHolder.getSurface().isValid())
        {
            // Locks the surface. Can't be accessed or changed before it is unlocked again
            canvas = surfaceHolder.lockCanvas();

            // Background color
            canvas.drawColor(Color.BLACK);


            // ===================== DRAW SPRITES ======================= //
            // Draw Pacman
            canvas.drawBitmap(pacman.getBitmap(), pacman.getX(), pacman.getY(), paint);

            // Draw all Monsters
            for(Monster monster : monsterList)
            {
                canvas.drawBitmap(monster.getBitmap(), monster.getX(), monster.getY(), paint);
            }

            // Draw Cherries
            for(Cherry cherry : cherryList)
            {
                canvas.drawBitmap(cherry.getBitmap(), cherry.getX(), cherry.getY(), paint);
            }

            // Draw Key
            canvas.drawBitmap(key.getBitmap(), key.getX(), key.getY(), paint);

            // Draw Stars
            paint.setColor(Color.WHITE);
            for(Star star : starList)
            {
                paint.setStrokeWidth(star.getStarWidth());
                canvas.drawPoint(star.getX(), star.getY(), paint);
            }

            // ======================================================= //


            if(!gameEnded)
            {
                // Draw user HUD
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.WHITE);
                paint.setTextSize(40);
                paint.setTypeface(Typeface.MONOSPACE);
                canvas.drawText("Level: " + level, 10, 50, paint);
                canvas.drawText("Hi Score: " + hiScore, (screenMax_X /4) * 3, 50 , paint);
                canvas.drawText("Score: " + score, screenMax_X / 3, 50 , paint);
                canvas.drawText("New level in: " + distanceRemaining + "km", screenMax_X / 3, screenMax_Y - 20, paint);
                canvas.drawText("Lives: " + pacman.getLifes(), 10, screenMax_Y - 20, paint);
                canvas.drawText("Speed " + pacman.getSpeed() * 100 + " Km/h", (screenMax_X /4) * 3, screenMax_Y - 20, paint);

            } else {

                // Draw 'Game Over' Screen
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(Typeface.MONOSPACE);
                paint.setFakeBoldText(true);
                paint.setTextSize(150);
                canvas.drawText("Game Over", screenMax_X / 2, 350, paint);
                paint.setTextSize(50);
                paint.setTypeface(Typeface.DEFAULT);
                paint.setFakeBoldText(false);
                canvas.drawText("Hi Score: " + hiScore, screenMax_X / 2, 480, paint);
                canvas.drawText("Your Score: " + score, screenMax_X / 2, 550, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", screenMax_X / 2, 700, paint);
            }

            if(levelSwitched)
            {
                // Notify the user whenever level is switched
                paint.setTextSize(100);
                paint.setTypeface(Typeface.MONOSPACE);
                paint.setFakeBoldText(true);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Level " + level + "!", screenMax_X / 2, 350, paint);
            }

            // Unlcock canvas and draw it the scene
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void setHiScore()
    {
        if(score > hiScore)
        {
            hiScore = score;
            editor.putInt("hiScore", score);
            editor.commit();
        }
    }

    //SurfaceView allows to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP: // When the user lifts their finger from screen

                pacman.stopMoving();
                break;

            case MotionEvent.ACTION_DOWN: // When the user presses the screen

                pacman.startMoving();

                if(gameEnded) // If the user is game over - press the screen to start a new one
                {
                    startGame();
                }
                break;
        }

        return true;
    }


    private void control()
    {
            try
            {
                gameThread.sleep(17);  // around 60fps is the target - 1000 / 17 = 58 FPS

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
    }



    // Cleans up the thread if the game is interrupted or user quits
    public void pause()
    {
        playing = false; // stop the update() method

        try {

            gameThread.join(); // Waiting for gameThread to finish

        } catch (InterruptedException e) {

            Log.e("error", "failed to stop the thread");
        }
    }

    // Make a new thread and start it
    public void resume()
    {
        // Starting the thread
        gameThread = new Thread(this);
        playing = true;
        gameThread.start();
    }

}
