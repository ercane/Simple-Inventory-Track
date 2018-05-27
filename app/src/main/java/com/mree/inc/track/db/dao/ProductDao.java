package com.mree.inc.track.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mree.inc.track.db.persist.Product;

import java.util.List;

/**
 * Created by MREE on 27.03.2018.
 */

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addProduct(Product product);

    @Query("select * from product")
    public List<Product> getAllProduct();

    @Query("select * from product where id = :productId")
    public List<Product> getProduct(long productId);

    @Query("select * from product where barcode = :barkod")
    public List<Product> getProductByBarcode(String barkod);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProduct(Product user);

    @Query("delete from product where id = :productId")
    void delete(long productId);

    @Query("delete from product")
    void removeAllProduct();


}
