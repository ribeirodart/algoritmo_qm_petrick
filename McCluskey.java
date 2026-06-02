// Autores: José Antônio, Daví Antonio, Rhuan Pablo //

import java.util.*;
import java.util.stream.Collectors;
import java.math.BigInteger;

public class McCluskey{

    List<Implicante> ImplicantesPrimos = new ArrayList<>(); // gGuarda Implicantes Primos
    List<Implicante> naoPrimos = new ArrayList<>(); // Guarda IMplicantes não Primos
    Map<Integer, List<Implicante>> gprs = new TreeMap<>(); // Mapa de Peso em Bits 1 e Implicante
    boolean quebraCiclo = false; // Variável de controle para encerrar lopp de cruzamentos

    public void ConverteBinarioInicio(List<BigInteger> mintermos, int numVar){ // Converte os mintermos para binários e cria implicantes
        List<Implicante> temp =  new ArrayList<>();
        for(BigInteger e : mintermos){
        Implicante obj = new Implicante();
        String bin = e.toString(2);
        bin = "0".repeat(numVar - bin.length()) + bin; // Pode parecer desperdicio reconverter em binário, porém evitará realizar comparações entre Strings em algumas funcões
        obj.binario = bin;
        obj.mintermos_pais.add(e);
        temp.add(obj);
        }
        naoPrimos = temp;
    }


    public void CriarPesos(List<Implicante> Rodada){
        Map<Integer, List<Implicante>> grupos = new TreeMap<>(); // Cria mapas com chaves sendo pesos dados número de bits 1 do tipo (1, [1-0, 001, 010 ]) (2, [1-0])
        for(Implicante num : Rodada){
            int peso = (int) num.binario.chars().filter(c -> c == '1').count(); // Impotante para utilizarmos a distância de Hamming
            grupos.computeIfAbsent(peso, k -> new ArrayList<Implicante>()).add(num);
        }
        gprs = grupos;
    }
    
    public String diferenciador(String g1, String g2){ // Função análoga a aplicacão da De Morgan, utilizamso um traço para indicar a variável eliminada
        int tamanho = g1.length();
        int count = 0;
        int posTroca = 0;
        String cruzado;
        for(int i = 0; i < tamanho; i++){
            if (g1.charAt(i) != g2.charAt(i)){
                count++;
                posTroca = i;
            }
        }
        if (count == 1 && ((g1.charAt(posTroca) != '-') && (g2.charAt(posTroca) != '-'))){
            StringBuilder str = new StringBuilder(g1);
            str.setCharAt(posTroca, '-');
            cruzado = str.toString();
            return cruzado;
        }
        return null;
    }

    public void cruzador(){ // Cruza os implicantes, diferenciando quem é Primo e quem não é

        List<Implicante> temporaria = new ArrayList<>();
        List<Integer> pesos = new ArrayList<>(gprs.keySet());

        for(int i = 0; i < gprs.size() - 1; i++){

            List<Implicante> temp = gprs.get(pesos.get(i));
            List<Implicante> temp2 = gprs.get(pesos.get(i+1));
                for(Implicante e : temp){
                    for (Implicante d : temp2){
                        String cruzado = diferenciador(d.binario, e.binario);
                        if (cruzado != null){
                            Implicante n = new Implicante();
                            n.binario = cruzado;
                            n.mintermos_pais.addAll(e.mintermos_pais);
                            n.mintermos_pais.addAll(d.mintermos_pais);
                            temporaria.add(n);
                            e.usado = true;
                            d.usado = true;
                        }
                    }
                }     
        }

        naoPrimos = temporaria.stream().collect(Collectors.toMap(p -> p.binario, p -> p, (a,b) -> a)).values().stream().toList();
        for(int i = 0; i < gprs.size(); i++){
            List<Implicante> primos = gprs.get(pesos.get(i));
            for(Implicante v : primos){
                if(!v.usado){
                    ImplicantesPrimos.add(v);
                }
            }
        }
        if (temporaria.isEmpty())
            quebraCiclo = true;
    }

    public void desduplicador(){ // Função auxiliar que remove Implicantes Primos redundantes (iguais, porém cobrindo mintermos distintos)
        ImplicantesPrimos = ImplicantesPrimos.stream().collect(Collectors.toMap(p -> p.binario, p -> p, (a,b) -> a)).values().stream().toList();
    }
}
