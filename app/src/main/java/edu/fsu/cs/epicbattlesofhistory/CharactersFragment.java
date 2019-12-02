package edu.fsu.cs.epicbattlesofhistory;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CharactersFragment extends Fragment {
    private OnCharactersFragmentInteractionListener mListener;

    ListView listView;
    String chosenFriend;

    public CharactersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_characters, container, false);
        mListener = (CharactersFragment.OnCharactersFragmentInteractionListener) getActivity();
        listView = (ListView) rootView.findViewById(R.id.my_friends_list_view);

        chosenFriend = "";
        List<String> myTempList = new ArrayList<>();
        myTempList.add("Bob");
        myTempList.add("Sarah");
        myTempList.add("Mike");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( this.getActivity(), android.R.layout.simple_list_item_1, myTempList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                chosenFriend = listView.getItemAtPosition(pos).toString();
                // TODO: find info need from choosing this friend, and call a OnMyFriendsFragmentInteractionListener to pass this info to the FriendMapFragment
                Log.d("FRIEND", "POS: " + pos + " ; Friend: " + chosenFriend);
                mListener.onCharactersClicked(chosenFriend);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCharactersFragmentInteractionListener) {
            mListener = (OnCharactersFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyFriendsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCharactersFragmentInteractionListener {
        void onCharactersClicked(String character);
        void onMenuMyHomeClicked();
        void onMenuCharactersClicked();
        void onMenuSummonClicked();
        void onMenuLogoutClicked();
    }
}