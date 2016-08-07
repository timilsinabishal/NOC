package np.com.bishaltimilsina.instant.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Bishal on 12/5/2015.
 */
public class AboutFragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceInstate){
        return inflater.inflate(R.layout.about,container,false);
    }

    @Override
    public void onStart(){
        super.onStart();
        TextView textView=(TextView) getActivity().findViewById(R.id.aboutText);
        textView.setText("Nepal Oil Corporation (NOC) was established in January 1970 by the Government of Nepal" +
                " as a state-owned trading company to deal with the import, transportation, storage and distribution" +
                " of various petroleum products in the country. NOC, headquartered in Kathmandu, has Five Regional" +
                " Offices and also Branch Offices, Fuel Depots and Aviation Fuel Depots with total storage capacity" +
                " of 71,622 Kilolitres (KL) and around 600 employees.\n" +
                " \n" +
                "Nepal is a land locked country bordering three sides by India and the northern part of snow fed" +
                " Himalayas by Tibet/China. Nepal does not produce any oil and depends totally on imports in the " +
                "refined form, as it does not have any oil refinery. NOC is the sole organization responsible for " +
                "the import and distribution of petroleum products through around 1500 Tank Trucks and 1500 retail " +
                "outlets owned by the private sector around all parts of the country.\n" +
                " \n" +
                "Nepal is becoming more dependent on petroleum products for meeting its energy requirement." +
                " The demand of products like MS, HSD, SKO, ATF and LPG is about 1.2 million ton (MT) per annum" +
                " with annual increase by around 10%. Petroleum products constitute about 15% of total energy consumed" +
                " in Nepal. The nearest sea port from Nepal is Haldia(Kolkata) which is about 900 km from nearest Indo-Nepal" +
                " border. The long transportation distance from nearest sea port to Nepal is the main constraint for import" +
                " of POL from third country.All the petroleum products consumed in Nepal are procured and imported from Government" +
                " of India (GOI) undertaking national oil company, i.e. Indian Oil Corporation Ltd (IOC) under a 5 years' Contract" +
                "" +
                "" +
                "a PDO (Product Delivery Order) system and IOC is providing Bulk LPG to Nepalese LPG industries from Haldia, Barauni," +
                "" +
                " The transportation from IOC locations to NOC depots and to retail outlets is done by Tank Trucks. To meet the increasing" +
                " demand, a MOU between IOC and NOC for construction of cross border Petroleum Product Pipeline from IOC's depot (Raxaul" +
                ") to NOC's depot (Amlekhgunj ) has been signed. The Detailed Feasibility Report (DFR) of the proposed pipeline has also" +
                " been prepared and the construction/investment modalities are under discussion between the companies. Similarly, the " +
                "scope of laying LPG pipeline across Indo-Nepal border has also come in the discussion between the companies.\n" +
                " \n" +
                "The highest policy making and controlling body of NOC is its Board of Directors .The Managing Director (MD) who is also " +
                "" +
                "products like MS, HSD, SKO and LPG needs approval from the Government of Nepal.\n" +
                "- See more at: http://nepaloil.com.np/About-Us/1/#sthash.77V7gGnO.dpuf");
    }
}
