/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Teeu Guima
 */
public class GerenteArquivos {

    private File file;
    private FileOutputStream fout;
    private FileInputStream finput;
    private JFileChooser filee;
    public GerenteArquivos() {
        this.filee = new JFileChooser();
    }

    public void gravarArquivo(String nome, byte[] arquivo) {
        file = new File("\\vendedor\\documentos"+nome + ".txt");

        try {
            OutputStream output = new FileOutputStream(file);
            output.write(arquivo);
            output.close();
        } catch (IOException e) {
        }
    }

    public byte[] selecionarArquivo() throws FileNotFoundException {
        
        FileNameExtensionFilter fileNameExt = new FileNameExtensionFilter(null, "txt");
        filee.setFileFilter(fileNameExt);
        filee.setDialogTitle("Selecione o Documento");
        int status = filee.showOpenDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            File file = new File(filee.getSelectedFile().getAbsolutePath());
            byte[] arqvConvrt = new byte[(int) file.length()];

            FileInputStream inFile = new FileInputStream(file);

            try {
                inFile.read(arqvConvrt, 0, (int) file.length());
            } catch (Exception e) {
            }
            return arqvConvrt;
        }
        return null;
    }
}