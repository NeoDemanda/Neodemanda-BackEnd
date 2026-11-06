package com.neoenergia.neodemanda.motor.regra;

import java.util.List;

public final class RegrasDemanda {

	private RegrasDemanda() {
	}

	public static List<RegraCalculo> todas() {
		return List.of(
				new RegraDr(),
				new RegraFc(),
				new RegraDtr(),
				new RegraFr(),
				new RegraDrf(),
				new RegraParcelaA(),
				new RegraParcelaG(),
				new RegraDs(),
				new RegraAcom(),
				new RegraFar(),
				new RegraDc(),
				new RegraDve(),
				new RegraDed(),
				new RegraEnquadramento(),
				new RegraDproj(),
				new RegraPadraoEntrada());
	}

}
