package edu.fsu.cs.epicbattlesofhistory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class SummonFragment extends Fragment {

    private OnSummonFragmentInteractionListener mListener;

    FirebaseAuth mAuth;
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
                Random rand = new Random();
                rand.setSeed(100);
                summonedCharacter = "arthur";
                String characterImageName = summonedCharacter + "_character";

                // TODO: Change token to corresponding token cound in users DB in firebase

                tokens.setText(Integer.toString(Integer.parseInt(tokens.getText().toString()) - 1));
                character.setImageResource(getResources().getIdentifier(characterImageName, "drawable", getContext().getPackageName()));
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
