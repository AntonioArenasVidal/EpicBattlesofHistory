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
                   // mListener.onLoggedIn();
                    //if local tests pass, moves to matching the input to the database
                    //reference to the root node

                    mAuth = FirebaseAuth.getInstance();
                    //We append "@youremail.com" to the username to trick firebase that we are using an email address for login
                    mAuth.signInWithEmailAndPassword(usrInput.getText().toString()+"@youremail.com", passInput.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //once we sign in we can move to the next fragment
                            if(task.isSuccessful()){
                                mListener.onLoggedIn();
                                Log.d("Login: ", "success");
                            }

                            else{
                                Log.d("Login: ", "failed");
                                usrInput.setError("incorrect username or password");
                                passInput.setError("incorrect username or password");
                            }
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
