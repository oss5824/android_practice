package com.example.android;
//push test file
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edit1,edit2;
    Button btnAdd,btnSub,btnMul,btnDiv;
    TextView textResult;
    String num1,num2;
    Integer result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("초간단 계산기");
        edit1 = (EditText)findViewById(R.id.Edit1);
        edit2 = (EditText)findViewById(R.id.Edit2);
        btnAdd = findViewById(R.id.BtnAdd);
        btnSub = findViewById(R.id.BtnSub);
        btnMul = findViewById(R.id.BtnMul);
        btnDiv = findViewById(R.id.BtnDiv);
        textResult=findViewById(R.id.TextResult);
        boolean emptyInput=false;
        btnAdd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(edit1.getText().toString().isEmpty()||
                        edit2.getText().toString().isEmpty())
                {
                    textResult.setText("입력이 비어있습니다.");
                }
                else {
                    num1 = edit1.getText().toString();
                    num2 = edit2.getText().toString();
                    result = Integer.parseInt(num1) + Integer.parseInt(num2);
                    textResult.setText("계산 결과 : " + result.toString());
                }
                return false;
            }
        });
        btnSub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(edit1.getText().toString().isEmpty()||
                        edit2.getText().toString().isEmpty())
                {
                    textResult.setText("입력이 비어있습니다.");
                }
                else {
                    num1 = edit1.getText().toString();
                    num2 = edit2.getText().toString();
                    result = Integer.parseInt(num1) - Integer.parseInt(num2);
                    textResult.setText("계산 결과 : " + result.toString());
                }
                return false;
            }
        });
        btnMul.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(edit1.getText().toString().isEmpty()||
                        edit2.getText().toString().isEmpty())
                {
                    textResult.setText("입력이 비어있습니다.");
                }
                else {
                    num1 = edit1.getText().toString();
                    num2 = edit2.getText().toString();
                    result = Integer.parseInt(num1) * Integer.parseInt(num2);
                    textResult.setText("계산 결과 : " + result.toString());
                }
                return false;
            }
        });
        btnDiv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(edit1.getText().toString().isEmpty()||
                        edit2.getText().toString().isEmpty())
                {
                    textResult.setText("입력이 비어있습니다.");
                }
                else {
                    num1 = edit1.getText().toString();
                    num2 = edit2.getText().toString();
                    result = Integer.parseInt(num1) / Integer.parseInt(num2);
                    textResult.setText("계산 결과 : " + result.toString());
                }
                return false;
            }
        });
    }
}
