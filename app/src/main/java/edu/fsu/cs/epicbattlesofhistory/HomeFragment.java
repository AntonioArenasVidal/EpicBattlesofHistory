package edu.fsu.cs.epicbattlesofhistory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private String[] characters = {"George Washington", "Julius Caesar", "King Arthur", "Montezuma", "Ozymandias"};

    private OnHomeFragmentInteractionListener mListener;
    private Boolean neutralSongRestartFlag;

    Button tempBattleButton;

    private MapView mapView;
    private GoogleMap map;

    String latitude = "38.89511", longitude = "-77.03637";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    private Marker cLoc;
    LatLng battleLoc;

    public HomeFragment() {
        neutralSongRestartFlag = false;
    }

    public HomeFragment(Boolean neutralSong) {
        neutralSongRestartFlag = neutralSong;
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
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mListener = (HomeFragment.OnHomeFragmentInteractionListener) getActivity();

        //request location access, do we need this?
        while(true) {
            if (!(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
                break;
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        }

        if(neutralSongRestartFlag.equals(true)) {
            Intent myIntent = new Intent(rootView.getContext(), MyMediaService.class);
            myIntent.setAction("PLAY_NEUTRAL");
            getActivity().startService(myIntent);
        }

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteractionListener) {
            mListener = (OnHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
        }
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);

           /*
        COMPARE TO CHARACTER LIST, if not present, red (default) marker
            if present, green marker

         */

        battleLoc = new LatLng(Double.valueOf(50.6673), Double.valueOf(-4.7585)); //tintagel castle
        map.addMarker(new MarkerOptions().position(battleLoc).title("arthur").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        battleLoc = new LatLng(Double.valueOf(41.9028), Double.valueOf(12.4964)); //rome
        map.addMarker(new MarkerOptions().position(battleLoc).title("julius_caesar"));

        battleLoc = new LatLng(Double.valueOf(42.364322), Double.valueOf(-71.034830)); //boston
        map.addMarker(new MarkerOptions().position(battleLoc).title("washington"));

        battleLoc = new LatLng(Double.valueOf(29.977380), Double.valueOf(31.131691)); //pyramids of giza
        map.addMarker(new MarkerOptions().position(battleLoc).title("ozymandias"));

        battleLoc = new LatLng(Double.valueOf(19.279649), Double.valueOf(-99.063986)); //tenochtitlan
        map.addMarker(new MarkerOptions().position(battleLoc).title("montezuma"));



        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                switch(marker.getTitle()) {
                    case "ozymandias": Toast.makeText(getContext(), "Prepare to fight Anubis!",Toast.LENGTH_SHORT).show(); break;
                    case "arthur": Toast.makeText(getContext(), "Prepare to fight Knight Mordred!",Toast.LENGTH_SHORT).show(); break;
                    case "montezuma": Toast.makeText(getContext(), "Prepare to fight Conquistador!",Toast.LENGTH_SHORT).show(); break;
                    case "washington": Toast.makeText(getContext(), "Prepare to fight Redcoat!",Toast.LENGTH_SHORT).show(); break;
                    case "julius_caesar": Toast.makeText(getContext(), "Prepare to fight Barbarian!",Toast.LENGTH_SHORT).show(); break;
                }

                BattleFragment battle_fragment = new BattleFragment();
                String tag = BattleFragment.class.getCanonicalName();

                Bundle extras = new Bundle();
                extras.putString("enemy", marker.getTitle());
                battle_fragment.setArguments(extras);

                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.frame_fragment, battle_fragment, tag).commit();

                return true;
            }
        });
    }

    public interface OnHomeFragmentInteractionListener {
        void onMenuMyHomeClicked();
        void onMenuCharactersClicked();
        void onMenuSummonClicked();
        void onMenuLogoutClicked();
        void onBattleClicked();
    }

    /* FUNCTION OBSOLETE - markers added in onMapReady
    public void addMarkers(){
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef = database.getReference("Characters/gold");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String character;
                int i = 0;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().equals(characters[i])){
                        Log.d("Character", data.getKey());
                        myRef.getDatabase().getReference("Characters/gold/" + characters[i]);
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data: dataSnapshot.getChildren()){
                                    Log.d("data.getKey()", data.getKey());
                                    Log.d("data.getValue()", data.getValue().toString());
                                    if(data.getValue().toString().equals("Latitude")){
                                        latitude = data.getValue().toString();
                                        Log.d("latitude", latitude);
                                    }
                                    if(data.getValue().toString().equals("Longitude")){
                                        longitude = data.getValue().toString();
                                        Log.d("longitude", longitude);
                                    }
                                }
                                //LatLng charMarker = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                                //map.addMarker(new MarkerOptions().position(charMarker).title("Character"));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

     */
}
