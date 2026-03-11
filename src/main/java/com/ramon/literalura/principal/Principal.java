package com.ramon.literalura.principal;

import com.ramon.literalura.model.DadosResposta;
import com.ramon.literalura.model.Livro;
import com.ramon.literalura.repository.AutorRepository;
import com.ramon.literalura.repository.LivroRepository;
import com.ramon.literalura.service.ConsumoApi;
import com.ramon.literalura.service.ConverteDados;

import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";


    private LivroRepository livroRepository;
    private AutorRepository autorRepository;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    
                    ---------------------------------------
                    *** Bem-vindo(a) ao LiterAlura ***
                    
                    Escolha o número de sua opção:
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    0 - Sair
                    ---------------------------------------
                    """;

            System.out.println(menu);
            try {
                opcao = Integer.parseInt(leitura.nextLine());

                switch (opcao) {
                    case 1:
                        buscarLivroPorTitulo();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosNoAno();
                        break;
                    case 5:
                       listarLivrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Encerrando a aplicação...");
                        break;
                    default:
                        System.out.println("Opção inválida! Digite um número do menu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, digite um NÚMERO.");
            }
        }
    }

    private void buscarLivroPorTitulo() {
        System.out.println("Digite o nome do livro que você deseja buscar:");
        var nomeLivro = leitura.nextLine();

        var json = consumoApi.obterDados(ENDERECO + nomeLivro.replace(" ", "+"));
        var dadosPesquisa = conversor.obterDados(json, DadosResposta.class);

        var livroBuscado = dadosPesquisa.resultados().stream().findFirst();

        if (livroBuscado.isPresent()) {
            Livro livro = new Livro(livroBuscado.get());
            try {
                livroRepository.save(livro);
                System.out.println("Livro salvo com sucesso no banco de dados!");
                System.out.println(livro);
            } catch (Exception e) {
                System.out.println("Erro: Não foi possível salvar. (Talvez o livro já esteja cadastrado).");
            }
        } else {
            System.out.println("Desculpe, livro não encontrado na API.");
        }
    }

    private void listarLivrosRegistrados() {
        var livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado no banco de dados ainda.");
        } else {
            System.out.println("\n--- LIVROS REGISTRADOS ---");
            livros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        var autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado no banco de dados ainda.");
        } else {
            System.out.println("\n--- AUTORES REGISTRADOS ---");
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosNoAno() {
        System.out.println("Digite o ano que deseja pesquisar (ex: 1850):");
        try {
            var ano = Integer.parseInt(leitura.nextLine());
            var autoresVivos = autorRepository.autoresVivosNoAno(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado no ano " + ano + " em nosso banco de dados.");
            } else {
                System.out.println("\n--- AUTORES VIVOS NO ANO " + ano + " ---");
                autoresVivos.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Por favor, digite um ano válido em formato numérico.");
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("""
                Insira o idioma para realizar a busca:
                es - espanhol
                en - inglês
                fr - francês
                pt - português
                """);
        var idioma = leitura.nextLine();

        var livrosNoIdioma = livroRepository.findByIdioma(idioma);

        if (livrosNoIdioma.isEmpty()) {
            System.out.println("Não existem livros registrados nesse idioma no momento.");
        } else {
            System.out.println("\n--- LIVROS ENCONTRADOS NO IDIOMA '" + idioma.toUpperCase() + "' ---");
            livrosNoIdioma.forEach(System.out::println);

            // Exibindo a estatística solicitada no desafio
            System.out.println("\n*** ESTATÍSTICA ***");
            System.out.println("O total de livros registrados nesse idioma é: " + livrosNoIdioma.size());
            System.out.println("*******************\n");
        }
    }

}