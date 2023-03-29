package com.sophra.pdf_merge;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    Button btn_back;
    Button btn_add_file;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    ArrayList<Files> items =  new ArrayList<Files>();

    Dialog dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);


        btn_back = findViewById(R.id.btn_back);
        btn_add_file = findViewById(R.id.btn_add_file);



        //Log.v("testwe","" + pdfList);
        MainActivity.mergeList = new ArrayList<>();

        for(int i = 0; i < MainActivity.pdfList.size(); i ++)
        {
            File mFile = new File(String.valueOf(MainActivity.pdfList.get(i)));
            long filesize = mFile.length();
            filesize = filesize / 1024;
            String size;
            //만약 파일 용량이 1000 kb 이상이면 mb 로 표시되도록 코드 작성
            if(filesize >= 1000)
            {
                filesize = filesize / 1024; //to MB
                size = Long.toString(filesize) + " MB";
            }
            else
            {
                size = Long.toString(filesize) + " KB";
            }
            Log.v("testwe", "" + size);
            items.add(new Files(MainActivity.pdfList.get(i).getName(),size));
        }

        //아래는 테스트 데이터들 최종 빌드때 지워도 됨
        /*items.add(new Files("PDF_MERGE_1.pdf", "14.72 KB"));
        items.add(new Files("PDF_MERGE_2.pdf", "19.35 KB"));
        items.add(new Files("PDF_MERGE_3.pdf", "9.32 KB"));
        items.add(new Files("PDF_MERGE_4.pdf", "112 KB"));
        items.add(new Files("PDF_MERGE_5.pdf", "98.3 KB"));
        items.add(new Files("PDF_MERGE_6.pdf", "4.9 KB"));
        items.add(new Files("PDF_MERGE_6.pdf", "4.9 KB"));
        items.add(new Files("PDF_MERGE_6.pdf", "4.9 KB"));
        items.add(new Files("PDF_MERGE_6.pdf", "4.9 KB"));
        items.add(new Files("PDF_MERGE_6.pdf", "4.9 KB"));*/


        btn_back.setOnClickListener(new View.OnClickListener() {  //뒤로가기 버튼 눌렀을때 - backpressed 아님

            @Override
            public void onClick(View view) {
                //뒤로가기 기능 만들어야 함
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_add_file.setOnClickListener(new View.OnClickListener() {  //파일 추가 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                if(MainActivity.mergeList.size() < 2)  //파일을 2개 이상 고르지 않았을 시 실행
                {
                    //Log.v("testwe" , "2개 미만으로 고름");
                    dialog = new Dialog(SelectActivity.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_add_error);
                    dialog.show();

                    Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

                    TextView dialog_title = dialog.findViewById(R.id.dialog_title);
                    TextView dialog_text = dialog.findViewById(R.id.dialog_text);

                    dialog_title.setText("병합 오류");
                    dialog_text.setText("2개 이상의 파일을 선택해주세요!");

                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                else {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MergeListActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView = findViewById(R.id.recyclerViews);

        recyclerAdapter = new RecyclerAdapter();

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter.setFileList(items);

        recyclerAdapter.notifyDataSetChanged(); //체크되어 있는거 초기화 하는 거

        MainActivity.mergeList = new ArrayList<>();
        MainActivity.ischecked = new Boolean[MainActivity.pdfList.size()];


        for(int i = 0; i < MainActivity.ischecked.length; i ++)
        {
            MainActivity.ischecked[i] = false;
            //Log.v("testwe", MainActivity.ischecked[i] + "");
        }
    }
}
