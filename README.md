# Cartão de Crédito

### Equipe

[Gustavo Domingos de Oliveira](mailto:gdo@ic.ufal.br) e 
[Samuel Lucas Vieira Lins Barbosa](mailto:slvlb@ic.ufal.br)

### Como executar o projeto

**Requisitos de sistema**

- [IntelliJ IDEA](https://www.jetbrains.com/pt-br/idea/download/?section=windows)
*(Com o email do ic temos acesso à versão ultimate)*
- [PostgreSQL](https://www.postgresql.org/)

### Passo a passo para executar o sistema

1. Abra o projeto no IntelliJ e aguarde todas as dependências serem baixadas (ele faz isso automaticamente)
2. No Postgres, crie a base de dados que será usada pela aplicação
3. Crie uma cópia do arquivo `.env.example`
4. Renomeie essa cópia para `.env` e preencha com os dados do banco de dados
5. Execute o arquivo sql `init.sql`
6. Execute o projeto. A api estará rodando no endereço [http://localhost:8080](http://localhost:8080)
