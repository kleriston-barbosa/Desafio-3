package teste3;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Faturamento {
	
	public static void main(String[] args) {
		  
		try{
				// Abrindo o arquivo XML
			File arqFaturamento = new File("dados.xml");
	      
				// Criando um parser DOM
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(arqFaturamento);
	      
				// garantindo consistência na estrutura do XML
			doc.getDocumentElement().normalize();
			
			BigDecimal menorFaturamento = new BigDecimal(Double.MAX_VALUE);
			BigDecimal maiorFaturamento = new BigDecimal(0);
			BigDecimal somaFaturamento = new BigDecimal(0);
			BigDecimal mediaMensal = new BigDecimal(0);
			BigDecimal faturamentoDia = new BigDecimal(0);
			int diasComFaturamento = 0;
			int diasAcimaMedia = 0;
			
				// passando dados xml 
			NodeList listaDadosXml = doc.getElementsByTagName("row");
	      
	      
			// loop for para percorrer toda a listaDadosXml			
			for(int i = 0; i < listaDadosXml.getLength(); i++){
				Element linha = (Element) listaDadosXml.item(i);
				
				// Passando o valor do faturamento diario  
				faturamentoDia = BigDecimal.valueOf(Double.parseDouble((linha.getElementsByTagName("valor").item(0).getTextContent().trim())));
				
				// serve como parametro no switch-case para verificar se o faturamento é maior que 0 (zero)
				int x = faturamentoDia.intValue() ;
				
				//definindo o menor e maior faturamento, contagem de dias que houve faturamento
				switch(x) {
					case 0: break;
					default: {
						if (faturamentoDia.compareTo(menorFaturamento) == -1){
							menorFaturamento = faturamentoDia;
						}else if(faturamentoDia.compareTo(maiorFaturamento) == 1){
							maiorFaturamento = faturamentoDia;
						}
						diasComFaturamento++; // conta os dias de faturamento ignorando dias com faturamento 0
						somaFaturamento = somaFaturamento.add(faturamentoDia); // soma dos faturamentos
					}
				}	
			}	
			// calculando a media mensal de faturamento
			mediaMensal = somaFaturamento.divide(new BigDecimal(diasComFaturamento), 2, RoundingMode.HALF_EVEN);
	      
				// contando dias com rendimento acima da media mensal 
			for (int i = 0; i < listaDadosXml.getLength(); i++){
				Element linha = (Element) listaDadosXml.item(i);
				faturamentoDia = BigDecimal.valueOf(Double.parseDouble(linha.getElementsByTagName("valor").item(0).getTextContent().trim()));
				
				if(faturamentoDia.compareTo(mediaMensal) == 1){
					diasAcimaMedia++;
				}
			}
		
				// Resultado
			System.out.println("Menor valor de faturamento: R$ " + menorFaturamento.setScale(2, RoundingMode.HALF_EVEN));
			System.out.println("Maior valor de faturamento: R$ " + maiorFaturamento.setScale(2, RoundingMode.HALF_EVEN));
	      	System.out.println("Número de dias com faturamento acima da média: " + diasAcimaMedia);
	      	System.out.println("valor de redimento total mensal "+somaFaturamento);
	      
	    }catch(Exception e){
	    		e.printStackTrace();
	    } 
	}
}
