package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BridgeSelectActivity extends AppCompatActivity {

    Button backButton;
    Button nextButton;
    EditText bridgeIDEditText;

    public static String bridgeID ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_select);

        backButton = (Button)findViewById(R.id.backButton);
        nextButton = (Button)findViewById(R.id.nextButton);
        bridgeIDEditText = (EditText)findViewById(R.id.bridgeIDEditText);
    }

    public void nextButtonMethod(View v){
        bridgeID = bridgeIDEditText.getText().toString();

        if(bridgeID == null || bridgeID.equals("")) {
            Toast.makeText(BridgeSelectActivity.this, "Please enter a bridge ID", Toast.LENGTH_SHORT).show();
            Log.d("ok", bridgeIDEditText.toString());
        }else{
//            Intent nextIntent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(nextIntent);

            Intent nextIntent = new Intent(getApplicationContext(), GridActivity.class);
            startActivity(nextIntent);
        }
    }

    public void backButtonMethod(View v){
        Intent backIntent = new Intent(getApplicationContext(), NavigateActivity.class);
        startActivity(backIntent);
    }
}
