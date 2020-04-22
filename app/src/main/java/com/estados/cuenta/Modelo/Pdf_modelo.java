package com.estados.cuenta.Modelo;

import android.graphics.fonts.Font;
import android.os.Environment;
import android.util.Log;

import com.estados.cuenta.Interface.PdfInterface;
import com.estados.cuenta.Pojo.CuentaHeader;
import com.estados.cuenta.Pojo.CuentaItem;
import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.Presentador.PdfPresentador;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Pdf_modelo implements PdfInterface.PdfModelo {

    PdfInterface.PdfPresentador pPresentador;

    public Pdf_modelo(PdfPresentador pPresentador) {
        this.pPresentador = pPresentador;
    }

    @Override
    public void generatePdf(ArrayList<ListItem> lCuentas) {
        createPdf(lCuentas);
    }

    private void createPdf(ArrayList<ListItem> lMovimientos) {
        // TODO Auto-generated method stub
        Document document = new com.itextpdf.text.Document(PageSize.A4);
        String filename = "";
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Estadocuenta";
            Paragraph paragraph = new Paragraph();
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault());
            filename = "EstCta"+sdf.format(new Date())+".pdf";
            File file = new File(dir, filename);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);
            //open the document
            document.open();

            Paragraph p1 = new Paragraph("Movimientos");
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);

            float[] columnWidths = {2f, 1.5f, 1f, 1.5f, 2f, 2f};
            float[] column_header = {10f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            PdfPTable table_header = new PdfPTable(column_header);
            document.add( Chunk.NEWLINE );
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);
            table_header.setWidthPercentage(90f);
            /*table.setHeaderRows(1);*/

            for(int x=0; x<lMovimientos.size(); x++){
                if(lMovimientos.get(x) instanceof CuentaItem){
                     CuentaItem cuentaItem = (CuentaItem) lMovimientos.get(x);
                      insertCell(table, cuentaItem.getFecha(), Element.ALIGN_CENTER, 1, null);
                      insertCell(table, cuentaItem.getTdoc(), Element.ALIGN_CENTER, 1, null);
                      insertCell(table, cuentaItem.getSerie(), Element.ALIGN_CENTER, 1, null);
                      insertCell(table, cuentaItem.getNumerodoc(), Element.ALIGN_CENTER, 1, null);
                      insertCell(table, cuentaItem.getImporte(), Element.ALIGN_CENTER, 1, null);
                      insertCell(table, cuentaItem.getSaldo(), Element.ALIGN_CENTER, 1, null);
                }else{
                    CuentaHeader cuentaHeader = (CuentaHeader) lMovimientos.get(x);
                    insertCell(table_header, cuentaHeader.getMoneda(), Element.ALIGN_CENTER, 1, null);
                    paragraph.add(table_header);
                    insertCell(table, "Fecha", Element.ALIGN_CENTER, 1, null);
                    insertCell(table, "Tipo doc", Element.ALIGN_CENTER, 1, null);
                    insertCell(table, "Serie", Element.ALIGN_CENTER, 1, null);
                    insertCell(table, "Nro", Element.ALIGN_CENTER, 1, null);
                    insertCell(table, "Importe", Element.ALIGN_CENTER, 1, null);
                    insertCell(table, "Saldo", Element.ALIGN_CENTER, 1, null);
                }
            }
            paragraph.add(table);
            document.add(paragraph);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally    {
            document.close();
            pPresentador.returnPdfPath(filename);
        }
    }


    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

}
