package com.example.android9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener{
    ActionBar.Tab tabSong, tabArtist, tabAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar=getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tabSong=bar.newTab();
        tabSong.setText("음악별");
        tabSong.setTabListener((this));
        bar.addTab(tabSong);

        tabArtist=bar.newTab();
        tabArtist.setText("가수별");
        tabArtist.setTabListener((this));
        bar.addTab(tabArtist);

        tabAlbum=bar.newTab();
        tabAlbum.setText("앨범별");
        tabAlbum.setTabListener((this));
        bar.addTab(tabAlbum);
    }

    MyTabFragment myFrags[]=new MyTabFragment[3];

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            MyTabFragment myTabFrag=null;
            if(myFrags[tab.getPosition()]==null){
                myTabFrag=new MyTabFragment();
                Bundle data = new Bundle();
                data.putString("tabName",tab.getText().toString());
                myTabFrag.setArguments(data);
                myFrags[tab.getPosition()]=myTabFrag;
            }
            else myTabFrag=myFrags[tab.getPosition()];
            ft.replace(android.R.id.content,myTabFrag);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}

