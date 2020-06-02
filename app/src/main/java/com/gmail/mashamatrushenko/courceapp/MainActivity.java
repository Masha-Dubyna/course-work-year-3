package com.gmail.mashamatrushenko.courceapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MainActivity extends AppCompatActivity {
    private static int SIGN_IN_CODE = 1;
    private RelativeLayout mainActivity;
    private FirebaseListAdapter<Message> adapter;
    private EmojiconEditText emojiconEditText;
    private ImageView emojiBtn;
    private ImageView submitBtn;
    private EmojIconActions emojIconActions;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            if (requestCode == RESULT_OK) {
                Snackbar.make(mainActivity, "You are authorized", Snackbar.LENGTH_SHORT).show();
                displayMessages();
            } else{
                Snackbar.make(mainActivity, "You are not authorized", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = findViewById(R.id.activity_main);
        submitBtn = findViewById(R.id.submit_btn);
        emojiBtn = findViewById(R.id.emoji_btn);
        emojiconEditText = findViewById(R.id.text_field);
        emojIconActions = new EmojIconActions(getApplicationContext(), mainActivity, emojiconEditText, emojiBtn);
        emojIconActions.ShowEmojIcon();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().push().setValue(
                        new Message(
                                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                emojiconEditText.getText().toString()
                        )
                );
                emojiconEditText.setText("");
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        } else {
            Snackbar.make(mainActivity, "You are authorized", Snackbar.LENGTH_SHORT).show();
            displayMessages();
        }







    }

    private void displayMessages() {
        ListView messegesList = findViewById(R.id.mess_list);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.message_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView user, time;
                BubbleTextView text;
                user = v.findViewById(R.id.mess_user);
                text = v.findViewById(R.id.mess_text);
                time = v.findViewById(R.id.mess_time);
                user.setText(model.getUserName());
                text.setText(model.getText());
                time.setText(DateFormat.format("dd-MM-yy HH:mm", model.getTime()));
            }
        };
        messegesList.setAdapter(adapter);
    }
}
