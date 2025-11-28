# Explicação do Sistema de Autenticação com Javalin

Este documento detalha o funcionamento do código do projeto de autenticação simples. O objetivo é ajudar alunos de Orientação a Objetos a entenderem como as classes interagem e como o framework Javalin é utilizado para criar uma aplicação web.

## Visão Geral

O projeto é uma aplicação web que permite login de usuários e protege certas rotas (páginas) para que apenas usuários logados possam acessá-las.

A estrutura segue uma divisão de responsabilidades comum:
1.  **Model**: Representa os dados (Usuário).
2.  **Service**: Contém a lógica de negócio (Autenticação).
3.  **Controller**: Gerencia as requisições HTTP (Web).
4.  **App**: Configura e inicia a aplicação.

---

## 1. O Modelo (`User.java`)

Localização: `br.edu.ifpr.pgua.eic.tads.model.User.java`

Esta é uma classe POJO (Plain Old Java Object) simples que representa um usuário do sistema.

*   **Atributos**: Possui `id`, `nome` e `senha`. Note que eles são `private`, seguindo o princípio de **encapsulamento**.
*   **Construtores**:
    *   Um construtor completo (id, nome, senha).
    *   Um construtor auxiliar (nome, senha) para quando o ID não for relevante.
*   **Getters**: Métodos públicos para permitir o acesso leitura aos atributos privados.
*   **toString()**: Sobrescrita do método para facilitar a visualização do objeto (imprime o nome do usuário).

---

## 2. A Lógica de Negócio (Services)

### `AuthService.java`

Localização: `br.edu.ifpr.pgua.eic.tads.service.AuthService.java`

Esta classe é o coração da autenticação. Ela simula um banco de dados em memória e gerencia quem está logado.

*   **Lista de Usuários (`users`)**:
    *   É uma lista `static`, ou seja, pertence à classe e não a uma instância específica. Isso simula um "banco de dados" compartilhado por toda a aplicação.
    *   O bloco `static { ... }` inicializa essa lista com alguns usuários fictícios (Alice, Bob, etc.) assim que a classe é carregada.
*   **Usuário Logado (`logado`)**:
    *   Um atributo de instância que guarda o objeto `User` que está autenticado no momento *nesta instância do serviço*.
*   **Método `autenticar(String nome, String senha)`**:
    *   Recebe as credenciais.
    *   Usa **Java Streams** (`users.stream().filter(...)`) para procurar na lista um usuário que tenha o mesmo nome E a mesma senha.
    *   Se encontrar (`user.isPresent()`), define o atributo `logado` e retorna um resultado de sucesso.
    *   Se não encontrar, retorna um erro.
    *   *Nota: O tipo de retorno `Resultado<User>` é um utilitário que apenas empacota se a operação deu certo ou errado, junto com uma mensagem.*
*   **Métodos `logout()` e `isLogado()`**:
    *   `logout()`: Limpa o usuário logado (define como `null`).
    *   `isLogado()`: Verifica se existe alguém logado (retorna `true` se `logado` for diferente de `null`).

### `AuthHandler.java`

Localização: `br.edu.ifpr.pgua.eic.tads.service.AuthHandler.java`

Esta classe atua como uma ponte entre a lógica de autenticação e o contexto web do Javalin.

*   **Dependência**: Ela recebe um `AuthService` no construtor.
*   **Método `isLogged(Context ctx)`**:
    *   Este método verifica se existe um usuário na **Sessão** do navegador.
    *   `ctx.sessionAttribute("usuario")`: Tenta recuperar um objeto salvo na sessão com a chave "usuario".
    *   Retorna `true` se o objeto existir (não for nulo).

---

## 3. Os Controladores (Controllers)

Os controladores são responsáveis por receber as requisições da web (GET, POST), processá-las e devolver uma resposta (geralmente uma página HTML).

### `IndexController.java`

Localização: `br.edu.ifpr.pgua.eic.tads.controllers.IndexController.java`

*   **`get`**: Um `Handler` (manipulador) simples que apenas renderiza a página inicial (`index.html`) quando o usuário acessa a raiz do site.

### `LoginController.java`

Localização: `br.edu.ifpr.pgua.eic.tads.controllers.LoginController.java`

Este controlador gerencia o processo de entrada e saída do sistema.

*   **Construtor**: Recebe o `AuthService` para poder realizar a lógica de login. Isso é um exemplo de **Injeção de Dependência** via construtor.
*   **`get`**: Exibe o formulário de login (`login.html`).
*   **`login` (Processamento do POST)**:
    1.  Recebe os dados do formulário: `ctx.formParam("nome")` e `ctx.formParam("senha")`.
    2.  Chama o serviço: `auth.autenticar(nome, senha)`.
    3.  **Se sucesso**:
        *   Salva o usuário na sessão: `ctx.sessionAttribute("usuario", ... )`. Isso é crucial para manter o usuário logado entre diferentes requisições.
        *   Redireciona para a área restrita: `ctx.redirect("/protegido")`.
    4.  **Se falha**: Renderiza uma página de erro (`501.html`).
*   **`logout`**:
    *   Chama o serviço para limpar o estado interno.
    *   Invalida a sessão HTTP: `ctx.req().getSession().invalidate()`.
    *   Redireciona para a home.

### `ProtegidoController.java`

Localização: `br.edu.ifpr.pgua.eic.tads.controllers.ProtegidoController.java`

Controla a página que só deveria ser vista por quem está logado.

*   **`get`**:
    *   Recupera o usuário da sessão: `ctx.sessionAttribute("usuario")`.
    *   Cria um mapa de dados (`Map<String, Object>`) para passar informações para o HTML.
    *   Renderiza a página `protegido.html` passando esses dados. Assim, o HTML pode mostrar "Olá, [Nome do Usuário]".

---

## 4. A Aplicação Principal (`App.java`)

Localização: `br.edu.ifpr.pgua.eic.tads.App.java`

É aqui que tudo é conectado e a aplicação inicia.

1.  **Criação da Aplicação**: `JavalinUtils.makeApp(7070)` cria e configura o servidor Javalin na porta 7070.
2.  **Instanciação de Objetos**:
    *   Cria o `AuthService`.
    *   Cria os controladores, passando o `AuthService` para quem precisa dele (`LoginController`, `AuthHandler`).
3.  **Filtro de Segurança (`before`)**:
    *   `app.before("/protegido/*", ...)`: Este comando diz ao Javalin: "Antes de acessar qualquer rota que comece com `/protegido/`, execute este código".
    *   Dentro do filtro, ele usa o `authHandler.isLogged(ctx)` para ver se o usuário está na sessão.
    *   Se **NÃO** estiver logado: Define o status 403 (Proibido), mostra uma página de erro e cancela a requisição (`ctx.skipRemainingHandlers()`).
    *   Isso garante que ninguém acesse a página protegida digitando a URL direto sem estar logado.
4.  **Definição de Rotas**:
    *   Mapeia as URLs (`/`, `/login`, `/logout`, `/protegido`) para os `Handlers` específicos dentro dos controladores.
    *   Exemplo: `app.post("/login", loginController.login)` diz que quando houver uma requisição POST para `/login`, o método `login` do `LoginController` deve ser executado.
