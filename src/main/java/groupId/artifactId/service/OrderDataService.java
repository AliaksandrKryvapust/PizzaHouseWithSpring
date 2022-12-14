package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.core.mapper.OrderStageMapper;
import groupId.artifactId.dao.api.IOrderDataDao;
import groupId.artifactId.dao.entity.OrderData;
import groupId.artifactId.dao.entity.OrderStage;
import groupId.artifactId.service.api.ICompletedOrderService;
import groupId.artifactId.service.api.IOrderDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static groupId.artifactId.core.Constants.ORDER_FINISH_DESCRIPTION;
import static java.util.Collections.singletonList;

@Service
public class OrderDataService implements IOrderDataService {
    private final IOrderDataDao orderDataDao;
    private final OrderStageMapper orderStageMapper;
    private final ICompletedOrderService completedOrderService;
    private final CompletedOrderMapper completedOrderMapper;

    @Autowired
    public OrderDataService(IOrderDataDao orderDataDao, OrderStageMapper orderStageMapper,
                            ICompletedOrderService completedOrderService, CompletedOrderMapper completedOrderMapper) {
        this.orderDataDao = orderDataDao;
        this.orderStageMapper = orderStageMapper;
        this.completedOrderService = completedOrderService;
        this.completedOrderMapper = completedOrderMapper;
    }

    @Override
    public void create(OrderDataDtoInput dtoInput) {
        OrderStage inputOrderStages = this.orderStageMapper.inputMapping(dtoInput.getDescription());
        OrderData orderData = OrderData.builder().orderHistory(singletonList(inputOrderStages))
                .ticket(dtoInput.getTicket()).build();
        if (orderData.getId() != null) {
            throw new IllegalStateException("Order data id should be empty");
        }
        orderDataDao.save(orderData);
    }

    @Override
    @Transactional
    public OrderData saveInTransaction(OrderDataDtoInput dtoInput) {
        OrderStage inputOrderStages = this.orderStageMapper.inputMapping(dtoInput.getDescription());
        OrderData orderData = this.findOrderDataByTicketId(dtoInput.getTicketId());
        orderData.getOrderHistory().add(inputOrderStages);
        orderData.setDone(inputOrderStages.getDescription().equals(ORDER_FINISH_DESCRIPTION));
        OrderData orderDataOutput = this.orderDataDao.saveAndFlush(orderData);
        if (inputOrderStages.getDescription().equals(ORDER_FINISH_DESCRIPTION)) {
            this.completedOrderService.create(completedOrderMapper.inputMapping(orderDataOutput));
        }
        return orderDataOutput;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderData findOrderDataByTicketId(Long id) {
        return this.orderDataDao.findOrderDataByTicket_Id(id).orElseThrow();
    }

    @Override
    public OrderData save(OrderData orderData) {
        return this.orderDataDao.save(orderData);
    }

    @Override
    public List<OrderData> get() {
        return this.orderDataDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderData get(Long id) {
        return this.orderDataDao.findById(id).orElseThrow();
    }
}
