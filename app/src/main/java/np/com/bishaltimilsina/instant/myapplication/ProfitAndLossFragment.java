package np.com.bishaltimilsina.instant.myapplication;

import android.app.ProgressDialog;
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
public class ProfitAndLossFragment extends Fragment{
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String defaultValue="<body style=\"background-color:#c9c7c7;\"><h4>Swipe to refresh</h4>";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences("np.com.bishaltimilsina.instant.myapplication", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.profit_and_loss,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWebView();
        swipeRefreshLayout=(SwipeRefreshLayout) getActivity().findViewById(R.id.swiperefreshProfit);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("Profit and loss", "onRefresh called from SwipeRefreshLayout");
                        setRefreshing(true);
                        new Title().execute();
                        fragmentRefresh();
                    }
                }
        );
    }

    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    public void loadWebView(){
        String loadWebData=sharedPreferences.getString("profitAndLoss",defaultValue);
        WebView webView = (WebView) getActivity().findViewById(R.id.webViewProfit);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadData(loadWebData, "text/html", "UTF-8");
    }

    public void fragmentRefresh(){
        //write code to refresh the fragment to repaint the webView
    }


    //Async task
    private class Title extends AsyncTask<Void, Void, Void> {

        String profitAndLoss;
        boolean RESPONSE=false;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //progressDialog=new ProgressDialog(getActivity());
            // progressDialog.setTitle("Nepal Oil Co. Ltd.");
            //progressDialog.setMessage("Loading...");
            //progressDialog.setIndeterminate(false);
            //progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                Document documentProfitAndLoss= Jsoup.connect("http://nepaloil.com.np/monthly-profit-and-loss-details").get();
                Log.d("profitandloss", "connected");
                String profitAndLossbak=documentProfitAndLoss.select("#content-inner > div.print_area > table > tbody").toString();
                profitAndLoss="<body style=\"background-color:#c9c7c7;\"> <table border=\"2\" bgcolor=#c9c7c7 cellpadding=\"0\" cellspacing=\"0\""+profitAndLossbak+"</table>";
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
            //progressDialog.dismiss();
            if(RESPONSE==false){
                Toast.makeText(getActivity(), "Error Network Connection", Toast.LENGTH_SHORT).show();
            }
            if(RESPONSE==true) {
                Toast.makeText(getActivity(), "Profit and loss Refreshed", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("profitAndLoss", profitAndLoss);
                editor.commit();
            }
            setRefreshing(false);
        }

    }
}
