package com.example.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	
	public Map<String, Integer> transform(Map<Integer, List<String>> old) {        
        Map<String, Integer> newMap = new HashMap<>();
        // For each letter in values, create a key with the key as value
        for (Integer x : old.keySet()) {
            for ( String y : old.get(x)) {
                newMap.put(y.toLowerCase(), x);
            }
        }
        System.out.println(newMap);
        return newMap;
    }

	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		Map<Integer, List<String>> old = new HashMap<Integer, List<String>>();
        // Cria lista 1 com as letras "A", "E", "I", "O", "U", "L", "N", "R", "S", "T",
        List<String> lista1 = Arrays.asList("A", "E", "I", "O", "U", "L", "N", "R", "S", "T");
        // Cria lista 2 com as letras "D", "G"
        List<String> lista2 = Arrays.asList("D", "G");
        // Cria lista 3 com as letras "B", "C", "M", "P"
        List<String> lista3 = Arrays.asList("B", "C", "M", "P");
        // Cria lista 4 com as letras "F", "H", "V", "W", "Y"
        List<String> lista4 = Arrays.asList("F", "H", "V", "W", "Y");
        // Cria lista 5 com as letras "K"
        List<String> lista5 = Arrays.asList("K");
        // Cria lista 6 com as letras "J", "X"
        List<String> lista6 = Arrays.asList("J", "X");
        // Cria lista 7 com as letras "Q", "Z"
        List<String> lista7 = Arrays.asList("Q", "Z");
        // Adiciona as listas ao mapa
        old.put(1, lista1);
        old.put(2, lista2);
        old.put(3, lista3);
        old.put(4, lista4);
        old.put(5, lista5);
        old.put(6, lista6);
        old.put(7, lista7);

        Map<String, Integer> newMap = new DemoApplication().transform(old);
	}

}
