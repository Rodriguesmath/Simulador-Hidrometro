package main.java.br.com.simulador.config;

public enum Bitola {
	MEIA("1/2", 0.015f, 0.000694f),
	TRES_QUARTOS("3/4", 0.020f, 0.000972f),
	UM("1", 0.025f, 0.001389f),
	UM_E_MEIO("1 1/2", 0.040f, 0.003055f),
	DOIS("2", 0.050f, 0.005555f),
	TRES("3", 0.075f, 0.0125f),
	QUATRO("4", 0.100f, 0.0222f);

	private final String polegada;
	private final float diametro; // em metros
	private final float qmax;     // em m³/s

	Bitola(String polegada, float diametro, float qmax) {
		this.polegada = polegada;
		this.diametro = diametro;
		this.qmax = qmax;
	}

	public String getPolegada() {
		return polegada;
	}

	public float getDiametro() {
		return diametro;
	}

	public float getQmax() {
		return qmax;
	}

	public static Bitola fromString(String polegada) {
		for (Bitola b : Bitola.values()) {
			if (b.polegada.equals(polegada)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Bitola não suportada: " + polegada);
	}
}