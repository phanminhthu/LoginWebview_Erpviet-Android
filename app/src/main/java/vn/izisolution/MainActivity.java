package vn.izisolution;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import vn.izisolution.fragments.HomeFragment;
import vn.izisolution.utils.Debug;
import vn.izisolution.utils.SharedPref;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;

    public LinearLayout appBarLayout;

    private ImageView menu;

    private Fragment f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        f = new HomeFragment();
        initViews();
    }

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

//        replaceFragment(navigationView.getMenu().getItem(0));

        replaceFragment();

//        TextView navHeaderName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_name);
//        navHeaderName.setText(SharedPref.getString(MainActivity.this, "USERNAME"));
//        TextView navHeaderTitle = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_title);
//        navHeaderTitle.setText(SharedPref.getString(MainActivity.this, "LOGIN"));

        appBarLayout = (LinearLayout) findViewById(R.id.appbar);

        menu = (ImageView) findViewById(R.id.actionbar_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        initDrawerLayout();
    }

    private void initDrawerLayout() {
        String hasPermissionCrm = SharedPref.getString(MainActivity.this, SharedPref.HAS_PERMISSION_CRM);
        String hasPermissionStock = SharedPref.getString(MainActivity.this, SharedPref.HAS_PERMISSION_STOCK);
        String hasPermissionAccount = SharedPref.getString(MainActivity.this, SharedPref.HAS_PERMISSION_ACCOUNT);
        String hasPermissionHR = SharedPref.getString(MainActivity.this, SharedPref.HAS_PERMISSION_HR);

        if (!hasPermissionCrm.equals("")) {
            navigationView.getMenu().add(0, 0, 0, "CRM");
            navigationView.getMenu().add(1, 1, 1, "POS");
        }

        if (!hasPermissionStock.equals("")) {
            final SubMenu subMenu = navigationView.getMenu().addSubMenu("Kho hàng");
            subMenu.add(3, 3, 3, "Xuất kho");
            subMenu.add(4, 4, 4, "Nhập kho");
            subMenu.add(5, 5, 5, "Chuyển kho");
        }

        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                mDrawerLayout.closeDrawers();
                return false;
            }
        });

        View header = LayoutInflater.from(MainActivity.this).inflate(R.layout.nav_header, null, false);
        navigationView.addHeaderView(header);
    }

    private void replaceFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, f);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToastExit();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void showToastExit() {
        Toast toast = Toast.makeText(this, getResources().getString(R.string.double_tap_to_exits), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.show();
    }
}
