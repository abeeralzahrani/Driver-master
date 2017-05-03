package wassilni.pl.driver.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import modules.ConnectivityReceiver;
import objects.MyApp;
import wassilni.pl.driver.R;

public class scheduleInfo extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    String original;


    TextView path , dayPrice , monthPrice , bookedSeat ;
    String S_ID="",dPrice,mPrice,BS="",pickup="",dropoff="",time="",destination="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        original= (String) getTitle();
        setContentView(R.layout.activity_schedule_info);

        path = (TextView)findViewById(R.id.path);

        dayPrice = (TextView)findViewById(R.id.priceDayTV);
        monthPrice = (TextView)findViewById(R.id.monthPriceTV);
        bookedSeat = (TextView)findViewById(R.id.bookedSeatTV);

        Intent in = getIntent();
        dPrice=in.getStringExtra("dayPrice");
        mPrice=in.getStringExtra("monthPrice");
        BS=in.getStringExtra("bookedSeat");
        pickup=in.getStringExtra("pickup");
        dropoff=in.getStringExtra("dropoff");
        time=in.getStringExtra("time");
        destination=in.getStringExtra("destination");
        System.out.println(dPrice);
        dayPrice.setText(dPrice);
        monthPrice.setText(mPrice);
        bookedSeat.setText(BS);

        S_ID=in.getStringExtra("S_ID");
        System.out.println("@@@@@@@@@##########@@@@@@@@@@ getExtra from f2"+S_ID);
        path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("$%%%%%%%%%%%%%%%$%$%$%$%$%$%$%#23543543%#$%#$% getExtra from F2  "+dropoff);
                FragmentFour f4=new FragmentFour();
                String json=f4.query(S_ID);
                //System.out.println("########@@@@@@@@@@@@################@@@@@@@@@@ "+json);
                if(!json.equals("nothing"))
                {
                    Intent simulate = new Intent(getApplicationContext(), MapsActivity.class);
                    simulate.putExtra("flag", "simulation");
                    simulate.putExtra("json", json);
                    simulate.putExtra("destination", destination);//driver's destination
                    simulate.putExtra("pickup", pickup);
                    simulate.putExtra("dropoff", dropoff);//destination needed to indicate end of path.
                    simulate.putExtra("time", time);
                    startActivity(simulate);
                }
            }
        });

       

    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApp.showFeedback(this, original,false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApp.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */

    @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {
        MyApp.showFeedback(this,original,true);
    }
}
