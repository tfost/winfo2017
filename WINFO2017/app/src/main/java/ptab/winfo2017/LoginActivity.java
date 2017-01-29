package ptab.winfo2017;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    public static final int PICK_CONTACT_REQUEST = 1;
    public static String name;
    public static ArrayList<Contact> contacts;

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
        TextView textView = (TextView) findViewById(R.id.welcome);
        textView.setText("Hello " + name);
        contacts = new ArrayList<Contact>();

        Button addContact = (Button) findViewById(R.id.add_contact);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                    pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                    startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                        ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);

                //  Add to contacts;
                Contact newContact = new Contact(name, number);
                contacts.add(newContact);

                //  create new field_create line
                LinearLayout contactHolder = (LinearLayout) findViewById(R.id.contact_holder);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout root = new LinearLayout(this);
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.contact_wrapper, root);
                layout.setId(contacts.size() - 1);

                //  fill in slots
                TextView displayName = (TextView) layout.findViewById(R.id.display_name);
                displayName.setText(name);

                TextView displayNumber = (TextView) layout.findViewById(R.id.display_number);
                displayNumber.setText(number);

                Button remove = (Button) layout.findViewById(R.id.remove_contact);
                remove.setId(layout.getId());
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  delete line
                        LinearLayout contactHolder = (LinearLayout) findViewById(R.id.contact_holder);
                        int id = view.getId();
                        contactHolder.removeView(contactHolder.findViewById(id));

                        //  update other lines
                        for(int i = id + 1; i < contacts.size(); i++) {
                            LinearLayout layout = (LinearLayout) contactHolder.findViewById(i);
                            layout.setId(i - 1);

                            Button delete = (Button) layout.findViewById(i);
                            delete.setId(i - 1);
                        }
                        contacts.remove(id);
                    }
                });

                contactHolder.addView(layout);
            }
        }
        
        //here
        Button done = (Button) findViewById(R.id.submit_button);
        done.setOnClickListener(new View.OnClickListener () {
            public void onClick(View view) {
                createFile(name + printContacts(), "settings.txt", view.getContext());
                finish();
            }
        });
    }

    public String printContacts() {
        String result = "";
        for(Contact contact : contacts) {
            result += "\n" + contact;
        }
        return result;
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
