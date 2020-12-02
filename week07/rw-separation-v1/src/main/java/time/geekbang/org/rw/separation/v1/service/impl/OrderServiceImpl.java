package time.geekbang.org.rw.separation.v1.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import time.geekbang.org.rw.separation.v1.annotation.ReadOnly;
import time.geekbang.org.rw.separation.v1.service.OrderService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public void insert(DataSource dataSource, String sql) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            log.info("try to insert, dataSource={}, sql={}", connection.getMetaData().getURL(), sql);
            statement.execute(sql);
        } catch (SQLException e) {
            log.error("insert fail", e);
        }
    }

    @ReadOnly
    @Override
    public List<Map<String, Object>> query(DataSource dataSource, String sql) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            List<Map<String, Object>> entities = new ArrayList<>();
            connection = dataSource.getConnection();
            log.info("try to query, dataSource={}, sql={}", connection.getMetaData().getURL(), sql);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Map<String, Object> data = Maps.newHashMap();
                data.put("id", resultSet.getLong("id"));
                entities.add(data);
            }
            return entities;
        } catch (SQLException e) {
            log.error("query fail", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                log.error("close fail", exception);
            }
        }
        return Lists.newArrayList();
    }
}
