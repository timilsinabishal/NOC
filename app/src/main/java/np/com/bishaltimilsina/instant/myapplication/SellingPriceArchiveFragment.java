package np.com.bishaltimilsina.instant.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Bishal on 12/5/2015.
 */
public class SellingPriceArchiveFragment extends Fragment {
    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    String defaultValue="<body style=\"background-color:#c9c7c7;\"><h4>Swipe to refresh</h4>";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences("np.com.bishaltimilsina.instant.myapplication", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.selling_price_archive,container,false);
    }

    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWebView();
        swipeRefreshLayout=(SwipeRefreshLayout) getActivity().findViewById(R.id.swiperefreshSelling);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("sellingPrice", "onRefresh called from SwipeRefreshLayout");
                        setRefreshing(true);
                        new Title().execute();
                        fragmentRefresh();
                    }
                }
        );
    }
    public void loadWebView(){
        String loadWebData=sharedPreferences.getString("sellingPriceArchive",defaultValue);
        webView = (WebView) getActivity().findViewById(R.id.webViewSellingPrice);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadData(loadWebData, "text/html", "UTF-8");
    }

    public void fragmentRefresh(){
        webView.reload();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    private class Title extends AsyncTask<Void, Void, Void> {

        String sellingPrice;
        boolean RESPONSE=false;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                Document documentSellingPriceArchive= Jsoup.connect("http://nepaloil.com.np/selling-price-archive-16.html").get();
                Log.d("sellingprice", "connected");
                String sellingPricebak=documentSellingPriceArchive.select("#content-inner > div.print_area > table > tbody").toString();
                sellingPrice="<body style=\"background-color:#c9c7c7;\"> <table border=\"2\" bgcolor=#c9c7c7 cellpadding=\"0\" cellspacing=\"0\""+sellingPricebak+"</table>";
                RESPONSE=true;
            }
            catch (IOException e){
                e.printStackTrace();
                RESPONSE=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(RESPONSE==false){
                Toast.makeText(getActivity(), "Error Network Connection", Toast.LENGTH_SHORT).show();
            }
            if(RESPONSE==true) {
                Toast.makeText(getActivity(), "Selling Price Archive Refreshed", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("sellingPriceArchive", sellingPrice);
                editor.commit();
            }
            setRefreshing(false);
        }

    }
}
