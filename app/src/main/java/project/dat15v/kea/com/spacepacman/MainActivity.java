package project.dat15v.kea.com.spacepacman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener
{
    Button buttonPlay;

    TextView textBestScore;

    private int hiScore;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) // THE ENTRY POINT FOR THE GAME

    {
        super.onCreate(savedInstanceState);

        // The UI layout 'activity_main.xml' is set as the view
        setContentView(R.layout.activity_main);


        // Get the Hi Score from SharedPreferences
        preferences = getSharedPreferences("HiScores", MODE_PRIVATE);
        hiScore = preferences.getInt("hiScore", 0);

        // Set the hi score in the textField
        textBestScore = (TextView) findViewById(R.id.textHighScore);
        textBestScore.setText("Best Score: " + hiScore);

        buttonPlay = (Button) findViewById(R.id.buttonStart);
        buttonPlay.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        // Intent object is used to switch between activities
        Intent intent = new Intent(this, GameActivity.class);

        // Start the new actitivy 'GameActivity'
        startActivity(intent);

        // Shut this 'MainActivity' down
        finish();
    }

}
