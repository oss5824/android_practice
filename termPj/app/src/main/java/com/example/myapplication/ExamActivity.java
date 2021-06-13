package com.example.myapplication;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ExamActivity extends AppCompatActivity {

    private final String dbName = "word";
    private final String tableName = "word";
    int size;
    ArrayList<HashMap<String, String>> wordList;
    String[] answerList = new String[10];
    ListAdapter adapter;
    ListView listView;
    SQLiteDatabase sampleDB;
    Button submitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        listView = findViewById(R.id.listView);
        submitBtn = findViewById(R.id.submitBtn);
        wordList = new ArrayList<HashMap<String, String>>();

        initSampleDb();
        initListView();
        initSubmitBtn();
        initClickListView();
    }

    private void initClickListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
                View v = view.inflate(ExamActivity.this, R.layout.list_item_answer, null);
                int idx = adapter.getItem(position).toString().indexOf(',');
                String title = intToString(position + 1) + ". "
                        + adapter.getItem(position).toString().substring(10, idx);
                builder.setTitle(title);
                builder.setPositiveButton("쓰기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = v.findViewById(R.id.answerEditText);
                        String answerEditText = editText.getText().toString();
                        answerList[position] = answerEditText;
                    }
                });
                builder.setNegativeButton("닫기", null);
                builder.setView(v);
                builder.show();
            }
        });
    }

    private void initSubmitBtn() {
        if (size == 0) {
            submitBtn.setText("외운단어 목록이 존재하지 않습니다");
        } else {
            submitBtn.setText("제출하기");
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cnt = 0;
                String wrongAnswer = "-------오답 목록------\n";
                for (int i = 0; i < size; i++) {
                    String s = wordList.get(i).toString();
                    String substring = s.substring(s.lastIndexOf('=') + 1, s.length() - 1);
                    String quesString = s.substring(s.indexOf('=') + 1, s.indexOf(','));
                    if (substring.equals(answerList[i])) cnt++;
                    else {
                        wrongAnswer = wrongAnswer + intToString(i + 1) + '.' + quesString + ':' + substring + '\n';
                    }
                    Log.d("답 : ", substring + ' ' + answerList[i]);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
                builder.setTitle(intToString(size) + "개 중에서 " + intToString(cnt) + "개가 정답입니다.\n");
                builder.setMessage(wrongAnswer);
                builder.setNegativeButton("홈으로 돌아가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();

            }
        });
    }

    private String intToString(int n) {
        return String.valueOf(n);
    }

    private void initListView() {
        try {
            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);
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
            Collections.shuffle(wordList);
            size = (wordList.size() > 10) ? 10 : wordList.size();
            wordList.subList(0, size);
            adapter = new SimpleAdapter(
                    this, wordList, R.layout.list_item_exam,
                    new String[]{"question"}, new int[]{R.id.questionExamTextView}
            );
            listView.setAdapter(adapter);
        } catch (SQLException se) {
        }
    }

    private void initSampleDb() {
        sampleDB = null;
        try {
            sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        } catch (SQLException se) {
        }
    }

    private void addWordToList(String question, String answer) {
        HashMap<String, String> word = new HashMap<String, String>();

        word.put("question", question);
        word.put("answer", answer);
        wordList.add(word);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sampleDB.close();
    }
}
