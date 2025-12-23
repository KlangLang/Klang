package org.klang.core.error;

import org.klang.core.diagnostic.DiagnosticColors;
import org.klang.core.diagnostic.DiagnosticCode;

public final class LexicalException extends KException {

    public LexicalException(
            DiagnosticCode code,
            SourceLocation location,
            String[] contextLines,
            String cause,
            String fix,
            String example,
            String note) {
        super(code, location, contextLines, cause, fix, example, note);
    }

    @Override
    public String format() {
        StringBuilder sb = new StringBuilder();

        // ========================================
        // CABEÇALHO DO ERRO
        // ========================================
        
        sb.append(DiagnosticColors.structure("[K:"))
          .append(DiagnosticColors.error(code.name()))
          .append(DiagnosticColors.structure("] "))
          .append(DiagnosticColors.error(code.name))
          .append("\n");
        
        sb.append(DiagnosticColors.structure("ERROR (Lexical)"))
          .append("\n");
        
        sb.append(DiagnosticColors.structure("at "))
          .append(DiagnosticColors.neutral(location.file()))
          .append(DiagnosticColors.separator(":"))
          .append(DiagnosticColors.structure(String.valueOf(location.line())))
          .append(DiagnosticColors.separator(":"))
          .append(DiagnosticColors.structure(String.valueOf(location.column())))
          .append("\n\n");

        // ========================================
        // CONTEXTO DE CÓDIGO
        // ========================================
        
        int errorLine = location.line();
        int maxLineDigitWidth = String.valueOf(errorLine).length();
        
        // Calcula primeira linha mostrada
        int firstLineInContext = errorLine - (contextLines.length - 1);

        for (int i = 0; i < contextLines.length; i++) {
            int currentLine = firstLineInContext + i;
            
            // Número da linha + separador
            sb.append(DiagnosticColors.structure(
                    String.format("%" + maxLineDigitWidth + "d", currentLine)))
              .append(DiagnosticColors.separator(" | "))
              .append(DiagnosticColors.neutral(contextLines[i]))
              .append("\n");
            
            // Se é a linha do erro, adiciona o caret
            if (currentLine == errorLine) {
                sb.append(" ".repeat(maxLineDigitWidth))
                  .append(DiagnosticColors.separator(" | "))
                  .append(" ".repeat(Math.max(0, location.column())))
                  .append(DiagnosticColors.error("^"))
                  .append("\n");
            }
        }

        // ========================================
        // RODAPÉ - CAUSE, FIX, EXAMPLE
        // ========================================
        
        sb.append("\n");
        sb.append(DiagnosticColors.structure("Cause:"))
          .append("\n  ")
          .append(DiagnosticColors.neutral(cause))
          .append("\n");
        
        sb.append("\n");
        sb.append(DiagnosticColors.help("Fix:"))
          .append("\n  ")
          .append(DiagnosticColors.help(fix))
          .append("\n");

        if (example != null) {
            sb.append("\n");
            sb.append(DiagnosticColors.help("Example:"))
              .append("\n  ")
              .append(DiagnosticColors.helpAccent(example))
              .append("\n");
        }

        if (note != null) {
            sb.append("\n");
            sb.append(DiagnosticColors.structure("Note:"))
              .append("\n  ")
              .append(DiagnosticColors.neutral(note))
              .append("\n");
        }

        return sb.toString();
    }

    /**
     * Retorna versão do erro sem cores (para logs, arquivos).
     */
    public String formatPlain() {
        DiagnosticColors.RenderMode original = DiagnosticColors.getMode();
        DiagnosticColors.setMode(DiagnosticColors.RenderMode.PLAIN);
        
        String result = format();
        
        DiagnosticColors.setMode(original);
        return result;
    }
}