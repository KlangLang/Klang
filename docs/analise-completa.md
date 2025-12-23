# Análise Completa do Projeto Klang

## 1. ANÁLISE COMPLETA

### Arquitetura Geral do Projeto
Klang é uma linguagem de programação experimental, focada em rigor, clareza e separação explícita de responsabilidades e futuro poliglotismo nativo, transpilando para diferentes linguagens e as comunicando com um tear. O pipeline de compilação segue as etapas clássicas:

1. **Análise Léxica (Lexer)**
2. **Parsing (Parser)**
3. **Validação Semântica (TypeChecker, TypeContext)**
4. **Validação de Intenção**
5. **Resolução de Backend**
6. **Geração de Código**

Cada etapa é modularizada e explicitamente separada, evitando comportamentos implícitos.

### Estrutura de Pastas e Organização
- `cli/` — Interface de linha de comando (comandos, entrada de arquivos, integração com Picocli)
- `core/` — Núcleo da linguagem (lexer, parser, AST, semântica, erros)
- `stdlib/` — Biblioteca padrão
- `docs/` — Documentação detalhada (filosofia, sintaxe, funcionamento do lexer, etc.)
- `examples/` — Exemplos de código Klang

### Componentes Principais
- **Lexer**: Tokeniza o código fonte, reconhecendo tipos, operadores, literais e keywords. Implementado em `core/lexer/Lexer.java`.
- **Parser**: Constrói a AST a partir dos tokens, validando sintaxe e regras de nomeação. Implementado em `core/parser/TokenStream.java` e subpastas `ast/`.
- **AST (Abstract Syntax Tree)**: Estruturas de nós para expressões, declarações, blocos, funções, etc. (ex: `FunctionDeclarationNode`, `VariableDeclarationNode`).
- **TypeChecker**: Valida tipos e escopos, detectando erros semânticos (`core/semantics/TypeChecker.java`).
- **Sistema de Erros**: Diagnósticos detalhados, coletores, renderizadores e exceções (`core/errors/`).
- **CLI**: Comandos para lexing, parsing, ajuda, versão, etc. (`cli/src/main/java/org/klang/cli/`).

### Análise de Módulos/Arquivos Importantes
- `Lexer.java`, `Token.java`, `TokenType.java`: Tokenização e especificação de tokens.
- `TokenStream.java`: Parsing e construção da AST.
- `ast/`: Nós da AST para todos os tipos de declaração e expressão.
- `TypeChecker.java`, `TypeContext.java`, `Type.java`: Tipagem e escopo.
- `ErrorReporter.java`, `Diagnostic.java`, `DiagnosticType.java`, etc.: Sistema de erros robusto.
- `KMain.java`, `LexCommand.java`, `ParseCommand.java`: CLI e integração com Picocli.

### Padrões de Design Utilizados
- **Separation of Concerns**: Cada módulo tem responsabilidade única.
- **Builder Pattern**: Para construção de diagnósticos de erro.
- **Enum**: Para tipos, operadores, tokens, erros.
- **Imutabilidade**: AST e tokens são imutáveis após criados.
- **Exception Handling**: Erros são tratados via exceções especializadas.

### Dependências e Bibliotecas
- **Picocli**: Framework para CLI.
- **JUnit**: Testes unitários.
- **Gradle**: Build system.

## 2. ANÁLISE TÉCNICA

### Qualidade do Código
- **Organização**: Código bem modularizado, com separação clara entre análise léxica, parsing, semântica e erros.
- **Nomenclatura**: Segue padrões explícitos, evitando ambiguidade.
- **Tratamento de Erros**: Diagnósticos detalhados, com mensagens, notas e spans.
- **AST**: Estrutura clara e extensível.

### Pontos Fortes
- **Rigor e Clareza**: Regras explícitas para nomes, tipos e intenções.
- **Sistema de Erros**: Mensagens ricas, com contexto e sugestões.
- **Separação de Responsabilidades**: Facilita manutenção e evolução.
- **Extensibilidade**: AST e pipeline permitem fácil adição de features.

### Pontos de Melhoria e Possíveis Problemas
- **Cobertura de Testes**: Poderia ser expandida para casos edge e integração.
- **Performance**: Lexer e parser podem ser otimizados para grandes arquivos.
- **Validação de Intenção**: Regras podem ser refinadas para casos complexos.
- **Documentação de APIs internas**: Alguns métodos e classes poderiam ter mais comentários.

### Bugs Potenciais / Edge Cases
- **Ambiguidade de Identificadores**: Em modos menos rigorosos, pode passar nomes ambíguos.
- **Erros de Escopo**: Variáveis em blocos aninhados podem gerar conflitos se não tratados.
- **Parsing de Literais Complexos**: Strings, caracteres e números com formatos incomuns.
- **Recursão Profunda**: Funções recursivas podem causar stack overflow se não limitadas.

### Performance e Otimizações Possíveis
- **Tokenização**: Uso de estruturas mais eficientes para lookup de tokens.
- **Parsing**: Algoritmos de parsing mais rápidos (ex: parser LL ou LR).
- **Erros**: Lazy evaluation de diagnósticos para grandes volumes de erros.

## 3. DOCUMENTAÇÃO

### Estado Atual da Documentação
- **Completa e detalhada**: Documentos sobre filosofia, sintaxe, funcionamento do lexer, pipeline técnico.
- **Exemplos**: Diversos exemplos em `examples/`.
- **Especificação de tokens e regras de nomes**: Documentos dedicados.

### Exemplos de Uso da Linguagem
```klang
integer x = 12;
integer y = x;
x = 1 + 2 * 4 / 7;
println(1);
foo(x, y * 3);

@Use("c")
public static Array<Integer> build(integer n) {
    if (n <= 0) {
        println("Using fallback size 10");
        n = 10;
    }
    Array<Integer> nums = fresh Array(n);
    for (integer i = 0; i < n; i++) {
        nums.append(i);
    }
    return nums;
}
```

### Sintaxe e Features Implementadas
- **Declaração de variáveis**: `integer x = 12;`
- **Atribuição e expressões**: `x = 1 + 2 * 4 / 7;`
- **Funções públicas e estáticas**: `public static void testeNumbers(){ ... }`
- **Arrays e tipos genéricos**: `Array<Integer> nums = new Array(n);`
- **Controle de fluxo**: `if`, `for`, etc.
- **Anotações de uso de backend**: `@Use("c")`
- **Operadores**: `+ - * / % ! != ++ -- -> ** == && ||`
- **Regras de nomes e rigor**: Verbos e nomes explícitos, proibição de nomes ambíguos.

### Roadmap Sugerido
- **Expansão da biblioteca padrão**
- **Implementação de backend para múltiplas linguagens**
- **Melhoria na cobertura de testes**
- **Documentação de APIs internas**
- **Ferramentas de análise estática**
- **Suporte a módulos e pacotes**
- **Otimizações de performance no lexer/parser**
- **Validação de tipos mais avançada (ex: generics, subtipos)**
- **Integração com IDEs e LSP**

---

*Análise gerada automaticamente pelo copilot em 22 de dezembro de 2025.*

___

# Conteudos em ./docs/*

# Klang Design Philosophy

Klang is built on a simple belief:

Ambiguity is more dangerous than verbosity.

---

## What Klang Refuses to Do

- Guess developer intent
- Correct code silently
- Infer semantics without declaration
- Optimize for brevity at the cost of clarity

---

## Defaults Are Moral Choices

Klang defaults to strict mode because defaults shape behavior.

A permissive default leads to fragile systems.
A strict default encourages discipline and clarity.

---

## On Lenient Mode

Lenient mode is not a recommendation.
It is an escape hatch.

Using lenient mode is an explicit acknowledgment
that some guarantees are being sacrificed.

---

## Long-Term Vision

Klang aims to:
- age well
- scale cognitively
- support large, long-lived systems

It prioritizes understanding over convenience.

# Klang Token Specification (v1.0)

## Single-character
- LPAREN      (
- RPAREN      )
- LBRACE      {
- RBRACE      }
- LBRACKET    [
- RBRACKET    ]
- COMMA       ,
- SEMICOLON   ;
- COLON       :
- DOT         .

- PLUS        +
- MINUS       -
- MULTIPLY    *
- DIVISION    /
- REMAINDER   %

- ASSIGNMENT  =
- LT          <
- GT          >
- BANG        !

## Multi-character
- INCREMENT       ++
- DECREMENT       --
- POWER           **

- LTE             <=
- GTE             >=
- DOUBLEEQUAL     ==
- NOTEQUAL        !=

- AND             &&
- OR              ||
 
- ARROW           ->

## Literals
- NUMBER                  [0-9]+
- IDENTIFIER              [A-Za-z_][A-Za-z0-9_]*
- STRING_LITERAL          " ... "
- CHARACTER_LITERAL       'x'

## Keywords
- USE
- RETURN
- IF
- OTHERWISE
- AFTERALL
- FOR
- WHILE
- BREAK
- CONTINUE
- PUBLIC
- PRIVATE
- INTERNAL
- TRUE
- FALSE
- INTEGER
- DOUBLE
- BOOLEAN
- CHARACTER
- STRING
- VOID
- FRESH

## Special
- AT              @
- EOF

## Note on Identifier Validation

The lexer recognizes identifiers generically.

Strict validation of identifier intent and naming rules
is performed at the parser and semantic analysis stages.

This separation is intentional.

# Klang Programming Language

Klang is a strictly explicit programming language designed to organize
complex, polyglot software systems.

It does not attempt to replace existing languages.
Instead, it orchestrates them.

## Core Principles

- Explicitness over convenience
- Determinism over magic
- Clarity over brevity
- Intent over convention

Klang treats language boundaries, types, and intent as first-class concepts.

---

## Strict by Default

Klang is **strict by default**.

Running:

    klang build

is equivalent to:

    klang build --rigor=strict

In strict mode:
- ambiguous identifiers are rejected
- public symbols must declare intent
- implicit behavior is forbidden
- semantic vagueness is treated as a compile-time error

This is intentional.

Klang believes that most long-term software problems are caused not by bugs,
but by ambiguity.

---

## Compilation Rigor Levels

Klang supports different rigor levels, but relaxing rules is always explicit.

### strict (default)
- full intent validation
- strict naming grammar
- no implicit conversions
- recommended for all production code

### explicit
- same as strict, but some violations emit warnings instead of errors

### lenient
- disables intent enforcement
- allows ambiguous naming
- intended only for experimentation

⚠ WARNING:
Lenient mode exists for learning and experimentation only.
It weakens the guarantees that define the Klang language.

---

## Why Klang Exists

Modern software systems are inherently polyglot.

Klang exists to:
- make cross-language boundaries explicit
- prevent semantic leakage between ecosystems
- reduce cognitive load in large systems
- enforce clarity where complexity is unavoidable

Klang does not optimize for speed of writing.
It optimizes for speed of understanding.

---

## Philosophy

Klang rejects:
- silent corrections
- implicit coercions
- ambiguous defaults
- convention without declaration

If something matters, it must be written down.

---

## Status

Klang is an experimental language.
Its design favors long-term correctness over short-term convenience.

# Klang Syntax Specification

This document defines the syntax and structural rules of the Klang language.

Klang syntax is intentionally explicit.
Clarity and predictability take precedence over brevity.

---

## Compilation Rigor

Klang enforces strict compilation rules by default.

Default behavior:

    --rigor=strict

Relaxing rigor requires explicit opt-in.

---

## Identifiers

### General Identifier Rules

- camelCase is mandatory
- underscores (`_`) are forbidden in public symbols
- underscores are allowed only in:
  - generated code
  - backend-specific glue code
  - internal compiler artifacts

---

## Strict Naming Rules (Public Symbols)

In `strict` mode, all public symbols must follow intent-based naming rules.

### Public Functions

Public function names must follow this grammar:

    Verb [Noun]

Examples:
- calculateChecksum
- validateUserData
- persistSession

Invalid examples:
- process
- run
- doStuff
- handle

---

### Allowed Verbs (Strict Mode)

The following verbs are permitted in strict mode:

- calculate, compute, build, create, generate
- validate, parse, serialize, deserialize
- persist, load, fetch, store
- send, dispatch, emit
- open, close, read, write
- convert, transform
- compare, check
- initialize, finalize

This list is controlled by the language specification.

---

### Functions With Return Values

Functions that return a value:
- must not start with generic verbs
- must describe the produced result

Invalid:
- doCalculation
- processData

---

### Void Functions

Void functions must imply side effects through their verb.

Valid:
- persistUser
- sendMessage
- writeFile

---

## Private and Local Functions

Private and local functions:
- may use short names
- may use abbreviations
- must not be exported
- must not cross module boundaries

---

## Variables

### Public Variables

Public variables must use descriptive nouns:

Examples:
- UserRepository
- SessionConfig

Invalid:
- data
- tmp
- x

---

### Local Variables

Local variables are less restricted, but:

- single-letter names are allowed only in loops
- generic names emit warnings in strict mode

---

## Types

Types must be named using PascalCase nouns.

Valid:
- UserData
- HttpRequest
- ChecksumResult

Invalid:
- data
- thing
- object

---

## Blacklisted Identifiers

The following identifiers are forbidden in strict mode:

- do
- stuff
- thing
- data
- process
- handle
- run
- exec
- work
- test
- misc
- util
- helper
- manager

These identifiers are rejected regardless of scope.

---

## Design Note

These rules exist to prevent ambiguity, not to enforce style.

Klang treats clarity as a structural requirement.

# Klang Technical Documentation

This document describes the internal architecture of the Klang language.

---

## Design Goals

- Deterministic compilation
- Explicit semantics
- Strong separation of concerns
- No hidden behavior

---

## Compilation Pipeline

1. Lexical Analysis
2. Parsing
3. Semantic Validation
4. Intent Validation
5. Backend Target Resolution
6. Code Generation

Intent validation occurs before any backend-specific transformation.

---

## Intent Metadata

Each public symbol carries intent metadata:

- rigor level
- verb
- noun (if present)
- ambiguity flag

This metadata is stored in the AST.

---

## Rigor Enforcement

Rigor level affects:
- naming validation
- error severity
- code generation strictness

Strict mode:
- rejects ambiguity
- emits hard errors

Lenient mode:
- allows ambiguity
- annotates AST nodes
- emits warnings

---

## Error Philosophy

Errors must:
- describe what failed
- explain why it failed
- suggest a corrective action

Silent failure is forbidden.

---

## Why Klang Avoids Implicit Behavior

Implicit behavior leads to:
- hidden bugs
- fragile integrations
- unpredictable systems

Klang favors explicit declarations even when verbose.

This is a deliberate tradeoff.




___
___

OBS: Comandos atualmentes suportados: 

- kc lex <arquivo.k> -> Tokeniza um arquivo (para debug)
- kc parse <arquivo..k> Tokeniza e faz o Parse de um arquivo (para debug)