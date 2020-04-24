package com.estados.cuenta.Interface;

import com.estados.cuenta.Pojo.CuentaItem;
import com.estados.cuenta.Pojo.ListItem;

import java.util.ArrayList;

public interface PdfInterface {

    interface PdfModelo{
        void generatePdf(ArrayList<ListItem> lCuentas, ArrayList<String> dataHeader);
    }

    interface PdfPresentador{
        void sendList(ArrayList<ListItem> lCuentas, ArrayList<String> dataHeader);
        void returnPdfPath(String path);
    }

    interface PdfVista{
        void showSnackBar(String path);
    }

}
