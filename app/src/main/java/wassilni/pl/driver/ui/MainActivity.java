package wassilni.pl.driver.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import modules.ConnectivityReceiver;
import objects.Driver;
import objects.MyApp;
import timber.log.Timber;
import wassilni.pl.driver.R;
import wassilni.pl.driver.data.Fragments;
import wassilni.pl.driver.data.model.NavigationDrawerItem;
import wassilni.pl.driver.ui.navigationdrawer.NavigationDrawerView;




public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private int currentSelectedPosition = 0;
    private static final String JSON_ARRAY ="result";
    private static final String ConfirmD = "confirmed";
//comment

    @InjectView(R.id.navigationDrawerListViewWrapper)
    NavigationDrawerView mNavigationDrawerListViewWrapper;

    @InjectView(R.id.linearDrawer)
    LinearLayout mLinearDrawerLayout;

    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.leftDrawerListView)
    ListView leftDrawerListView;

    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private List<NavigationDrawerItem> navigationItems;
    public SharedPreferences sharedPreferences;
    String original;
    TextView userName,email,confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        original= (String) getTitle();
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        //super.setTheme(R.style.Theme_Transparento);
        SessionSetup();
        mTitle = mDrawerTitle = getTitle();

        Timber.tag("LifeCycles");
        Timber.d("Activity Created");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentFrame,
                    Fragment.instantiate(MainActivity.this, Fragments.ONE.getFragment())).commit();
        } else {
            currentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        navigationItems = new ArrayList<>();
        navigationItems.add(new NavigationDrawerItem(getString(R.string.fragment_one), true));
        navigationItems.add(new NavigationDrawerItem(getString(R.string.fragment_two), true));
        navigationItems.add(new NavigationDrawerItem(getString(R.string.fragment_three), true));
        navigationItems.add(new NavigationDrawerItem(getString(R.string.fragment_four), true));
        navigationItems.add(new NavigationDrawerItem(getString(R.string.fragment_fifth), true));
        navigationItems.add(new NavigationDrawerItem("تقديم شكوى",true));
        navigationItems.add(new NavigationDrawerItem(getString(R.string.fragment_about),
                R.drawable.ic_action_about, false));

        mNavigationDrawerListViewWrapper.replaceWith(navigationItems);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                 R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }
        };

       /* mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_ab_transparent);*/
        userName=(TextView) findViewById(R.id.drawerUserName);
        email=(TextView) findViewById(R.id.drawerUserEmail);
        confirm=(TextView) findViewById(R.id.drawerUserConfirmed);
      // getSupportActionBar().setIcon(R.drawable.ic_action_ab_transparent);

        selectItem(currentSelectedPosition);


    }//end oncreat



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, currentSelectedPosition);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        mDrawerToggle.setDrawerIndicatorEnabled(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // to make the menu from right said

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String confirmStr="";
        if((!MyApp.driver_from_session.getFName().equals(null))) {
            userName.setText(MyApp.driver_from_session.getFName() + " " + MyApp.driver_from_session.getLName());
            email.setText(MyApp.driver_from_session.getEmail());
            String method="driverConfirm";
            backgroundTask backgroundTask = new backgroundTask(this);
            try {
                String result= backgroundTask.execute(method,MyApp.driver_from_session.getID()+"").get();
              try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray users = jsonObject.getJSONArray(JSON_ARRAY);
                    JSONObject jsonObj;
                    jsonObj = users.getJSONObject(0);
                    confirmStr = jsonObj.getString(ConfirmD);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(confirmStr.equals("0"))
                confirm.setText("الحساب غير مفعل");
            else{
                confirm.setText("الحساب مفعل ");
            confirm.setTextColor(Color.GREEN);}
        }else{
            userName.setText("");
            email.setText("");
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.menuRight) {
           if( mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
           {
               mDrawerLayout.closeDrawer(Gravity.RIGHT);
           }
            else {
               mDrawerLayout.openDrawer(Gravity.RIGHT);
           }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.leftDrawerListView)
    public void OnItemClick(int position, long id) {
        if (mDrawerLayout.isDrawerOpen(mLinearDrawerLayout)) {
            mDrawerLayout.closeDrawer(mLinearDrawerLayout);
            onNavigationDrawerItemSelected(position);

            selectItem(position);
        }
    }

    private void selectItem(int position) {

        if (leftDrawerListView != null) {
            leftDrawerListView.setItemChecked(position, true);

            navigationItems.get(currentSelectedPosition).setSelected(false);
            navigationItems.get(position).setSelected(true);

            currentSelectedPosition = position;

            getSupportActionBar()
                    .setTitle(navigationItems.get(currentSelectedPosition).getItemName());
        }

        if (mLinearDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mLinearDrawerLayout);
        }
    }

    private void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                if (!(getSupportFragmentManager().getFragments()
                        .get(0) instanceof FragmentOne)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(MainActivity.this, Fragments.ONE.getFragment()))
                            .commit();
                }
                break;
            case 1:
                if (!(getSupportFragmentManager().getFragments().get(0) instanceof FragmentTwo)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(MainActivity.this, Fragments.TWO.getFragment()))
                            .commit();
                }
                break;
            case 2:
                if (!(getSupportFragmentManager().getFragments().get(0) instanceof FragmentThree)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(MainActivity.this, Fragments.THREE.getFragment()))
                            .commit();
                }
                break;
            case 3:
                if (!(getSupportFragmentManager().getFragments().get(0) instanceof FragmentFour)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(MainActivity.this, Fragments.FOUR.getFragment()))
                            .commit();
                }
                break;
            case 4:
                if (!(getSupportFragmentManager().getFragments().get(0) instanceof FragmentFifth)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(MainActivity.this, Fragments.FiFth.getFragment()))
                            .commit();
                }
                break;

            case 5:
                if (!(getSupportFragmentManager().getFragments().get(0) instanceof FragmentSixth)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(MainActivity.this, Fragments.Sixth.getFragment()))
                            .commit();
                }
                break;
           case 6:
                if (!(getSupportFragmentManager().getFragments().get(0) instanceof FragmentAbout)) {
                    /*getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, Fragment
                                    .instantiate(MainActivity.this, Fragments.ABOUT.getFragment()))
                            .commit();*/

                    new AlertDialog.Builder(this)
                            .setTitle("تأكيد تسجيل الخروج")
                            .setMessage("هل أنت متأكد من تسجيل الخروج؟")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(getApplicationContext(),"تم تسجيل الخروج بنجاح",Toast.LENGTH_SHORT).show();
                                    SharedPreferences sp= getSharedPreferences("session", Context.MODE_APPEND);
                                    SharedPreferences.Editor editor= sp.edit();
                                    editor=editor.clear();
                                    editor.clear();
                                    editor.commit();
                                    MyApp.driver_from_session=null;
                                    startActivity(new Intent(getApplicationContext(),login.class));
                                    finish();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();


                    //System.out.println("    هنا قيمة رقم التعريف المسترجع من السيشن     "+ sp.getInt("ID",-1));
                    // move user to login page.
                }
                break;
        }

    }

    public void SessionSetup() {
        sharedPreferences = getSharedPreferences("session", Context.MODE_APPEND);


                Log.d("MA:doInBackground 0","*******************************************************************************");
                String flag="";
                if (sharedPreferences.contains("ID"))//means that the XML file isn't empty.
                {//retrieve session , and set the variable MyApp.driver_from_session
                    Driver.retrieveSession(sharedPreferences);
                    flag="succeed";
                } else // there's no session saved, then create one!
                {
                    flag="failed";
                    // 1.prompt for login, using Toast
                    //createSession();
                }
                Log.d("MA:doInBackground 1","*******************************************************************************"+flag);
                //return flag;

                if(flag.equalsIgnoreCase("succeed")){
                    //Toast.makeText(getApplicationContext(),"مرحبا مجدداً!",Toast.LENGTH_LONG).show();

                    //moveNext();
                    //direct user to next page.
                    Log.d("MA:doInBackground 2","******************************************************************************* after succeed");
                }
                else{
                    //prompt to logiin
                    Toast.makeText(getApplicationContext(),"لا يوجد ملف تعريف, الرجاء تسجيل الدخول",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),login.class));
                    finish();
                    Log.d("MA:doInBackground 3","******************************************************************************* after failed");
                }

            }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(this.getIntent());
        //startActivity(new Intent(getApplicationContext(),FragmentFour.class));//causes exception
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApp.showFeedback(this, original,false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApp.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */

    @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {
        MyApp.showFeedback(this,original,true);
    }

}
