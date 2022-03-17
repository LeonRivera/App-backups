package com.lrv3.backs;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.val;

@Controller

public class WebController {

    @GetMapping("/other")
    public String other(Model model) {
        return "other";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @PostMapping("/form")
    public String form(Model model, @RequestParam(name = "backupCompleto",  required = false) String backupCompleto,
    @RequestParam(name = "backupDiferencial",  required = false) String backupDiferencial){


        Connection conn = null;
 
        try {
 
            // String dbURL = "jdbc:sqlserver://BDSERVER1/MSSQLSERVER2;database=bdUsuarios;user=sa;password=sa";
            // String dbURL = "jdbc:sqlserver://BDSERVER1/MSSQLSERVER2;database=bdUsuarios;user=sa;password=sa;integratedSecurity=true;";
            String dbURL = "jdbc:sqlserver://127.0.0.1;database=bdUsuarios;user=sa;password=sa";
            // INNOWAVE-99\SQLEXPRESS01
          
            conn = DriverManager.getConnection(dbURL);
            

            Statement stmt = conn.createStatement();

            String sql = "";

            if(backupCompleto == "true"){
                sql = "USE bdUsuarios "+
                // " GO "+
                " BACKUP DATABASE bdUsuarios "+
                " TO  DISK = N'C:/DatabaseBackups/CE3.bak' "+
                " WITH CHECKSUM ";
            }

            if(backupDiferencial == "true"){
                sql = "USE bdUsuarios "+
                // " GO "+
                " BACKUP DATABASE bdUsuarios "+
                " TO  DISK = N'C:/DatabaseBackups/CE3.bak' "+
                " WITH CHECKSUM ";
            }


            int val = stmt.executeUpdate(sql);
            System.out.println(val);

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
