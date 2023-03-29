package com.sophra.pdf_merge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<File> pdfList;
    public static List<File> mergeList;
    public static Boolean[] ischecked;

    public static String filename;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfList = new ArrayList<>();
        mergeList = new ArrayList<>();
        ischecked = new Boolean[0];

        pdfList = StartLoadActivity.FileLIST;


        LinearLayout add = findViewById(R.id.btn_main_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "파일 추가 버튼 누름", Toast.LENGTH_SHORT).show();
                Log.v("pdfuitest", "파일 추가 버튼 누름");

                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                startActivity(intent);
            }


        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //앱 종료 묻는 다이얼로그 띄우기
        Log.v("testwe", "뒤로 가기 누름");
        dialog = new Dialog(MainActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_backpress);
        dialog.show();

        TextView dialog_title = dialog.findViewById(R.id.dialog_title);
        TextView dialog_text = dialog.findViewById(R.id.dialog_text);

        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

        dialog_title.setText("앱 종료하기");
        dialog_text.setText("앱을 종료하시겠습니까?");

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() { //앱 종료시키기
            @Override
            public void onClick(View view) {
                moveTaskToBack(true); // 태스크를 백그라운드로 이동
                finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기

                System.exit(0);
            }
        });
    }
}