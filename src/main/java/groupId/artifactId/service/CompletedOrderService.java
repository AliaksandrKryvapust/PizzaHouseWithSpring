package groupId.artifactId.service;

import groupId.artifactId.dao.api.ICompletedOrderDao;
import groupId.artifactId.dao.entity.CompletedOrder;
import groupId.artifactId.service.api.ICompletedOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompletedOrderService implements ICompletedOrderService {
    private final ICompletedOrderDao completedOrderDao;

    @Autowired
    public CompletedOrderService(ICompletedOrderDao completedOrderDao) {
        this.completedOrderDao = completedOrderDao;
    }

    @Override
    public CompletedOrder create(CompletedOrder type) {
        return this.completedOrderDao.save(type);
    }

    @Override
    @Transactional(readOnly = true)
    public CompletedOrder findCompletedOrderByTicketId(Long id) {
        return this.completedOrderDao.findCompletedOrderByTicket_Id(id).orElseThrow();
    }

    @Override
    @Transactional
    public CompletedOrder save(CompletedOrder completedOrder) {
        if (completedOrder.getId() != null) {
            throw new IllegalStateException("Completed order id should be empty");
        }
        return this.completedOrderDao.save(completedOrder);
    }

    @Override
    public List<CompletedOrder> get() {
        return this.completedOrderDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CompletedOrder get(Long id) {
        return this.completedOrderDao.findById(id).orElseThrow();
    }
}
