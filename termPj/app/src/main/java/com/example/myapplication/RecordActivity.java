package com.example.myapplication;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class RecordActivity extends AppCompatActivity {
    private final String dbName = "word";
    private final String tableName = "word";

    ArrayList<HashMap<String, String>> wordList;
    ArrayList<String> quesList;
    ListAdapter adapter;
    ListView listView;
    Button addBtn;
    TextView toastTextView;
    SQLiteDatabase sampleDB;
    EditText questionEditText, answerEditText;
    View dialogView, toastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        addBtn = findViewById(R.id.addBtn);

        listView = findViewById(R.id.listView);
        wordList = new ArrayList<HashMap<String, String>>();
        quesList = new ArrayList<String>();

        initSampleDb();
        initAddBtn();
        initListView();
        initClick();
    }

    private void initClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                builder.setTitle("삭제");
                builder.setMessage("다 외웠다면 지워주세요");
                builder.setPositiveButton("지우기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sampleDB.delete(tableName, "question=?", new String[]{quesList.get(position)});
                        initListView();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
                return true;
            }
        });
    }

    private void initListView() {
        try {
            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);
            wordList.clear();
            quesList.clear();
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String question = c.getString(c.getColumnIndex("question"));
                        String answer = c.getString(c.getColumnIndex("answer"));
                        addWordToList(question, answer);
                    } while (c.moveToNext());
                }
            }
            ReadDB.close();
            adapter = new SimpleAdapter(
                    this, wordList, R.layout.list_item,
                    new String[]{"question", "answer"}, new int[]{R.id.questionTextView, R.id.answerTextView}
            );
            listView.setAdapter(adapter);
        } catch (SQLException se) {
        }
    }

    private void addWordToList(String question, String answer) {
        HashMap<String, String> word = new HashMap<String, String>();

        word.put("question", question);
        word.put("answer", answer);
        quesList.add(question);
        wordList.add(word);
    }

    private void initSampleDb() {
        sampleDB = null;
        try {
            sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        } catch (SQLException se) {
        }
    }

    private void initAddBtn() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAlertDialog();
            }
        });
    }

    private void initAlertDialog() {
        dialogView = View.inflate(this, R.layout.word_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("단어 등록");
        builder.setView(dialogView);
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                questionEditText = dialogView.findViewById(R.id.questionEditText);
                answerEditText = dialogView.findViewById(R.id.answerEditText);

                String ques = questionEditText.getText().toString();
                String ans = answerEditText.getText().toString();
                if (ques.isEmpty() || ans.isEmpty()) {
                    initToast("단어와 뜻 모두 입력해주세요");
                } else if (checkWord(ques)) {
                    sampleDB.execSQL("INSERT INTO " + tableName +
                            " (question, answer) Values('" + ques + "','" + ans + "');");
                    initListView();
                    initToast(ques + " 을 외웠습니다");
                } else {
                    initToast("이미 외운 단어입니다");
                }
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();

    }

    private void initToast(String str) {
        Toast toast = new Toast(RecordActivity.this);
        toastView = View.inflate(RecordActivity.this, R.layout.word_toast, null);
        toastTextView = toastView.findViewById(R.id.toastTextView);
        toastTextView.setText(str);
        toast.setView(toastView);
        toast.show();
    }

    private Boolean checkWord(String ques) {
        if (quesList.contains(ques)) return false;
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sampleDB.close();
    }
}