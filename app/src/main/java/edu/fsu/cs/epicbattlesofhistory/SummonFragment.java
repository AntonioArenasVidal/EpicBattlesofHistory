package edu.fsu.cs.epicbattlesofhistory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Node;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SummonFragment extends Fragment {

    private OnSummonFragmentInteractionListener mListener;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    ImageView character;
    Button summonBtn;
    TextView tokens;

    public SummonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootView = inflater.inflate(R.layout.fragment_summon, container, false);
        mListener = (SummonFragment.OnSummonFragmentInteractionListener) getActivity();

        character = (ImageView) rootView.findViewById(R.id.summon_character_image);
        tokens = (TextView) rootView.findViewById(R.id.tokens);
        summonBtn = (Button) rootView.findViewById(R.id.summon_button);
        summonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String summonedCharacter = "";
                // TODO: Antonio randomly generate the character from DB based on the gold logic we were speaking off
                String summon = null;
                Random rand = new Random();
                //rand.setSeed(100);

                int randomInt = rand.nextInt(100)+1;
                Log.d("Random Value", String.valueOf(randomInt));

                if (randomInt <= 10){
                    int randomGold =  rand.nextInt(5)+1;
                    switch (randomGold){
                        case 1:
                            summonedCharacter = "arthur";
                            break;
                        case 2:
                            summonedCharacter = "caesar";
                            break;
                        case 3:
                            summonedCharacter = "ozymandias";
                            break;
                        case 4:
                            summonedCharacter = "montezuma";
                            break;
                        case 5:
                            summonedCharacter = "washington";
                            break;
                    }

                }
                else if(randomInt > 10 && randomInt <= 30){
                    int randomSilver =  rand.nextInt(5)+1;
                    switch (randomSilver){
                        case 1:
                            summonedCharacter = "lancelot";
                            break;
                        case 2:
                            summonedCharacter = "vlad";
                            break;
                        case 3:
                            summonedCharacter = "robin";
                            break;
                        case 4:
                            summonedCharacter = "Bolivar";
                            break;
                        case 5:
                            summonedCharacter = "joan";
                            break;
                    }

                }
                else {
                    int randomBronze =  rand.nextInt(5)+1;
                    switch (randomBronze){
                        case 1:
                            summonedCharacter = "blackbeard";
                            break;
                        case 2:
                            summonedCharacter = "leonidas";
                            break;
                        case 3:
                            summonedCharacter = "erikson";
                            break;
                        case 4:
                            summonedCharacter = "atahualpa";
                            break;
                        case 5:
                            summonedCharacter = "cleopatra";
                            break;
                    }

                }
                String characterImageName = summonedCharacter + "_character";

                FirebaseUser user = mAuth.getCurrentUser();
                String cUser = user.getEmail();
                String usr =  cUser.replaceAll("@youremail.com", "");

                // TODO: Change token to corresponding token cound in users DB in firebase
                myRef = database.getReference("users/" + usr);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.getKey().equals("tokens")) {
                                String token = data.getValue().toString();
                                tokens.setText(token);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                String check = tokens.toString();
                if (check == "0"){
                    Toast.makeText(getContext(), "You have no more Tokens", Toast.LENGTH_LONG).show();
                }
                else {
                    int t = Integer.parseInt(tokens.toString());
                    t -= 1;
                    character.setImageResource(getResources().getIdentifier(characterImageName, "drawable", getContext().getPackageName()));
                    myRef.child("tokens").setValue(String.valueOf(t));
                }
            }
        });


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home_item:
                mListener.onMenuMyHomeClicked();
                break;
            case R.id.menu_characters_item:
                mListener.onMenuCharactersClicked();
                break;
            case R.id.menu_summon_item:
                mListener.onMenuSummonClicked();
                break;
            case R.id.menu_logout_item:
                mListener.onMenuLogoutClicked();
                break;
        }
        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSummonFragmentInteractionListener) {
            mListener = (OnSummonFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnSummonFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSummonCLicked();
        void onMenuMyHomeClicked();
        void onMenuCharactersClicked();
        void onMenuSummonClicked();
        void onMenuLogoutClicked();
        void onBattleClicked();
    }
}
