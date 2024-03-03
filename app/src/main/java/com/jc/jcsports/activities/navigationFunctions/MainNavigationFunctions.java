package com.jc.jcsports.activities.navigationFunctions;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jc.jcsports.R;
import com.jc.jcsports.activities.chatFunctions.ChatFragment;
import com.jc.jcsports.activities.filelistFunctions.FileListMainFragment;
import com.jc.jcsports.activities.phoneFunctions.PhoneAuthenticActivity;
import com.jc.jcsports.activities.storeFunctions.StoreFragment;
import com.jc.jcsports.activities.userFunctions.UserFragment;
import com.jc.jcsports.activities.userListFunctions.UserListFragment;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.kakao.sdk.user.UserApiClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainNavigationFunctions {

    private FragmentActivity fragmentActivity;
    private FileListMainFragment fileListMainFragment;
    private ChatFragment chatFragment;
    private UserFragment u_Fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private UserListFragment userListFragment;
    private StoreFragment storeFragment;
    private BottomNavigationView bottomNavigationView;

    public MainNavigationFunctions(FragmentActivity fragmentActivity, BottomNavigationView bottomNavigationView) {
        this.fragmentActivity = fragmentActivity;
        this.bottomNavigationView = bottomNavigationView;
        bottomNavigationInit(bottomNavigationView);
        createFragment();
    }


    private void createFragment() {
        fileListMainFragment = FileListMainFragment.newInstance("create", "fileListMainFragment");
        moveFragment(fileListMainFragment);
    }


    private void bottomNavigationInit(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.main:
                            if (fileListMainFragment == null) {
                                fileListMainFragment = FileListMainFragment.newInstance("create", "fileListMainFragment");
                                addFragment(fileListMainFragment);
                            }
                            showFragment(fileListMainFragment);
                            hideFragment(chatFragment);
                            hideFragment(u_Fragment);
                            hideFragment(userListFragment);
                            hideFragment(storeFragment);
                            break;

                        case R.id.main1:
                            if (chatFragment == null) {
                                chatFragment = ChatFragment.newInstance("create", "chatFragment");
                                addFragment(chatFragment);
                            }
                            showFragment(chatFragment);
                            hideFragment(fileListMainFragment);
                            hideFragment(u_Fragment);
                            hideFragment(userListFragment);
                            hideFragment(storeFragment);
                            break;

                        case R.id.main2:
                            if (userListFragment == null) {
                                userListFragment = UserListFragment.newInstance("create", "userListFragment");
                                addFragment(userListFragment);
                            }
                            showFragment(userListFragment);
                            hideFragment(fileListMainFragment);
                            hideFragment(u_Fragment);
                            hideFragment(chatFragment);
                            hideFragment(storeFragment);
                            break;

                        case R.id.main3:
                            if (storeFragment == null) {
                                storeFragment = StoreFragment.newInstance("create", "storeFragment");
                                addFragment(storeFragment);
                            }
                            showFragment(storeFragment);
                            hideFragment(fileListMainFragment);
                            hideFragment(u_Fragment);
                            hideFragment(chatFragment);
                            hideFragment(userListFragment);
                            break;

                        default:
                            break;

                    }
                    return true;
                }
        );
    }


    public void show_UserInfo() {
        if (u_Fragment == null) {
            u_Fragment = UserFragment.newInstance("create", "u_Fragment");
            addFragment(u_Fragment);
        }
        showFragment(u_Fragment);
        hideFragment(fileListMainFragment);
        hideFragment(chatFragment);
        hideFragment(userListFragment);
        hideFragment(storeFragment);
        bottomNavigationView.setSelectedItemId(R.id.main0);

    }

    private void fragmentManagerInit() {
        fragmentManager = fragmentActivity.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
    }


    public void moveFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManagerInit();
            transaction.replace(R.id.fragmentContainerFrameLayout, fragment).commit();
        }

    }

    public void showFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManagerInit();
            transaction.show(fragment).commit();
        }

    }

    public void hideFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManagerInit();
            transaction.hide(fragment).commit();
        }
    }

    public void addFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManagerInit();
            transaction.add(R.id.fragmentContainerFrameLayout, fragment).commit();
        }
    }

}
