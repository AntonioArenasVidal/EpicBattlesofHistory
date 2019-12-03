package edu.fsu.cs.epicbattlesofhistory;

import android.content.Context;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment {
    private OnLoginFragmentInteractionListener mListener;
    // these three lines create firebase instances so the app can communicate with the database
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    Button backBtn;
    Button loginBtn;
    EditText usrInput;
    EditText passInput;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mListener = (LoginFragment.OnLoginFragmentInteractionListener) getActivity();

        backBtn = (Button) rootView.findViewById(R.id.login_back_btn);
        loginBtn = (Button) rootView.findViewById(R.id.login_login_btn);
        usrInput = (EditText) rootView.findViewById(R.id.login_usr_input);
        passInput = (EditText) rootView.findViewById(R.id.login_pass_input);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.backToMain();
            }
        });

        //user clicks on login after entering login info
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //error checking
                if(usrInput.length() == 0)
                    usrInput.setError("Enter Username");
                if(passInput.length() == 0)
                    passInput.setError("Enter Password");
                if( (usrInput.length() != 0) && (passInput.length() != 0) ) {
                    mListener.onLoggedIn();
                    //if local tests pass, moves to matching the input to the database
                    //reference to the root node
//                    myRef = database.getReference("Users");
//                    //searches for a username node in the db
//                    myRef.child(usrInput.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            //if username exists. success
//                            if(dataSnapshot.exists()){
//                                Log.w(" success", "username found in db");
//                                //now a query for the password
//                                Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("password").equalTo(passInput.getText().toString());
//                                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        //if password matches. success.
//                                        if(dataSnapshot.exists()){
//                                            Log.w("success", "password found in db");
//                                            //now that we confirmed the user, we can sign in using the given username and password
//                                            mAuth = FirebaseAuth.getInstance();
//                                            //We append "@youremail.com" to the username to trick firebase that we are using an email address for login
//                                            mAuth.signInWithEmailAndPassword(usrInput.getText().toString()+"@youremail.com", passInput.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                                    //once we sign in we can move to the next fragment
//                                                    mListener.onLoggedIn();
//                                                }
//                                            });
//
//                                        }
//                                        else{
//                                            //if password not found logs error and displays it in the password field.
//                                            Log.w("error", "password not found in db");
//                                            passInput.setError("incorrect password");
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                            }
//                            else {
//                                //if username not found logs error and displays it in username field
//                                Log.w("error", "username not found in db");
//                                usrInput.setError("Username not found");
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
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

    public interface OnLoginFragmentInteractionListener {
        void backToMain();
        void onLoggedIn();
    }
}
