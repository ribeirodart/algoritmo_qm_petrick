// Autores: José Antônio, Daví Antonio, Rhuan Pablo //

import java.util.*;
import java.math.BigInteger;

public class Patrick {

    Set<Integer> implicantesEssenciais = new HashSet<>(); // Guarda os a posições dos Primos Implicantes para que trabalhemos em Patrick com números, evitando comparação de Strings excessivamente

    Map<BigInteger, List<Integer>> table =  new TreeMap<>(); // Mapa do tipo, (mintermo, lista de implicantes que cobrem ele)

    Set<BigInteger> mintermos_cobertores = new HashSet<>();// Lista dos mintermos passados ao algoritmo

    public void Tabela(List<Implicante> primos, List<BigInteger> mintermos){ // Cria o mapa de (mintermos, lista de quem cobre ele)
        Map<BigInteger, List<Integer>> tabela =  new TreeMap<>(); // argumento do Separador (retorna mintermo e a posicao na tabela primo de quem cobre ele)
        for(BigInteger e : mintermos){
            for(int i = 0; i < primos.size(); i++){
                if (primos.get(i).mintermos_pais.contains(e))
                tabela.computeIfAbsent(e, k -> new ArrayList<Integer>()).add(i);
            }
        }
        table = tabela;
    }

    public void Separador_Essenciais_e_naoEssenciais(Map<BigInteger, List<Integer>> tabela){ // Separa os Implicantes Primos dos mintermos que possuem aopenas um cobertor (primo essêncial)
        Set<BigInteger> mintermos_com_mais_de_um_cobertor = new HashSet<>();
        for(BigInteger a : tabela.keySet()){
            if(tabela.get(a).size() == 1){
                implicantesEssenciais.add(tabela.get(a).get(0));
            }
            else{
                mintermos_com_mais_de_um_cobertor.add(a);
            }
    } 

        for (BigInteger a : new HashSet<>(mintermos_com_mais_de_um_cobertor)) {
            for (Integer idx : implicantesEssenciais) {
                if (tabela.get(a).contains(idx)) {
                    mintermos_com_mais_de_um_cobertor.remove(a);
                    break; 
                }
            }
        }
        mintermos_cobertores = mintermos_com_mais_de_um_cobertor;
    }

    public void multiplicador(Set<BigInteger> mintermos_cobertores_multiplos, Map<BigInteger, List<Integer>> tabela){ // AQUI É O GARGALO!!!
        if (mintermos_cobertores_multiplos.isEmpty()) return;
        List<Set<Integer>> todas_tuplas = new ArrayList<>();
        List<Set<Integer>> campo = new ArrayList<>();
        for(BigInteger e : mintermos_cobertores_multiplos){
            todas_tuplas.add(new HashSet<>(tabela.get(e))); // Realiza o cruzamento entre os não essênciais de forma a obter todas as combinações possíveis
        }
        for (Integer x : todas_tuplas.get(0)) {
            Set<Integer> s = new HashSet<>();
            s.add(x);
            campo.add(s);
        }
        // Ao final escolhemos o menor conjunto que cobre os mintermos com mais de um cobertor

        for(int i = 1; i < todas_tuplas.size(); i++){
            campo = cruza(campo, todas_tuplas.get(i));
            campo.sort(Comparator.comparingInt(Set::size)); 
            campo = absorve(campo); // Algoritmos mais modernos implementam poda inteligente, porém complexa e que nãio reduz efetivamente o tempo de execução para grandes entradas
        }

        if (campo.size() != 1) 
        implicantesEssenciais.addAll(campo.get(0));
        else{
            Integer minterm = campo.get(0).iterator().next();
            implicantesEssenciais.add(minterm);
        }
}

    public List<Set<Integer>> absorve(List<Set<Integer>> lista_de_retorno){ // Elimina elementos repetidos nos cruzamento, essêncial para obter menor conjunto
            int tamanho = lista_de_retorno.size();
            List<Set<Integer>> copia_local = new ArrayList<>();
            boolean[] posicao_removida_false = new boolean[tamanho];
            for(int i = 0; i < tamanho - 1; i++){
                for (int k = i + 1; k < tamanho; k++){
                    if(lista_de_retorno.get(k).containsAll(lista_de_retorno.get(i))){
                        posicao_removida_false[k] = true;
                    }
                }
            }
            for(int j = 0; j < tamanho; j++){
                if(!posicao_removida_false[j]){
                    copia_local.add(lista_de_retorno.get(j));
                }
            }
            return copia_local;         
    }

    public List<Set<Integer>> cruza(List<Set<Integer>> campo, Set<Integer> dupla){ // Com a lista de primos essênciais pronta, cruzamos pelo índice com Array de Implicantes Primos 
        List<Set<Integer>> lista_de_retorno = new ArrayList<>();
        for(Set<Integer> e : campo){
                for(Integer z : dupla){
                   Set<Integer> camp = new HashSet<>(e); // Assim extraímos expressão por SoP
                   camp.add(z);
                   lista_de_retorno.add(camp);
            }
        }
        return lista_de_retorno;
    }
}
