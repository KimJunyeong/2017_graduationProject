package com.estimote.showroom;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.showroom.estimote.NearableID;
import com.estimote.showroom.estimote.Product;
import com.estimote.showroom.estimote.ShowroomManager;
//import com.estimote.sdk.connection.DeviceConnectionProvider;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final static int INTERVAL = 1000 * 10;
    private static final String TAG = "MainActivity";

    Button buttonConnect, buttonSend;
    TextView textViewState;

    ClientHandler clientHandler;
    ClientThread clientThread;

    Handler mHandler = new Handler();

    private int send_counter=0;
    private ShowroomManager showroomManager;
    private Map<NearableID, Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonSend = (Button)findViewById(R.id.send);
        textViewState = (TextView)findViewById(R.id.state);

        buttonSend.setEnabled(false);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonSend.setOnClickListener(buttonSendOnClickListener);
        clientHandler = new ClientHandler(this);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Map<NearableID, Product> products = new HashMap<>();
        products = new HashMap<>();
        products.put(new NearableID("f70815120671b2d9"), new Product("dog", "ID : f70815120671b2d9"));
        products.put(new NearableID("75272d8cbd2c38b6"), new Product("shoe", "ID : 75272d8cbd2c38b6"));
        products.put(new NearableID("797f9cba026ad0f4"), new Product("fridge", "ID : 797f9cba026ad0f4"));
        products.put(new NearableID("e3e08453dab86271"), new Product("bag", "ID : e3e08453dab86271"));
        products.put(new NearableID("cfc50e35bd5eebb7"), new Product("bed", "ID : cfc50e35bd5eebb7"));
        products.put(new NearableID("918ba00396988f42"), new Product("door", "ID : 918ba00396988f42"));
        showroomManager = new ShowroomManager(this, products);

        showroomManager.setListener(new ShowroomManager.Listener() {
            @Override
            public void onProductPickup(Product product) {
                switch (product.getName()){
                    case "dog": {
                        ((TextView) findViewById(R.id.titleLabel)).setText(product.getName());
                        ((TextView) findViewById(R.id.descriptionLabel)).setText(product.getRssi());
                        findViewById(R.id.descriptionLabel).setVisibility(View.VISIBLE);
                        break;
                    }
                    case "shoe": {
                        ((TextView) findViewById(R.id.titleLabel2)).setText(product.getName());
                        ((TextView) findViewById(R.id.descriptionLabel2)).setText(product.getRssi());
                        findViewById(R.id.descriptionLabel3).setVisibility(View.VISIBLE);
                        break;
                    }
                    case "fridge": {
                        ((TextView) findViewById(R.id.titleLabel3)).setText(product.getName());
                        ((TextView) findViewById(R.id.descriptionLabel3)).setText(product.getRssi());
                        findViewById(R.id.descriptionLabel3).setVisibility(View.VISIBLE);
                        break;
                    }
                    case "bag": {
                        ((TextView) findViewById(R.id.titleLabel4)).setText(product.getName());
                        ((TextView) findViewById(R.id.descriptionLabel4)).setText(product.getRssi());
                        findViewById(R.id.descriptionLabel4).setVisibility(View.VISIBLE);
                        break;
                    }
                    case "bed": {
                        ((TextView) findViewById(R.id.titleLabel5)).setText(product.getName());
                        ((TextView) findViewById(R.id.descriptionLabel5)).setText(product.getRssi());
                        findViewById(R.id.descriptionLabel5).setVisibility(View.VISIBLE);
                        break;
                    }
                    case "door": {
                        ((TextView) findViewById(R.id.titleLabel6)).setText(product.getName());
                        ((TextView) findViewById(R.id.descriptionLabel6)).setText(product.getRssi());
                        findViewById(R.id.descriptionLabel6).setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
            @Override
            public void onConnectedToService() {
                // Handle your actions here. You are now connected to connection service.
                // For example: you can create DeviceConnection object here from connectionProvider.
            }
        });
        try{
            startRepeatingTask();
        }catch(Exception e){
            clientThread.txMsg("\n Go fuck yourself" );
        }
    }
///////////////////////////////////////////////////////////////////////////////////
    View.OnClickListener buttonConnectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            clientThread = new ClientThread(
                    "192.168.43.119", 13150, clientHandler); //"192.168.43.80", Integer.parseInt(editTextPort.getText().toString())
            clientThread.start();

            buttonConnect.setEnabled(false);
            buttonSend.setEnabled(true);
        }
    };

    View.OnClickListener buttonSendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(clientThread != null){
                Iterator<Map.Entry<NearableID, Product> > here = products.entrySet().iterator();
                send_counter++;
                clientThread.txMsg("\n"+ send_counter + "th msg" );
                while(here.hasNext()){
                    Map.Entry<NearableID, Product> now = here.next();
                    Product item = now.getValue();
                    clientThread.txMsg(item.getName() +" ["+ item.getCounter() +"]= "+item.getSum());
                    item.flush();
                }
            }
        }
    };

    private void updateState(String state){
        textViewState.setText(state);
    }

    private void clientEnd(){
        clientThread = null;
        textViewState.setText("clientEnd()");
        buttonConnect.setEnabled(true);
        buttonSend.setEnabled(false);
    }

    public static class ClientHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private MainActivity parent;

        public ClientHandler(MainActivity parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_STATE:
                    parent.updateState((String)msg.obj);
                    break;
                case UPDATE_MSG:
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
    ////////////////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ShowroomManager updates");
            showroomManager.startUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Stopping ShowroomManager updates");
        showroomManager.stopUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showroomManager.destroy();
    }

    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            if(clientThread != null){
                Iterator<Map.Entry<NearableID, Product> > here = products.entrySet().iterator();
                send_counter++;
                clientThread.txMsg("\n"+ send_counter + "th msg" );
                while(here.hasNext()){
                    Map.Entry<NearableID, Product> now = here.next();
                    Product item = now.getValue();
                    clientThread.txMsg(item.getName() +" ["+ item.getCounter() +"]= "+item.getSum());
                    item.flush();
                }
            }
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };
    void startRepeatingTask()
    {
        mHandlerTask.run();
    }
    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }

}
