package time.geekbang.org.dao;

import org.springframework.stereotype.Repository;

@Repository
public class DaoImpl implements Dao {
    @Override
    public void remove() {
        System.out.println(getClass().getName() + " # " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
