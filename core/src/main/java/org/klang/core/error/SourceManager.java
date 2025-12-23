package org.klang.core.error;

public final class SourceManager {

    private final String[] lines;

    public SourceManager(String source) {
        this.lines = source.split("\n", -1);
    }

    /** 
    * @return Returns context lines with the error line ALWAYS as the last line. 
    * @param errorLine error line (1-indexed) 
    * @param linesBefore how many lines to show BEFORE the error 
    */
    public String[] getContextLines(int errorLine, int linesBefore) {
        int errorIndex = errorLine - 1; // Converte para para 0
        
        // Calcula o início: errorLine - linesBefore, mas não pode ser negativo
        int startIndex = Math.max(0, errorIndex - linesBefore);
        
        // Fim é sempre a linha do erro
        int endIndex = errorIndex;
        
        // Cria o array de contexto
        String[] context = new String[endIndex - startIndex + 1];
        
        for (int i = startIndex; i <= endIndex; i++) {
            context[i - startIndex] = lines[i];
        }
        
        return context;
    }
}