package com.estados.cuenta.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.estados.cuenta.Interface.PdfInterface;
import com.estados.cuenta.Pojo.CuentaHeader;
import com.estados.cuenta.Pojo.CuentaItem;
import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.Presentador.PdfPresentador;
import com.estados.cuenta.Vista.GlobalApplication;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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
    SharedPreferences sPreferences;
    Context context;

    public Pdf_modelo(PdfPresentador pPresentador) {
        this.pPresentador = pPresentador;
        context = GlobalApplication.getContext();
        sPreferences = context.getSharedPreferences("datosconexion", Context.MODE_PRIVATE);
    }

    @Override
    public void generatePdf(ArrayList<ListItem> lCuentas, ArrayList<String> dataHeader) {
        createPdf(lCuentas, dataHeader);
    }

    private void createPdf(ArrayList<ListItem> lMovimientos, ArrayList<String> dataHeader) {
        // TODO Auto-generated method stub
        Document document = new com.itextpdf.text.Document(PageSize.A4);
        String filename = "";
        String empresa = sPreferences.getString("empresa","");
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
            Font font = new Font(Font.getFamily("TIMES_ROMAN"), 12,    Font.BOLD|Font.UNDERLINE);
            Font header_font = new Font(Font.getFamily("TIMES_ROMAN"), 12,    Font.BOLD);
            Font line_font = new Font(Font.getFamily("TIMES_ROMAN"), 12,    Font.NORMAL);
            Paragraph p1 = new Paragraph("Estado de cuenta - "+empresa, font);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);
            document.add( Chunk.NEWLINE );
            //------Data header---//
            float[] columnFecha = {4f, 1f, 4f};
            PdfPTable fechas = new PdfPTable(columnFecha);
            fechas.setWidthPercentage(91.5f);
            fechas.addCell(getCell("Nro cuenta: "+dataHeader.get(0), PdfPCell.ALIGN_LEFT));
            fechas.addCell(getCell("", PdfPCell.ALIGN_CENTER));
            fechas.addCell(getCell("Desde: "+dataHeader.get(3)+" "+"Hasta: "+dataHeader.get(4), PdfPCell.ALIGN_RIGHT));
            document.add(fechas);
            Paragraph descripcion = new Paragraph("Descripcion: "+dataHeader.get(1));
            descripcion.setAlignment(Paragraph.ALIGN_LEFT);
            descripcion.setFirstLineIndent(22f);
            document.add(descripcion);
            Paragraph razon_social = new Paragraph("Razon social: "+dataHeader.get(2));
            razon_social.setAlignment(Paragraph.ALIGN_LEFT);
            razon_social.setFirstLineIndent(22f);
            document.add(razon_social);
            Paragraph rubro = new Paragraph("Rubro: "+dataHeader.get(5)+" "+dataHeader.get(6));
            rubro.setAlignment(Paragraph.ALIGN_LEFT);
            rubro.setFirstLineIndent(22f);
            document.add(rubro);

            float[] columnWidths = {2f, 2f, 1.5f, 1f, 1.5f, 2f, 2f};
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
                      insertCell(table, cuentaItem.getFecha(), Element.ALIGN_CENTER, 1, line_font);
                      insertCell(table, cuentaItem.getFecha(), Element.ALIGN_CENTER, 1, line_font);
                      insertCell(table, cuentaItem.getTdoc(), Element.ALIGN_CENTER, 1, line_font);
                      insertCell(table, cuentaItem.getSerie(), Element.ALIGN_CENTER, 1, line_font);
                      insertCell(table, cuentaItem.getNumerodoc(), Element.ALIGN_CENTER, 1, line_font);
                      insertCell(table, cuentaItem.getImporte(), Element.ALIGN_RIGHT, 1, line_font);
                      insertCell(table, cuentaItem.getSaldo(), Element.ALIGN_RIGHT, 1, line_font);
                }else{
                    CuentaHeader cuentaHeader = (CuentaHeader) lMovimientos.get(x);
                    insertCell(table_header, cuentaHeader.getMoneda(), Element.ALIGN_CENTER, 1, header_font);
                    paragraph.add(table_header);
                    insertCell(table, "Fecha", Element.ALIGN_CENTER, 1, header_font);
                    insertCell(table, "Fecha Vto", Element.ALIGN_CENTER, 1, header_font);
                    insertCell(table, "Tipo doc", Element.ALIGN_CENTER, 1, header_font);
                    insertCell(table, "Serie", Element.ALIGN_CENTER, 1, header_font);
                    insertCell(table, "Nro", Element.ALIGN_CENTER, 1, header_font);
                    insertCell(table, "Importe", Element.ALIGN_CENTER, 1, header_font);
                    insertCell(table, "Saldo", Element.ALIGN_CENTER, 1, header_font);
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

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
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
