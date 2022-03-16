package com.lrv3.backs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String form(Model model){


        Connection conn = null;
 
        try {
 
            String dbURL = "jdbc:sqlserver://localhost:1433;database=bdUsuarios;user=sa;password=sa";
          
            conn = DriverManager.getConnection(dbURL);
            

            Statement stmt = conn.createStatement();

            String sql = "USE bdUsuarios "+
            // " GO "+
            " BACKUP DATABASE bdUsuarios "+
            " TO  DISK = N'D:/DatabaseBackups/CE3.bak' "+
            " WITH CHECKSUM ";



            int val = stmt.executeUpdate(sql);

            System.out.println("En el form");
 
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

        System.out.println("En el form");
        return "index";
    }

}
