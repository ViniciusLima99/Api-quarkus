package org.acme;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.math.BigInteger;
import java.util.Locale;

@Path("/")
public class RetornarCalculo {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
// o framework converte automaticamente o objeto Calculo em um JSON com base nos seus atributos.
    public Calculo calcularProbabilidade(CalculoRequest request) {
        int deckSize = request.getDecksize();
        int handSize = request.getHandsize();
        int amt = request.getAmt();
        int min = request.getMin();
        int max = request.getMax();
        
        //Cálculo da probabilidade hipergeométrica
        double probabilidade = calcularProbabilidadeHipergeométrica(deckSize, handSize, amt, min, max);
        
        return new Calculo(probabilidade);
    }

    private double calcularProbabilidadeHipergeométrica(int deckSize, int handSize, int amt, int min, int max) {
        BigInteger combinacaoDeck = combinacao(deckSize, handSize);
        double probabilidadeTotal = 0.0;
    
        for (int x = min; x <= max; x++) {
            if (x > handSize || x > amt) continue;
            BigInteger combinacaoM = combinacao(amt, x);
            BigInteger combinacaoN = combinacao(deckSize - amt, handSize - x);
            BigInteger combinacaoResultado = combinacaoM.multiply(combinacaoN);
    
            probabilidadeTotal += combinacaoResultado.doubleValue() / combinacaoDeck.doubleValue();
        }
    
    return Double.parseDouble(String.format(Locale.US, "%.4f", probabilidadeTotal)) *100;
    }

    private BigInteger combinacao(int n, int k) {
        if (k > n) return BigInteger.ZERO;
        if (k == 0 || k == n) return BigInteger.ONE;
        return fatorial(n).divide(fatorial(k).multiply(fatorial(n - k)));
    }

    private BigInteger fatorial(int n) {
        BigInteger fatorial = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            fatorial = fatorial.multiply(BigInteger.valueOf(i));
        }
        return fatorial;
    }

// Os getters e setters permitem que o framework "preencha" a instância sem precisar de um
//  construtor ou argumentos explícitos.

// Isso é feito automaticamente porque você usa o framework Jakarta REST (anteriormente JAX-RS). 
// Essa funcionalidade é chamada de binding (ligação), e o suporte ao JSON geralmente depende
//  de bibliotecas como:

// Jackson (a mais comum).
// JSON-B (Java API for JSON Binding, parte do Jakarta EE).
    public static class CalculoRequest {
        private int decksize;
        private int handsize;
        private int amt;
        private int min;
        private int max;

        public int getDecksize() {
            return decksize;
        }

        public void setDecksize(int decksize) {
            this.decksize = decksize;
        }

        public int getHandsize() {
            return handsize;
        }

        public void setHandsize(int handsize) {
            this.handsize = handsize;
        }

        public int getAmt() {
            return amt;
        }

        public void setAmt(int amt) {
            this.amt = amt;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }
    }

    public static class Calculo {
        private double probabilidade;

        public Calculo(double probabilidade) {
            this.probabilidade = probabilidade;
        }

        public double getProbabilidade() {
            return probabilidade;
        }

        public void setProbabilidade(double probabilidade) {
            this.probabilidade = probabilidade;
        }
    }
}