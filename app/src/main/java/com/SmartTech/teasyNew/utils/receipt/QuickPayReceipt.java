package com.SmartTech.teasyNew.utils.receipt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.Utils;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class QuickPayReceipt extends Receipt implements Serializable {

    private final String taxCode;
    private final long amountPaid;
    private final String agentName;
    private String billRef;

    public QuickPayReceipt(String taxCode, long amountPaid, String agentName, String billRef) {
        this.taxCode = taxCode;
        this.amountPaid = amountPaid;
        this.agentName = agentName;
        this.billRef = billRef;
    }

    @Override
    public byte[] generateReceipt() {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(INIT);
            os.write(CHAR_SPACING_0);

            os.write(NEW_LINE);

            Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.airs_receipt_img);
            os.write(printLogo(logoBitmap, 240));

            os.write(NEW_LINE);
            os.write(NEW_LINE);

            os.write(ALIGN_CENTER);
            os.write(FONT_12X24);
            os.write(FONT_SCALE_X2);
            os.write("TEASYPAY RECEIPT".getBytes());
            os.write(NEW_LINE);
            os.write(NEW_LINE);

            os.write(FONT_SCALE_X1);
            os.write(leftRightAlign(
                    "Date/Time",
                    new SimpleDateFormat("dd.MM.yyyy HH:mm").format(System.currentTimeMillis()),
                    '.'
            ));

            String marketType, txnType, category, subCategory;
            String[] parts = taxCode.split("/");
            switch (parts.length) {
                case 1:
                    marketType = parts[0];
                    txnType = parts[0];
                    category = "N.A.";
                    subCategory = "N.A.";
                    break;
                case 2:
                    marketType = parts[0];
                    txnType = parts[1];
                    category = "N.A.";
                    subCategory = "N.A.";
                    break;
                case 3:
                    marketType = parts[0];
                    txnType = parts[1];
                    category = parts[2];
                    subCategory = "N.A.";
                    break;
                case 4:
                    marketType = parts[0];
                    txnType = parts[1];
                    category = parts[2];
                    subCategory = parts[3];
                    break;

                default:
                    marketType = parts[0];
                    txnType = parts[1];
                    category = parts[2];
                    subCategory = parts[parts.length -1];
                    break;
            }

            os.write(leftRightAlign("Market Type", marketType, '.'));
            os.write(leftRightAlign("Txn Type", txnType, '.'));
            os.write(leftRightAlign("Txn Category", category, '.'));
            os.write(leftRightAlign("Tax Cat", subCategory, '.'));
            os.write(leftRightAlign("Amount", "N" + Utils.formatBalance(amountPaid), '.'));
            os.write(leftRightAlign("Bill Ref", billRef, '.'));
            os.write(leftRightAlign("Agent", agentName, '.'));

            os.write(NEW_LINE);
            os.write(NEW_LINE);
            os.write(NEW_LINE);
            os.write(NEW_LINE);
            os.write(NEW_LINE);
            os.write(NEW_LINE);
            os.write(NEW_LINE);
            return os.toByteArray();
        }
        catch (Exception e) {
            return null;
        }
    }

}
