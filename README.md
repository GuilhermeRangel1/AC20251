# AC20251 - Atividade Contínua de Programação Orientada a Objetos (POO) 2025.1

Este repositório contém o desenvolvimento da Atividade Contínua (AC) da disciplina de Programação Orientada a Objetos (POO) do semestre 2025.1. O projeto aborda um sistema de seguros simplificado, com foco em apólices de veículos.

## Visão Geral do Projeto

O sistema de seguros permite o gerenciamento de apólices de seguro para veículos, envolvendo entidades como:

* **Segurados:** Pessoas físicas (com CPF e bônus) e empresas (com CNPJ, indicação se é locadora e bônus).
* **Veículos:** Com informações como placa, ano e o proprietário (pessoa ou empresa), além da categoria.
* **Apólices:** Contendo informações como número da apólice, veículo segurado, valores de franquia e prêmio, valor máximo segurado e data de criação.
* **Sinistros:** Registros de eventos de sinistro associados a um veículo e uma apólice.

O sistema implementa funcionalidades como:

* Inclusão de novas apólices, com validações de dados e cálculos de valores (prêmio, franquia).
* Associação de apólices a segurados e veículos.
* Mecanismo de bônus para segurados sem sinistros no ano anterior.
* Consulta e exclusão de apólices.
* Registro de sinistros e verificação de existência de sinistros no mesmo ano da apólice para exclusão.

## Estrutura do Projeto

A estrutura do projeto segue um padrão de organização comum para aplicações Java, com as seguintes principais pastas:

* `src/main/java`: Contém o código fonte principal da aplicação, organizado em pacotes por responsabilidade (entidades, DAOs, mediators, etc.).
* `src/test/java`: Contém os testes unitários para as diferentes partes do sistema, garantindo a qualidade e o correto funcionamento das funcionalidades implementadas.

## Tecnologias Utilizadas

* **Java:** Linguagem de programação principal.
* **JUnit:** Framework para a criação e execução de testes unitários.
* **Java Time API:** Para manipulação de datas e horas.
* **BigDecimal:** Para operações financeiras com precisão.
* **Persistência de Objetos (Arquivo):** A persistência dos dados está sendo realizada através da serialização de objetos em arquivos.

## Como Executar os Testes

Para executar os testes unitários e verificar o funcionamento do sistema, siga os seguintes passos (assumindo que você tem o ambiente de desenvolvimento Java configurado):

1.  Clone este repositório para a sua máquina local.
2.  Navegue até a raiz do projeto no seu terminal.
3.  Utilize a sua IDE (como Eclipse ou IntelliJ IDEA) para abrir o projeto e executar os testes presentes no pacote `src/test/java`.
4.  Alternativamente, você pode utilizar um runner de testes JUnit a partir da linha de comando, caso configurado no seu projeto.

## Contribuições

Este projeto é parte de uma atividade acadêmica. 

