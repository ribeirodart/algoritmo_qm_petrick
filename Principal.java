// Autores: José Antônio, Daví Antonio, Rhuan Pablo //

import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class Principal {
    public static void main(String[] args) throws Exception {
        System.out.print("Escreva o caminho do arquivo pra eu esbagaçar: ");
        Scanner scan = new Scanner(System.in);

        // Coloca o caminho do arquivo
        String caminho = scan.nextLine();
        System.out.println();
        scan.close();
        File arq = new File(caminho);

        Leitor_PLA obj3 = new Leitor_PLA();
        List<BigInteger> minterm = obj3.Leitor(arq.getPath());

        McCluskey obj = new McCluskey();
        obj.ConverteBinarioInicio(minterm, obj3.numVar);
        while (!obj.quebraCiclo) {
            obj.CriarPesos(obj.naoPrimos);
            obj.cruzador();
        }
        obj.desduplicador();

        Patrick obj2 = new Patrick();
        obj2.Tabela(obj.ImplicantesPrimos, minterm);
        obj2.Separador_Essenciais_e_naoEssenciais(obj2.table);
        obj2.multiplicador(obj2.mintermos_cobertores, obj2.table);

        int indiceEx = caminho.indexOf("ex");

        
        if (indiceEx != -1) {
            caminho = caminho.substring(indiceEx);
        }
        System.out.printf("Expressão lógica minimizada como SoP de \"%s\"\n", caminho);
        // Imprime as expressões binárias dos implicantes essenciais, uma por linha
        int tamanho = obj2.implicantesEssenciais.size();
        int i = 0;
        for (Integer idx : obj2.implicantesEssenciais) {
            if(i != tamanho -1){
                System.out.print(obj.ImplicantesPrimos.get(idx).binario + " + ");
                i++;
            }
            else
                System.out.println(obj.ImplicantesPrimos.get(idx).binario);
        }

        // Resumo final
        System.out.println("---");
        System.out.println("Variaveis  : " + obj3.numVar);
        System.out.println("Mintermos  : " + minterm.size());
        System.out.println("Implicantes de Cobertura Total da Tabela : " + obj2.implicantesEssenciais.size());
    }
}