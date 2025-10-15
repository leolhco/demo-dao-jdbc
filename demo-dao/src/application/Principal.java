package application;

import java.util.Date;

import modelDao.DaoFactory;
import modelDao.SellerDao;
import modelEntities.*;

public class Principal {
    
    public static void main(String[] args) {
       
       SellerDao sld = DaoFactory.creatSellerDao();

       System.out.println("=== Test 1: findById ===");
       Seller sl = sld.findById(3);

       System.out.println(sl);
    
    }

}
