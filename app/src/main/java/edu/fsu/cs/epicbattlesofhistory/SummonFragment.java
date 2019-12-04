package edu.fsu.cs.epicbattlesofhistory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    Button backBtn;
    Button summonBtn;
    TextView tokens;

    public SummonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_summon, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_summon, container, false);
        mListener = (SummonFragment.OnSummonFragmentInteractionListener) getActivity();

        Random rand = new Random();
        rand.setSeed(100);

        return rootView;
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
        //void backToMain();
        //void onSummonClicked();
    }
}
