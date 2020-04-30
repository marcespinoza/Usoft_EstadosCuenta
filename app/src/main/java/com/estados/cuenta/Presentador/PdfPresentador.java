package com.estados.cuenta.Presentador;

import com.estados.cuenta.Interface.PdfInterface;
import com.estados.cuenta.Modelo.Pdf_modelo;
import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.Vista.MovimientoActivity;

import java.util.ArrayList;

public class PdfPresentador implements PdfInterface.PdfPresentador {

    PdfInterface.PdfModelo pModelo;
    PdfInterface.PdfVista pVista;

    public PdfPresentador(MovimientoActivity pVista) {
        this.pVista = pVista;
        pModelo = new Pdf_modelo(this);
    }

    @Override
    public void sendList(ArrayList<ListItem> lCuentas, ArrayList<String> dataHeader, boolean b) {
        pModelo.generatePdf(lCuentas, dataHeader, b);
    }

    @Override
    public void returnPdfPath(String filename) {
        pVista.showSnackBar(filename);
    }
}
