package ru.novikov.themoviedb.view.baseviews;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.presenter.basepresenters.Presenter;

/**
 * Created by Ivan on 08.10.2016.
 */

public abstract class BaseActivity<T extends Presenter> extends AppCompatActivity implements View{

    public final static String FRAGMENT_PRESENTER_TAG = "fragment_presenter_tag";

    protected T mPresenter;

    protected abstract Fragment createInstancePresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = (T) getSupportFragmentManager().findFragmentByTag(FRAGMENT_PRESENTER_TAG);
        if (mPresenter == null) {
            Fragment fragment = createInstancePresenter();
            mPresenter = (T) fragment;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(fragment, FRAGMENT_PRESENTER_TAG).commit();
        }

    }

    @Override
    public void showError(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_connection_title);
        builder.setMessage(R.string.error_connection_message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
