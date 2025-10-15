package application;

import java.util.Date;

import modelEntities.*;

public class Principal {
    
    public static void main(String[] args) {
       
        Department dp = new Department(2, "Luke");
        Seller seller = new Seller(2, "Bob", "bob@gmail.com", new Date(), 3000.0, dp);       
        System.out.println(seller);
    
    }

}
