# Klang — Linguagem Poliglota (v1.0.0, Draft)

> “Code once, speak many.” — *Klang Core Team*

---

## 🧠 Visão Geral

**Klang (K)** é uma linguagem poliglota cujo objetivo é **aproveitar o melhor de cada linguagem existente** por meio de **transpilação seletiva**.  
A versão **1.0.0** foca em três pilares:

- **Estabilidade da sintaxe**
- **Tipagem estática opcional (Java-like)**
- **Transpilação modular com @Use**

> **Filosofia:** cada arquivo ou função pode “falar” a linguagem mais eficiente para o seu propósito — sem perder legibilidade, interoperabilidade ou coesão.

---

## ⚙️ Princípios (v1.0.0)

- Sintaxe legível e familiar (inspirada em **Java** e **Python**)
- Semântica determinística
- Transpilação controlada por `@Use`
- Biblioteca padrão mínima (I/O, Math, Collections, Input)
- Filosofia **"o essencial primeiro"**

---

## 📘 Sumário

1. [Layout Léxico](#-layout-léxico)  
2. [Tipos](#-tipos)  
3. [Declaração de Variáveis](#-declaração-de-variáveis)  
4. [Operadores Aritméticos](#-operadores-aritméticos)  
5. [Operadores Lógicos e Comparações](#-operadores-lógicos-e-comparações)  
6. [I/O (Prints)](#-io-e-print)  
7. [Funções / Métodos](#-funções--métodos)  
8. [Anotação @Use](#-anotação-use)  
9. [Estruturas de Controle](#-estruturas-de-controle)  
10. [Coleções](#-coleções)  
11. [Input (Console)](#-input-console)  
12. [Biblioteca Math](#-biblioteca-math)  
13. [Erros e Exceções](#-erros-e-exceções)  
14. [Convenções e Boas Práticas](#-convenções-e-boas-práticas)  
15. [Exemplo Completo](#-exemplo-completo)  
16. [Mudanças Rápidas](#-mudanças-rápidas)

---

## 🧩 Layout Léxico

- **Terminador:** `;` (obrigatório em declarações e expressões)  
- **Blocos:** `{ ... }`  
- **Comentários:**  
  - Linha: `// comentário`  
  - Bloco: `/* comentário */`  
- **Strings:** `"texto"` (suporta `\n`, `\"`, `\\`)  
- **Character:** `'a'` ou `'\uXXXX'`  
- **Identificadores:** `[A-Za-z_][A-Za-z0-9_]*`

---

## 🔢 Tipos

**Primitivos:**  
`integer`, `double`, `character`, `boolean`

**Referência / Não-primitivos:**  
`String`, `Array<T>`, `Map<K,V>`, `Set<T>`  
e wrappers `Integer`, `Double`, `Character`, `Boolean` (nullable)

Exemplo:
```k
integer i = 10;
String nome = "Klang";
Array<Integer> lista = new Array(10);


---

💬 Declaração de Variáveis

integer x = 10;
double y = 3.14;
String nome = "Klang";
Array<Integer> lista = new Array();


---

➕ Operadores Aritméticos

+ - * / % **

> ** é açúcar sintático para Math.pow(a, b)



integer a = 5;
integer b = 2;
double p = a ** b; // 25.0


---

🧮 Operadores Lógicos e Comparações

Comparações: >, <, >=, <=, ==, !=
Lógicos: !, &&, ||
Aliases opcionais: and, or


---

🖨️ I/O e Print

print("texto");       // sem quebra de linha
println("texto");     // com \n
print("olá", end=" fim\n");

> end="..." é suportado apenas em funções core (print, println).




---

🧱 Funções / Métodos

public static integer somar(integer a, integer b){
    return a + b;
}

public, private, static seguem semântica Java.

Tipagem explícita obrigatória.



---

🏷️ Anotação @Use

Controla qual linguagem alvo será usada na transpilação.

@Use("java")
public static integer somar(integer a, integer b){ ... }

@Use("c")
public static void main(){ ... }

> Escopo: arquivo ou método.
Default global: "java".




---

🔀 Estruturas de Controle

if (x > 0){
    println("positivo");
} otherwise (x == 0){
    println("zero");
} afterall {
    println("negativo");
}

otherwise → alias de else if
afterall → substitui else

Loops:

for (integer i = 0; i < n; i++){
    println(i);
}

for (integer numero -> numeros){
    println(numero);
}


---

🧰 Coleções

Array<T>

Array<Integer> numeros = {1, 2, 3};
numeros.append(4);
println(numeros.get(0));

Map<K,V>

Map<String,Integer> idades = new Map();
idades.put("K", 25);

Set<T>

Set<String> nomes = new Set();
nomes.add("Klang");


---

⌨️ Input (Console)

String nome = Input.askNextLine();
integer idade = Input.askNextInteger();

Validações úteis:

String.isEmpty()

Integer.isDigit(str)

Integer.isPositive(n)



---

📐 Biblioteca Math

Math.pow(a, b);
Math.sin(x);
Math.sqrt(x);

** → açúcar sintático de Math.pow.


---

⚠️ Erros e Exceções

try {
    // ...
} catch (Exception e) {
    println("Erro: " + e);
}

> Exceções customizadas virão em versões futuras.




---

🧭 Convenções e Boas Práticas

Declare @Use sempre que usar APIs específicas de outro target.

Prefira Math.pow à ** para compatibilidade.

Use afterall em vez de else.

Use i < n em loops indexados.

Documente seus métodos e linguagens de destino.



---

💡 Exemplo Completo

@Use("c")
public static Array<Integer> generateArrayTo(integer n){
    if (n <= 0){
        println("O número precisa ser maior ou igual a 0");
        println("Gerando array de 10 elementos");
        n = 10;
    }

    Array<Integer> numeros = new Array(n);
    for (integer i = 0; i < n; i++){
        numeros.append(i);
    }

    return numeros;
}

public static void showNumbers(Array<Integer> numeros){
    if (numeros.isEmpty()){
        println("Não há números a serem exibidos.");
        return;
    }

    for (integer n -> numeros){
        printf("%d - %d\n", numeros.getIndexOf(n), n);
    }
}

@Use("java")
public static void main(Array<String> args){
    Array<Integer> numeros = new Array();
    integer n;

    n = askNumber();
    numeros = generateArrayTo(n);
    showNumbers(numeros);
}


---