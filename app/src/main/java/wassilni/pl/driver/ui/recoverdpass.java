package wassilni.pl.driver.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import modules.ConnectivityReceiver;
import objects.MyApp;
import wassilni.pl.driver.R;

public class recoverdpass extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    String original;
    EditText email_ET;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoverdpass);
        original= (String) getTitle();
        email_ET=(EditText)findViewById(R.id.EmailET);
        send=(Button)findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_ET.getText().toString().trim();
try{
                String method = "recoverPass";
                backgroundTask bc = new backgroundTask(getApplication());
                String result = bc.execute(method, email).get();
                System.out.println(result);
                if (result.toLowerCase().contains("the new password was send")){
                    Toast.makeText(getApplicationContext(), "تم إرسال كلمة المرور الجديدة على بريدك الإلكتروني ", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "البريد الإلكتروني لم يسبق التسجيل به ", Toast.LENGTH_LONG).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
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
