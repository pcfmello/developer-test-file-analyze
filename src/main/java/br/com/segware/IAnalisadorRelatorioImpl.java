package br.com.segware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import br.com.segware.entity.Evento;

public class IAnalisadorRelatorioImpl implements IAnalisadorRelatorio {
	
	MontadorRelatorio montador =  new MontadorRelatorio();	
	List<Evento> listaDeEventos; 
	
	@Override
	public Map<String, Integer> getTotalEventosCliente() {
		this.listaDeEventos = montador.gerarRelatorio();
		Map<String, Integer> totalEventos = new HashMap<String, Integer>();		
		for(Evento evento : listaDeEventos) {			
			totalEventos.put(evento.getCodCliente(), calculaTotalEventosCliente(evento, listaDeEventos));			
		}		
		return totalEventos;		
	}
	
	private Integer calculaTotalEventosCliente(Evento evento, List<Evento> lista) {
		Integer total = 0;			
		for(Evento item : listaDeEventos) {
			if(item.getCodCliente().equals(evento.getCodCliente())) total++; 
		}
		return total;
	}

	@Override
	public Map<String, Long> getTempoMedioAtendimentoAtendente() {
		this.listaDeEventos = montador.gerarRelatorio();
		Map<String, Long> tempoMedioAtendimento = new HashMap<String, Long>();				
		for(Evento evento : listaDeEventos) {					
			tempoMedioAtendimento.put(evento.getCodAtendente(), calculaTempoMedioAtendimento(evento, listaDeEventos));			
		}		
		return tempoMedioAtendimento;	
	}
	
	private Long calculaTempoMedioAtendimento(Evento evento, List<Evento> lista) {
		Long tempo = 0L;
		Integer totalDeAtendimentos = 0;
		for(Evento item : lista) {
			if(item.getCodAtendente().equals(evento.getCodAtendente())) {
				tempo += calculaIntervaloDeTempoEmSegundos(item.getDataInicio(), item.getDataFim()); 
				totalDeAtendimentos++;
			}
		}
		return tempo / totalDeAtendimentos;
	}
	
	@Override
	public List<Tipo> getTiposOrdenadosNumerosEventosDecrescente() {	
		this.listaDeEventos = montador.gerarRelatorio();
		Map<Integer, Tipo> eventosMap = new TreeMap<>();				
		for(Evento evento : listaDeEventos) {		
			
			eventosMap.put(calculaTotalEventosTipo(evento, listaDeEventos), evento.getTipoEvento());			
		}		
		List<Tipo> eventosList = new ArrayList<>(eventosMap.values());
		Collections.reverse(eventosList);
		return eventosList; 
	}
	
	private Integer calculaTotalEventosTipo(Evento evento, List<Evento> lista) {
		Integer totalEventos = 0;
		for(Evento item : listaDeEventos) {
			if(item.getTipoEvento().equals(evento.getTipoEvento())) {					
				totalEventos++;
			}
		}
		return totalEventos;
	}

	@Override
	public List<Integer> getCodigoSequencialEventosDesarmeAposAlarme() {
		this.listaDeEventos = montador.gerarRelatorio();
		Map<String, List<Evento>> listaDeEventosPorCliente = criaListaEventosPorCliente(listaDeEventos);
		List<Integer> listaCodigosSequenciais = criaListaCodigosSequenciais(listaDeEventosPorCliente);
		return listaCodigosSequenciais;
	}

	private Map<String, List<Evento>> criaListaEventosPorCliente(List<Evento> eventos) {
		Map<String, List<Evento>> lista = new TreeMap<>();
		for(Evento evento : eventos) {
			if(lista.containsKey(evento.getCodCliente())) {
				lista.get(evento.getCodCliente()).add(evento);
			} else {
				List<Evento> novaListaDeEventos = new ArrayList<>();
				novaListaDeEventos.add(evento);
				lista.put(evento.getCodCliente(), novaListaDeEventos);
			}
		}
		return lista;
	}
	
	private List<Integer> criaListaCodigosSequenciais(Map<String, List<Evento>> listaDeEventosPorCliente) {
		List<Integer> listaCodigosSequenciais = new ArrayList<>();
		for(Map.Entry<String, List<Evento>> pair: listaDeEventosPorCliente.entrySet()) {
			for(int i = 0; i < pair.getValue().size(); i++) {
				Evento eventoAtual = pair.getValue().get(i);
				if(i > 0) {
					Evento eventoAnterior = pair.getValue().get(i - 1);
					if(eventoAtual.getTipoEvento().equals(Tipo.DESARME) && 
							eventoAnterior.getTipoEvento().equals(Tipo.ALARME)) {												
						Integer tempo = calculaIntervaloDeTempoEmSegundos(eventoAnterior.getDataInicio(), eventoAtual.getDataInicio());
						
						if(isIntervaloTempo(tempo)) {
							listaCodigosSequenciais.add(eventoAtual.getCodSequencial().intValue());
						}											
					}
				}				
			}						
		}
		return listaCodigosSequenciais;
	}
	
	private Boolean isIntervaloTempo(Integer tempo) {
		Integer cincoMinutos = 300;
		return tempo <= cincoMinutos ? true : false;
	}

	private Integer calculaIntervaloDeTempoEmSegundos(DateTime inicio, DateTime fim) {
		return Seconds.secondsBetween(inicio, fim).getSeconds();
	}
}
