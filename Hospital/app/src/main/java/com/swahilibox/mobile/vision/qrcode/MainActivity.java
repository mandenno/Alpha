package com.swahilibox.mobile.vision.qrcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ContactsAdapterFavs.ContactsAdapterListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private List<Contactfavs> contactList;
    private ContactsAdapterFavs mAdapter;
    private SearchView searchView;
    Context mcontext;
    LoginDataBaseAdapter loginDataBaseAdapter;
    ProgressDialog progress;
    Utils utils;
    FloatingActionButton fab;
    private static final String URLTWO = "https://alpha.nupola.com/medic_recent.php?phone=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        Toolbar toolbar = findViewById(R.id.toolbar_v);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ALPHA MEDIC");
fab=(FloatingActionButton)findViewById(R.id.fab);
fab.setOnClickListener(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        mcontext = getApplicationContext();
        utils = new Utils(mcontext);
        recyclerView = findViewById(R.id.rview);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapterFavs(this, contactList, this);

        // white background notification bar
        //whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        fetchContactsf();
    }


    private void fetchContactsf() {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        JsonArrayRequest request = new JsonArrayRequest(URLTWO +loginDataBaseAdapter.getSinlgeEntry1(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
                            linlaHeaderProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Contactfavs> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contactfavs>>() {
                        }.getType());
                        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
                        linlaHeaderProgress.setVisibility(View.GONE);
                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                //  Log.e(TAG, "Error: " + error.getMessage());
                LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
                linlaHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
//            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText("Confirm!")
//                    .setContentText("Proceed to business permit verification?")
//                    .setCancelText("NO")
//                    .setConfirmText("YES")
//                    .showCancelButton(true)
//                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sDialog) {
//                            sDialog.cancel();
//                        }
//                    })
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sDialog) {
//                            Intent intent = new Intent(MainActivity.this, Dono_Id.class);
//                            startActivity(intent);
//                            sDialog.cancel();
//                        }
//                    })
//                    .show();
        }
        else if(id==R.id.action_refresh_main)
        {
            fetchContactsf();
            Toasty.success(this, "Refreshing...",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.register) {
            Intent intent = new Intent(MainActivity.this, Dono_Id.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.add) {

        }
        else if (id == R.id.records) {
            fetchContactsf();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onContactSelected(Contactfavs contact) {

    }

    @Override
    public void onClick(View view) {
        if(view==fab)
        {
            Intent intent = new Intent(MainActivity.this, Dono_Id.class);
            startActivity(intent);

        }
    }
}
