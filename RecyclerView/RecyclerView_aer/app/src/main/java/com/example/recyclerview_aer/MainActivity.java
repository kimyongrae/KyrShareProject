package com.example.recyclerview_aer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;
    private int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        mArrayList = new ArrayList<>();

//        mAdapter = new CustomAdapter( mArrayList);

        mAdapter = new CustomAdapter( this, mArrayList);

        mRecyclerView.setAdapter(mAdapter);


/*        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);*/



        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                count++;
//
//                // Dictionary 생성자를 사용하여 ArrayList에 삽입할 데이터를 만듭니다.
//                Dictionary data = new Dictionary(count+"","Apple" + count, "사과" + count);
//
//                //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
//                mArrayList.add(data); // RecyclerView의 마지막 줄에 삽입
//
//                mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.edit_box, null, false);
                builder.setView(view);
                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                final EditText editTextID = (EditText) view.findViewById(R.id.edittext_dialog_id);
                final EditText editTextEnglish = (EditText) view.findViewById(R.id.edittext_dialog_endlish);
                final EditText editTextKorean = (EditText) view.findViewById(R.id.edittext_dialog_korean);

                ButtonSubmit.setText("삽입");


                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String strID = editTextID.getText().toString();
                        String strEnglish = editTextEnglish.getText().toString();
                        String strKorean = editTextKorean.getText().toString();

                        Dictionary dict = new Dictionary(strID, strEnglish, strKorean );

                        mArrayList.add(0, dict); //첫 줄에 삽입
                        //mArrayList.add(dict); //마지막 줄에 삽입
                        mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


    }
}