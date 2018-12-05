/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 *
 * package com.example.android.justjava; 
 */
package com.example.the_insider.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method displays the given text on screen
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox addWhippedCream = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = addWhippedCream.isChecked();
        CheckBox addChocolate = (CheckBox) findViewById(R.id.chocolate_cream_checkbox);
        boolean hasChocolate = addChocolate.isChecked();
        int price = calculatePrice(quantity, hasWhippedCream, hasChocolate);
        String customerName = getCustomerName();
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, customerName);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); //only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, customerName));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        displayMessage(priceMessage);
    }

    /* Get customer name by the name field
    *
    * @return the customer name
     */
    private String getCustomerName() {
        EditText nameField = (EditText) findViewById(R.id.customer_name);
        String customerName = nameField.getText().toString();
        //nameField.setText("");
        return customerName;
    }

    /* Calculate the price of the order
    *
    * @param quantity is the number of cups of coffee ordered
    * @param hasWhippedCream if coffee has whipped cream or not
    * @param hasChocolate if coffee has chocolate or not
    * @return the total price
     */
    private int calculatePrice(int quantity, boolean hasWhippedCream, boolean hasChocolate) {
        int finalPrice = 5;
        int whippedCreamPrice = 1;
        int chocolatePrice = 2;
        if(hasWhippedCream) {
            finalPrice += whippedCreamPrice;
        }
        if(hasChocolate) {
            finalPrice += chocolatePrice;
        }
        return quantity * finalPrice;
    }
    /*
    * Create order summary
    *
    * @param total price of coffee
    * @param hasWhippedCream if coffee has whipped cream or not
    * @param hasChocolate if coffee has chocolate or not
    * @param customerName name entered by client in name field
    * @return order summary
    */
    private String createOrderSummary(int price, boolean hasWhippedCream,
                                      boolean hasChocolate, String customerName) {
        String priceMessage = getString(R.string.order_summary_name, customerName);
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, (hasWhippedCream? getString(R.string.yes) : getString(R.string.no)));
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, (hasChocolate? getString(R.string.yes) : getString(R.string.no)));
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if(quantity == 100) {
            Toast.makeText(this, R.string.upper_limit, Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        displayQuantity(quantity);
    }
    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if(quantity == 1) {
            Toast.makeText(this, R.string.lower_limit, Toast.LENGTH_SHORT).show();
            return;
        }
        --quantity;
        displayQuantity(quantity);
    }
}
