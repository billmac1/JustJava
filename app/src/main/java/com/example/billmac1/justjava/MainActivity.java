package com.example.billmac1.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        int price = 0;
        String mailMessage = "";
        EditText customerName = (EditText)findViewById(R.id.customer_name);
        String custName = customerName.getText().toString();
        CheckBox whippedCreamCheckbox = (CheckBox)findViewById(R.id.whipped_cream_checkbox);
        Boolean hasWhippedCream = whippedCreamCheckbox.isChecked();
        CheckBox chocolateCheckbox = (CheckBox)findViewById(R.id.chocolate_checkbox);
        Boolean hasChocolate = chocolateCheckbox.isChecked();
        price = calculatePrice(hasWhippedCream, hasChocolate);
        mailMessage = calculateOrderSummary(price, hasWhippedCream, hasChocolate, custName);
     /* displayMessage(calculateOrderSummary(price, hasWhippedCream, hasChocolate, custName)); */
    /*    sendEmail("billmacnyc@gmail.com", "Coffee Order", mailMessage); */
        otherSendEmail(custName,"billmacnyc@gmail.com", "Coffee Order", mailMessage);
    }

    public String calculateOrderSummary(int totPrice, boolean addWhippedCream,boolean addChocolate,
                                        String customer){
        String priceMessage = "Name: " + customer + "\n";
        priceMessage = priceMessage + "Add whipped cream? " + addWhippedCream + "\n";
        priceMessage = priceMessage + "Add chocolate? " + addChocolate + "\n";
        priceMessage = priceMessage + "Quantity: " + quantity + "\n";
        priceMessage = priceMessage + "Total $" + totPrice + "\n" + getString(R.string.thanks) + "\n";
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int myNumber) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + myNumber);
    }

    /**
     * Calculates the price of the order.
     * quantity is the number of cups of coffee ordered
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;  /* price for just coffee */
        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }

        if (addChocolate){
            basePrice = basePrice + 2;
        }

        return quantity * basePrice;
    }

    /* commented for email only version
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
    */

    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    public void sendEmail(String mailRecipient, String mailSubject, String mailBody){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailRecipient});
        intent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, mailBody);

        startActivity(intent);
    }

    public void otherSendEmail(String name, String mailRecipient, String mailSubject, String mailBody){
        // Use an intent to launch an email app.
        // Send the order summary in the email body.
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"pocket_stock@yahoo.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "JustJava order for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, mailBody);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}