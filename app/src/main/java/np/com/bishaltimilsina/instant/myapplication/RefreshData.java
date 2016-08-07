package np.com.bishaltimilsina.instant.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Bishal on 12/17/2015.
 */
public class RefreshData extends AsyncTask<Void,Void,Void> {
    SharedPreferences sharedPreferences;
    DatabaseOpenHelper db;
    String urlNews="http://nepaloil.com.np/news-events/News/1/";
    String urlNotice="http://nepaloil.com.np/news-events/Notice/5/";
    String urlTender="http://nepaloil.com.np/news-events/Tender/3/";
    boolean RESPONSE=false;
    private Context context;

    public RefreshData(Context c) {
        this.context=c;
    }

    @Override
    protected Void doInBackground(Void... params) {
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        db=new DatabaseOpenHelper(context);
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
                titlesNews[i]=element.select("p").html();
                urlNews[i]=element.select(" h4 > a").attr("href");
                i++;
            }
            int j=0;
            for(Element element:linkNotice){
                titlesNotice[j]=element.select("p").html();
                urlNotice[j]=element.select(" h4 > a").attr("href");
                j++;
            }
            int k=0;
            for(Element element:linkTender){
                titlesTender[k]=element.select("p").html();
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
}
