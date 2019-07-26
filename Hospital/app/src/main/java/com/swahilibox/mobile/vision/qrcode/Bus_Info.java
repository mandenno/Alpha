package com.swahilibox.mobile.vision.qrcode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileOutputStream;
import java.io.IOException;

public class Bus_Info extends AppCompatActivity {
TextView bus_name, bus_loc, permit_no, cert_no, amount_paid, plot_no, doi, doe;
ImageView image_url;
    Bitmap bitmap1,bitmap2;
    Canvas canvas;
    Paint paint;
    int BitmapSize = 30;
    int width, height;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus__info);
        bus_name=(TextView)findViewById(R.id.donor_name);
        bus_loc=(TextView)findViewById(R.id.bus_loc);
        permit_no=(TextView)findViewById(R.id.permit_no);
        cert_no=(TextView)findViewById(R.id.cert_no);
        amount_paid=(TextView)findViewById(R.id.amount_paid);
        plot_no=(TextView)findViewById(R.id.plot_no);
        doi=(TextView)findViewById(R.id.doi);
        doe=(TextView)findViewById(R.id.doe);
        image_url=(ImageView)findViewById(R.id.image);
        resources = getResources();


        Glide.with(this)
                .load(Bus_Info.this.getIntent().getExtras().getString("image_url"))
                .apply(RequestOptions.circleCropTransform())
                .into(image_url);

        bus_name.setText(Bus_Info.this.getIntent().getExtras().getString("bus_name"));
        bus_loc.setText("Location\n"+Bus_Info.this.getIntent().getExtras().getString("bus_location"));
        permit_no.setText("Permit No\n"+Bus_Info.this.getIntent().getExtras().getString("permit_no"));
        cert_no.setText("Certificate No\n"+Bus_Info.this.getIntent().getExtras().getString("cert_no"));
        amount_paid.setText("Amount Paid\nKSH "+Bus_Info.this.getIntent().getExtras().getString("amount_paid"));
        plot_no.setText("Plot No\n"+Bus_Info.this.getIntent().getExtras().getString("plotno"));
        doi.setText("Issue Date: "+Bus_Info.this.getIntent().getExtras().getString("doi"));
        doe.setText("Expiry Date: "+Bus_Info.this.getIntent().getExtras().getString("doe"));
    }



    public void printDocument(View view)
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = Bus_Info.this.getIntent().getExtras().getString("bus_name") +
                " Document";

        printManager.print(jobName, new
                        MyPrintDocumentAdapter(this),
                null);
    }

    public class MyPrintDocumentAdapter extends PrintDocumentAdapter
    {
        Context context;
        private int pageHeight;
        private int pageWidth;
        public PdfDocument myPdfDocument;
        public int totalpages = 1;

        public MyPrintDocumentAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback,
                             Bundle metadata) {

            myPdfDocument = new PrintedPdfDocument(context, newAttributes);

            pageHeight =
                    newAttributes.getMediaSize().getHeightMils()/1000 * 72;
            pageWidth =
                    newAttributes.getMediaSize().getWidthMils()/1000 * 72;

            if (cancellationSignal.isCanceled() ) {
                callback.onLayoutCancelled();
                return;
            }

            if (totalpages > 0) {
                PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                        .Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(totalpages);

                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            } else {
                callback.onLayoutFailed("Page count is zero.");
            }
        }


        @Override
        public void onWrite(final PageRange[] pageRanges,
                            final ParcelFileDescriptor destination,
                            final CancellationSignal
                                    cancellationSignal,
                            final WriteResultCallback callback) {

            for (int i = 0; i < totalpages; i++) {
                if (pageInRange(pageRanges, i))
                {
                    PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                            pageHeight, i).create();

                    PdfDocument.Page page =
                            myPdfDocument.startPage(newPage);

                    if (cancellationSignal.isCanceled()) {
                        callback.onWriteCancelled();
                        myPdfDocument.close();
                        myPdfDocument = null;
                        return;
                    }
                    drawPage(page, i);
                    myPdfDocument.finishPage(page);
                }
            }

            try {
                myPdfDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                myPdfDocument.close();
                myPdfDocument = null;
            }

            callback.onWriteFinished(pageRanges);
        }

    }

    private void drawPage(PdfDocument.Page page,
                          int pagenumber) {
        Canvas canvas = page.getCanvas();

        pagenumber++; // Make sure page numbers start at 1

        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText(
                "MOMBASA COUNTY",
                leftMargin,
                titleBaseLine,
                paint);
        paint.setTextSize(25);
        canvas.drawText(
                "Business Permit Verification Receipt",
                leftMargin,
                titleBaseLine+35,
                paint);


        canvas.drawText("Business Name: "+Bus_Info.this.getIntent().getExtras().getString("bus_name"), leftMargin, titleBaseLine + 65, paint);
        canvas.drawText(
                "Business Location: "+Bus_Info.this.getIntent().getExtras().getString("bus_location"), leftMargin, titleBaseLine + 95, paint);

        canvas.drawText(
                "Permit No: "+Bus_Info.this.getIntent().getExtras().getString("permit_no"), leftMargin, titleBaseLine + 125, paint);

        canvas.drawText(
                "Plot No: "+Bus_Info.this.getIntent().getExtras().getString("plotno"), leftMargin, titleBaseLine + 155, paint);

        if (pagenumber % 2 == 0)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.GREEN);

        PdfDocument.PageInfo pageInfo = page.getInfo();

        bitmap1 = BitmapFactory.decodeResource(
                resources,
                R.mipmap.approved
        );

        canvas.drawBitmap(
                bitmap1,null,new RectF(pageInfo.getPageWidth()/2,pageInfo.getPageHeight()/2,pageInfo.getPageWidth()/2, pageInfo.getPageHeight()/2),null
        );

    }


    private boolean pageInRange(PageRange[] pageRanges, int page)
    {
        for (int i = 0; i<pageRanges.length; i++)
        {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }


}
