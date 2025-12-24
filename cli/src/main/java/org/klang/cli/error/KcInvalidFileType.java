package org.klang.cli.error;

import org.klang.cli.error.diagnostic.KcDiagnosticCode;
import org.klang.cli.error.diagnostic.KcDiagnosticColors;

public class KcInvalidFileType extends KcCliException{
    private final String fileName;

    public KcInvalidFileType(KcDiagnosticCode code, String command, String fix, String fileName){
        super(code, command, fix);

        this.fileName = fileName;
    }

    @Override
    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append(KcDiagnosticColors.structure("[K:"))
          .append(KcDiagnosticColors.cliErrorCode(code.name()))
          .append(KcDiagnosticColors.structure("] "))
          .append(KcDiagnosticColors.neutral(code.name))
          .append("\n");
        
        // ERROR (Lexical) discreto, só informativo
        sb.append(KcDiagnosticColors.structure("ERROR (" + code.phase.name() + ")"))
          .append("\n");
        
        sb.append(KcDiagnosticColors.structure("at input file"))
          .append("\n\n");
          
        sb.append(KcDiagnosticColors.structure("The file '")).append(KcDiagnosticColors.neutral(fileName)).append(KcDiagnosticColors.structure("' is not a Klang source file."))
          .append("\n\n");

        sb.append(KcDiagnosticColors.structure("Cause:"))
          .append("\n  ")
          .append(KcDiagnosticColors.neutral("Klang only processes files with the '.k' extension."))
          .append("\n\n");
        
        sb.append(KcDiagnosticColors.structure("Fix:"))
          .append("\n  ")
          .append(KcDiagnosticColors.neutral("Rename the file or select a valid '.k' source."))
          .append("\n\n");

        sb.append(KcDiagnosticColors.structure("Example:"))
          .append("\n  ")
          .append(KcDiagnosticColors.neutral("kc " + this.command +" program.k"));
        
        return sb.toString();
    }
}
