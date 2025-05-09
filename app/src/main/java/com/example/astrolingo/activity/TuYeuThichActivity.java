package com.example.astrolingo.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.astrolingo.R;


public class TuYeuThichActivity extends AppCompatActivity {

    ImageView addVocab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_tuyeuthich);

        addVocab = findViewById(R.id.addVocab);
        addVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddVocabDialog();
            }
        });
    }

    // mở page_tuyeuthich_dialog_add_vocab để tự tạo từ yêu thích
    private void showAddVocabDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.page_tuyeuthich_dialog_add_vocab, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText etWord = dialogView.findViewById(R.id.etVocabulary);
        EditText etPronounce = dialogView.findViewById(R.id.etPronounce);
        EditText etMeaning = dialogView.findViewById(R.id.etMeaning);
        Button btnAdd = dialogView.findViewById(R.id.btnAddWord);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = etWord.getText().toString();
                String pronounce = etPronounce.getText().toString();
                String meaning = etMeaning.getText().toString();

                Toast.makeText(TuYeuThichActivity.this, "Đã thêm từ: " + word, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
