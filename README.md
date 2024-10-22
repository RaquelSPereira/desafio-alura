# Projeto Alura

Bem-vinda ao teste para **Pessoa Desenvolvedora Java** da Alura!

## Requisitos

- Java 21.
- Spring Boot
- Docker
- Plataforma de teste - indicado Postman

## Instruções

1. Rode o comando:
````
docker-compose up -d
````
2. Rode o projeto na classe `ProjetoAluraApplication.java`.
## Endpoints

### 1. Login

Realize o login para obter o **Bearer Token**, necessário para acessar outros endpoints protegidos.
Faça o login utilizando esses dados para obter o token de INSTRUTOR:
- **Endpoint**: `POST /login`
- **URL**: `http://localhost:8080/login`

**Exemplo de requisição**:
```json
{
  "email": "raquel.silva@alura.com.br",
  "password": "senhaforte123*"
}
```

---

### 2. Cadastrar Novo Estudante

Permite cadastrar um novo estudante sem a necessidade de autenticação.


- **Endpoint**: `POST /user/newStudent`
- **URL**: `http://localhost:8080/user/newStudent`

**Exemplo de requisição**:
```json
{
    "name": "Lara Clara",
    "email": "lara@gmail.com",
    "password": "12345"
}
```

---

### 3. Cadastrar Novo Instrutor

Permite cadastrar um novo instrutor, apenas com autenticação de instrutor.

- **Endpoint**: `POST /user/newInstructor`
- **URL**: `http://localhost:8080/user/newInstructor`

**Exemplo de requisição**:
```json
{
    "name": "Gustavo Mattos",
    "email": "gustavo.mattos@alura.com.br",
    "password": "senhaforte9876!"
}
```
---

### 4. Solicitar Relatório

Permite que alunos e instrutores logados solicitem um relatório.

- **Endpoint**: `GET /registration/report`
- **URL**: `http://localhost:8080/registration/report`
- **Autorização**: Bearer Token

---

### 5. Matrícula em Curso

Apenas alunos podem se matricular em cursos, e precisam estar logados.

- **Endpoint**: `POST /registration/new`
- **URL**: `http://localhost:8080/registration/new`
- **Autorização**: Bearer Token

**Exemplo de requisição**:
```json
{
  "courseCode": "CDSB-A",
  "studentEmail": "lara@gmail.com"
}
```

---

### 6. Criar Novo Curso

Apenas instrutores podem criar novos cursos, e precisam estar logados.

- **Endpoint**: `POST /course/new`
- **URL**: `http://localhost:8080/course/new`
- **Autorização**: Bearer Token

**Exemplo de requisição**:
```json
{
  "name": "Curso de Desenvolvimento Security",
  "code": "SEC-NT",
  "description": "Curso completo sobre desenvolvimento com Security.",
  "instructorEmail": "mariana.costa@alura.com.br"
}
```

---

### 7. Inativar Curso

Instrutores logados podem inativar um curso.

- **Endpoint**: `PATCH /course/inactive/{courseCode}`
- **URL**: `http://localhost:8080/course/inactive/WCDM`
- **Autorização**: Bearer Token

---

## Como Usar no Postman

1. Abra o Postman.
2. Crie uma nova coleção ou importe o JSON da API.
3. Adicione o **Bearer Token** no campo de `Authorization` para endpoints que exigem autenticação.
4. Utilize o corpo das requisições com **raw JSON** e siga os exemplos fornecidos.

---

## Autenticação

Requisições autenticadas utilizam o Bearer Token obtido via login. Inclua esse token no cabeçalho `Authorization` das requisições.
