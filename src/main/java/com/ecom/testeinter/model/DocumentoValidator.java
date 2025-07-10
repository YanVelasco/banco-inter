package com.ecom.testeinter.model;

import java.util.InputMismatchException;

public class DocumentoValidator {

    public static boolean isCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("^(\\d)\\1{10}$")) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int digito1 = 11 - (soma % 11);
            if (digito1 >= 10) digito1 = 0;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int digito2 = 11 - (soma % 11);
            if (digito2 >= 10) digito2 = 0;

            return cpf.charAt(9) - '0' == digito1 && cpf.charAt(10) - '0' == digito2;
        } catch (InputMismatchException | NumberFormatException e) {
            return false;
        }
    }

    public static boolean isCNPJ(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || cnpj.matches("^(\\d)\\1{13}$")) {
            return false;
        }

        try {
            int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += (cnpj.charAt(i) - '0') * pesos1[i];
            }
            int digito1 = 11 - (soma % 11);
            if (digito1 >= 10) digito1 = 0;

            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += (cnpj.charAt(i) - '0') * pesos2[i];
            }
            int digito2 = 11 - (soma % 11);
            if (digito2 >= 10) digito2 = 0;

            return cnpj.charAt(12) - '0' == digito1 && cnpj.charAt(13) - '0' == digito2;
        } catch (InputMismatchException | NumberFormatException e) {
            return false;
        }
    }
}
