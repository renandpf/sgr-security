# sgr-security

Este projeto faz parte da entrega do curso de arquitetura da FIAP

**SISTEMA DE GESTÃO DE REFEIÇÃO**

Trata-se de um sistema de gerenciamento de pedidos. O sistema gere produtos, cliente e pedidos.

Este módulo é a parte responsavel em gerar e validar token de autenticação

**TECNOLOGIAS**

Neste sistema foram utlizadas as tecnologias:

* Java 18
* Maven
* AWS API GATEWAY
* AWS LAMBDA

**ARQUITETURA**

O projeto foi desenvolvido usando Arquitetura Limpa.

# DETALHAMENTO DA API

O sistema utiliza-se do API Gateway para expor a integração.

Login

```sh
curl --location 'https://base-url/sgr/login' \
--header 'Content-Type: application/json' \
--data '{
    "username":"555",
    "password":"senha"
}'
```

Validar Token

* Não há endpoint exposto no API Gateway. O método de validação é chamado internamente pelo Api Gateway da aplicação principal.