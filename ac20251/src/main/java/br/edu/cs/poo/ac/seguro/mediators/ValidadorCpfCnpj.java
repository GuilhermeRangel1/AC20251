package br.edu.cs.poo.ac.seguro.mediators;

public class ValidadorCpfCnpj {
	
    private static int calcularDigitoCnpj(String base) {
        int[] pesos;
        if (base.length() == 12) {
            pesos = new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        } else {
            pesos = new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        }

        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
        	soma += (base.charAt(i) - '0') * pesos[i];
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    private static int calcularDigito(String base, int peso) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += (base.charAt(i) - '0') * (peso - i);
        }
        int resto = soma % 11;
        if(resto<2) {
        	return 0;
        }else {
        	return 11 - resto;
        }
    }

    public static boolean ehCpfValido(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;  
        }
        if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            return false;
        }
        String cpfBase = cpf.substring(0, 9); 
        int digito1 = calcularDigito(cpfBase, 10);
        int digito2 = calcularDigito(cpfBase + digito1, 11); 
        return cpf.equals(cpfBase + digito1 + digito2); 
    }

    public static boolean ehCnpjValido(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }

        if (cnpj.length() != 14 || !cnpj.matches("\\d{14}")) {
            return false;
        }

        if (cnpj.chars().distinct().count() == 1) {
            return false;
        }

        String cnpjBase = cnpj.substring(0, 12);
        int digito1 = calcularDigitoCnpj(cnpjBase);
        int digito2 = calcularDigitoCnpj(cnpjBase + digito1);
        return cnpj.equals(cnpjBase + digito1 + digito2);
    }


}

