package br.com.segware;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.segware.entity.Evento;

public class MontadorRelatorio {

	public List<Evento> gerarRelatorio() {

	    String arquivoCSV = "/home/pcfmello/git/developer-test-file-analyze/src/test/java/br/com/segware/relatorio.csv";
	    BufferedReader br = null;
	    String linha = "";
	    String divisor = ",";
	    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
	    List<Evento> listaDeEventos = new ArrayList<>();
	    try {
	    	br = new BufferedReader(new FileReader(arquivoCSV));
	        while ((linha = br.readLine()) != null) {
	        	String[] evento = linha.split(divisor);	        	
	            listaDeEventos.add(new Evento(Integer.parseInt(evento[0]), evento[1], evento[2], 
	            		Tipo.valueOf(evento[3]), DateTime.parse(evento[4], format), DateTime.parse(evento[5], format), 
	            		evento[6]));
	        }	      

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	    	if (br != null) {
	    		try {
	                br.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }	    
		return listaDeEventos;
	}
	
}
