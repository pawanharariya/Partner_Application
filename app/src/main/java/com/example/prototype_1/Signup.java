package com.example.prototype_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Signup extends AppCompatActivity {

    EditText name,id,password,password2;
    String mobile;
    Spinner s1;
    Button b1;
    TextView userLogin;
    DatabaseReference databasepartner,databaseReference;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databasepartner = FirebaseDatabase.getInstance("https://customerprototype-29375-fbcfa.firebaseio.com/")
                .getReference();

        mFirebaseAuth=FirebaseAuth.getInstance();
        // as now we have access to both the db we can look at the customer profile as well...


        //

        name=(EditText)findViewById(R.id.nam);
        id=(EditText)findViewById(R.id.email);
        Intent intent = getIntent();
         mobile = intent.getStringExtra("mobile");
        password=(EditText)findViewById(R.id.editText2);
        password2=(EditText)findViewById(R.id.editText3);
        userLogin=(TextView)findViewById(R.id.tvUserLogin);

        s1=(Spinner)findViewById(R.id.spinner);
        b1=(Button)findViewById(R.id.button4);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            // Upon hitting the button it must verify the email address and then redirect to login page...
            public void onClick(View v) {

                    addata();
            }
        });

        userLogin=(TextView)findViewById(R.id.tvUserLogin);
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this,Login.class));
            }
        });
    }

    public void addata(){
        String uname=name.getText().toString();
        String em= id.getText().toString();

        String p1=(String)password.getText().toString();
        String genre=s1.getSelectedItem().toString();
        String user_name=getid();
        validate();
        String id= databasepartner.push().getKey();
        profile p= new profile(uname,em,mobile,p1,genre,user_name);

        createLogin(em,p1);
        databasepartner.child(id).setValue(p);


    }

    private void createLogin(String em, String p1) {

        mFirebaseAuth.createUserWithEmailAndPassword(em,p1).addOnCompleteListener (Signup.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "User Created!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signup.this, Login.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Try again after sometime",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validate(){

        boolean result=false;

        String uname=name.getText().toString();
        String em= id.getText().toString();
        String p1=(String)password.getText().toString();
        String p2=(String)password2.getText().toString();

        if(uname.isEmpty() && em.isEmpty() && mobile.isEmpty() &&p1.isEmpty() && p2.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please enter all the details",Toast.LENGTH_LONG).show();

        }

        else{
            boolean check=checkpass(p1,p2);

            if (check==true) {
                result=true;
            }
            else{
                Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_LONG).show();
            }
        }
        return  result;

    }
    public boolean checkpass(String p1,String p2){
        boolean ans= (p1.matches(p2));

        return  ans;
    }
    public static String getid(){

       /*
        databaseReference = FirebaseDatabase.getInstance("https://customerprototype-29375-fbcfa.firebaseio.com/")

                .getReference("Id");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id=dataSnapshot.child("id").getValue().toString();
                newVersion = "usr" + (Integer.parseInt(id.substring(3,id.length()))+1);
                commit(newVersion);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        */

       int length_of_id=6;
       final String characters="abcdefijklmnopqrstuvwxyz0123456789";
       StringBuilder result= new StringBuilder();
       while(length_of_id > 0){
           Random rand= new Random();
           result.append(characters.charAt(rand.nextInt(characters.length())));
           length_of_id--;
       }
       return result.toString();

    }

   // one case with when the random id generated is already assigned to another user in the database...

}
