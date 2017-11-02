package bulgogi1216.gmail.photogenic;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import bulgogi1216.gmail.photogenic.databinding.ActivityMainBinding;
import bulgogi1216.gmail.photogenic.fragment_in_main.HomeFragment;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private ActivityMainBinding mBinding;
    private ActionBar mActionBar;
    private Boolean mIsLoggedin;
    private Handler mHandler;

    //네비바에 정보를 띄우기 위함
    View header;
    ImageView avater;
    TextView niktxt, emailtxt;
    NavigationView navigationView;

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbarShell.toolbar);
        mActionBar = getSupportActionBar();
        Log.e(TAG, "ActionBar is null");
        assert mActionBar != null;
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setTitle(getResources().getString(R.string.app_name));
    }

    private void initNavigationMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.toolbarShell.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                mActionBar.setTitle(item.getTitle());
                Fragment fragment = getFragmentByDrawerTag(item);
                commitFragment(fragment);
                mBinding.drawerLayout.closeDrawers();
                return true;
            }
        });

        // open drawer at start
        mBinding.drawerLayout.openDrawer(GravityCompat.START);
    }

    private Fragment getFragmentByDrawerTag(MenuItem _item) {
        Fragment fragment;
        String[] drawerTitles = getResources().getStringArray(R.array.drawer_titles_in_main);

        if(_item.getTitle().equals(drawerTitles[0])) {
            fragment = HomeFragment.newInstance();
        } else if(_item.getTitle().equals(drawerTitles[1])) {
            fragment = HomeFragment.newInstance();
        } else {
            Log.e(TAG, "fragment variable is null");
            fragment = null;
        }

        return fragment;
    }

    public void commitFragment(Fragment fragment) {
        // Using Handler class to avoid lagging while committing fragment in same time as closing navigation drawer
        mHandler.post(new CommitFragmentRunnable(fragment));
    }

    private class CommitFragmentRunnable implements Runnable {

        private Fragment fragment;

        public CommitFragmentRunnable(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void run() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(mBinding.contentMain.getId(), fragment).commit();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        //네비바에 정보를 띄우기 위함, 닉네임과 이메일을 띄움
        niktxt = mBinding.navView.getHeaderView(0).findViewById(R.id.profile_name);
        emailtxt = mBinding.navView.getHeaderView(0).findViewById(R.id.email);
        avater = mBinding.navView.getHeaderView(0).findViewById(R.id.avatar);

        Intent intent = getIntent();
        String data = intent.getStringExtra("value");
        String email = intent.getStringExtra("email");
        niktxt.setText(data);
        emailtxt.setText(email);
        //이미지 PATH 불러오기 null이 아닌경우만 들어감 (아직 설정안함)
        if(avater!=null) avater.setImageResource(R.drawable.btn_rounded_accent);
/*
        //로그아웃버튼
        mBinding.navView.getMenu().findItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.e("dadasdasd","asdasasdasdas");
                return false;
            }
        });
*/


        initToolbar();
        initNavigationMenu();

        mHandler = new Handler();
        commitFragment(HomeFragment.newInstance());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //login창으로 intent
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
