package service;

import dao.DiningTableDAO;
import model.restaurant.DiningTable;
import java.util.List;

public class DiningTableService {
    private DiningTableDAO tableDAO = new DiningTableDAO();

    public List<DiningTable> getAllTables() { return tableDAO.findAll(); }

    public boolean addTable(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return tableDAO.insert(new DiningTable(0, name, "EMPTY"));
    }

    public boolean updateTableName(int id, String newName) {
        if (id <= 0 || newName == null || newName.trim().isEmpty()) return false;
        return tableDAO.update(new DiningTable(id, newName, null));
    }

    public boolean deleteTable(int id) {
        return tableDAO.delete(id);
    }
}