package com.admission.educationapp.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.admission.educationapp.Bsession;
import com.admission.educationapp.R;

public class ReferalActivity extends AppCompatActivity {

    Button refer,skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_referal);
        refer=findViewById(R.id.btn_refer);
        skip=findViewById(R.id.btn_skip);
        refer.setOnClickListener(v -> {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "APP LINK : " + "https://play.google.com/store/apps/details?id=com.admission.educationapp"+ "\n" + "Refer-ID = " + Bsession.getInstance().getUser_id(ReferalActivity.this);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });
        skip.setOnClickListener(v ->{
            Intent intent=new Intent(ReferalActivity.this,RegisterActivity.class);
            startActivity(intent);
        });
    }
}