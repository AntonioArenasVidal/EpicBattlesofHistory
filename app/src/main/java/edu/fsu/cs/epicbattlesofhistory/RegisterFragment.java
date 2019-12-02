package edu.fsu.cs.epicbattlesofhistory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment{
    private OnRegisterFragmentInteractionListener mListener;
    // these three lines create firebase instances so the app can communicate with the database
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    Button backBtn;
    Button registerBtn;
    EditText usrInput;
    EditText passInput;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        mListener = (RegisterFragment.OnRegisterFragmentInteractionListener) getActivity();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Map<String, Object> user=new HashMap<>();

        backBtn = (Button) rootView.findViewById(R.id.register_back_btn);
        registerBtn = (Button) rootView.findViewById(R.id.register_register_btn);
        usrInput = (EditText) rootView.findViewById(R.id.register_usr_input);
        passInput = (EditText) rootView.findViewById(R.id.register_pass_input);

        mAuth = FirebaseAuth.getInstance();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.backToMain();
            }
        });

        //clicking on register
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int flag = 0;

                //error checking. if there is no input, if user starts with capital
                //or if password less than 6 chars long sets error
                if(Character.isUpperCase(usrInput.getText().toString().charAt(0))){
                    usrInput.setError("Username cannot start with a capital letter");
                 //   flag = 1;
                }
                else if(usrInput.length() == 0){
                    usrInput.setError("Enter Username");
                 //   flag = 1;
                }
                else if(passInput.length() < 6){
                    passInput.setError("Password needs to be at least 6 characters long");
                //    flag = 1;
                }
                else if( (usrInput.length() != 0) && (passInput.length() != 0) ) {

                    //if it passes all local tests calls the database to check if user already exists
                    myRef = database.getReference("Users");
                    myRef.child(usrInput.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                usrInput.setError("Username Already Exists");
                                Log.w("Db error", "username already exists");
                            }
                            //if it passes the db test, adds the user
                            else {
                                usrInput.setError(null);

                                //creates fields for the input received and also some empty ones to be added later
                                //all these fields get pushed into a hashmap and that gets pushed to db
                                user.put("user", usrInput.getText().toString());
                                user.put("password", passInput.getText().toString());
                                //user.put("TimeStamp", "");
                                user.put("Characters", "");
                                user.put("friendLocation", "");

                                //this block creates the user and pushes the userdata to database.
                                //in database the authentication and the data is saved separately
                                //for now if you want to delete a user, you must remove their "email" in authentication tab
                                //then delete the userdata in the realtime database

                                //database authentication requires email address, so there is a fake email address appended to the name
                                //users will be able to register and sign in using just their username
                                mAuth.createUserWithEmailAndPassword(usrInput.getText().toString()+"@youremail.com",passInput.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Log.w("DB Success", "Registration success");
                                            //reference to the Users node in db.
                                            //for this project Users is the root
                                            myRef = database.getReference("Users");
                                            //pushes the new userdata as a child node, with the username as "primary key"
                                            myRef.child(usrInput.getText().toString()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.w("DB success", "added data to DB");
                                                        mListener.onRegistered();
                                                    }
                                                }
                                            });

                                        }
                                        else
                                            Log.w("DB error", "could not authorize user");

                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRegisterFragmentInteractionListener {
        void backToMain();
        void onRegistered();
    }


}