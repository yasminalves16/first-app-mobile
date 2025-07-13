package com.example.aulaspraticas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Calculadora extends AppCompatActivity {

    private EditText etNumber1;
    private EditText etNumber2;
    private TextView tvResult;
    private int resultado = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculadora);

        etNumber1 = findViewById(R.id.editTextNumber1);
        etNumber2 = findViewById(R.id.editTextNumber2);
        tvResult = findViewById(R.id.textViewResult);

    }

    public void setSum(View view){
        String firstValueText = etNumber1.getText().toString();
        String secondValueText = etNumber2.getText().toString();

        int firstValueTextToNumber = Integer.parseInt(firstValueText);
        int secondValueTextToNumber = Integer.parseInt(secondValueText);

        resultado = firstValueTextToNumber + secondValueTextToNumber;
    }

    public void setSubtract(View view){
        String firstValueText = etNumber1.getText().toString();
        String secondValueText = etNumber2.getText().toString();

        int firstValueTextToNumber = Integer.parseInt(firstValueText);
        int secondValueTextToNumber = Integer.parseInt(secondValueText);

        resultado = firstValueTextToNumber - secondValueTextToNumber;
    }

    public void setMultiply(View view){
        String firstValueText = etNumber1.getText().toString();
        String secondValueText = etNumber2.getText().toString();

        int firstValueTextToNumber = Integer.parseInt(firstValueText);
        int secondValueTextToNumber = Integer.parseInt(secondValueText);

        resultado = firstValueTextToNumber * secondValueTextToNumber;
    }

    public void setDivide(View view){
        String firstValueText = etNumber1.getText().toString();
        String secondValueText = etNumber2.getText().toString();

        int firstValueTextToNumber = Integer.parseInt(firstValueText);
        int secondValueTextToNumber = Integer.parseInt(secondValueText);

        resultado = firstValueTextToNumber / secondValueTextToNumber;
    }

    public void showResult(View view){
        tvResult.setText(String.valueOf(resultado));
    }
    public void cleanResult(View view){
        tvResult.setText(String.valueOf(' '));
    }

}