package org.klang.core.diagnostic;

/**
 * Sistema modular de cores para diagnósticos da Klang.
 * Baseado em camadas semânticas, não decoração.
 */
public final class DiagnosticColors {

    // ========================================
    // CAMADA 0 - TEXTO NEUTRO
    // ========================================
    
    /** Texto de código, exemplos, conteúdo geral */
    public static final String NEUTRAL = "\u001B[38;2;220;220;220m";
    
    /** Texto absolutamente neutro (sem cor) */
    public static final String PLAIN = "\u001B[0m";

    // ========================================
    // CAMADA 1 - ESTRUTURA (metadados)
    // ========================================
    
    /** Metadados: [K:E001], at file:line:col, números de linha */
    public static final String STRUCTURE = "\u001B[38;2;150;150;150m";
    
    /** Separadores: pipes |, dois-pontos em labels */
    public static final String SEPARATOR = "\u001B[38;2;100;100;100m";

    // ========================================
    // CAMADA 2 - ERRO (foco visual)
    // ========================================
    
    /** Erro primário: nome do erro, caret ^, token inválido */
    public static final String ERROR = "\u001B[38;2;220;50;47m";
    
    /** Erro secundário: contexto adicional */
    public static final String ERROR_DIM = "\u001B[38;2;180;80;77m";

    // ========================================
    // CAMADA 3 - AJUDA (resolução)
    // ========================================
    
    /** Fix, Example, Note - instrução construtiva */
    public static final String HELP = "\u001B[38;2;100;150;200m";
    
    /** Destaque em exemplos (opcional) */
    public static final String HELP_ACCENT = "\u001B[38;2;120;180;220m";

    // ========================================
    // RESET
    // ========================================
    
    /** Sempre use após aplicar cor */
    public static final String RESET = "\u001B[0m";

    // ========================================
    // MODO DE RENDERIZAÇÃO
    // ========================================
    
    private static RenderMode mode = RenderMode.AUTO;

    public enum RenderMode {
        /** Detecção automática baseada em terminal */
        AUTO,
        /** Força cores ANSI 24-bit */
        RICH,
        /** Sem cores (ASCII puro) */
        PLAIN,
        /** Modo de teste (mostra códigos ANSI) */
        DEBUG
    }

    public static void setMode(RenderMode newMode) {
        mode = newMode;
    }

    public static RenderMode getMode() {
        return mode;
    }

    // ========================================
    // API PÚBLICA - COLORIZAÇÃO
    // ========================================

    /**
     * Aplica cor semântica a um texto.
     * Respeita o modo de renderização configurado.
     */
    public static String colorize(String text, String color) {
        return switch (mode) {
            case PLAIN -> text;
            case DEBUG -> "[COLOR:" + color + "]" + text + "[/COLOR]";
            case RICH, AUTO -> color + text + RESET;
        };
    }

    /** Texto neutro (código, exemplos) */
    public static String neutral(String text) {
        return colorize(text, NEUTRAL);
    }

    /** Estrutura (metadados, números de linha) */
    public static String structure(String text) {
        return colorize(text, STRUCTURE);
    }

    /** Separadores (pipes, dois-pontos) */
    public static String separator(String text) {
        return colorize(text, SEPARATOR);
    }

    /** Erro primário (foco visual) */
    public static String error(String text) {
        return colorize(text, ERROR);
    }

    /** Erro secundário (contexto) */
    public static String errorDim(String text) {
        return colorize(text, ERROR_DIM);
    }

    /** Ajuda (fix, example) */
    public static String help(String text) {
        return colorize(text, HELP);
    }

    /** Destaque em ajuda */
    public static String helpAccent(String text) {
        return colorize(text, HELP_ACCENT);
    }

    // ========================================
    // DETECÇÃO DE SUPORTE
    // ========================================

    /**
     * Detecta se o terminal atual suporta cores ANSI.
     * Baseado em variáveis de ambiente e propriedades do sistema.
     */
    public static boolean isColorSupported() {
        // Windows 10+ com terminal moderno
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            String wtSession = System.getenv("WT_SESSION");
            return wtSession != null; // Windows Terminal
        }

        // Unix-like: verifica TERM
        String term = System.getenv("TERM");
        if (term == null) return false;

        return term.contains("color") 
            || term.contains("xterm") 
            || term.contains("screen")
            || term.equals("linux");
    }

    /**
     * Configura modo automaticamente baseado no ambiente.
     */
    public static void autoDetect() {
        if (isColorSupported()) {
            setMode(RenderMode.RICH);
        } else {
            setMode(RenderMode.PLAIN);
        }
    }

    // ========================================
    // BUILDER SEMÂNTICO (uso avançado)
    // ========================================

    /**
     * Builder para composição complexa de mensagens coloridas.
     */
    public static class Builder {
        private final StringBuilder sb = new StringBuilder();

        public Builder neutral(String text) {
            sb.append(DiagnosticColors.neutral(text));
            return this;
        }

        public Builder structure(String text) {
            sb.append(DiagnosticColors.structure(text));
            return this;
        }

        public Builder separator(String text) {
            sb.append(DiagnosticColors.separator(text));
            return this;
        }

        public Builder error(String text) {
            sb.append(DiagnosticColors.error(text));
            return this;
        }

        public Builder help(String text) {
            sb.append(DiagnosticColors.help(text));
            return this;
        }

        public Builder raw(String text) {
            sb.append(text);
            return this;
        }

        public String build() {
            return sb.toString();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // ========================================
    // TESTES VISUAIS
    // ========================================

    /**
     * Imprime exemplo visual de todas as cores.
     * Útil para testar em diferentes terminais.
     */
    public static void printColorTest() {
        System.out.println("\n=== Klang Diagnostic Colors Test ===\n");
        
        System.out.println(neutral("NEUTRAL:   ") + neutral("This is neutral text (code, examples)"));
        System.out.println(structure("STRUCTURE: ") + structure("This is structure (metadata, line numbers)"));
        System.out.println(separator("SEPARATOR: ") + separator("| : (pipes and colons)"));
        System.out.println(error("ERROR:     ") + error("This is primary error (focus)"));
        System.out.println(errorDim("ERROR_DIM: ") + errorDim("This is secondary error (context)"));
        System.out.println(help("HELP:      ") + help("This is help (fix, example)"));
        System.out.println(helpAccent("HELP_ACC:  ") + helpAccent("This is help accent"));
        
        System.out.println("\n=== Combined Example ===\n");
        System.out.println(
            structure("[K:E001] ") + 
            error("InvalidCharacter") + 
            structure("\nERROR (Lexical)\nat ") + 
            neutral("examples/teste.k:") + 
            structure("38:0")
        );
        
        System.out.println("\nCurrent mode: " + mode);
    }

    public static void main(String[] args) {
        printColorTest();
    }
}