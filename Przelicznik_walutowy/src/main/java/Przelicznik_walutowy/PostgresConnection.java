/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Przelicznik_walutowy;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Przemyslaw Rodzik
 */
public class PostgresConnection {
    String databaseUrl = "jdbc:postgresql://localhost:5432/Kursy";


    private String error(String err){
        return err;
    }



    public List<String> SelectRowFromAllTables(List <String> tableName,String waluta)
    {
        List<String> ratesFromTables=new ArrayList<>();
        try {




            for (int i = 0; i < tableName.size(); i++)
            {
                ConnectionSource connection = new JdbcConnectionSource(databaseUrl,"postgres","admin");
                DatabaseTableConfig table=new DatabaseTableConfig(Kurs.class,null,null);
                String nazwa=tableName.get(i);
                table.setTableName(nazwa);
                Dao<Kurs,Integer> tabela = DaoManager.createDao(connection, table);
                List<Kurs> result= tabela.query(tabela.queryBuilder().where().eq("kodWaluty",waluta).prepare());
                String rate=result.get(0).getKurs_sredni();
                ratesFromTables.add(rate.replaceAll(",","."));
                connection.close();
            }

            ratesFromTables.forEach(e->{
                System.out.println(e + "fuunkcja");
            });




        } catch (SQLException | IOException ex) {
            Logger.getLogger(PostgresConnection.class.getName()).log(Level.SEVERE, null, ex);
        }



        return ratesFromTables;
    }




    public void CreateTableByDateAndFill(List<Kurs> kurs,String nameofTable) throws IOException
    {
        String databaseUrl = "jdbc:postgresql://localhost:5432/Kursy";




        DatabaseTableConfig table=new DatabaseTableConfig(Kurs.class,null,null);
        table.setTableName(nameofTable);


        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl,"postgres","admin");
            TableUtils.createTableIfNotExists(connectionSource, table);

            Dao<Kurs, Integer> kursDao=DaoManager.createDao(connectionSource,Kurs.class);
            for(Kurs x:kurs)
            {
                kursDao.createIfNotExists(x);
            }




            connectionSource.close();
        } catch (SQLException ex) {
            error("Ktoras z tabel ktora chcesz stworzyc juz istnieje, lub wystapil problem z polaczeniem z baza danych!");
        }
















    }

}
