package np.com.bishaltimilsina.instant.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Bishal on 12/5/2015.
 */
public class HomeFragment extends Fragment implements OnClickListener{
    Button stockReport;
    Button lgpUpliftment;
    Button lgpInformation;
    SharedPreferences sharedPreferences;
    String defaultValue="<body style=\"background-color:#c9c7c7;\"><h4>Swipe to refresh</h4>";
    WebView webView;
    DatabaseOpenHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences("np.com.bishaltimilsina.instant.myapplication", Context.MODE_PRIVATE);
        db=new DatabaseOpenHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.home, container,false);
    }

    @Override
    public void onStart(){
        super.onStart();
        String list[]=db.getAllTitle("notice");
        ArrayList<String> arrayList=new ArrayList();
        if(list.length!=0)
        {
            for(int i=0;i<7;i++)
            arrayList.add(list[i]);
        }
        ListView listView=(ListView)getActivity().findViewById(R.id.listViewNoticeHome);
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url=db.getUrl("notice",position);
                Intent intent=new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        webView=(WebView) getActivity().findViewById(R.id.webViewFortnight);
        Button buttonTender=(Button) getActivity().findViewById(R.id.buttonTenders);
        loadWebView();
    }

    public void loadWebView() {
        String loadWebData = sharedPreferences.getString("fortnight", defaultValue);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        float scale=(((float) width)/480)*100;
        Log.d( width+"   "+scale, "connected");
        webView.setInitialScale((int) scale);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadData(loadWebData, "text/html", "UTF-8");
    }

    @Override
    public void onClick(View v) {
        switch (getId()){
            case (R.id.buttonTenders):
                TenderFragment fragment=new TenderFragment();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment).setCustomAnimations(android.R.anim.bounce_interpolator,android.R.anim.slide_out_right).addToBackStack(null).commit();
                break;
        }
    }

}
