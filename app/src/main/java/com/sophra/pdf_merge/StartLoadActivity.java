package com.sophra.pdf_merge;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StartLoadActivity extends AppCompatActivity {

    public static List<File> FileLIST;
    private final int PERMISSIONS_REQUEST_RESULT = 1;

    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_loading);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE);

        //ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE }, 1);




        /*Intent intent = new Intent();
        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);*/

        //참고 : https://ourcodeworld.com/articles/read/1559/how-does-manage-external-storage-permission-work-in-android
        //위에 코드 모든 파일 허용 켜는 창임 - 권환 확인 후 없으면 저거 띄우는 식으로 구현
        //여기 작업하기

    }

    @Override
    protected void onResume() {
        super.onResume();

        // ******* -> 안드로이드 11 아닌 경우도 권한 요청 하게 설정해야함

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //안드로이드 11이 경우
            if (Environment.isExternalStorageManager()){ //권한이 허용되어 있을 경우
                FileLIST = new ArrayList<>();
                Search_Dir(Environment.getExternalStorageDirectory());
                startActivity(new Intent(this, MainActivity.class));
                finish();
                //아마 이거 try/catch 넣어서 광고 넣고 하기
            }else{ //권한이 허용 안되어있을 때

                Uri uri = Uri.fromParts("package", this.getPackageName(), null);

                dialog = new Dialog(StartLoadActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_backpress);
                dialog.show();

                TextView dialog_title = dialog.findViewById(R.id.dialog_title);
                TextView dialog_text = dialog.findViewById(R.id.dialog_text);

                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
                Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

                dialog_title.setText("권한 요청");
                dialog_text.setText("모든 PDF 파일을 보여주기 위해서는 파일 접근 권한이 필요합니다. 이 앱은 사용자의 어떠한 정보도 수집하거나 전송하지 않습니다.");
                btn_confirm.setText("설정");

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                    }
                });

                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });



            }
        }
    }

    public void Search_Dir(File dir) {
        String pdfPattern = ".pdf";

        File FileList[] = dir.listFiles();
        ArrayList<String> test = new ArrayList<>();


        if (FileList != null) {
            for (int i = 0; i < FileList.length; i++) {

                if (FileList[i].isDirectory()) {
                    Search_Dir(FileList[i]);
                } else {
                    if (FileList[i].getName().endsWith(pdfPattern)) {
                        //here you have that file.
                        //Log.v("testwe", String.valueOf(FileList[i]));
                        FileLIST.add(new File(FileList[i].getPath()));

                    }
                }
            }
        }
    }
}
