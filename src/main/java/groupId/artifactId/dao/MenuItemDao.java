package groupId.artifactId.dao;

import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.OptimisticLockException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static groupId.artifactId.core.Constants.*;

public class MenuItemDao implements IMenuItemDao {
    private final DataSource dataSource;
    private static final String INSERT_MENU_ITEM_SQL = "INSERT INTO pizza_manager.menu_item (price, pizza_info_id, menu_id)" +
            " VALUES (?, ?, ?);";
    private static final String UPDATE_MENU_ITEM_SQL = "UPDATE pizza_manager.menu_item SET price=?, pizza_info_id=?, " +
            "menu_id=?, version=version+1 WHERE id=? AND version=?;";
    private static final String SELECT_MENU_ITEM_SQL = "SELECT id, price, pizza_info_id, creation_date, version, menu_id " +
            "FROM pizza_manager.menu_item ORDER BY id;";
    private static final String SELECT_MENU_ITEM_BY_ID_SQL = "SELECT id, price, pizza_info_id, creation_date, version," +
            " menu_id FROM pizza_manager.menu_item WHERE id=?;";
    private static final String SELECT_MENU_ITEM_ALL_DATA_SQL = "SELECT menu_item.id AS id, price, pizza_info_id, " +
            "menu_item.creation_date AS cd, menu_item.version AS ver, menu_id, name, description, size," +
            "pi.creation_date AS picd, pi.version AS piv, menu_id FROM pizza_manager.menu_item " +
            "INNER JOIN pizza_manager.pizza_info pi on menu_item.pizza_info_id = pi.id WHERE menu_item.id=?;";
    private static final String DELETE_MENU_ITEM_SQL = "DELETE FROM pizza_manager.menu_item WHERE id=? AND version=?;";

    public MenuItemDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public IMenuItem save(IMenuItem menuItem) {
        if (menuItem.getId() != null || menuItem.getVersion() != null) {
            throw new IllegalStateException("MenuItem id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(INSERT_MENU_ITEM_SQL, Statement.RETURN_GENERATED_KEYS)) {
                long rows = 0;
                statement.setDouble(1, menuItem.getPrice());
                statement.setLong(2, menuItem.getPizzaInfoId());
                statement.setLong(3, menuItem.getMenuId());
                rows += statement.executeUpdate();
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect menu_item table update, more than 1 row affected");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    return new MenuItem(generatedKeys.getLong(1), menuItem.getPrice(),
                            menuItem.getPizzaInfoId(), menuItem.getMenuId());
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains(MENU_ITEM_FK) || e.getMessage().contains(MENU_ITEM_FK2)
                    || e.getMessage().contains(MENU_ITEM_UK)) {
                throw new NoContentException("menu_item table insert failed, check preconditions and FK values: "
                + menuItem);
            } else {
                throw new DaoException("Failed to save new MenuItem" + menuItem, e);
            }
        }
    }

    @Override
    public IMenuItem update(IMenuItem menuItem, Long id, Integer version) {
        if (menuItem.getId() != null || menuItem.getVersion() != null) {
            throw new IllegalStateException("MenuItem id & version should be empty");
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(UPDATE_MENU_ITEM_SQL)) {
                long rows = 0;
                statement.setDouble(1, menuItem.getPrice());
                statement.setLong(2, menuItem.getPizzaInfoId());
                statement.setLong(3, menuItem.getMenuId());
                statement.setLong(4, id);
                statement.setInt(5, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new OptimisticLockException("menu_item table update failed, version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect menu_item table update, more than 1 row affected");
                }
                return new MenuItem(id, menuItem.getPrice(), menuItem.getPizzaInfoId(), menuItem.getMenuId());
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to update menu_item" + menuItem + " by id:" + id, e);
        }
    }

    @Override
    public IMenuItem get(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_ITEM_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    if (!resultSet.isLast()) {
                        throw new NoContentException("There is no Menu Item with id:" + id);
                    }
                    return this.mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get Menu Item by id:" + id, e);
        }
    }

    @Override
    public void delete(Long id, Integer version, Boolean delete){
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(DELETE_MENU_ITEM_SQL)) {
                long rows = 0;
                statement.setLong(1, id);
                statement.setInt(2, version);
                rows += statement.executeUpdate();
                if (rows == 0) {
                    throw new OptimisticLockException("menu_item table delete failed,version does not match update denied");
                }
                if (rows > 1) {
                    throw new IllegalStateException("Incorrect pizza_info table delete, more than 1 row affected");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to delete menu item with id:" + id, e);
        }
    }

    @Override
    public List<IMenuItem> get() {
        try (Connection con = dataSource.getConnection()) {
            List<IMenuItem> items = new ArrayList<>();
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_ITEM_SQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        items.add(this.mapper(resultSet));
                    }
                }
                return items;
            }
        } catch (Exception e) {
            throw new DaoException("Failed to get List of menu items", e);
        }
    }

    @Override
    public IMenuItem getAllData(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_ITEM_ALL_DATA_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return this.allDataMapper(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to get Menu Item with pizza info by id:" + id, e);
        }
    }

    @Override
    public Boolean exist(Long id) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(SELECT_MENU_ITEM_BY_ID_SQL)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (Exception e) {
            throw new DaoException("Failed to select Menu Item with id:" + id, e);
        }
    }

    private IMenuItem mapper(ResultSet resultSet) throws SQLException {
        return new MenuItem(resultSet.getLong("id"), new PizzaInfo(), resultSet.getDouble("price"),
                resultSet.getLong("pizza_info_id"), resultSet.getTimestamp("creation_date").toInstant(),
                resultSet.getInt("version"), resultSet.getLong("menu_id"));
    }
    private IMenuItem allDataMapper(ResultSet resultSet) throws SQLException {
        IPizzaInfo pizzaInfo =  new PizzaInfo(resultSet.getLong("pizza_info_id"), resultSet.getString("name"),
                resultSet.getString("description"), resultSet.getInt("size"),
                resultSet.getTimestamp("picd").toInstant(), resultSet.getInt("piv"));
        MenuItem menuItem = new MenuItem(resultSet.getLong("id"), pizzaInfo, resultSet.getDouble("price"),
                resultSet.getLong("pizza_info_id"), resultSet.getTimestamp("cd").toInstant(),
                resultSet.getInt("ver"), resultSet.getLong("menu_id"));
        if (menuItem.getInfo()==null || menuItem.getId()==null){
            throw new NoContentException("There is no Menu Item with such id");
        }
        return menuItem;
    }
}
