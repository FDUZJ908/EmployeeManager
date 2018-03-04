package EmployeeManager.cls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lsh on 08/10/2017.
 */
public class Mapper<T> implements RowMapper<T> {

    private Class<T> cls;
    private Map<String, Object> defValue;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Mapper(Class<T> _cls) {
        cls = _cls;
        defValue = new HashMap<String, Object>();
    }

    public Mapper(Class<T> _cls, Map<String, Object> _defValue) {
        cls = _cls;
        defValue = _defValue;
    }

    public T mapRow(ResultSet result, int rowNum) throws SQLException {
        try {
            T obj = cls.newInstance();
            Field field = cls.getDeclaredField("Attrs");
            String[] Attrs = (String[]) (field.get(obj));
            Map<String, Object> argv = new HashMap<String, Object>();
            for (String attr : Attrs) {
                System.out.println(attr);
                try {
                    Object value = result.getObject(attr);
                    if (value != null) argv.put(attr, value);
                    else argv.put(attr, "");
                } catch (Exception e) {
                    if (defValue.containsKey(attr))
                        argv.put(attr, defValue.get(attr));
                    else argv.put(attr, "");
                }
            }
            Constructor cons = cls.getDeclaredConstructor(Map.class);
            return (T) (cons.newInstance(argv));
        } catch (Exception e) {
            logger.error("Mapper error: " + e.getMessage());
            return null;
        }
    }
}
