# Klang — A Polyglot Programming Language

Klang é uma linguagem experimental focada em **clareza**, **consistência semântica** e **interoperabilidade real entre linguagens**.  
Criada por ~K', a Klang busca oferecer uma sintaxe moderna e previsível, inspirada em Java e Python, mantendo uma base sólida para evolução do compilador.

---

## 🚀 Visão

A Klang nasce para:

- Ser uma **linguagem poliglota real**, interoperando diretamente com Java, Python, Go, Rust e C.  
- Oferecer sintaxe simples, minimalista e expressiva.  
- Permitir que cada módulo escolha sua **linguagem-alvo** ideal sem perder coesão.  
- Servir como ponte entre ecossistemas, não como substituta deles.

> Klang existe para interligar linguagens — não competir com elas.

---

## ⚙️ Filosofia

- **Legibilidade acima de tudo**  
- **Semântica determinística**  
- **Módulos transpiláveis isolados**  
- **Interop como fundamento do design**

---

## 💡 Exemplo de Sintaxe

```k
if (x > 0) {
    println("Positivo");
} afterall {
    println("Negativo ou zero");
}
````

---

## 🧩 Estrutura do Projeto

```
klang/
├── docs/        # Documentação e especificação
├── src/         # Lexer, parser, AST, transpilers
├── examples/    # Exemplos de uso
├── tests/       # Testes de unidade e integração
└── LICENSE      # Apache-2.0
```

---

## 📌 Roadmap

* [ ] Lexer estável
* [ ] Parser + AST
* [ ] Transpiler Java
* [ ] CLI (build/run/transpile)
* [ ] Documentação da sintaxe v1
* [ ] Interoperabilidade modular
* [ ] Runtime básico

Prioridade atual: **lexer → parser → AST**.

---

## 📄 Licença

Este projeto é licenciado sob a **Apache License 2.0**.
Você pode usar, modificar e distribuir livremente o software, inclusive para fins comerciais, desde que preserve os avisos de copyright e a licença.

Para detalhes completos, consulte o arquivo `LICENSE`.

---

## 🤝 Contribuindo

Contribuições são bem-vindas — especialmente em áreas como compiladores, AST, runtime e documentação.

1. Faça um fork
2. Crie uma branch (`feature/nome`)
3. Abra um Pull Request

Issues são bem-vindas para discussão de design e roadmap.

---

## 📬 Autor

Criado e mantido por **~K' (Lucas Paulino da Silva)**
Klang © 2025 — Apache-2.0
