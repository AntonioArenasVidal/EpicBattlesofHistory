package edu.fsu.cs.epicbattlesofhistory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BattleFragment extends Fragment {


    private OnBattleFragmentInteractionListener mListener;
    ImageView enemyImage;
    ImageView userImageHP;
    ImageView enemyImageHP;
    Button attackButton;
    TextView enemyHP;
    TextView userHP;
    TextView battleResult;
    Boolean battleFlag = false;
    String summonedCharacter = "arthur";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    DatabaseReference myRef2;

    //we are signed in so get current username using these lines
    FirebaseUser usr = mAuth.getCurrentUser();
    String cUser = usr.getEmail();
    //we remove the @youremail.com part from the string in order to get the current username saved in a string
    String UserName = cUser.replaceAll("@youremail.com", "");
    String dbTokens = "";

    public BattleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_battle, container, false);
        mListener = (OnBattleFragmentInteractionListener) getActivity();

        // TODO: connect to appropriate background location

        enemyImage = (ImageView) rootView.findViewById(R.id.battle_enemy_image);
        userImageHP = (ImageView) rootView.findViewById(R.id.battle_user_hp_image);
        enemyImageHP = (ImageView) rootView.findViewById(R.id.battle_enemy_hp_image);
        enemyHP = (TextView) rootView.findViewById(R.id.battle_enemy_hp);
        userHP = (TextView) rootView.findViewById(R.id.battle_user_hp);
        attackButton = (Button) rootView.findViewById(R.id.battle_attack_button);
        battleResult = (TextView) rootView.findViewById(R.id.battle_result_label);


        // TODO: Connect it to the appropriate user being clicked on
        Bundle args = getArguments();

        if (args != null) {
            summonedCharacter = args.getString("enemy");
        }


        switch(summonedCharacter) {
            case "ozymandias": rootView.setBackgroundResource(R.drawable.ozymandias); break;
            case "arthur": rootView.setBackgroundResource(R.drawable.arthur); break;
            case "montezuma": rootView.setBackgroundResource(R.drawable.montezuma); break;
            case "washington": rootView.setBackgroundResource(R.drawable.george_washington); break;
            case "julius_caesar": rootView.setBackgroundResource(R.drawable.julius_caesar); break;
        }

        String characterImageName = summonedCharacter + "_character";
        userImageHP.setImageResource(getResources().getIdentifier(characterImageName, "drawable", getContext().getPackageName()));
        String enemyImageName = summonedCharacter + "_enemy";
        enemyImage.setImageResource(getResources().getIdentifier(enemyImageName, "drawable", getContext().getPackageName()));
        enemyImageHP.setImageResource(getResources().getIdentifier(enemyImageName, "drawable", getContext().getPackageName()));

        Intent myIntent = new Intent(rootView.getContext(), MyMediaService.class);
        myIntent.setAction("PLAY_EPIC");
        getActivity().startService(myIntent);

        attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rand = new Random();
                Integer userAttackPower = rand.nextInt(10)+1;
                if(Integer.parseInt(enemyHP.getText().toString()) <= 0) {
                    battleResult.setText("You Won!");
                    attackButton.setClickable(false);
                } else {
                    enemyHP.setText(Integer.toString(Integer.parseInt(enemyHP.getText().toString()) - userAttackPower));
                }
                // check again after attacking
                if(Integer.parseInt(enemyHP.getText().toString()) <= 0) {
                    battleResult.setText("You Won!");
                    attackButton.setClickable(false);


                    myRef = database.getReference("Users/"+ UserName);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String tokens = "";
                            //again character field
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (data.getKey().equals("tokens")) {
                                    dbTokens = data.getValue().toString();
                                    Log.d("myTOkens", dbTokens);
                                    Log.d("wh", "at");
                                }
                            }
                            dbTokens = Integer.toString((Integer.parseInt(dbTokens) +5));
                            final Map<String, Object> token = new HashMap<>();
                            token.put("tokens", dbTokens);
                            myRef.updateChildren(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.w("DB SUCCESS", "removed token");
                                    } else {
                                        Log.w("DB FAIL", "did not remove token");
                                    }
                                }
                            });

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                if(!battleResult.getText().toString().equals("You Won!")) {

                    Integer enemyAttackPower = rand.nextInt(10)+1;

                    if(Integer.parseInt(userHP.getText().toString()) <= 0) {
                        battleResult.setText("You Lost!");
                        attackButton.setClickable(false);
                    } else {
                        userHP.setText(Integer.toString(Integer.parseInt(userHP.getText().toString()) - enemyAttackPower));
                        Toast.makeText(getContext(),"Enemy: -" + Integer.toString(userAttackPower) + "\nUser: -" + Integer.toString(enemyAttackPower),Toast.LENGTH_SHORT).show();
                    }
                    // check again after attacking
                    if(Integer.parseInt(userHP.getText().toString()) <= 0) {
                        battleResult.setText("You Lost!");
                        attackButton.setClickable(false);
                    }
                }
            }
        });

        myRef = database.getReference("Users/" + UserName);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String characters;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("Characters")){
                        characters = data.getValue().toString();
                        final String[] cL = characters.split(" ");
                        for(int i = 0; i < cL.length; i++) {
                            if(summonedCharacter.equals(cL[i])) {
                                battleFlag = true;
                                Log.d("foundit", "it");
                            }
                        }
                        if(battleFlag.equals(false)) {
                            attackButton.setClickable(false);
                            Log.d("IN", "HERE");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.battle_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit_battle_item:
                mListener.onMenuMyHomeClickedFromBattle();
                break;
        }
        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBattleFragmentInteractionListener) {
            mListener = (OnBattleFragmentInteractionListener) context;
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


    public interface OnBattleFragmentInteractionListener {
        void onMenuMyHomeClickedFromBattle();
    }

}
