// Autores: José Antônio, Daví Antonio, Rhuan Pablo //

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.math.BigInteger;

public class Leitor_PLA { // Leitor de aquivos no formato PLA 

    int numVar = 0;
    public List<BigInteger> Leitor(String path) throws IOException
    {
        BufferedReader ler = new BufferedReader(new FileReader(path));
        String linha = "";

        linha = ler.readLine();
        String[] str = linha.split(" ");
        numVar = Integer.parseInt(str[1]);
        while ((linha = ler.readLine()) != null && linha.startsWith(".")) {}

        List<BigInteger> mintermos = new ArrayList<>();
        do{
            
            String[] temp = linha.split(" ");
            if(Integer.parseInt(temp[1]) == 1){
                BigInteger minterm = new BigInteger(temp[0], 2);
                mintermos.add(minterm);
            }

            linha = ler.readLine();
        }
        while ((linha != null && !linha.equals(".e")));
        ler.close();

        return mintermos;
    }

    public List<BigInteger> arquivo(String path){
        try{
           return Leitor(path);
        }
        catch (IOException e){
            System.out.println("Falha ao abrir arquivo!");
        };   
        return null;
    }
}
