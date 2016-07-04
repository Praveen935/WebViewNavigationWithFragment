package sample.webviewtest;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    FragmentManager supportFragmentManager;
    Awebviewfragment webViewFragment;
    IBackPressedListener backPressListener;
    NotAwebviewfragment notAwebviewfragment;


    Awebviewfragment.OnFragmentInteractionListener weblistener = new Awebviewfragment.OnFragmentInteractionListener() {
        @Override
        public void onFragmentInteraction(Uri uri) {
            addNotAwebviewfragment();
        }
    };

    FirstFragment.OnFragmentInteractionListener firstListener = new FirstFragment.OnFragmentInteractionListener() {
        @Override
        public void onFragmentInteraction(Uri uri) {
            addFragWebview();
        }
    };

    private void addNotAwebviewfragment() {
        supportFragmentManager.beginTransaction();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        notAwebviewfragment = NotAwebviewfragment.newInstance("test", "test");

        fragmentTransaction.add(R.id.contents, notAwebviewfragment, NotAwebviewfragment.TAG_NOT).addToBackStack(NotAwebviewfragment.TAG_NOT);
        fragmentTransaction.commit();
    }

    private void addFragWebview(){
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        webViewFragment = Awebviewfragment.newInstance("test", "test");
        webViewFragment.setFragmentListener(weblistener);
        backPressListener = webViewFragment;
        fragmentTransaction.add(R.id.contents, webViewFragment, Awebviewfragment.TAG).addToBackStack(Awebviewfragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackStackChanged() {
        System.out.println("called back stack");
    }


    public interface IBackPressedListener {
        public boolean backPressed(MainActivity activity);
    }

    FirstFragment firstFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supportFragmentManager = getSupportFragmentManager();
        //supportFragmentManager.addOnBackStackChangedListener(this);

        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        firstFragment = FirstFragment.newInstance("test", "test");
        firstFragment.setFragmentListener(firstListener);
        //backPressListener = webViewFragment;


        fragmentTransaction.add(R.id.contents, firstFragment, Awebviewfragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getActiveFragment();

        if(fragment instanceof Awebviewfragment){
            System.out.println("web view fragment");
            if(backPressListener != null && backPressListener.backPressed(this)){
                System.out.println("web view fragment will return");
                return;
            }
        }

        int count = supportFragmentManager.getBackStackEntryCount();
        if (count > 0 ){
            supportFragmentManager.popBackStackImmediate();
        }else {
            super.onBackPressed();
        }


    }

    public Fragment getActiveFragment() {
        if (supportFragmentManager.getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.getBackStackEntryCount() - 1).getName();
        return supportFragmentManager.findFragmentByTag(tag);
    }
}
