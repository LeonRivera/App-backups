package com.lrv3.backs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller

public class WebController {

    /**
     *
     */
    private static final String CMD_DUMP = "cmd /c start cmd.exe /K \" mongodump  --out c:/backups_mongo";
    private static final String CMD_RESTORE = "cmd /c start cmd.exe /K \" mongorestore C:/backups_mongo";
    private static final String CMD_IMPORT = "cmd /c start cmd.exe /K \" mongoimport -d database2 -c puntuaciones --type csv --file database2.csv --headerline";
    private static final String CMD_EXPORT = "cmd /c start cmd.exe /K \" mongoexport --db database2 --collection puntuaciones --type json --out ./scores.json";
    // private static final String CMD_RESTORE = "cmd /c start cmd.exe /K \" cd C:\\Users\\L\\Desktop  && mongorestore C:/backups_mongo";
    // private static final String CMD_RESTORE = "cmd /c start cmd.exe /K \" cd C:\\Users\\L\\Desktop  && mongorestore C:/backups_mongo";

    @GetMapping("/other")
    public String other(Model model) {
        return "other";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        
        String comandoRespaldo = "cmd.exe /K C:/Users/L/Desktop/respaldo.bat";
        String comandoExport = "cmd.exe /K C:/Users/L/Desktop/export.bat";
        String comandoRestore = "cmd.exe /K C:/Users/L/Desktop/restauracion.bat";
        String comandoImport = "cmd.exe /K C:/Users/L/Desktop/import.bat";

        System.out.println("En index");

        try {
            Process process = Runtime.getRuntime().exec(CMD_IMPORT);
            printResults(process);
            // process.destroy();
        } catch (IOException e) {
            
        }

        return "index";
    }


    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    @PostMapping("/form")
    public String form(Model model, @RequestParam(name = "backupCompleto",  required = false) String backupCompleto,
    @RequestParam(name = "backupDiferencial",  required = false) String backupDiferencial){


        Connection conn = null;
 
        try {
 
            // String dbURL = "jdbc:sqlserver://BDSERVER1/MSSQLSERVER2;database=bdUsuarios;user=sa;password=sa";
            // String dbURL = "jdbc:sqlserver://BDSERVER1/MSSQLSERVER2;database=bdUsuarios;user=sa;password=sa;integratedSecurity=true;";
            String dbURL = "jdbc:sqlserver://127.0.0.1;database=northwind;user=sa;password=sa";
            // INNOWAVE-99\SQLEXPRESS01
          
            conn = DriverManager.getConnection(dbURL);
            

            Statement stmt = conn.createStatement();


            Date fecha = new Date(Calendar.getInstance().getTimeInMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String fechaTexto = formatter.format(fecha);

            String sql = "";

            if(backupCompleto != null){
                if(backupCompleto.equals("true")){
                    sql = "USE northwind "+
                    // " GO "+
                    " BACKUP DATABASE northwind "+
                    " TO  DISK = N'C:/DatabaseBackups/Northwind-Completo"+fechaTexto+".bak' ";
                    // " WITH NAME = 'Backup Completo' ";
                    // " Description = 'Primer backup completo' ";
                }
            }
            
        
            if(backupDiferencial != null){
                if(backupDiferencial.equals("true")){
                    sql = "USE northwind "+
                    // " GO "+
                    " BACKUP DATABASE northwind "+
                    " TO  DISK = N'C:/DatabaseBackups/Northwind-Diferencial-"+fechaTexto+".bak' "+
                    " WITH NAME = 'Backup Diferencial' ,"+
                    " DIFFERENTIAL";
                }
            }
           

            model.addAttribute("resultado", "C:/DatabaseBackups");
           


            int val = stmt.executeUpdate(sql);
            System.out.println(val);
            // System.out.println(backupCompletos);

            // conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }

 
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return "index";
    }

}
