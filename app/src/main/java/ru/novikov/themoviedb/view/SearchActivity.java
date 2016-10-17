package ru.novikov.themoviedb.view;

import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.DataProviderCallBacks;
import ru.novikov.themoviedb.presenter.MoviesListPresenterImpl;
import ru.novikov.themoviedb.view.baseviews.BaseListActivity;
import ru.novikov.themoviedb.view.utils.UiTools;

public class SearchActivity extends BaseListActivity implements SearchView.OnQueryTextListener {

    private String mSearchQuery;

    @Override
    protected Fragment createInstancePresenter() {
        return new MoviesListPresenterImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        mPresenter.setTypeInfoReceiver(DataProviderCallBacks.TYPE_INFO_SEARCH_MOVIES);
        mEmptyTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_search_black_48dp, 0, 0);
        mEmptyTextView.setText(R.string.empty_enter_search_text);
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
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            mPresenter.loadSearchList(query);
            UiTools.hideKeyBoard(this);
            mSearchQuery = query;
            return true;
        }
        return false;
    }

    @Override
    protected void emptyList(boolean state) {
        super.emptyList(state);
        mEmptyTextView.setText(getString(R.string.empty_search_not_results, mSearchQuery));
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
