package com.upc.appcentroidiomas;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InvoiceDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_detail_activity);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        TextView detailInvoiceDescription = findViewById(R.id.detailInvoiceDescription);
        TextView detailInvoiceSemester = findViewById(R.id.detailInvoiceSemester);
        TextView detailInvoiceAmount = findViewById(R.id.detailInvoiceAmount);
        TextView detailInvoiceIsPaid = findViewById(R.id.detailInvoiceIsPaid);
        TextView detailInvoiceDeadline = findViewById(R.id.detailInvoiceDeadline);
        TextView detailInvoicePaidAt = findViewById(R.id.detailInvoicePaidAt);
        Button btnInvoiceBack = findViewById(R.id.btnInvoiceBack);
        btnInvoiceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int id = extras.getInt("id", 0);
        String description = extras.getString("description", "");
        String semester = extras.getString("semester", "");
        Double amount = extras.getDouble("amount", 0);
        boolean isPaid = extras.getBoolean("isPaid", false);
        String deadline = extras.getString("deadline", "");
        String paidAt = extras.getString("paidAt", "");

        detailInvoiceDescription.setText("DESCRIPCIÓN: " + description);
        detailInvoiceSemester.setText("CICLO: " + semester);
        detailInvoiceAmount.setText("MONTO: " + String.valueOf(amount) + " soles.");
        detailInvoiceDeadline.setText("FECHA DE VENCIMIENTO: " + deadline);
        String isPaidMessage;
        if (isPaid){
            isPaidMessage = "Esta boleta ha sido cancelada.";
            detailInvoiceIsPaid.setTextColor(Color.parseColor("#00FF00"));
            detailInvoiceIsPaid.setText(isPaidMessage);

            detailInvoicePaidAt.setText("FECHA DE PAGO: " + paidAt);
        }else {
            isPaidMessage = "Esta boleta no ha sido cancelada aún.";
            detailInvoiceIsPaid.setTextColor(Color.parseColor("#FF0000"));
            detailInvoiceIsPaid.setText(isPaidMessage);
        }
    }
}
