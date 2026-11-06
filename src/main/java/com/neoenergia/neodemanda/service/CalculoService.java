package com.neoenergia.neodemanda.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.neoenergia.neodemanda.dto.CalculoRequest;
import com.neoenergia.neodemanda.dto.CalculoResponse;
import com.neoenergia.neodemanda.exception.ResourceNotFoundException;
import com.neoenergia.neodemanda.model.CalculoGuardado;
import com.neoenergia.neodemanda.motor.CalculadoraDemanda;
import com.neoenergia.neodemanda.motor.entrada.DadosEntradaCalculo;
import com.neoenergia.neodemanda.motor.excecao.EntradaInvalidaException;
import com.neoenergia.neodemanda.motor.saida.ResultadoCalculo;
import com.neoenergia.neodemanda.repository.CalculoRepository;

@Service
public class CalculoService {

	private final ProjetoService projetoService;

	private final CalculadoraDemanda calculadora;

	private final CalculoRepository calculoRepository;

	public CalculoService(ProjetoService projetoService, CalculadoraDemanda calculadora,
			CalculoRepository calculoRepository) {
		this.projetoService = projetoService;
		this.calculadora = calculadora;
		this.calculoRepository = calculoRepository;
	}

	public CalculoResponse calcular(Long projetoId, CalculoRequest requisicao) {
		ProjetoParaCalculo projeto = projetoService.consultarParaNovoCalculo(projetoId);
		validarCargaInstalada(projeto.cargaInstaladaKva());

		DadosEntradaCalculo entradas = paraEntradas(projeto, requisicao);
		ResultadoCalculo resultado = calculadora.calcular(entradas);

		CalculoGuardado calculo = new CalculoGuardado(projetoId, projeto.revisao(), OffsetDateTime.now(), entradas,
				resultado);
		calculoRepository.salvar(calculo);

		return CalculoResponse.de(calculo);
	}

	public CalculoResponse consultar(Long projetoId) {
		ProjetoParaCalculo projeto = projetoService.consultarParaCalculo(projetoId);

		return calculoRepository.buscarPorProjeto(projetoId)
				.filter(calculo -> calculo.revisaoProjeto() == projeto.revisao())
				.map(CalculoResponse::de)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum calculo valido para o projeto: " + projetoId));
	}

	private static void validarCargaInstalada(BigDecimal cargaInstaladaKva) {
		BigDecimal normalizada = cargaInstaladaKva.stripTrailingZeros();
		long digitosInteiros = (long) normalizada.precision() - normalizada.scale();
		if (digitosInteiros > CalculoRequest.DIGITOS_INTEIROS || normalizada.scale() > CalculoRequest.DIGITOS_FRACAO) {
			throw new EntradaInvalidaException(
					"A carga instalada do projeto esta fora do limite aceito para o calculo. Corrija o projeto.");
		}
	}

	private static DadosEntradaCalculo paraEntradas(ProjetoParaCalculo projeto, CalculoRequest requisicao) {
		DadosEntradaCalculo.Builder entradas = DadosEntradaCalculo.builder()
				.tipoEdificacao(projeto.tipoEdificacao())
				.quantidadeUnidadesConsumidoras(projeto.quantidadeUnidadesConsumidoras())
				.cargaInstaladaKva(projeto.cargaInstaladaKva())
				.condominioSmartStudio(Boolean.TRUE.equals(requisicao.condominioSmartStudio()))
				.iluminacaoAreasComunsW(requisicao.iluminacaoAreasComunsW())
				.tomadasAreasComunsW(requisicao.tomadasAreasComunsW())
				.cargaComercialKw(requisicao.cargaComercialKw())
				.clienteGrupoA(requisicao.clienteGrupoA());

		adicionar(requisicao.apartamentos(), item -> entradas.apartamento(item.areaUtilM2(), item.quantidade().intValueExact()));
		adicionar(requisicao.motores(), item -> entradas.motor(item.potenciaCv(), item.quantidade().intValueExact()));
		adicionar(requisicao.condicionadoresAr(),
				item -> entradas.condicionadorAr(item.potencia(), item.unidadePotencia(), item.quantidade().intValueExact()));
		adicionar(requisicao.estacoesRecarga(),
				item -> entradas.estacaoRecarga(item.potenciaKw(), item.quantidade().intValueExact(), item.tipoConexao()));

		return entradas.build();
	}

	private static <T> void adicionar(List<T> itens, Consumer<T> acao) {
		if (itens != null) {
			itens.forEach(acao);
		}
	}

}
