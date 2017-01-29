package ptab.winfo2017;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.PrintStream;

public class LoginActivity extends AppCompatActivity {
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = "";

        Button continueButton = (Button) findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputName = (EditText) findViewById(R.id.user_first_name);
                name = inputName.getText().toString();
                promptContacts();
            }
        });

        Button skipButton = (Button) findViewById(R.id.skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createFile("hi", "settings.txt", view.getContext());
                finish();
            }
        });
    }

    public void promptContacts() {
        setContentView(R.layout.activity_contacts);
        //  Brandon do what you want
        //  pull contact data
    }

    //  creates a file with a given starting content. If the file exists, it is left alone.
    public static void createFile(String input, String filename, Context context) {
        File file = new File(context.getFilesDir(), filename);
        if (!file.exists()) {
            try {
                PrintStream output = new PrintStream(file);
                output.print(input);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalStateException("writing issue");
            }
        }
    }
}
