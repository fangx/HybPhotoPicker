package cn.fxnn.hybphotopicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.fxnn.hybphoto.ui.activity.PhotoFlowActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, PhotoFlowActivity.class);

        MainActivity.this.startActivityForResult(intent,1);


//        TextView textView = (TextView) findViewById(R.id.test);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(MainActivity.this, PhotoFlowActivity.class);
//
//                MainActivity.this.startActivityForResult(intent,1);
//
//            }
//        });

    }
}
