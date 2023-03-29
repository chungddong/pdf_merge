package com.sophra.pdf_merge;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MergeListActivity extends AppCompatActivity {

    ArrayList<Files> items =  new ArrayList<Files>();
    RecyclerView recyclerView;
    MergelistAdapter mergelistAdapter;
    String file_name;

    PDFMergerUtility ut = new PDFMergerUtility();

    Boolean ische[];

    Button merge;
    Button btn_back;
    Dialog dialog;

    int file_name_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mergelist);
        ische = MainActivity.ischecked;
        file_name_num = 0;

        Log.v("testwe", "" + MainActivity.mergeList);

        merge = findViewById(R.id.btn_merge);
        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recyclerViews);

        mergelistAdapter = new MergelistAdapter();

        recyclerView.setAdapter(mergelistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.v("testwe", "" + MainActivity.mergeList);

        for(int i = 0; i < MainActivity.mergeList.size(); i ++)
        {
            File mFile = new File(String.valueOf(MainActivity.mergeList.get(i)));
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
            //Log.v("testwe", "" + MainActivity.mergeList.get(i));
            items.add(new Files(MainActivity.mergeList.get(i).getName(), size));
        }

        /*for(int i = 0; i < ische.length; i++)
        {
            //Log.v("testwe", "" + ische[i]);
            if(ische[i] == true)
            {
                items.add(new Files(MainActivity.mergeList.get(i).getName(),"19.24 KB"));
            }
        }*/

        Log.v("testwe", "mergelist 길이 : " + MainActivity.mergeList.size());

        mergelistAdapter.setFileList(items);

        merge.setOnClickListener(new View.OnClickListener() {  //합치기 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                Log.v("testwe", "" + MainActivity.mergeList);

                if(MainActivity.mergeList.size() < 2)  //파일을 2개 이상 고르지 않았을 시 실행
                {
                    //Log.v("testwe" , "2개 미만으로 고름");
                    dialog = new Dialog(MergeListActivity.this);
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
                    mergebtn();
                }
            }
        });


        mergelistAdapter.setOnItemCLickListener(new MergelistAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                items.remove(position);
                //여기 수정
                mergelistAdapter.notifyDataSetChanged();
                //mergelist에서도 삭제되도록
                MainActivity.mergeList.remove(position);
                Log.v("testwe", "머지리스트 : " + MainActivity.mergeList);
                /*for(int j = 0; j < MainActivity.mergeList.size(); j++){

                    if(items.get(j).equals(MainActivity.mergeList.get(j)))
                    {
                        MainActivity.mergeList.remove(j);
                        Log.v("testwe", "실행함");
                        Log.v("testwe", "머지리스트 : " + MainActivity.mergeList);
                    }
                }*/
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() { //뒤로가기버튼 (UI상) 눌렀을때
            @Override
            public void onClick(View view) {
                dialog = new Dialog(MergeListActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_backpress);
                dialog.show();

                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
                Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });


    }

    public void mergebtn() {
        for(int i = 0; i < items.size(); i++)
        {
            try {
                ut.addSource(MainActivity.mergeList.get(i));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        dialog = new Dialog(MergeListActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_merge);
        dialog.show();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        file_name_num = pref.getInt("file_name_num", 1);  //저장된 file_name_num 값을 불러오기

        EditText et_merge = dialog.findViewById(R.id.et_merge);
        et_merge.setHint("PDF_MERGE_" + file_name_num); //기본 이름 설정

        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("testwe", "이거실행");
                dialog.dismiss();

                file_name = "";
                EditText et_merge = dialog.findViewById(R.id.et_merge);

                if(et_merge.length() != 0)
                {
                    file_name = String.valueOf(et_merge.getText());
                    MainActivity.filename = file_name;
                }
                else
                {
                    file_name = "PDF_MERGE_" + file_name_num;
                    MainActivity.filename = file_name;
                    file_name_num += 1;  //이 이름을 사용했으면 숫자 1 더해서 데이터 저장
                    saveState();
                }

                //File file = new File(Environment.getDataDirectory(), )
                File find_folder = new File(Environment.getExternalStorageDirectory(), "PDF병합");
                if(!find_folder.exists())
                {
                    find_folder.mkdir();

                }

                //Log.v("testwe", "폴더 경로 : " + Environment.getDataDirectory() + "/PDF병합");


                ut.setDestinationFileName("/storage/emulated/0/PDF병합/" + file_name +".pdf");  //저장 경로


                CheckTypesTask task = new CheckTypesTask();
                task.execute();
            }
        });
    }

    protected void saveState(){ // 데이터 저장
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("file_name_num", file_name_num);

        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ische = MainActivity.ischecked;
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {
        Dialog dialog_wait = new Dialog(MergeListActivity.this, android.R.style.Theme_Black_NoTitleBar); //다이얼로그 전체화면 하는 거


        @Override
        protected void onPreExecute() {
            dialog_wait.setContentView(R.layout.dialog_waiting);
            ProgressBar progressBar = dialog_wait.findViewById(R.id.progress_circular);

            progressBar.setIndeterminate(true);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#F34A47"), PorterDuff.Mode.MULTIPLY);
            dialog_wait.show();

            // show dialog
            //asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
                Log.v("testwe", "합쳐짐?");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //asyncDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            finish();
            startActivity(intent);
            dialog_wait.dismiss();
            Toast.makeText(MergeListActivity.this, "" + "/storage/emulated/0/Download/" + file_name +".pdf 경로에 저장되었습니다!" , Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }
    }


    @Override
    public void onBackPressed() { //뒤로 가기 눌렀을 떄
        //super.onBackPressed();
        Log.v("testwe", "뒤로 가기 누름");
        dialog = new Dialog(MergeListActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_backpress);
        dialog.show();

        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                startActivity(intent);
            }
        });
    }


}
