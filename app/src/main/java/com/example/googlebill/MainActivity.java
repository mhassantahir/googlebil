package com.example.googlebill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
        public BillingProcessor bp;
        public Button btn_premium;
        public TextView textView;
    private TransactionDetails subscriptionTransactionDetails= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_premium=findViewById(R.id.btn_premium);
        textView=findViewById(R.id.textView);

        bp = new BillingProcessor(this, "YOUR LICENSE KEY FROM GOOGLE PLAY CONSOLE HERE", this);
        bp.initialize();
    }
    private boolean hassubscription() {
        if (subscriptionTransactionDetails != null) {
            return subscriptionTransactionDetails.purchaseInfo != null;
        }else return false;
    }
    @Override
    public void onBillingInitialized() {
        String productid="YOUR SUBSCRIPTION ID FROM GOOGLE PLAY CONSOLE HERE";
        subscriptionTransactionDetails = bp.getSubscriptionTransactionDetails(productid);
        btn_premium.setOnClickListener(v -> {
            if (bp.isSubscriptionUpdateSupported()){
                bp.subscribe(this, productid);
            }
            else{
                Toast.makeText(MainActivity.this," Subscription is not supported ",Toast.LENGTH_LONG).show();

            }
            if (hassubscription()){
                textView.setText("Premium");
            }
            else {
                textView.setText("Free");
            }

        });
    }
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Log.d(TAG, "onProductPurchased: ");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d(TAG, "onPurchaseHistoryRestored: ");
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d(TAG, "onBillingError: ");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

}