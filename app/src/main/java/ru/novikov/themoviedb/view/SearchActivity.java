package ru.novikov.themoviedb.view;

import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.presenter.MoviesListPresenterImpl;
import ru.novikov.themoviedb.view.baseviews.BaseListActivity;
import ru.novikov.themoviedb.view.utils.UiTools;

public class SearchActivity extends BaseListActivity implements SearchView.OnQueryTextListener {

    @Override
    protected Fragment createInstancePresenter() {
        return new MoviesListPresenterImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); //NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            mPresenter.loadSearchList(query);
            UiTools.hideKeyBoard(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
