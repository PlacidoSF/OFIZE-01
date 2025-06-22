package com.ufersa.OFIZE.utils;


import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public void datatest() {
        // Configurações de conexão (padrão do XAMPP)
        String url = "jdbc:mysql://localhost:3306/";
        String usuario = "root";    // usuário padrão do XAMPP
        String senha = "";          // sem senha no XAMPP padrão

        System.out.println("Testando conexão com o MySQL...");

        try {
            // Tenta conectar ao MySQL
            Connection conexao = DriverManager.getConnection(url, usuario, senha);

            System.out.println("✅ Conexão bem-sucedida!");
            System.out.println("Versão do MySQL: " + conexao.getMetaData().getDatabaseProductVersion());

            conexao.close(); // Fecha a conexão
        } catch (Exception e) {
            System.err.println("❌ Erro na conexão:");
            e.printStackTrace();
        }
    }
}