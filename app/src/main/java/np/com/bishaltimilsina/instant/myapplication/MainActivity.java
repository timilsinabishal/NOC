package np.com.bishaltimilsina.instant.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progressDialog;
    DatabaseOpenHelper db;
    String urlNews="http://nepaloil.com.np/news-events/News/1/";
    String urlNotice="http://nepaloil.com.np/news-events/Notice/5/";
    String urlTender="http://nepaloil.com.np/news-events/Tender/3/";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences=getSharedPreferences("np.com.bishaltimilsina.instant.myapplication", Context.MODE_PRIVATE);
        //create database
        db=new DatabaseOpenHelper(this);

        //check if null and add home fragment to MainActivity
		if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null){
			FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
			HomeFragment homeFragment=new HomeFragment();
			fragmentTransaction.add(R.id.fragment_container,homeFragment).commit();
		}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.dbRefresh) {
            new Title().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            HomeFragment homeFragment=new HomeFragment();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();

        } else if (id == R.id.selling_price_archive) {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            SellingPriceArchiveFragment fragment=new SellingPriceArchiveFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

        } else if (id == R.id.profit_and_loss_details) {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            ProfitAndLossFragment fragment=new ProfitAndLossFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

        } else if (id == R.id.notice) {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            NoticeFragment fragment=new NoticeFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

        } else if (id == R.id.news) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            NewsFragment fragment = new NewsFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        }else if(id==R.id.tender){
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            TenderFragment fragment=new TenderFragment();
            fragmentTransaction.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

        }else if(id==R.id.about) {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            AboutFragment fragment=new AboutFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

        }else if (id == R.id.feedback) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"bishal.timilsina@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback for NOC app");
            i.putExtra(Intent.EXTRA_TEXT, "body of email");

            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class Title extends AsyncTask<Void, Void, Void> {

        ArrayList<String> item;
        boolean RESPONSE=false;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Nepal Oil Co. Ltd.");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                Document documentNews= Jsoup.connect(urlNews).timeout(5000).get();
                Log.d("news", "connected");
                Document documentNotice=Jsoup.connect(urlNotice).timeout(5000).get();
                Log.d("notice","connected");
                Document documentTender= Jsoup.connect(urlTender).timeout(5000).get();
                Log.d("tender","connected");
                Document documentFortnight= Jsoup.connect("http://nepaloil.com.np/").timeout(5000).get();
                Log.d("fortnight","connected");
                Document documentSellingPriceArchive= Jsoup.connect("http://nepaloil.com.np/selling-price-archive-16.html").timeout(7000).get();
                Log.d("sellingprice", "connected");
                Document documentProfitAndLoss= Jsoup.connect("http://nepaloil.com.np/monthly-profit-and-loss-details").timeout(7000).get();
                Log.d("profitandloss", "connected");
                String profitAndLossbak=documentProfitAndLoss.select("#content-inner > div.print_area > table > tbody").toString();
                String profitAndLoss="<body style=\"background-color:#c9c7c7;\"> <table border=\"2\" bgcolor=#c9c7c7 cellpadding=\"0\" cellspacing=\"0\""+profitAndLossbak+"</table>";
                String sellingPricebak=documentSellingPriceArchive.select("#content-inner > div.print_area > table > tbody").toString();
                String sellingPrice="<body style=\"background-color:#c9c7c7;\"> <table border=\"2\" bgcolor=#c9c7c7 cellpadding=\"0\" cellspacing=\"0\""+sellingPricebak+"</table>";
                String fortnightbak=documentFortnight.select("#content-left > div.boxModel > div.boxModelLeft > div.leftBox > table > tbody").toString();
                String fortnight="<body style=\"background-color:#c9c7c7;\"> <table border=\"2\" bgcolor=#c9c7c7 cellpadding=\"0\" cellspacing=\"0\""+fortnightbak+"</table>";
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("sellingPriceArchive", sellingPrice);
                editor.putString("profitAndLoss", profitAndLoss);
                editor.putString("fortnight",fortnight);
                editor.commit();
                Elements linkNews=documentNews.select("div#news ul li");
                Elements linkNotice=documentNotice.select("div#news ul li");
                Elements linkTender=documentTender.select("div#news ul li");
                String[] urlNews=new String[linkNews.size()];
                String[] titlesNews=new String[linkNews.size()];
                String[] titlesNotice=new String[linkNotice.size()];
                String[] urlNotice=new String[linkTender.size()];
                String[] titlesTender=new String[linkTender.size()];
                String[] urlTender=new String[linkTender.size()];
                int i=0;
                for(Element element:linkNews){
                    titlesNews[i]=element.select("h4 > a").html();
                    urlNews[i]=element.select(" h4 > a").attr("href");
                    i++;
                }
                int j=0;
                for(Element element:linkNotice){
                    titlesNotice[j]=element.select("h4 > a").html();
                    urlNotice[j]=element.select(" h4 > a").attr("href");
                    j++;
                }
                int k=0;
                for(Element element:linkTender){
                    titlesTender[k]=element.select("h4 > a").html();
                    urlTender[k]=element.select(" h4 > a").attr("href");
                    k++;
                }
                RESPONSE=true;
                db.saveTable(titlesNews,urlNews, "news");
                db.saveTable(titlesNotice,urlNotice, "notice");
                db.saveTable(titlesTender,urlTender, "tender");
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

            progressDialog.dismiss();
            if(RESPONSE==false){
                Toast.makeText(MainActivity.this, "Error Network Connection", Toast.LENGTH_SHORT).show();
            }
            if(RESPONSE==true){
                Toast.makeText(MainActivity.this, "News and Notice Refreshed", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void tendersClicked(View view){
        TenderFragment fragment=new TenderFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }

    public void stockClicked(View view){
        String url="http://nepaloil.com.np/stock-report/";
        Intent intent=new Intent(this,WebViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
    public  void LPGUpliftmentClicked(View view){}

}
