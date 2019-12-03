package edu.fsu.cs.epicbattlesofhistory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class BattleFragment extends Fragment {


    private OnBattleFragmentInteractionListener mListener;

    public BattleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_battle, container, false);
        mListener = (OnBattleFragmentInteractionListener) getActivity();

        Intent myIntent = new Intent(rootView.getContext(), MyMediaService.class);
        myIntent.setAction("PLAY_EPIC");
        getActivity().startService(myIntent);

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
