package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.ICompletedOrder;

public interface ICompletedOrderDao extends IDao<ICompletedOrder> {
    ICompletedOrder getAllData(Long id);
    Boolean exist(Long id);
}
