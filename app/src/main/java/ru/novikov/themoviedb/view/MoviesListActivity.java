package ru.novikov.themoviedb.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.presenter.MoviesListPresenterImpl;
import ru.novikov.themoviedb.view.baseviews.BaseListActivity;

public class MoviesListActivity extends BaseListActivity {

    @Override
    protected MoviesListPresenterImpl createInstancePresenter() {
        return new MoviesListPresenterImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clearListeners();
    }
}
