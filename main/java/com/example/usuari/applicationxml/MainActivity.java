package com.example.usuari.applicationxml;

import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    TextView tvdatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvdatos = (TextView) this.findViewById(R.id.tvDatos);
    }
    public void recogerDatos(View v){
        CommTask ct = new CommTask();
        ct.execute("http://www.aemet.es/xml/municipios/localidad_28079.xml");
    }
    private class CommTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvdatos.setText(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado="";
            try {
                URL url = new URL(strings[0]);
                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();
                Document doc;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dbuilder;
                dbuilder = factory.newDocumentBuilder();
                doc = dbuilder.parse(is);
                NodeList listanombre = doc.getElementsByTagName("nombre");
                resultado+="Localidad: "+listanombre.item(0).getTextContent()+"\n";
                NodeList listaPrecipitacion = doc.getElementsByTagName("prob_precipitacion");
                NodeList lista = doc.getElementsByTagName("temperatura");
                System.out.println("bien");
                /*Element el2 = (Element) lista.item(0);
                el2.getElementsByTagName("dato");
                System.out.println(lista.item(1).getTextContent());*/
                double mediatemp=0;
                double media = 0;
                for (int i=0;i<listaPrecipitacion.getLength();i++){
                    String valor = listaPrecipitacion.item(i).getTextContent();
                    if (valor == null||valor.equals(""))
                        valor="0";
                    media +=Double.parseDouble(valor);
                }
                media = media/listaPrecipitacion.getLength();
                resultado+="Media Precipitaciones: "+media;
                /*System.out.println("bien2");
                for (int i=0;i<lista.getLength();i++){
                    Element el = (Element) lista.item(i);
                    el.getElementsByTagName("dato");
                    String valor = lista.item(i).getTextContent();
                    if (valor == null||valor.equals(""))
                        valor="0";
                    mediatemp +=Double.parseDouble(valor);
                }
                System.out.println("bien3");
                mediatemp = mediatemp/lista.getLength();
                resultado+="Media Temperatura: "+mediatemp;*/
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            return resultado;
        }
    }
}
