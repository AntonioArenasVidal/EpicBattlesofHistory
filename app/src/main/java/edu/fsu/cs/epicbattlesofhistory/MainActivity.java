package edu.fsu.cs.epicbattlesofhistory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity implements

        MainFragment.OnFragmentInteractionListener,
        LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        HomeFragment.OnHomeFragmentInteractionListener,
        SummonFragment.OnSummonFragmentInteractionListener,
        CharactersFragment.OnCharactersFragmentInteractionListener,
        MapFragment.OnMapFragmentInteractionListener,
        BattleFragment.OnBattleFragmentInteractionListener {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    //private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onMain();

    }

    @Override
    public void backToMain() {
        MainFragment main_fragment = new MainFragment();
        String tag = MainFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, main_fragment, tag).commit();
    }

//    @Override
//    public void backToHome() {
//        HomeFragment home_fragment = new HomeFragment();
//        String tag = HomeFragment.class.getCanonicalName();
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, home_fragment, tag).commit();
//    }

    public void onMain() {
        MainFragment main_fragment = new MainFragment();
        String tag = MainFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, main_fragment, tag).commit();
    }

    @Override
    public void onStartLogin() {
        LoginFragment login_fragment = new LoginFragment();
        String tag = LoginFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, login_fragment, tag).commit();
    }

    @Override
    public void onStartRegister() {
        RegisterFragment register_fragment = new RegisterFragment();
        String tag = RegisterFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, register_fragment, tag).commit();
    }

    @Override
    public void onLoggedIn() {
        HomeFragment home_fragment = new HomeFragment();
        String tag = HomeFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, home_fragment, tag).commit();
    }

    @Override
    public void onRegistered() {
        HomeFragment home_fragment = new HomeFragment();
        String tag = HomeFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, home_fragment, tag).commit();

    }

    @Override
    public void onCharactersClicked(String character) {
        MapFragment map_fragment = new MapFragment(character);
        String tag = MapFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, map_fragment, tag).commit();
    }

    /*
    @Override
    public void onMarkerClicked(String battleCharacter) {

        //TODO: Implement Battles Fragment. When clicking on a map marker it should open the battle from that location
        BattleFragment battle_fragment = new BattleFragment(battleCharacter);
        String tag = BattleFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, battle_fragment, tag).commit();
    }*/

    @Override
    public void onMenuCharactersClicked() {
        CharactersFragment characters_fragment = new CharactersFragment();
        String tag = CharactersFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, characters_fragment, tag).commit();
    }

    @Override
    public void onMenuSummonClicked() {
        SummonFragment summon_fragment = new SummonFragment();
        String tag = SummonFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, summon_fragment, tag).commit();
    }

    @Override
    public void onMenuMyHomeClicked() {
        HomeFragment home_fragment = new HomeFragment();
        String tag = HomeFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, home_fragment, tag).commit();
    }

    @Override
    public void onSummonCLicked(){

        SummonFragment summon_fragment = new SummonFragment();
        String tag = SummonFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, summon_fragment, tag).commit();
    }



    @Override
    public void onMenuLogoutClicked() {
        FirebaseAuth.getInstance().signOut();

        MainFragment main_fragment = new MainFragment();
        String tag = MainFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, main_fragment, tag).commit();
    }

    @Override
    public void onMenuMyHomeClickedFromBattle() {
        HomeFragment home_fragment = new HomeFragment(true);
        String tag = HomeFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, home_fragment, tag).commit();
    }

    @Override
    public void onBattleClicked() {
        BattleFragment battle_fragment = new BattleFragment();
        String tag = BattleFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, battle_fragment, tag).commit();
    }
}
