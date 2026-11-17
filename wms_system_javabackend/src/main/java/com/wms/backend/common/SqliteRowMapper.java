package com.wms.backend.common;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/** SQLite stores Java time values as ISO text rather than native JDBC date types. */
public class SqliteRowMapper<T> extends BeanPropertyRowMapper<T> {
    public SqliteRowMapper(Class<T> mappedClass) {
        super(mappedClass);
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        if (pd.getPropertyType() == LocalDate.class || pd.getPropertyType() == LocalDateTime.class) {
            String value = rs.getString(index);
            if (value == null) return null;
            return pd.getPropertyType() == LocalDate.class
                    ? LocalDate.parse(value.substring(0, 10))
                    : LocalDateTime.parse(value.replace(' ', 'T'));
        }
        return super.getColumnValue(rs, index, pd);
    }
}
