package com.example.tms_caretaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Bills extends AppCompatActivity {

    Spinner spinner;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://tms006-7c621-default-rtdb.firebaseio.com/");
    List<String> names;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        getSupportActionBar().hide();

        spinner=findViewById(R.id.List);
        names=new ArrayList<>();
        Button submit=findViewById(R.id.submit);
        EditText amt=findViewById(R.id.Amount);
        EditText desc=findViewById(R.id.Description);

        databaseReference= FirebaseDatabase.getInstance().getReference();





        databaseReference.child("UsersForBills").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot childSnapshot:snapshot.getChildren())
                {
                    String SpinName = childSnapshot.child("door_no").getValue(String.class);
                    names.add(SpinName);
                }
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(Bills.this, android.R.layout.simple_spinner_dropdown_item,names);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String door=spinner.getSelectedItem().toString();
                String amts=amt.getText().toString();
                String des=desc.getText().toString();

                if(amts.isEmpty() || des.isEmpty())  {

                    Toast.makeText(Bills.this, "Fields cant be empty", Toast.LENGTH_SHORT).show();

                }else {

                    databaseReference.child("UsersForBills").child(door).child("amount").setValue(amts);
                    databaseReference.child("UsersForBills").child(door).child("b_description").setValue(des);

                    Toast.makeText(Bills.this, "Bill Sent", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(Bills.this,MainActivity.class));
                }
            }
        });

    }
}