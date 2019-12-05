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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SummonFragment extends Fragment {

    private OnSummonFragmentInteractionListener mListener;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    ImageView character;
    Button summonBtn;
    TextView tokens;
    String summonedCharacter = "";
    String characterImageName = "";

    FirebaseUser user = mAuth.getCurrentUser();
    String cUser = user.getEmail();
    String dbTokens = "";
    final String usr =  cUser.replaceAll("@youremail.com", "");

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
        myRef = database.getReference("Users/" + usr);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String f;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().equals("tokens")) {
                        String token = data.getValue().toString();
                        tokens.setText(token);
                        String check = tokens.getText().toString();
                        if (check.equals("0")){
                            //Toast.makeText(getContext(), "You have no more Tokens", Toast.LENGTH_LONG).show();
                            tokens.setError("You have no more tokens");
                            summonBtn.setClickable(false);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        summonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = mAuth.getCurrentUser();
                String cUser = user.getEmail();
                String usr =  cUser.replaceAll("@youremail.com", "");

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
                    Toast.makeText(getContext(), "Gold Character!!!\n" + summonedCharacter, Toast.LENGTH_SHORT).show();

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
                            summonedCharacter = "bolivar";
                            break;
                        case 5:
                            summonedCharacter = "joan";
                            break;
                    }
                    Toast.makeText(getContext(), "Silver Character!\n" + summonedCharacter, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getContext(), "Bronze Character\n" + summonedCharacter, Toast.LENGTH_SHORT).show();
                }
                characterImageName = summonedCharacter + "_character";


                myRef = database.getReference("Users/" + usr);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String characters;
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            if(data.getKey().equals("tokens")){
                               dbTokens = data.getValue().toString();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                myRef = database.getReference("Users/" + usr);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String c;
                        //again character field
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if(data.getKey().equals("tokens")){
                                dbTokens = data.getValue().toString();
                                Log.d("myTOkens", dbTokens);
                                Log.d("wh", "at");
                                tokens.setText(dbTokens);
                            }
                            if (data.getKey().equals("Characters")) {
                                c = data.getValue().toString();
                                final String[] cL = c.split(" ");
                                if(Arrays.asList(cL).contains(summonedCharacter)) {
                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String f;
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                if (data.getKey().equals("tokens")) {
                                                    String token = data.getValue().toString();
                                                    tokens.setText(token);
                                                    String check = tokens.getText().toString();
                                                    if (check.equals("0")){
                                                        tokens.setError("You have no more tokens");
                                                        summonBtn.setClickable(false);
                                                    }
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    Toast.makeText(getContext(), "You already own this character " + summonedCharacter, Toast.LENGTH_SHORT).show();
                                    character.setImageResource(getResources().getIdentifier(characterImageName, "drawable", getContext().getPackageName()));
                                }
                                else {
                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String f;
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                if (data.getKey().equals("tokens")) {
                                                    String token = data.getValue().toString();
                                                    tokens.setText(token);
                                                }
                                            }
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                if (data.getKey().equals("Characters")) {
                                                    f = data.getValue().toString();
                                                    if (f.isEmpty())
                                                        f = summonedCharacter;
                                                    else
                                                        f = f + " " + summonedCharacter;
                                                    final Map<String, Object> Character = new HashMap<>();
                                                    Character.put("Characters", f);
                                                    myRef.updateChildren(Character).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                character.setImageResource(getResources().getIdentifier(characterImageName, "drawable", getContext().getPackageName()));
                                                                Log.w("DB success", "character added " + summonedCharacter);
                                                            } else
                                                                Log.w("DB fail", "character not added");
                                                        }
                                                    });

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                String check = tokens.getText().toString();
                                if (check.equals("0")){
                                    tokens.setError("You have no more tokens");
                                    summonBtn.setClickable(false);
                                }
                                else {
                                    if (dbTokens.equals("0")){
                                        tokens.setError("You have no more tokens");
                                        summonBtn.setClickable(false);
                                    } else {
                                        dbTokens = Integer.toString((Integer.parseInt(dbTokens) - 1));
                                        tokens.setText(dbTokens);
                                        final Map<String, Object> token = new HashMap<>();
                                        token.put("tokens", dbTokens);
                                        myRef.updateChildren(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Log.w("DB SUCCESS", "removed token");
                                                }
                                                else {
                                                    Log.w("DB FAIL", "did not remove token");
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
        void onSummonCLicked();
        void onMenuMyHomeClicked();
        void onMenuCharactersClicked();
        void onMenuSummonClicked();
        void onMenuLogoutClicked();
        void onBattleClicked();
    }
}
