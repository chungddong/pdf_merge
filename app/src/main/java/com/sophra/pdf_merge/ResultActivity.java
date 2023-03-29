package com.sophra.pdf_merge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {

    Button btn_home;
    Button btn_open_file;
    Button btn_share_file;
    Button btn_back;

    TextView result_name;
    TextView result_storage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String file_name = MainActivity.filename;

        File mFile = new File("/storage/emulated/0/PDF병합/" + file_name +".pdf");
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
        result_storage = findViewById(R.id.result_storage);
        result_storage.setText(size);

        //여기다가 결과 용량 입력되게

        result_name = findViewById(R.id.result_name);
        result_name.setText(MainActivity.filename + ".pdf");

        btn_home = findViewById(R.id.btn_home);
        btn_back = findViewById(R.id.btn_back);
        btn_share_file = findViewById(R.id.btn_share_file);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(inent);
            }
        });
        ImageView imageView = findViewById(R.id.imgview);
        Bitmap bitmap;

        File file = mFile;



        ParcelFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //min. API Level 21
        PdfRenderer pdfRenderer = null;
        try {
            pdfRenderer = new PdfRenderer(fileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final int pageCount = pdfRenderer.getPageCount();

        //첫번째 페이지 보이기
        PdfRenderer.Page rendererPage = pdfRenderer.openPage(0);
        int rendererPageWidth = rendererPage.getWidth();
        int rendererPageHeight = rendererPage.getHeight();
        bitmap = Bitmap.createBitmap(rendererPageWidth, rendererPageHeight, Bitmap.Config.ARGB_8888);
        rendererPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        imageView.setImageBitmap(bitmap);
        rendererPage.close();

        pdfRenderer.close();
        try {
            fileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn_open_file = findViewById(R.id.btn_open_file);
        btn_open_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = mFile.getPath().toString();
                openPDF(mFile.getName());

                //나중에 여기 try/catch 로 뷰어 없을시 예외처리 해줘야함 - 북마크 참조
            }
        });

        btn_share_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = mFile.getName();

                File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+ name);
                Uri uri= FileProvider.getUriForFile(ResultActivity.this,"com.sophra.pdf_merge"+".provider",file);
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setDataAndType(uri,"application/pdf");
                i.putExtra(Intent.EXTRA_STREAM, uri);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(i,"PDF 공유"));

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() { // UI 상 뒤로가기 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void openPDF(String name) {

        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+ name);
        Uri uri= FileProvider.getUriForFile(ResultActivity.this,"com.sophra.pdf_merge"+".provider",file);
        Intent i=new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(uri,"application/pdf");
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }
}
