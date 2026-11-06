package com.neoenergia.neodemanda.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.neoenergia.neodemanda.model.CalculoGuardado;
import com.neoenergia.neodemanda.model.enums.TipoEdificacao;
import com.neoenergia.neodemanda.motor.entrada.ApartamentoEntrada;
import com.neoenergia.neodemanda.motor.entrada.CondicionadorArEntrada;
import com.neoenergia.neodemanda.motor.entrada.DadosEntradaCalculo;
import com.neoenergia.neodemanda.motor.entrada.EstacaoRecargaEntrada;
import com.neoenergia.neodemanda.motor.entrada.MotorEntrada;
import com.neoenergia.neodemanda.motor.entrada.TipoConexaoRecarga;
import com.neoenergia.neodemanda.motor.entrada.UnidadePotenciaAr;
import com.neoenergia.neodemanda.motor.norma.Tabela2PadraoEntradaBt;
import com.neoenergia.neodemanda.motor.saida.Enquadramento;
import com.neoenergia.neodemanda.motor.saida.EntradaPasso;
import com.neoenergia.neodemanda.motor.saida.NormaRevisao;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;
import com.neoenergia.neodemanda.motor.saida.ResultadoCalculo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

public record CalculoResponse(
		Long projetoId,
		OffsetDateTime calculadoEm,
		Entradas entradas,
		List<Norma> normas,
		Resultado resultado,
		List<Passo> memorial) {

	private static final int CASAS_UNIDADE = 2;

	private static final int CASAS_FATOR = 4;

	private static final Set<UnidadeMedida> UNIDADES_INFORMADAS = EnumSet.of(UnidadeMedida.M2, UnidadeMedida.W,
			UnidadeMedida.KW, UnidadeMedida.CV);

	public static CalculoResponse de(CalculoGuardado guardado) {
		ResultadoCalculo resultado = guardado.resultado();
		return new CalculoResponse(
				guardado.projetoId(),
				guardado.calculadoEm(),
				Entradas.de(guardado.entradas()),
				resultado.memoria().normasUsadas().stream().map(Norma::de).toList(),
				Resultado.de(resultado),
				resultado.memoria().passos().stream().map(Passo::de).toList());
	}

	private static BigDecimal arredondar(BigDecimal valor, int casas) {
		return valor == null ? null : valor.setScale(casas, RoundingMode.HALF_UP);
	}

	private static BigDecimal porUnidade(BigDecimal valor) {
		return arredondar(valor, CASAS_UNIDADE);
	}

	private static BigDecimal fator(BigDecimal valor) {
		return arredondar(valor, CASAS_FATOR);
	}

	private static BigDecimal porUnidade(BigDecimal valor, UnidadeMedida unidade) {
		if (unidade != UnidadeMedida.ADIMENSIONAL) {
			return porUnidade(valor);
		}
		return valor.scale() == 0 ? valor : fator(valor);
	}

	public record Entradas(
			TipoEdificacao tipoEdificacao,
			Integer quantidadeUnidadesConsumidoras,
			BigDecimal cargaInstaladaKva,
			boolean condominioSmartStudio,
			List<Apartamento> apartamentos,
			BigDecimal iluminacaoAreasComunsW,
			BigDecimal tomadasAreasComunsW,
			List<Motor> motores,
			BigDecimal cargaComercialKw,
			List<CondicionadorAr> condicionadoresAr,
			List<EstacaoRecarga> estacoesRecarga,
			Boolean clienteGrupoA) {

		private static Entradas de(DadosEntradaCalculo dados) {
			return new Entradas(
					dados.tipoEdificacao(),
					dados.quantidadeUnidadesConsumidoras(),
					dados.cargaInstaladaKva(),
					dados.condominioSmartStudio(),
					dados.apartamentos().stream().map(Apartamento::de).toList(),
					dados.iluminacaoAreasComunsW(),
					dados.tomadasAreasComunsW(),
					dados.motores().stream().map(Motor::de).toList(),
					dados.cargaComercialKw(),
					dados.condicionadoresAr().stream().map(CondicionadorAr::de).toList(),
					dados.estacoesRecarga().stream().map(EstacaoRecarga::de).toList(),
					dados.clienteGrupoA());
		}

	}

	public record Apartamento(BigDecimal areaUtilM2, Integer quantidade) {

		private static Apartamento de(ApartamentoEntrada item) {
			return new Apartamento(item.areaUtilM2(), item.quantidade());
		}

	}

	public record Motor(BigDecimal potenciaCv, Integer quantidade) {

		private static Motor de(MotorEntrada item) {
			return new Motor(item.potenciaCv(), item.quantidade());
		}

	}

	public record CondicionadorAr(BigDecimal potencia, UnidadePotenciaAr unidadePotencia, Integer quantidade) {

		private static CondicionadorAr de(CondicionadorArEntrada item) {
			return new CondicionadorAr(item.potencia(), item.unidadePotencia(), item.quantidade());
		}

	}

	public record EstacaoRecarga(BigDecimal potenciaKw, Integer quantidade, TipoConexaoRecarga tipoConexao) {

		private static EstacaoRecarga de(EstacaoRecargaEntrada item) {
			return new EstacaoRecarga(item.potenciaKw(), item.quantidade(), item.tipoConexao());
		}

	}

	public record Norma(String norma, String revisao) {

		private static Norma de(NormaRevisao item) {
			return new Norma(item.norma(), item.revisao());
		}

	}

	public record Resultado(
			BigDecimal dr,
			BigDecimal fc,
			BigDecimal dtr,
			BigDecimal fr,
			BigDecimal drf,
			BigDecimal a,
			BigDecimal g,
			BigDecimal ds,
			BigDecimal acom,
			BigDecimal far,
			BigDecimal dc,
			BigDecimal dve,
			BigDecimal ded,
			Enquadramento enquadramento,
			BigDecimal dproj,
			PadraoEntrada padraoEntrada) {

		private static Resultado de(ResultadoCalculo resultado) {
			return new Resultado(
					porUnidade(resultado.dr()),
					fator(resultado.fc()),
					porUnidade(resultado.dtr()),
					fator(resultado.fr()),
					porUnidade(resultado.drf()),
					porUnidade(resultado.a()),
					porUnidade(resultado.g()),
					porUnidade(resultado.ds()),
					porUnidade(resultado.acom()),
					porUnidade(resultado.far()),
					porUnidade(resultado.dc()),
					porUnidade(resultado.dve()),
					porUnidade(resultado.ded()),
					resultado.enquadramento(),
					porUnidade(resultado.dproj()),
					PadraoEntrada.de(resultado.padraoEntrada()));
		}

	}

	public record PadraoEntrada(
			String faixa,
			String condutorPorFase,
			BigDecimal disjuntorA,
			BigDecimal carcacaA,
			String eletroduto) {

		private static PadraoEntrada de(Tabela2PadraoEntradaBt padrao) {
			if (padrao == null) {
				return null;
			}
			return new PadraoEntrada(
					padrao.faixa(),
					padrao.condutorPorFase(),
					porUnidade(padrao.disjuntorA()),
					porUnidade(padrao.carcacaA()),
					padrao.eletroduto());
		}

	}

	public record Passo(
			int ordem,
			String idRegra,
			String simbolo,
			String descricao,
			String formulaSimbolica,
			String formulaAplicada,
			String condicao,
			List<EntradaDoPasso> entradas,
			BigDecimal valor,
			UnidadeMedida unidade,
			String resultadoTexto,
			List<Referencia> referencias) {

		private static Passo de(PassoCalculo passo) {
			return new Passo(
					passo.ordem(),
					passo.idRegra(),
					passo.simbolo(),
					passo.descricao(),
					passo.formulaSimbolica(),
					passo.formulaAplicada(),
					passo.condicao(),
					passo.entradas().stream().map(EntradaDoPasso::de).toList(),
					porUnidade(passo.valorProduzido(), passo.unidade()),
					passo.unidade(),
					passo.resultadoTexto(),
					passo.referenciasNormativas().stream().map(Referencia::de).toList());
		}

	}

	public record EntradaDoPasso(String nome, BigDecimal valor, UnidadeMedida unidade) {

		private static EntradaDoPasso de(EntradaPasso entrada) {
			BigDecimal valor = UNIDADES_INFORMADAS.contains(entrada.unidade())
					? entrada.valor()
					: porUnidade(entrada.valor(), entrada.unidade());
			return new EntradaDoPasso(entrada.nome(), valor, entrada.unidade());
		}

	}

	public record Referencia(String norma, String revisao, String item) {

		private static Referencia de(ReferenciaNormativa referencia) {
			return new Referencia(referencia.norma(), referencia.revisao(), referencia.item());
		}

	}

}
