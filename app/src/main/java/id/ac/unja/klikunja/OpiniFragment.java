package id.ac.unja.klikunja;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.ac.unja.klikunja.api.ApiClient;
import id.ac.unja.klikunja.api.ApiInterface;
import id.ac.unja.klikunja.models.News;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpiniFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View view;

    private final String CATEGORY = "205";

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<News> allArticles = new ArrayList<>();
    private OpiniAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private ProgressBar paginationProgress;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;

    // Pagination variables
    private int pageNumber = 1;
    private final int PER_PAGE = 9;
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_opini, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = getView();

        Toolbar newsToolbar = view.findViewById(R.id.news_toolbar);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recyclerView);
        paginationProgress = view.findViewById(R.id.pagination_progress);
        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage = view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ((AppCompatActivity)getActivity()).setSupportActionBar(newsToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Opini");

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        initContent();
    }

    private void resetPagination() {
        pastVisibleItems = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        previousTotal = 0;
        pageNumber = 1;
    }

    @Override
    public void onRefresh() {
        searchView.setQuery("", false);
        searchView.setIconified(true);
        resetPagination();
        loadJson("");
    }

    public void loadJson(final String keyword) {

        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<News>> call;

        if(keyword.length() > 0) {
            call = apiInterface.getPostSearch(CATEGORY, "", keyword, PER_PAGE, pageNumber);
        }else{
            call = apiInterface.getPostInfo(CATEGORY, "", PER_PAGE, pageNumber);
        }

        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if(response.isSuccessful() && response.body() != null) {

                    allArticles = response.body();
                    adapter = new OpiniAdapter(allArticles, getActivity());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();

                    if(String.valueOf(response.body()).equals("[]")) {
                        showNotFoundMessage(
                                R.drawable.no_result,
                                "I wish there was something, but there's not",
                                "We couldn't find what you're looking for. Sorry :(");
                    }

                }else{
                    Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                showErrorMessage(
                        R.drawable.oops,
                        "Oops",
                        "It looks like you're offline.");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void initListener() {
        adapter.setOnItemClickListener(new OpiniAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OpiniDetailActivity.class);

                News article = allArticles.get(position);

                intent.putExtra("url", article.getLink());
                intent.putExtra("title", article.getTitle().getRendered());
                intent.putExtra("author", article.getEmbedded().getAuthor().get(0).getName());
                intent.putExtra("content", article.getContent().getRendered());

                startActivity(intent);
            }
        });
    }

    private void initScrollRecycler() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if(dy > 0) {
                    if(isLoading) {
                        if(totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    if(!isLoading && (totalItemCount-visibleItemCount) <= pastVisibleItems) {
                        pageNumber++;
                        performPagination();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void performPagination() {
        paginationProgress.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<News>> call;

        if(!searchView.isIconified()) {
            call = apiInterface.getPostSearch(CATEGORY, "", searchView.getQuery().toString(), PER_PAGE, pageNumber);
        }else{
            call = apiInterface.getPostInfo(CATEGORY, "", PER_PAGE, pageNumber);
        }

        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<News> articles = response.body();
                    adapter.addItem(articles);
                    initListener();
                }

                paginationProgress.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                paginationProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "You're offline. Check your connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.newssearch_menu, menu);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search article...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 2) {
                    resetPagination();

                    if(errorLayout.getVisibility() == View.VISIBLE) {
                        errorLayout.setVisibility(View.GONE);
                    }

                    onLoadingSwipeRefresh(query);
                }else{
                    Toast.makeText(getActivity(), "Type more that 2 letters", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
    }

    private void onLoadingSwipeRefresh(final String keyword) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadJson(keyword);
            }
        });
    }


    private void showErrorMessage(int imageView, String title, String message) {
        if(errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            btnRetry.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initContent();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initContent() {
        if(isNetworkAvailable()) {
            if(errorLayout.getVisibility() == View.VISIBLE) {
                errorLayout.setVisibility(View.GONE);
            }

            onLoadingSwipeRefresh("");
            initScrollRecycler();
        }else{
            showErrorMessage(
                    R.drawable.oops,
                    "Network Error",
                    "Umm.. You need the Internet for this");
        }
    }

    private void showNotFoundMessage(int imageView, String title, String message) {
        if(errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            btnRetry.setVisibility(View.GONE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);
    }
}